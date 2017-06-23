package websocket;

import javax.websocket.server.ServerEndpoint;

import org.codehaus.jackson.map.ObjectMapper;

import helper.ConsoleMessage;
import requests.Requests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ServerEndpoint("/agents")
public class WebSocket {
	
	public static List<Session> sessions=new ArrayList<>();

	
	@OnOpen
	public void onOpen(Session session){
		
		
		sessions.add(session);
        
		
	}
	
	@OnMessage
	public void onMessage(String message,Session session){
		
		
		try {
			WebSocketRequest req=new ObjectMapper().readValue(message,WebSocketRequest.class);
			System.out.println(req.getMethod());
			if(req.getMethod().equals("GET")){
				new Requests().makeGetRequest(req.getAdress());
			}else if(req.getMethod().equals("POST")){
				new Requests().makePostRequest(req.getAdress(), req.getObject());
			}else if(req.getMethod().equals("PUT")){
				new Requests().makePutRequest(req.getAdress());
			}else if(req.getMethod().equals("DELETE")){
				new Requests().makeDeleteRequest(req.getAdress());
			}else{
				System.out.println("Mistake in request!");
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@OnClose
	public void onClose(Session session){
		
		sessions.remove(session);
	}
}
