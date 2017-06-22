package helper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.websocket.Session;

import com.sun.research.ws.wadl.Request;

import data.Data;
import models.AgentCenter;
import requests.Requests;

import websocket.WebSocket;


public class ConsoleMessage {
	
	private  String message;

	public ConsoleMessage(String message) {
		super();
	//	System.out.println("Usao u CONSOLE MENAGER");
	//	System.out.println(message);
		if(message.equals("REFRESH")){
		//	System.out.println("USAO u REFRESH KONZOLE");
			refreshConsole();
			return;
		}else{
		//	System.out.println("USAO ELSE");
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String formatDateTime = ldt.format(formatter);
        
		this.message = "["+formatDateTime+"]"+message;
		String url="/messages/send/";
		for(AgentCenter agentCenter : Data.getAgentCenters()){
			
			new Requests().makePostRequest("http://"+agentCenter.getAddress()+"/AgentApp/rest"+url,getMessage());
			refreshConsole();
		}
		refreshConsole();
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void refreshConsole(){
		for (Session session : WebSocket.sessions) {
			try {
				session.getBasicRemote().sendText("refresh");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
