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
	public void activateAgent(@PathParam("type")String type, @PathParam("name")String name) {
		System.out.println("USAO U REST CREATE AGENT");
		//TODO: ZAVRSI POKRATANJE AGENATA
		Data.addRunningAID(new AID(name, Data.getCurrentCenter(), new AgentType(type, type)));
	}

	
	//zaustavi odredenog agenta
	@DELETE
	@Path("/running/{aid}")
	public void stopAgent(@PathParam("aid")String aidJSON) throws JsonParseException, JsonMappingException, IOException {
		boolean stop = false;
		for(AID a:Data.getRunningAID()){
			if(a.getName().equals(aidJSON)){
				stop = true;
			}
		}
		if(stop==true)
			Data.stopAgent(Data.getAIDByName(aidJSON));
		
	}
	
	

}
