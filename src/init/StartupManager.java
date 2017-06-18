package init;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import data.Data;
import helper.CenterInfo;
import models.AID;
import models.Agent;
import models.AgentCenter;
import models.AgentType;

@Startup
@Singleton
public class StartupManager {

	@PostConstruct
	public void postConstruct() {
		AgentCenter ac=new AgentCenter("localhost:8080","localhost:8080");
		Data.setCurrentCenter(ac);
		CenterInfo.setAgentCenter(ac);
		AgentType ping=new AgentType("Ping","Ping");
		AgentType pong=new AgentType("Pong","Pong");
		AID aid=new AID("Ping",ac,ping);
		AID aid2=new AID("Pong",ac,pong);
		Agent a=new Agent(aid);
		Data.getAgentTypes().add(ping);
		Data.getAgentTypes().add(pong);
		Data.addAgentCenter(ac);
		Data.addRunningAID(aid);
		Data.addRunningAID(aid2);
	}
}
