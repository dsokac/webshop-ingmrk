package hr.danisoka.webshopingmrk;

public class WebshopErrorResponse extends WebshopResponse {

	public WebshopErrorResponse(Object data) {
		this.type = "error";
		this.data = data;
	}
	
}
