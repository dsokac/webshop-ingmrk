package hr.danisoka.webshopingmrk.APIs;

import java.beans.JavaBean;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import hr.danisoka.webshopingmrk.APIs.models.ExchangeRateResponse;

@JavaBean
public class HnbRestApi {

	@Autowired
	private RestTemplate restTemplate;
	private static final String BASE_URL = "http://api.hnb.hr/tecajn/v1";
	public static final String CURRENCY_EUR = "EUR";
	
	public HnbRestApi() {
	}
	
	public List<ExchangeRateResponse> getResponseforCurrency(String currency) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL);
		builder.queryParam("valuta", currency);
		URI uri = builder.build().encode().toUri();
		ExchangeRateResponse[] response = restTemplate.getForObject(uri, ExchangeRateResponse[].class);
		return Arrays.asList(response);
	}
	
	public ExchangeRateResponse getExchangeRateForCurrency(String currency) {
		return getResponseforCurrency(currency).get(0);
	}
	
	
}
