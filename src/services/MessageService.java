package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


import models.ACLMessage;




@Path("messages")
public class MessageService {
	
	//pošalji ACL poruku
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void postMessages(String messageJSON) throws JsonParseException, JsonMappingException, IOException {
		
		ACLMessage message = new ObjectMapper().readValue(messageJSON, ACLMessage.class);
		
		//TODO: ZAVRSI SLANJE PORUKE 
	}
	
	
	// dobavi listu performativa.
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getPerformative() throws JsonGenerationException, JsonMappingException, IOException {
		
		return new ObjectMapper().writeValueAsString(new ArrayList<ACLMessage.Performative>(Arrays.asList(ACLMessage.Performative.values())));
		
	}

	
	
	
	
}
