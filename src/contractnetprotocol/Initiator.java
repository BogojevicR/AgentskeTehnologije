package contractnetprotocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateful;
import javax.persistence.Convert;

import data.Data;
import helper.CenterInfo;
import helper.ConsoleMessage;
import helper.Message;
import models.ACLMessage;
import models.ACLMessage.Performative;
import models.AID;
import models.Agent;


@Stateful
public class Initiator extends Agent{
	private Map<AID,Integer> offers=new HashMap<>();
	private List<AID> participants;
	private int participantsCounter=0;
	private int processed=0;
	private int best=0;
	public Initiator(){}
	
	public Initiator(String name){
		AID aid=new AID();
		aid.setHost(CenterInfo.getAgentCenter());
		aid.setName(name);
		aid.setType(Data.getAgentType(Initiator.class.getSimpleName()));
		setId(aid);
		
	}
	
	@Override
	public void handleMessage(ACLMessage message){
		
		if(message.getPerformative()==Performative.REQUEST){
			participantsCounter=0;
			processed=0;
			offers=new HashMap<>();
			best=0;
			
			participants=message.getReceivers();
			participantsCounter=participants.size();
			
			participants.remove(participantsCounter-1);
			participantsCounter=participants.size();
			
			if(participantsCounter==0){
			new ConsoleMessage("Error, no participants found");
				return;
			}
			ACLMessage msg=new ACLMessage(Performative.CALL_FOR_PROPOSAL);
			msg.setSender(getId());
			msg.setReceivers(participants);
			Message.sendMessage(msg);
		}else if(message.getPerformative()==Performative.REFUSE){

			processed++;
			new ConsoleMessage(getId().getType().getName()+"-"+getId().getName()+" :"+"Processed: "+processed+"/"+participantsCounter);
			if(participantsCounter==processed){
				AID bestOffer= getBestOffer();
				if(bestOffer!=null){
					for(AID aid:participants){
						if(aid.matches(bestOffer)){
							ACLMessage msg=new ACLMessage(Performative.ACCEPT_PROPOSAL);
							msg.setSender(getId());
							msg.addReceiver(aid);
							msg.setContent(Integer.toString(best));
							Message.sendMessage(msg);
							
						}else{
							ACLMessage msg=new ACLMessage(Performative.REJECT_PROPOSAL);
							msg.setSender(getId());
							msg.addReceiver(aid);
							Message.sendMessage(msg);
						}
					}	
				}else{
					new ConsoleMessage(getId().getType().getName()+"-"+getId().getName()+": Every Participant refused! ");
				}
			}


		}else if(message.getPerformative() == Performative.PROPOSE){
			offers.put(message.getSender(),Integer.parseInt(message.getContent()));
			
			processed++;
			new ConsoleMessage(getId().getType().getName()+"-"+getId().getName()+" :"+"Processed: "+processed+"/"+participantsCounter);
			if(participantsCounter==processed){
				AID bestOffer= getBestOffer();
				if(bestOffer!=null){
					for(AID aid:participants){
						if(aid.matches(bestOffer)){
							ACLMessage msg=new ACLMessage(Performative.ACCEPT_PROPOSAL);
							msg.setSender(getId());
							msg.addReceiver(aid);
							msg.setContent(message.getContent());
							Message.sendMessage(msg);
							
						}else{
							ACLMessage msg=new ACLMessage(Performative.REJECT_PROPOSAL);
							msg.setSender(getId());
							msg.addReceiver(aid);
							Message.sendMessage(msg);
						}
					}	
				}else{
					new ConsoleMessage(getId().getType().getName()+"-"+getId().getName()+": Every Participant refused! ");
				}
			}
		
		
		}else if(message.getPerformative() == Performative.FAILURE) {
			new ConsoleMessage(getId().getType().getName()+"-"+getId().getName()+": I accepted best offer from "+message.getSender().getName()+" but he failed to delivery!");
			processed=0;
			participantsCounter=0;
			offers=new HashMap<>();
		
		}else if(message.getPerformative() == Performative.INFORM) {
			new ConsoleMessage(getId().getType().getName()+"-"+getId().getName()+": I accepted best offer from "+message.getSender().getName()+" inform-done!");
			processed=0;
			participantsCounter=0;
			offers=new HashMap<>();
		}else if(message.getPerformative() == Performative.INFORM_REF) {
			new ConsoleMessage(getId().getType().getName()+"-"+getId().getName()+": I accepted best offer from "+message.getSender().getName()+" inform-result!");
			processed=0;
			participantsCounter=0;
			offers=new HashMap<>();
		}
		
		
		
	}
	
	private AID getBestOffer(){
		AID bestOffer= null;
		best=0;
		for(AID aid:offers.keySet()){
			if(offers.get(aid)>=best){
				bestOffer=aid;
				best=offers.get(aid);
			}
		}
		return bestOffer;
	}
	
	

}
