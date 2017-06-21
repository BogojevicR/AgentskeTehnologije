package mapreduce;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateful;

import org.codehaus.jackson.map.ObjectMapper;

import data.Data;
import helper.CenterInfo;
import helper.Message;
import models.ACLMessage;
import models.AID;
import models.Agent;
import models.ACLMessage.Performative;

@Stateful
public class WordCounter extends Agent {
	
	
	public WordCounter(){}
	
	public WordCounter(String name){
	AID aid=new AID();
	aid.setHost(CenterInfo.getAgentCenter());
	aid.setName(name);
	aid.setType(Data.getAgentType(WordCounter.class.getSimpleName()));
	setId(aid);
	}
	
	@Override
	public void handleMessage(ACLMessage message){
		System.out.println("USAO U HANDLE OD WORD COUNTERA");
		if(message.getPerformative()==ACLMessage.Performative.REQUEST){
			Data.addConsoleMessage(new helper.ConsoleMessage(this.getId().getType().getName()+"-"+this.getId().getName()+ ": Request recived").getMessage());
			String output=filereader(message.getContent());
			Map<String,Integer> words=new HashMap<>();
			String[] out=output.replaceAll("[^a-zA-Z]","").toUpperCase().split("\\s+");
			System.out.println("String size:"+out.length);
			for(String w: out){
				for(char c:w.toCharArray()){
					String str = Character.toString(c);
					System.out.println("STRING: "+w);
					if(words.get(str)== null){
						words.put(str, 1);
					}else{
						words.put(str,words.get(str)+1);
					}
				}
				
			}
			
			ACLMessage msg=new ACLMessage(Performative.INFORM);
			List<AID> recivers=new ArrayList();
			recivers.add(message.getSender());
			msg.setReceivers(recivers);
			msg.setSender(this.getId());
			Data.stopAgent(getId());
			try {
				msg.setContent(new ObjectMapper().writeValueAsString(words));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Message.sendMessage(msg);
			
		}
		
	}
	
private String filereader(String file) {
		
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();

		    
		    return everything;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
		
	}

}
