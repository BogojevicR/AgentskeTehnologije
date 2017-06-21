package services;



import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import data.Data;
import helper.CenterInfo;
import jms.JMSMessage;
import models.AID;
import models.AgentCenter;
import models.AgentType;
import requests.Requests;




@Path("center")
public class AgentCenterService {
	
	@POST
	@Path("/node")
	@Consumes(MediaType.APPLICATION_JSON)
	public void agentCenters(String agentCentersJSON) {
		try {
			if (agentCentersJSON.indexOf("{") == 0) {
				agentCentersJSON = "["+agentCentersJSON+"]";
			}
			AgentCenter[] agentCenters = new ObjectMapper().readValue(agentCentersJSON, AgentCenter[].class);
			if (CenterInfo.MASTER && agentCenters.length == 1) {
				Data.addAgentCenter(agentCenters[0]);
				
				// add new type if it is not already here, and if there are any changes send change to all other slaves
				String typesJSON = new Requests().makeGetRequest("http://"+agentCenters[0].getAddress()+"/AgentApp/rest/center/agents/classes");
				AgentType[] agentTypes = new ObjectMapper().readValue(typesJSON, AgentType[].class);
				Data.addToMapClasses(agentCenters[0], agentTypes);
				boolean changes = Data.addAgentType(agentTypes);
				if (changes) {
					sendChangeToSlaves("/agents/classes", Data.getAgentTypes());
				}

				// send new agent center to all other slaves
				sendChangeToSlaves("/center/node", agentCenters[0]);

				// send other agent centers to new slave
				String otherAgentCentersJSON = new ObjectMapper().writeValueAsString(Data.getAgentCenters());
				new Requests().makePostRequestForSigningNode("http://"+agentCenters[0].getAddress()+"/AgentApp/rest/center/node", otherAgentCentersJSON, 0, agentCenters[0]);

				// send running agents to new slave
				String runningAgentsJSON = new ObjectMapper().writeValueAsString(Data.getRunningAID());
				new Requests().makePostRequestForSigningNode("http://"+agentCenters[0].getAddress()+"/AgentApp/rest/center/agents/running", runningAgentsJSON, 0, agentCenters[0]);

				
			} else if (!CenterInfo.MASTER && agentCenters.length == 1) {
				Data.addAgentCenter(agentCenters[0]);
				//Printer.print(this, "Agent center added in slave");
			} else if (!CenterInfo.MASTER && agentCenters.length > 1) {
				Data.setAgentCenters(agentCenters);
				//Printer.print(this, "Agent centers set in slave");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Master čvor traži spisak tipova agenata koje podržava nov ne-master čvor
	@GET
	@Path("/agents/classes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getAgentTypesFromCenter(String agentCenterJSON) throws JsonGenerationException, JsonMappingException, IOException {
		//TODO: napravi rest poziv ka hostu gde je centar i iz njegovog data pokupi tipove agenata
		return new ObjectMapper().writeValueAsString(Data.getAgentTypes());
	}
	
	//1) Master čvor dostavlja spisak novih tipova agenata (ukoliko ih ima) ostalim ne-master čvorovima;
	//2)Master čvor dostavlja spisak tipova agenata novom ne-master čvoru koje podržava on ili neki od ostalih ne-master čvorova;
	@POST
	@Path("/agents/classes")
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean addNewAgentTypes(String agentTypesJSON) throws JsonGenerationException, JsonMappingException, IOException{
		try {
			AgentType[] agentTypes = new ObjectMapper().readValue(agentTypesJSON, AgentType[].class);
			Data.addAgentType(agentTypes);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	//Master čvor dostavlja spisak pokrenutih agenata novom ne-master čvoru koji se nalaze kod njega ili nekog od preostalih ne-master čvorova;
	@POST
	@Path("/agents/running")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addRunningAgent(String runningAIDSJSON) {
		try {
			AID[] newAID = new ObjectMapper().readValue(runningAIDSJSON, AID[].class);
			for (AID aid : newAID) {
				Data.addRunningAID(aid);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@POST
	@Path("/new_message")
	@Consumes(MediaType.APPLICATION_JSON)
	public void newMessage(String aclMessageJSON) {
		new JMSMessage(aclMessageJSON);
	}
	
	@DELETE
	@Path("node/{address}")
	public void deleteNode(@PathParam("address")String address) {
		AgentCenter agentCenter = Data.getAgentCentar(address);
		if (agentCenter != null) {
			Data.removeAgentCenter(agentCenter);
			Data.removeFromMapClasses(agentCenter);
		}
	}
	
	//Update others methods
	
	public static void sendChangeToAll(String url, Object object) {
		try {
			String jsonInString = new ObjectMapper().writeValueAsString(object);
			for (AgentCenter agentCenter: Data.getAgentCenters()) {
				new Requests().makePostRequest("http://"+agentCenter.getAddress()+"/AgentApp/rest"+url, jsonInString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Object sendChangeToMaster(String url, Object object) {
		try {
			String jsonInString = new ObjectMapper().writeValueAsString(object);
			String masterAddress = CenterInfo.getMasterAddress();
			return new Requests().makePostRequest("http://"+masterAddress+"/AgentApp/rest"+url, jsonInString);
		} catch (IOException e) {
			return null;
		}

	}
	
	public static void sendChangeToSlaves(String url, Object object) {
		try {
			String jsonInString = new ObjectMapper().writeValueAsString(object);
			for (AgentCenter agentCenter: Data.getAgentCenters()) {
				if (!agentCenter.getAddress().equals(CenterInfo.getMasterAddress())) {
					new Requests().makePostRequest("http://"+agentCenter.getAddress()+"/AgentApp/rest"+url, jsonInString);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void sendChangeToSpecific(String url, Object object, AgentCenter agentCenter) {
		try {
			String jsonInString = new ObjectMapper().writeValueAsString(object);
			new Requests().makePostRequest("http://"+agentCenter.getAddress()+"/AgentApp/rest"+url, jsonInString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	

}
