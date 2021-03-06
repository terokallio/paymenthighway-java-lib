package io.paymenthighway.connect;

import io.paymenthighway.security.SecureSigner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * PaymentHighway Form API Connections
 */
public class FormAPIConnection {

  private final static String USER_AGENT = "PaymentHighway Java Lib";
  private final static String CONTENT_TYPE = "application/x-www-form-urlencoded";
  private final static String METHOD_POST = "POST";

  String serviceUrl = null;
  String signatureKeyId = null;
  String signatureSecret = null;

  /**
   * Constructor
   */
  public FormAPIConnection(String serviceUrl, String signatureKeyId, String signatureSecret) {

    this.serviceUrl = serviceUrl;
    this.signatureKeyId = signatureKeyId;
    this.signatureSecret = signatureSecret;
  }

  /**
   * Form API call to add card
   *
   * @return String responseBody
   * @throws IOException
   */
  public String addCardRequest(List<NameValuePair> nameValuePairs) throws IOException {
    final String formUri = "/form/view/add_card";

    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost(this.serviceUrl + formUri);

      String signature = this.createSignature(METHOD_POST, formUri, nameValuePairs);
      nameValuePairs.add(new BasicNameValuePair("signature", signature));

      this.addHeaders(httpPost);

      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

      ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
        public String handleResponse(
            final HttpResponse response) throws ClientProtocolException, IOException {
          int status = response.getStatusLine().getStatusCode();
          if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
          } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
          }
        }

      };
      return httpclient.execute(httpPost, responseHandler);

    }
  }

  /**
   * Form API call to make a payment
   *
   * @return
   * @throws IOException
   */
  public String paymentRequest(List<NameValuePair> nameValuePairs) throws IOException {
    final String formPaymentUri = "/form/view/pay_with_card";

    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost(this.serviceUrl + formPaymentUri);

      String signature = this.createSignature(METHOD_POST, formPaymentUri, nameValuePairs);
      nameValuePairs.add(new BasicNameValuePair("signature", signature));

      this.addHeaders(httpPost);

      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

      ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

        public String handleResponse(
            final HttpResponse response) throws IOException {
          int status = response.getStatusLine().getStatusCode();
          if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
          } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
          }
        }

      };
      return httpclient.execute(httpPost, responseHandler);

    }
  }

  /**
   * Form API call to add card and make a payment
   *
   * @return
   * @throws IOException
   */
  public String addCardAndPayRequest(List<NameValuePair> nameValuePairs) throws IOException {

    final String formPaymentUri = "/form/view/add_and_pay_with_card";

    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost(this.serviceUrl + formPaymentUri);

      String signature = this.createSignature(METHOD_POST, formPaymentUri, nameValuePairs);
      nameValuePairs.add(new BasicNameValuePair("signature", signature));

      this.addHeaders(httpPost);

      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

      ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

        public String handleResponse(final HttpResponse response) throws IOException {
          int status = response.getStatusLine().getStatusCode();
          if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
          } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
          }
        }

      };
      return httpclient.execute(httpPost, responseHandler);

    }
  }

  /**
   * Form API call to pay with a tokenized card and a CVC
   *
   * @param nameValuePairs The Form API http parameters
   * @return
   * @throws IOException
   */
  public String payWithTokenAndCvcRequest(List<NameValuePair> nameValuePairs) throws IOException {

    final String formPaymentUri = "/form/view/pay_with_token_and_cvc";

    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost(this.serviceUrl + formPaymentUri);

      String signature = this.createSignature(METHOD_POST, formPaymentUri, nameValuePairs);
      nameValuePairs.add(new BasicNameValuePair("signature", signature));

      this.addHeaders(httpPost);

      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

      ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

        public String handleResponse(final HttpResponse response) throws IOException {
          int status = response.getStatusLine().getStatusCode();
          if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
          } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
          }
        }

      };
      return httpclient.execute(httpPost, responseHandler);

    }
  }

  /**
   * Create a secure signature
   *
   * @param method
   * @param uri
   * @return String signature
   */
  private String createSignature(String method, String uri, List<NameValuePair> nameValuePairs) {
    SecureSigner ss = new SecureSigner(this.signatureKeyId, this.signatureSecret);
    return ss.createSignature(method, uri, nameValuePairs, "");
  }

  /**
   * Add headers to request
   *
   * @param httpPost
   */
  protected void addHeaders(HttpPost httpPost) {
    httpPost.addHeader("User-Agent", USER_AGENT);
    httpPost.addHeader("Content-Type", CONTENT_TYPE);
  }
}
