package websocket;

public class WebSocketRequest {
	
	private String method;
	private String adress;
	private String object;
	
	
	
	
	public WebSocketRequest() {
		super();
	}
	public WebSocketRequest(String method, String adress, String object) {
		super();
		this.method = method;
		this.adress = adress;
		this.object = object;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	
	
	
}
