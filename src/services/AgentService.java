package services;

import java.io.IOException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


import data.Data;
import helper.CenterInfo;
import helper.ConsoleMessage;
import models.AID;
import models.Agent;
import models.AgentType;
import sun.management.resources.agent;
import models.AgentCenter;


@Path("agents")
public class AgentService {
	
	
	//dobavi listu svih tipova agenata na sistemu;
	@GET
	@Path("/classes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getClasses() throws JsonGenerationException, JsonMappingException, IOException {
		return new ObjectMapper().writeValueAsString(Data.getAgentTypes());
	}
	
	//dobavi listu agentskih centara
	@GET
	@Path("/centers")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCenters() throws JsonGenerationException, JsonMappingException, IOException {
		
		return new ObjectMapper().writeValueAsString(Data.getAgentCenters());
	}
	
	@POST
	@Path("/classes")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean setClasses(String agentTypesJSON) {
		if(agentTypesJSON==null)
			return false;
		try {
			AgentType[] agentTypes = new ObjectMapper().readValue(agentTypesJSON, AgentType[].class);
			Data.addAgentType(agentTypes);
			return true;
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}
		
	}
	//dobavi sve pokrenute agente sa sistema;
	@GET
	@Path("/running")
	@Produces(MediaType.APPLICATION_JSON)
	public String getRunningAgents() throws JsonGenerationException, JsonMappingException, IOException {
		
		return new ObjectMapper().writeValueAsString(Data.getRunningAID());
	}
	
	//pokreni agenta odredjenog tipa sa zadatim imenom
	@PUT
	@Path("/running/{type}/{name}")
	public void addAgent(@PathParam("type")String type, @PathParam("name")String name) {
		//TODO: ZAVRSI POKRATANJE AGENATA
		Data.addAgent(type, name);
		AID newAID = Data.getAIDByName(name);
		AgentCenterService.sendChangeToAll("/center/add_agent", newAID);
		new ConsoleMessage(CenterInfo.getAgentCenter().getAddress()+" has created Agent "+name+" of Agent type "+type);
	}

	
	//zaustavi odredenog agenta
	@DELETE
	@Path("/running/{aid}")
	public void stopAgent(@PathParam("aid")String aidJSON) throws JsonParseException, JsonMappingException, IOException {
		AID aid = new ObjectMapper().readValue(aidJSON, AID.class);
		AID retAID = Data.stopAgent(aid);
		new ConsoleMessage(CenterInfo.getAgentCenter().getAddress()+" has deleted "+ aid.getName()+" agent of AgentType "+aid.getType().getName());
		if (retAID != null) {
			AgentCenterService.sendChangeToAll("/center/stop_agent", retAID);
		}
	}
	
	

}
