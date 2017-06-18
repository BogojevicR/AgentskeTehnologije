package helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleMessage {
	
	private  String message;

	public ConsoleMessage(String message) {
		super();
		
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String formatDateTime = ldt.format(formatter);
        
		this.message = "["+formatDateTime+"]"+message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
