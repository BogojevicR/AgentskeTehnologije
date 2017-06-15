package services;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import data.Data;
import models.AID;
import models.AgentCenter;

@Path("center")
public class AgentCenterService {

	//1) Nov ne-master cvor kontaktira master cvor koji ga registruje;
	//2) Master čvor javlja ostalim ne-master čvorovima da je nov ne-master čvor ušao u mrežu;
	//3) Master čvor dostavlja spisak ostalih ne-master čvorova novom ne-master čvoru;
	@POST
	@Path("/node")
	@Consumes(MediaType.APPLICATION_JSON)
	public void registerNewCenter(String agentCenterJSON) throws JsonGenerationException, JsonMappingException, IOException{
		AgentCenter ac = new ObjectMapper().readValue(agentCenterJSON, AgentCenter.class);
		Data.addAgentCenter(ac);
		
		//TODO: odradi drugi komentar, treba poslati rest poziv ka hostu svih ne master cvorova
		//TODO: odradi treci komentar, treba poslati rest poziv ka hostu novog cvora
	}
	
	//Master čvor traži spisak tipova agenata koje podržava nov ne-master čvor
	@GET
	@Path("/agents/classes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getAgentTypesFromCenter(String agentCenterJSON) throws JsonGenerationException, JsonMappingException, IOException {
		//TODO: napravi rest poziv ka hostu gde je centar i iz njegovog data pokupi tipove agenata
		AgentCenter ac = new ObjectMapper().readValue(agentCenterJSON, AgentCenter.class);
		return  "SPISAK";
	}
	
	//1) Master čvor dostavlja spisak novih tipova agenata (ukoliko ih ima) ostalim ne-master čvorovima;
	//2)Master čvor dostavlja spisak tipova agenata novom ne-master čvoru koje podržava on ili neki od ostalih ne-master čvorova;
	
	@POST
	@Path("/agents/classes")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addNewAgentTypes(String agentCenterJSON) throws JsonGenerationException, JsonMappingException, IOException{
		//TODO: odradi prvi komentar, za svaki ne master cvor poslati rest poziv i smestiti u data nove tipove
		//TODO: odradi drugi komentar, poslati novom cvoru sve tipove koje master i ostali podrzavaju
		
	}
	
	
	//Master čvor dostavlja spisak pokrenutih agenata novom ne-master čvoru koji se nalaze kod njega ili nekog od preostalih ne-master čvorova;
	
	@POST
	@Path("/agents/running")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addRunningAgent(String runningAIDSJSON) {
		//TODO:Zavrsiti
	}
	
	
	//TODO:Fale ova 2 dugacka 
	
	
	
	
	

}
