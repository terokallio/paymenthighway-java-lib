package io.paymenthighway.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Transaction status POJO
 */
public class TransactionStatus {
  UUID id;
  @JsonProperty("acquirer")
  Acquirer acquirer;
  String type;
  String amount;
  @JsonProperty("current_amount")
  String currentAmount;
  String currency;
  String timestamp;
  String modified;
  @JsonProperty("filing_code")
  String filingCode;
  @JsonProperty("authorization_code")
  String authorizationCode;
  String token;
  @JsonProperty("status")
  Status status;
  @JsonProperty("card")
  PartialCard card;
  @JsonProperty("reverts")
  Revert[] reverts;
  Customer customer;
  @JsonProperty("cardholder_authentication")
  String cardholderAuthentication;
  String order;

  public UUID getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public String getAmount() {
    return amount;
  }

  public String getCurrentAmount() {
    return currentAmount;
  }

  public String getCurrency() {
    return currency;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getModified() {
    return modified;
  }

  public String getFilingCode() {
    return filingCode;
  }

  public String getAuthorizationCode() {
    return authorizationCode;
  }

  public String getToken() {
    return token;
  }

  public Acquirer getAcquirer() {
    return acquirer;
  }

  public Status getStatus() {
    return status;
  }

  public PartialCard getCard() {
    return card;
  }

  public Revert[] getReverts() {
    return reverts;
  }

  public Customer getCustomer() {
    return customer;
  }

  public String getCardholderAuthentication() {
    return cardholderAuthentication;
  }

  public String getOrder() {
    return order;
  }
}
