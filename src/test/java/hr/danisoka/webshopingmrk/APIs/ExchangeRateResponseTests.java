package hr.danisoka.webshopingmrk.APIs;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
public class ExchangeRateResponseTests {

	@Autowired
	private HnbRestApi hnbRestApi;
	
	@Test
	public void contextLoads() {
		assertTrue(hnbRestApi.getExchangeRateForCurrency(HnbRestApi.CURRENCY_EUR).getId() != null);
	}
	
}
