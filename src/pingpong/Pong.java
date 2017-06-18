package pingpong;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
public class Pong extends Agent{
	public Pong() {}
	
	public Pong(String name) {
		AID aid = new AID();
		aid.setHost(CenterInfo.getAgentCenter());
		aid.setName(name);
		aid.setType(Data.getAgentType(Pong.class.getSimpleName()));
		setId(aid);
	}
	
	@Override
	public void handleMessage(ACLMessage message) {
		if(message.getPerformative() == Performative.REQUEST) {
			ACLMessage reply  = message.makeReply(Performative.INFORM);
			reply.setContent(message.getContent());
			reply.setSender(this.getId());
			List<AID> sender=new ArrayList<>();
			sender.add(message.getSender());
			
			reply.setReceivers(sender);
			Message.sendMessage(reply);
			System.out.println("Pong: Request received.");

			Data.addConsoleMessage(new ConsoleMessage(this.getId().getType().getName()+"-"+this.getId().getName()+":"
			+message.getSender().getType().getName()+"-"+message.getSender().getName()+" sent request with content: "+message.getContent()).getMessage());
		}
	}
}
