package hr.danisoka.webshopingmrk.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GenericUtilsTests {

	@Test
	public void isPriceCorrect_PriceIsZero_returnsTrue() {
		assertTrue(GenericUtils.isPriceCorrect(new BigDecimal(0)));
	}
	
	@Test
	public void isPriceCorrect_PriceIsNegative_returnsFalse() {
		assertFalse(GenericUtils.isPriceCorrect(new BigDecimal(-2.34)));
	}
	
	@Test
	public void isPriceCorrect_PriceIsPositiveNonZeroValue_returnsTrue() {
		assertTrue(GenericUtils.isPriceCorrect(new BigDecimal(12.44)));
	}
	
}
