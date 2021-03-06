package io.paymenthighway.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Commit Transaction request POJO
 */
public class CommitTransactionResponse extends Response {

  @JsonProperty("card_token")
  UUID cardToken;
  PartialCard card;
  Customer customer;
  @JsonProperty("cardholder_authentication")
  String cardholderAuthentication;
  @JsonProperty("filing_code")
  private String filingCode;

  public UUID getCardToken() {
    return cardToken;
  }

  public PartialCard getCard() {
    return this.card;
  }

  public Customer getCustomer() {
    return customer;
  }

  public String getCardholderAuthentication() {
    return cardholderAuthentication;
  }

  public String getFilingCode() {
    return filingCode;
  }
}
