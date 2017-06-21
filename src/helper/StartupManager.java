package helper;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;


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
		AgentCenter ac=new AgentCenter("localhost:8080","localhost:8080");
		
		Data.setCurrentCenter(ac);
		CenterInfo.setAgentCenter(ac);
		
		Data.addConsoleMessage(new ConsoleMessage(ac.getAddress()+ " has connected").getMessage());
		
		AgentType ping=new AgentType("Ping",ac.getAddress());
		AgentType pong=new AgentType("Pong",ac.getAddress());
		AgentType mapReducer=new AgentType("MapReduce",ac.getAddress());
		
		Data.addConsoleMessage(new ConsoleMessage("Agent type " +ping.getName()+" is created on "+ping.getModule()).getMessage());
		Data.addConsoleMessage(new ConsoleMessage("Agent type "+ pong.getName()+" is created on "+pong.getModule()).getMessage());
		Data.addConsoleMessage(new ConsoleMessage("Agent type "+ mapReducer.getName()+" is created on "+mapReducer.getModule()).getMessage());
		
		Data.getAgentTypes().add(ping);
		Data.getAgentTypes().add(pong);
		Data.getAgentTypes().add(mapReducer);
		Data.addAgentCenter(ac);

	}
}
