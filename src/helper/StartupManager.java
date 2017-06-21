package helper;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import data.Data;
import models.AID;
import models.Agent;
import models.AgentCenter;
import models.AgentType;


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
				
				setTypes();
				if (!CenterInfo.getMasterAddress().equals(getCurrentAddress())) {
					CenterInfo.MASTER = false;
					//initialHandshake(currentAddress, masterAddress);
				} else {
					CenterInfo.MASTER = true;
				}
				
				Data.addConsoleMessage(new ConsoleMessage(ac.getAddress()+ " has connected").getMessage());
				Data.addAgentCenter(ac);
			}  catch (InstanceNotFoundException | AttributeNotFoundException | MalformedObjectNameException
					| ReflectionException | MBeanException e) {
				e.printStackTrace();
			}
		  }
		}, 1*1000, 1*1000);	  
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
		AgentType ping = new AgentType("Ping",CenterInfo.getAgentCenter().getAddress());
		AgentType pong = new AgentType("Pong",CenterInfo.getAgentCenter().getAddress());


		Data.addConsoleMessage(new ConsoleMessage("Agent type " +ping.getName()+" is created on "+ping.getModule()).getMessage());
		Data.addConsoleMessage(new ConsoleMessage("Agent type "+ pong.getName()+" is created on "+pong.getModule()).getMessage());
		
		Data.getAgentTypes().add(ping);
		Data.getAgentTypes().add(pong);
	}
	
}
