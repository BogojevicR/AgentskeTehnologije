package helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.sun.research.ws.wadl.Request;

import data.Data;
import models.AgentCenter;
import requests.Requests;

public class ConsoleMessage {
	
	private  String message;

	public ConsoleMessage(String message) {
		super();
		
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String formatDateTime = ldt.format(formatter);
        
		this.message = "["+formatDateTime+"]"+message;
		String url="/messages/send/";
		for(AgentCenter agentCenter : Data.getAgentCenters()){
			
			new Requests().makePostRequest("http://"+agentCenter.getAddress()+"/AgentApp/rest"+url,getMessage());
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
