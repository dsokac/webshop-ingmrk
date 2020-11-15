package hr.danisoka.webshopingmrk;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WebshopResponse {

	protected String type = "response";
	protected Object data;
	
	public WebshopResponse() {}
	
	public WebshopResponse(Object data) {
		this.data = data;
	}
	
}
