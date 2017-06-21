package pingpong;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ejb.Stateful;

import data.Data;
import helper.CenterInfo;
import helper.ConsoleMessage;
import helper.Message;
import models.ACLMessage;
import models.AID;
import models.Agent;
import models.ACLMessage.Performative;

@Stateful
public class Ping extends Agent {
	public Ping() {}
	
	public Ping(String name) {
		AID aid = new AID();
		aid.setHost(CenterInfo.getAgentCenter());
		aid.setName(name);
		aid.setType(Data.getAgentType(Ping.class.getSimpleName()));
		setId(aid);
	}
	
	@Override
	public void handleMessage(ACLMessage message) {
		if(message.getPerformative() == Performative.REQUEST) {

			AID aid = Data.getAIDByName(message.getContent());
			if (aid == null) {
				return;
			}
			ACLMessage msg = new ACLMessage(Performative.REQUEST);
			msg.setSender(getId());
			msg.addReceiver(aid);
			System.out.println("Ping: Request recived");
			Data.addConsoleMessage(new ConsoleMessage(this.getId().getType().getName()+"-"+this.getId().getName()+ ": Request recived").getMessage());
			msg.setContent(message.getContent()+" please respond");
			Message.sendMessage(msg);
			
			
		} else if (message.getPerformative()==Performative.INFORM) {
			ACLMessage msg = message;
			System.out.println("Ping: Pong responded.");
			Data.addConsoleMessage(new ConsoleMessage(this.getId().getType().getName()+"-"+this.getId().getName()+":"
			+msg.getSender().getType().getName()+"-"+msg.getSender().getName()+" responded").getMessage());
			
		}
	}

}
