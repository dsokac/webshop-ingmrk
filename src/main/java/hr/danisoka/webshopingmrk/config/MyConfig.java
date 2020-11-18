package hr.danisoka.webshopingmrk.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import hr.danisoka.webshopingmrk.APIs.HnbRestApi;

@SpringBootConfiguration
public class MyConfig {

	@Bean
	public RestTemplate restTemplate() {
	 
	    var factory = new SimpleClientHttpRequestFactory();
	    factory.setConnectTimeout(3000);
	    factory.setReadTimeout(3000);
	    return new RestTemplate(factory);
	}
	
	@Bean
	public HnbRestApi hnbRestApi() {
		return new HnbRestApi();
	}
	
}
