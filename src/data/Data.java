package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import helper.CenterInfo;
import models.AID;
import models.Agent;
import models.AgentCenter;
import models.AgentType;

public class Data {

	private static List<AgentType> agentTypes = new ArrayList<>();
	private static List<Agent> runningAgents = new ArrayList<>();
	private static List<AgentCenter> agentCenters = new ArrayList<>();
	private static List<AID> runningAID = new ArrayList<>();
	
	static {
		AgentCenter ac=new AgentCenter("localhost:8080","localhost:8080");
		CenterInfo.setAgentCenter(ac);
		AgentType ping=new AgentType("Ping","Ping");
		AgentType pong=new AgentType("Pong","Pong");
		AID aid=new AID("Ping",ac,ping);
		AID aid2=new AID("Pong",ac,pong);
		Agent a=new Agent(aid);
		agentTypes.add(ping);
		agentTypes.add(pong);
		runningAgents.add(a);
		runningAgents.add(a);
		agentCenters.add(ac);
		runningAID.add(aid);
		runningAID.add(aid2);
		
		
	}
	
	//AGENT TYPES
	public static List<AgentType> getAgentTypes() {
		return agentTypes;
	}
	
	public static AgentType getAgentType(String name) {
		for (AgentType agentType : agentTypes) {
			if (agentType.getName().equals(name)) {
				return agentType;
			}
		}
		return null;
	}
	
	public static void setAgentTypes(List<AgentType> agentTypes) {
		Data.agentTypes = agentTypes;
	}
	
	public static boolean addAgentType(AgentType agentTypes) {
		for(AgentType at: Data.agentTypes){
			if(at.matches(agentTypes)){
				return false;
			}
		}
		return true;
	
	}
	
	public static void removeFromTypes(AgentType type) {
		for (AgentType at : Data.agentTypes) {
			if (at.matches(type)) {
				Data.agentTypes.remove(at);
				return;
			}
		}
	}
	
	//RUNNING AGENTS
	
	public static List<Agent> getRunningAgents() {
		return runningAgents;
	}
	
	public static void setRunningAgents(List<Agent> runningAgents) {
		Data.runningAgents = runningAgents;
	}
	
	public static boolean addRunningAgent(Agent runningAgent) {
		if ("".equals(runningAgent.getId().getName())) {
			return false;
		}
		for (Agent agent : Data.runningAgents) {
			if (agent.getId().matches(runningAgent.getId())) {
				return false;
			}
		}
		Data.getRunningAgents().add(runningAgent);
		
		return true;

	}
	
	public static AID stopAgent(AID aid) {
		for (Agent ag : runningAgents) {
			if (ag.getId().matches(aid)) {
				runningAgents.remove(ag);
				return ag.getId();
			}
		}
		return null;
	}
	
	
	
	
	public static List<AgentCenter> getAgentCenters() {
		return agentCenters;
	}

	public static void setAgentCenters(List<AgentCenter> agentCenters) {
		Data.agentCenters = agentCenters;
	}
	
	public static void addAgentCenter(AgentCenter agentCenter) {
		for (AgentCenter ac: Data.agentCenters) {
			if (ac.getAddress().equals(agentCenter.getAddress())) {
				return;
			}
		}
		Data.agentCenters.add(agentCenter);
	}
	
	public static void removeAgentCenter(AgentCenter agentCenter) {
		Data.agentCenters.remove(agentCenter);
	}
	
	
	
	public static AgentCenter getAgentCentar(String address) {
		for (AgentCenter ac : Data.agentCenters) {
			if (ac.getAddress().equals(address)) {
				return ac;
			}
		}
		return null;
	}
	
	public static Agent getAgentFromAID(AID aid) {
		for (Agent agent : Data.getRunningAgents()) {
			if (agent.getId().matches(aid)) {
				return agent;
			}
		}
		return null;
	}
	
	public static Agent getAgentByName(String name) {
		for (Agent agent : Data.getRunningAgents()) {
			if (agent.getId().getName().equals(name)) {
				return agent;
			}
		}
		return null;
	}
	
	public static AID getAIDByName(String name) {
		for (AID aid: Data.getRunningAID()) {
			if (aid.getName().equals(name)) {
				return aid;
			}
		}
		return null;
	}
	
	public static List<AID> getRunningAID() {
		return runningAID;
	}

	public static void setRunningAID(List<AID> runningAID) {
		Data.runningAID = runningAID;
	}
	
	public static void addRunningAID(AID newAID) {
		boolean exists = false;
		for (AID aid : Data.runningAID) {
			if (aid.matches(newAID)) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			Data.runningAID.add(newAID);
		}
		
	}
	
	public static void removeAID(AID newAID) {
		for (AID aid : Data.runningAID) {
			if (aid.matches(newAID)) {
				Data.runningAID.remove(aid);
				return;
			}
		}
		
	}

	
	

	
}
