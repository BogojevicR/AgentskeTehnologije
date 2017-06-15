package jms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import models.AID;
import models.Agent;
import data.Data;
import models.ACLMessage;


@MessageDriven(activationConfig = { 
        @ActivationConfigProperty(propertyName = "destinationType", 
                propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName="destination",
                propertyValue="jms/topic/informTopic")                
        })
public class MDBConsumer implements MessageListener {
	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
            TextMessage tm = (TextMessage) message;
            try {
                String text = tm.getText();

                String host = tm.getStringProperty("host");

                ACLMessage aclMessage = new ObjectMapper().readValue(text, ACLMessage.class);
                                
                List<Agent> receivers = getReceiversFromMessage(aclMessage);

                for (Agent receiver : receivers) {
                	receiver.handleMessage(aclMessage);
                }

            } catch (JMSException e) {
                e.printStackTrace();
            } catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
        }
		
	}
	
	private List<Agent> getReceiversFromMessage(ACLMessage message) {
		List<Agent> receivers = new ArrayList<>();
		for (AID aid : message.getReceivers()) {
			for (Agent agent : Data.getRunningAgents()) {
				if (agent.getId().matches(aid)) {
					receivers.add(agent);
				}
			}
		}
		return receivers;
			
	}
}
