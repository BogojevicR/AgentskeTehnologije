package mapreduce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateful;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import models.AID;
import data.Data;
import helper.CenterInfo;
import helper.ConsoleMessage;
import helper.Message;
import models.ACLMessage;
import models.ACLMessage.Performative;
import models.Agent;
import models.AgentType;

@Stateful
public class MapReduce extends Agent {
	
public MapReduce() {}
	
	private Map<String, Integer> totalWords = new HashMap<>();
	private Map<String, Integer> words = new HashMap<>();
	private int filecounter=0;
	private int processed=0;

	public MapReduce(String name) {
		AID aid = new AID();
		aid.setHost(CenterInfo.getAgentCenter());
		aid.setName(name);
		aid.setType(Data.getAgentType(MapReduce.class.getSimpleName()));
		setId(aid);
		
	}
	
	@Override
	public void handleMessage(ACLMessage message) {
		System.out.println("USAO U HANDLE OD MAP REDUCERA");
		if (message.getPerformative() == Performative.REQUEST) {
			System.out.println("USAO U REQUEST");
			new helper.ConsoleMessage(this.getId().getType().getName()+"-"+this.getId().getName()+ ": Request recived");
			File folder = new File(message.getContent());
			File[] files=folder.listFiles();
			
			if (files == null) {
				ACLMessage msg = new ACLMessage(Performative.FAILURE);
				msg.setSender(getId());
				msg.addReceiver(getId());
				System.out.println("MapReduce: Unknown Path!");
				new helper.ConsoleMessage(this.getId().getType().getName()+"-"+this.getId().getName()+ ": Unknown Path!");
				Message.sendMessage(msg);
				return;
			}
			
			
			
			for (File file : files) {
			    if (file.isFile()) {
			    	filecounter++;
			    	System.out.println("FILE PATH: "+file.getAbsolutePath());
			    	AgentType wc=new AgentType("WordCounter",CenterInfo.getAgentCenter().getAddress());
			    	
			    	Data.addAgentType(wc);
			    	WordCounter wordCounter=new WordCounter(file.getName());
			    	
			    	Data.addAgent(wc.getName(), file.getName());
			    
			    	ACLMessage msg=new ACLMessage(Performative.REQUEST);
			    	msg.setSender(getId());
			    	List<AID> recivers=new ArrayList();
			    	recivers.add(wordCounter.getId());
			    	
			    	msg.setReceivers(recivers);
			    	msg.setContent(file.getAbsolutePath());
			    	
			    	Message.sendMessage(msg);
			    	
			    }
			
		}
		}else if(message.getPerformative()== Performative.INFORM){
			
			System.out.println("USAO U INFORM");
			try {
				Map<String, Integer> result = new ObjectMapper().readValue(message.getContent(),  new TypeReference<HashMap<String,Integer>>() {});
				for(String k:result.keySet()){
					if(this.totalWords.get(k)==null){
						this.totalWords.put(k,result.get(k));
					}else{
						System.out.println("PRE DODAVANJA: "+message.getSender().getName()+" slovo "+ k +" :"+totalWords.get(k));
						this.totalWords.put(k, this.totalWords.get(k)+result.get(k));
						System.out.println("POSLE DODAVANJA: "+message.getSender().getName()+" slovo "+ k +" :"+totalWords.get(k));
					}
					
				}
				processed++;
			new ConsoleMessage(message.getReceivers().get(0).getType().getName()+"-"+message.getReceivers().get(0).getName()+" has done: "+getProcessed()+"/"+getFilecounter()+" processed:"+message.getSender().getName()+" file results: "+ new ObjectMapper().writeValueAsString(result));
			if(processed==filecounter){
				ACLMessage msg=new ACLMessage(Performative.CONFIRM);
				msg.setSender(getId());
				List<AID> receivers=new ArrayList();
				receivers.add(getId());
				msg.setReceivers(receivers);
				Message.sendMessage(msg);
				
			}
			
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(message.getPerformative()== Performative.CONFIRM){
			try {
				new ConsoleMessage(message.getReceivers().get(0).getType().getName()+"-"+message.getReceivers().get(0).getName()+" has processed all files, results: "+ new ObjectMapper().writeValueAsString(totalWords));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			totalWords=new HashMap<>();
			processed=0;
			filecounter=0;
			Data.removeFromTypes(Data.getAgentType("WordCounter"));	
		}
	}

	public int getFilecounter() {
		return filecounter;
	}

	public void setFilecounter(int filecounter) {
		this.filecounter = filecounter;
	}

	public int getProcessed() {
		return processed;
	}

	public void setProcessed(int processed) {
		this.processed = processed;
	}
	
	
	
	

}
