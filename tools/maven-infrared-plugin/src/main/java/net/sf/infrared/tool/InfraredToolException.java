package net.sf.infrared.tool;

public class InfraredToolException extends RuntimeException {
	private String infraredExpMessage="";
	public InfraredToolException() {
	}

	public InfraredToolException(String message) {
		super(message);
		this.infraredExpMessage = message;
	}

	public InfraredToolException(Throwable cause) {
		super(cause);
	}

	public InfraredToolException(String message, Throwable cause) {
		super(message, cause);
		this.infraredExpMessage = message;
	}
	
	public String getInfraredMessage(){
		return this.infraredExpMessage;
	}

}
