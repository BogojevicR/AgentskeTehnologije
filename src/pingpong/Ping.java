package pingpong;

import javax.ejb.Stateful;

import data.Data;
import helper.CenterInfo;
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
			message.setSender(getId());
			message.addReceiver(aid);
			
			Message.sendMessage(message);
		} else if (message.getPerformative()==Performative.INFORM) {
			ACLMessage msg = message;
		}
	}

}
