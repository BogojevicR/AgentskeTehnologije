package jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;




public class JMSMessage implements MessageListener{
	
	public JMSMessage(String chatPair) {
		instantiateJMS(chatPair);
	}
	
	private void instantiateJMS(String chatPair) {
    	
    	try {
			Context context = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) context.lookup("ConnectionFactory");
			final Topic topic = (Topic) context.lookup("jms/topic/informTopic");
			context.close();
    		
			Connection connection = cf.createConnection("guest", "guestguest");
			final javax.jms.Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

			connection.start();

			MessageConsumer consumer = session.createConsumer(topic);
			consumer.setMessageListener(this);
			
			// create and publish a message
			TextMessage msg = session.createTextMessage();
			//TODO: create other way to get agent center and its adress
		//	msg.setStringProperty("host", Constants.getAgentCenter().getAddress());
			msg.setText(chatPair);
			MessageProducer producer = session.createProducer(topic);
			producer.send(msg);
			

			
			producer.close();
			consumer.close();
			connection.stop();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
	
	@Override
	public void onMessage(javax.jms.Message msg) {

	}

}

