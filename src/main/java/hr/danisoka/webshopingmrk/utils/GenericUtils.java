package hr.danisoka.webshopingmrk.utils;

import java.math.BigDecimal;

public final class GenericUtils {

	public static boolean isPriceCorrect(BigDecimal price) {
		return price.floatValue() >= 0;
	}
	
}
