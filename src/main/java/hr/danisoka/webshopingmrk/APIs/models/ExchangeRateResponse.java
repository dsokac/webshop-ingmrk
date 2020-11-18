package hr.danisoka.webshopingmrk.APIs.models;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class ExchangeRateResponse {
	@Getter @Setter
	@JsonProperty(value = "Broj tečajnice")
	private String id;
	
	@Getter @Setter
	@JsonProperty(value = "Datum primjene")
	@JsonFormat(pattern = "dd.MM.yyyy")
	private Date appliedAt;
	
	@Getter @Setter
	@JsonProperty(value = "Država")
	private String country;
	
	@Getter @Setter
	@JsonProperty(value = "Šifra valute")
	private String exchangeCurrencyCode;
	
	@Getter @Setter
	@JsonProperty(value = "Valuta")
	private String exchangeCurrencyName;
	
	@Getter @Setter
	@JsonProperty(value = "Jedinica")
	private int unitValue;
	
	@Getter
	@JsonProperty(value = "Kupovni za devize")
	private String buyingRateString;
	
	@Getter
	@JsonProperty(value = "Srednji za devize")
	private String avgRateString;
	
	@Getter
	@JsonProperty(value = "Prodajni za devize")
	private String sellingRateString;
	
	@Getter
	private float buyingRate;
	
	@Getter
	private float avgRate;
	
	@Getter
	private float sellingRate;
	
	public void setBuyingRateString(String buyingRateString) throws ParseException {
		this.buyingRateString = buyingRateString;
		this.buyingRate = NumberFormat.getNumberInstance(Locale.forLanguageTag("HR")).parse(buyingRateString).floatValue();
	}

	public void setAvgRateString(String avgRateString) throws ParseException {
		this.avgRateString = avgRateString;
		this.avgRate = NumberFormat.getNumberInstance(Locale.forLanguageTag("HR")).parse(avgRateString).floatValue();
	}

	public void setSellingRateString(String sellingRateString) throws ParseException {
		this.sellingRateString = sellingRateString;
		this.sellingRate = NumberFormat.getNumberInstance(Locale.forLanguageTag("HR")).parse(sellingRateString).floatValue();
	}

}
