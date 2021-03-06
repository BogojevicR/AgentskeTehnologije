package models;


public class AID {
	private String name;
	private AgentCenter host;
	private AgentType type;
	
	public AID() {}

	public AID(String name, AgentCenter host, AgentType type) {
		this.name = name;
		this.host = host;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AgentCenter getHost() {
		return host;
	}

	public void setHost(AgentCenter host) {
		this.host = host;
	}

	public AgentType getType() {
		return type;
	}

	public void setType(AgentType type) {
		this.type = type;
	}
	
	public boolean matches(AID aid) {
		return name.equals(aid.name) && host.matches(aid.host) && type.matches(aid.type);
	}
}