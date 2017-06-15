package models;

public class AgentCenter {
	
	private String alias;
	private String address;
	
	public AgentCenter() {
		
	}
	
	public AgentCenter(String alias, String address) {
		super();
		this.alias = alias;
		this.address = address;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public boolean matches(AgentCenter ac) {
		return alias.equals(ac.alias) && address.equals(ac.address);
	}
	
	public boolean isMaster(){
		String port=this.address.substring(10,this.address.length());
		if(port.equals("8080")){
			return true;
		}
		return false;
	}
	
}
