package helper;

import models.AgentCenter;
import services.AgentCenterService;
import models.AID;
import data.Data;
import models.ACLMessage;


public class Message {
	public static void sendMessage(ACLMessage message) {
		for (AgentCenter center : Data.getAgentCenters()) {
			boolean exists = false;
			for (AID aid : message.getReceivers()) {
				if (aid.getHost().matches(center)) {
					exists = true;
				}
			}
			if (exists) {
				AgentCenterService.sendChangeToSpecific("/synchronize/new_message", message, center);
			}
		}
		//new SendJMSMessage(Converter.getJSONString(message));
		
		
	}
}
