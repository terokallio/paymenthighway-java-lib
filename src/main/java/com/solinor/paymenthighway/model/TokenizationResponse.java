package com.solinor.paymenthighway.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Tokenization response POJO
 * @author Tero Kallio <tero.kallio@solinor.com>
 */
public class TokenizationResponse {
	
	@JsonProperty("card_token")
	UUID cardToken;
	TokenizationResponse.Card card;
	TokenizationResponse.Result result;
	
	public UUID getCardToken() {
		return cardToken;
	}
	public void setCardToken(UUID cardToken) {
		this.cardToken = cardToken;
	}
	public TokenizationResponse.Card getCard() {
		return this.card;
	}
	public void setCard(TokenizationResponse.Card card) {
		this.card = card;
	}
	public TokenizationResponse.Result getResult() {
		return this.result;
	}
	public void setResult(TokenizationResponse.Result result) {
		this.result = result;
	}
	
	public static class Card {
		String type;
		@JsonProperty("partial_pan")
		String partialPan;
		@JsonProperty("expire_year")
		String expireYear;
		@JsonProperty("expire_month")
		String expireMonth;
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getPartialPan() {
			return partialPan;
		}
		public void setPartialPan(String partialPan) {
			this.partialPan = partialPan;
		}
		public String getExpireMonth() {
			return expireMonth;
		}
		public void setExpireMonth(String expireMonth) {
			this.expireMonth = expireMonth;
		}
		public String getExpireYear() {
			return expireYear;
		}
		public void setExpireYear(String expireYear) {
			this.expireYear = expireYear;
		}
		
	}
	
	public static class Result {
		String code;
		String message;
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
	}
	
}