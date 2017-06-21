package contractnetprotocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateful;

import data.Data;
import helper.CenterInfo;
import helper.ConsoleMessage;
import helper.Message;
import models.ACLMessage;
import models.ACLMessage.Performative;
import models.AID;
import models.Agent;

@Stateful
public class Participant extends Agent {
	
	public Participant(){}
	
	public Participant(String name){
		AID aid=new AID();
		aid.setHost(CenterInfo.getAgentCenter());
		aid.setName(name);
		aid.setType(Data.getAgentType(Participant.class.getSimpleName()));
		setId(aid);
	}
	
	@Override
	public void handleMessage(ACLMessage message){
		if(message.getPerformative()==Performative.CALL_FOR_PROPOSAL){
			int offer=new Random().nextInt(500);
			if(offer<250){
				ACLMessage msg=new ACLMessage(Performative.REFUSE);
				msg.setSender(getId());
				List<AID> receivers=new ArrayList<>();
				receivers.add(message.getSender());
				msg.setReceivers(receivers);
				Data.addConsoleMessage(new ConsoleMessage(getId().getType().getName()+"-"+getId().getName()+": I refuse!").getMessage());
				Message.sendMessage(msg);
			}else{
				ACLMessage msg=new ACLMessage(Performative.PROPOSE);
				msg.setSender(getId());
				List<AID> receivers=new ArrayList<>();
				receivers.add(message.getSender());
				msg.setReceivers(receivers);
				msg.setContent(Integer.toString(offer));
				Data.addConsoleMessage(new ConsoleMessage(getId().getType().getName()+"-"+getId().getName()+": My offer is: !"+offer).getMessage());
				Message.sendMessage(msg);
			}
			
		}else if(message.getPerformative() == Performative.REJECT_PROPOSAL){
			Data.addConsoleMessage(new ConsoleMessage(getId().getType().getName()+"-"+getId().getName()+": My offer got rejected!").getMessage());
			
			
			
		}else if(message.getPerformative() == Performative.ACCEPT_PROPOSAL){
			Data.addConsoleMessage(new ConsoleMessage(getId().getType().getName()+"-"+getId().getName()+": My bid of "+message.getContent()+" got accepted!").getMessage());
			int number=new Random().nextInt(3);
			System.out.println(number);
			if(number==0){
				ACLMessage msg = new ACLMessage(Performative.FAILURE);
				msg.setSender(getId());
				msg.addReceiver(message.getSender());
				Message.sendMessage(msg);
			}else if(number==1){
				ACLMessage msg = new ACLMessage(Performative.INFORM);
				msg.setSender(getId());
				msg.addReceiver(message.getSender());
				Message.sendMessage(msg);
			}else if(number==2){
				ACLMessage msg = new ACLMessage(Performative.INFORM_REF);
				msg.setSender(getId());
				msg.addReceiver(message.getSender());
				Message.sendMessage(msg);
			}
			
			
		}else if(message.getPerformative() == Performative.REQUEST){
			return;
		}
		
		
	}

}
