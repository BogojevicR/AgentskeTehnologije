package services;

import java.io.IOException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import models.AID;
import models.Agent;
import models.AgentType;
import models.AgentCenter;


@Path("agents")
public class AgentService {
	
	
	
	@GET
	@Path("/classes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getClasses() throws JsonGenerationException, JsonMappingException, IOException {
		System.out.println("USAO U CLASSES REST");
		return new ObjectMapper().writeValueAsString(Data.getAgentTypes());
	}
	
	
	@GET
	@Path("/centers")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCenters() throws JsonGenerationException, JsonMappingException, IOException {
		
		return new ObjectMapper().writeValueAsString(Data.getAgentCenters());
	}
	
	@GET
	@Path("/running")
	@Produces(MediaType.APPLICATION_JSON)
	public String getRunningAgents() throws JsonGenerationException, JsonMappingException, IOException {
		
		return new ObjectMapper().writeValueAsString(Data.getRunningAID());
	}
	
	@PUT
	@Path("/running/{type}/{name}")
	public void activateAgent(@PathParam("type")String type, @PathParam("name")String name) {
		
		
	}
	
	@DELETE
	@Path("/running/{aid}")
	public void stopAgent(@PathParam("aid")String aidJSON) throws JsonParseException, JsonMappingException, IOException {
		
		AID aid = new ObjectMapper().readValue(aidJSON, AID.class);
		AID retAID = Data.stopAgent(aid);
	}
	
	

}
