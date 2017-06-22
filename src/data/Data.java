package data;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import contractnetprotocol.Initiator;
import contractnetprotocol.Participant;
import helper.CenterInfo;
import helper.ConsoleMessage;
import mapreduce.MapReduce;
import mapreduce.WordCounter;
import models.ACLMessage;
import models.AID;
import models.Agent;
import models.AgentCenter;
import models.AgentType;
import pingpong.Ping;
import pingpong.Pong;

public class Data {
	public static HashMap<AID, Agent> cache = new HashMap<>();
	
	private static List<AgentType> agentTypes = new ArrayList<>();
	private static List<AgentCenter> agentCenters = new ArrayList<>();
	private static Map<AgentCenter, List<AgentType>> mapClasses = new HashMap<>();	

	private static List<ACLMessage> messages=new ArrayList<>();
	
	private static List<String> console=new ArrayList<>();
	
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
		Data.getAgentTypes().add(agentTypes);
		new ConsoleMessage(CenterInfo.getMasterAddress()+" has created AgentType named: "+agentTypes.getName());
		return true;
	}
	
	public static boolean addAgentType(AgentType[] agentTypes) {
		boolean retVal = false;
		for (AgentType at1 : agentTypes) {
			boolean exists = false;
			for (AgentType at2 : Data.agentTypes) {
				if (at1.matches(at2)) {
					exists = true;
				}
			}
			if (!exists) {
				System.out.println("Dodao tip");
				Data.agentTypes.add(at1);
				retVal = true;
			}
		}
		retVal = false;
		return retVal;
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
	
	public static Collection<Agent> getRunningAgents() {
		return (Collection<Agent>) cache.values();
	}
	
	public static void setRunningAgents(Agent[] runningAgents) {
		//Data.runningAgents.clear();
		for (Agent agent : runningAgents) {
			Data.cache.put(agent.getId(), agent);
			//Data.runningAgents.add(agent);
		}
	
	}
	
	public static void addRunningAgents(Agent[] runningAgents) {
		for (Agent a1 : runningAgents) {
			boolean exists = false;
			for (Agent a2 : Data.cache.values()) {
				if (a1.getId().matches(a2.getId())) {
					exists = true;
				}
			}
			if (!exists) {
				Data.cache.put(a1.getId(), a1);
			}
		}
	}
	
	public static AID stopAgent(AID aid) {
		boolean exists = false;
		for (AID ag : cache.keySet()) {
			if (ag.getName().equals(aid.getName())) {
				exists = true;
			}
		}
		if(exists) {
			cache.remove(aid);
			return aid;
		}
		return null;
	}
	
	
	
	
	public static List<AgentCenter> getAgentCenters() {
		return agentCenters;
	}

	public static void setAgentCenters(List<AgentCenter> agentCenters) {
		Data.agentCenters = agentCenters;
	}
	public static void setAgentCenters(AgentCenter[] agentCenters) {
		Data.agentCenters.clear();
		for(AgentCenter ac: agentCenters) {
			Data.addAgentCenter(ac);
		}
	}
	public static void addAgentCenter(AgentCenter agentCenter) {
		for (AgentCenter ac: Data.agentCenters) {
			if (ac.getAddress().equals(agentCenter.getAddress())) {
				return;
			}
		}
		Data.agentCenters.add(agentCenter);
		new ConsoleMessage(CenterInfo.getAgentCenter().getAddress()+"has joined!");
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
	
	public static Set<AID> getRunningAID() {
		return cache.keySet();
	}
	
	public static void addRunningAID(AID newAID) {
		boolean exists = false;
		for (AID aid : Data.cache.keySet()) {
			if (aid.matches(newAID)) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			Agent agent = new Agent(newAID);
			Data.cache.put(newAID, agent);
		}
		
	}
	
	public static void removeAID(AID newAID) {
		for (AID aid : Data.cache.keySet()) {
			if (aid.matches(newAID)) {
				Data.cache.remove(aid);
				return;
			}
		}
		
	}
	
	public static void addAgent(String typeName, String agentName) {
		if (Ping.class.getSimpleName().equals(typeName)) {
			
			Ping ping = new Ping(agentName);
			AID aid = ping.getId(); 
			boolean exists = false;
			for(Agent ag : Data.getRunningAgents())
				if(ag.getId().matches(aid))
					exists = true;
			if(!exists)
				Data.cache.put(ping.getId(), ping);
		} else if (Pong.class.getSimpleName().equals(typeName)) {
			Pong pong = new Pong(agentName);
			AID aid = pong.getId(); 
			boolean exists = false;
			for(Agent ag : Data.getRunningAgents())
				if(ag.getId().matches(aid))
					exists = true;
			if(!exists)
				Data.cache.put(pong.getId(), pong);
		} else if (MapReduce.class.getSimpleName().equals(typeName)) {
			
			MapReduce map = new MapReduce(agentName);
			AID aid = map.getId(); 
			boolean exists = false;
			for(Agent ag : Data.getRunningAgents())
				if(ag.getId().matches(aid))
					exists = true;
			if(!exists)
				Data.cache.put(map.getId(), map);
		} else if (WordCounter.class.getSimpleName().equals(typeName)) {
			
			WordCounter wc = new WordCounter(agentName);
			AID aid = wc.getId(); 
			boolean exists = false;
			for(Agent ag : Data.getRunningAgents())
				if(ag.getId().matches(aid))
					exists = true;
			if(!exists)
				Data.cache.put(wc.getId(), wc);
		}else if(Initiator.class.getSimpleName().equals(typeName)){
			
			Initiator in=new Initiator(agentName);
			AID aid=in.getId();
			boolean exists = false;
			for(Agent ag : Data.getRunningAgents())
				if(ag.getId().matches(aid))
					exists = true;
			if(!exists)
				Data.cache.put(in.getId(), in);		
		}else if(Participant.class.getSimpleName().equals(typeName)){
			Participant p=new Participant(agentName);
			AID aid=p.getId();
			boolean exists = false;
			for(Agent ag : Data.getRunningAgents())
				if(ag.getId().matches(aid))
					exists = true;
			if(!exists)
				Data.cache.put(p.getId(), p);		
			
		}
		
		
	}

	public static List<ACLMessage> getMessages() {
		return messages;
	}

	public static void setMessages(List<ACLMessage> messages) {
		Data.messages = messages;
	}
	
	public static void addACLMessage(ACLMessage acl){
		Data.messages.add(acl);
	}
	
	public static List<String> getConsole() {
		return console;
	}

	public static void setConsole(List<String> messages) {
		Data.console = messages;
	}
	
	public static void addConsoleMessage(String msg){
		Data.console.add(msg);
	}

	public static Map<AgentCenter, List<AgentType>> getMapClasses() {
		return mapClasses;
	}

	public static void setMapClasses(Map<AgentCenter, List<AgentType>> mapClasses) {
		Data.mapClasses = mapClasses;
	}

	public static void addToMapClasses(AgentCenter center, AgentType[] agentTypes) {
		Data.addToMapClasses(center, Arrays.asList(agentTypes));
	}
	
	public static void addToMapClasses(AgentCenter center, List<AgentType> agentTypes) {
		Data.mapClasses.put(center, agentTypes);
	}
	
	public static boolean mapOfTypesContains(AgentCenter center, AgentType type) {
		List<AgentType> types = Data.mapClasses.get(center);
		for (AgentType t : types) {
			if (t.matches(type)) {
				return true;
			}
		}
		return false;
	}
	
	public static void removeFromMapClasses(AgentCenter center) {
		List<AgentType> types = Data.mapClasses.get(center);
		Set<AgentType> found = new HashSet<>();
		Data.mapClasses.remove(center);
		for (AgentType agentType : types) {
			for (AgentCenter ac : Data.mapClasses.keySet()) {
				if (mapOfTypesContains(ac, agentType)) {
					found.add(agentType);
				}
			}
		}
		
		if (found.size() == types.size()) {
			for (AgentType at1 : found) {
				boolean exists = false;
				for (AgentType at2 : types) {
					if (at1.matches(at2)) {
						exists = true;
					}
				}
				if (!exists) {
					removeFromTypes(at1);
				}
			}
		}
	}
	
	

	
}
