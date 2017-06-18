package pingpong;

import javax.ejb.Stateful;

import data.Data;
import helper.CenterInfo;
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
			Message.sendMessage(reply);
			System.out.println("Pong: Request received.");
		}
	}
}
