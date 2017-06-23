package helper;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;


import contractnetprotocol.Initiator;
import contractnetprotocol.Participant;
import data.Data;
import mapreduce.MapReduce;
import models.AID;
import models.Agent;
import models.AgentCenter;
import models.AgentType;
import pingpong.Ping;
import pingpong.Pong;
import requests.Requests;
import services.AgentCenterService;



@Startup
@Singleton
public class StartupManager {

	@PostConstruct
	public void postConstruct() {
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
		  @Override
		  public void run() {
			try{
				AgentCenter ac=new AgentCenter(getHostName(),getCurrentAddress());
				CenterInfo.setAgentCenter(ac);
				
				
				if (!CenterInfo.getMasterAddress().equals(getCurrentAddress())) {
					CenterInfo.MASTER = false;
					initialHandshake(getCurrentAddress(), CenterInfo.getMasterAddress());
				} else {
					CenterInfo.MASTER = true;
				}
				
				Data.addAgentCenter(ac);
				setTypes();
				heartbeat();
				 
			}  catch (InstanceNotFoundException | AttributeNotFoundException | MalformedObjectNameException
					| ReflectionException | MBeanException e) {
				e.printStackTrace();
			}
		  }
		}, 1*3000, 1*3000);	  
	}
	
	@PreDestroy
	public void preDestroy() {
		AgentCenterService.sendChangeToAll("/center/stop_agents/"+CenterInfo.getAgentCenter().getAddress(), Data.getRunningAgents());
		new ConsoleMessage(CenterInfo.getAgentCenter().getAddress() + " has closed!");
		Data.removeAgentCenter(CenterInfo.getAgentCenter());
		for(AgentCenter center : Data.getAgentCenters()) {
			new Requests().makeDeleteRequest("http://"+center.getAddress()+"/AgentApp/rest/center/node/"+CenterInfo.getAgentCenter().getAddress());
		}
	}
	
	public void initialHandshake(String currentAddress, String masterAddress) {
		AgentCenterService.sendChangeToMaster("/center/node", Data.getAgentCenters().toArray());
	}
	
	public String getCurrentAddress() throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException {
		String port = ManagementFactory.getPlatformMBeanServer().getAttribute(new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http"), "boundPort").toString();
		String host = ManagementFactory.getPlatformMBeanServer().getAttribute(new ObjectName("jboss.as:interface=public"), "inet-address").toString();	
		return host+":"+port;
	}
	
	public String getHostName() {
		try  {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e){
			return "unknown-PC";
		}
	}
	
	private void setTypes() {
		AgentType ping = new AgentType(Ping.class.getSimpleName(),CenterInfo.getAgentCenter().getAddress());
		AgentType pong = new AgentType(Pong.class.getSimpleName(),CenterInfo.getAgentCenter().getAddress());
		AgentType mapreduce = new AgentType(MapReduce.class.getSimpleName(),CenterInfo.getAgentCenter().getAddress());
		AgentType initiator = new AgentType(Initiator.class.getSimpleName(),CenterInfo.getAgentCenter().getAddress());
		AgentType participant = new AgentType(Participant.class.getSimpleName(),CenterInfo.getAgentCenter().getAddress());
		
		Data.addAgentType(ping);
		Data.addAgentType(pong);
		Data.addAgentType(mapreduce);
		Data.addAgentType(initiator);
		Data.addAgentType(participant);
	}
	
	public void heartbeat() {
		Timer timer = new Timer();

		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (AgentCenter agentCenter : Data.getAgentCenters()) {
					if (!agentCenter.matches(CenterInfo.getAgentCenter())) {
						heartbeatRequest(agentCenter);
					}
				}
			}
			
		}, 60*1000, 60*1000);
	}
	
	public void heartbeatRequest(AgentCenter agentCenter) {
		try {			
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet("http://"+agentCenter.getAddress()+"/AgentApp/rest/center/node");
			client.execute(request);
		} catch (Exception e) {
			if (e instanceof SocketTimeoutException) {
				Data.removeAgentCenter(agentCenter);
				new Requests().makeDeleteRequest("http://"+CenterInfo.getAgentCenter().getAddress()+"/AgentApp/rest/center/node/"+agentCenter.getAddress());
			}
		}
	}
	
}
