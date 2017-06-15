package helper;

import models.AgentCenter;


public class CenterInfo {
	public static final String AGENT_APP_MODULE = "agentAppModule";
	
	public static boolean MASTER;
	
	private static AgentCenter agentCenter;

	public static AgentCenter getAgentCenter() {
		return agentCenter;
	}

	public static void setAgentCenter(AgentCenter agentCenter) {
		CenterInfo.agentCenter = agentCenter;
	}
	
}
