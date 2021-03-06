package io.paymenthighway;

import io.paymenthighway.connect.FormAPIConnection;
import org.apache.http.NameValuePair;
import org.junit.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 */
public class FormBuilderTest {

  Properties props = null;
  private String serviceUrl;
  private String signatureKeyId;
  private String signatureSecret;

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    // required system information and authentication credentials
    // we read this from file, but can be from everywhere
    try {
      this.props = PaymentHighwayUtility.getProperties();
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.serviceUrl = this.props.getProperty("service_url");
    this.signatureKeyId = this.props.getProperty("signature_key_id");
    this.signatureSecret = this.props.getProperty("signature_secret");
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test with acceptCvcRequired set to false.
   */
  @Test
  public void testAddCardParameters() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "https://paymenthighway.fi/index-en.html";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    Boolean acceptCvcRequired = false;

    // create the payment highway request parameters
    FormBuilder formBuilder = new FormBuilder(method,
        this.signatureKeyId, this.signatureSecret, account, merchant,
        this.serviceUrl);

    FormContainer formContainer = formBuilder.generateAddCardParameters(successUrl, failureUrl, cancelUrl, language, acceptCvcRequired);

    List<NameValuePair> nameValuePairs = formContainer.getFields();

    // test that the result has a signature
    Iterator<NameValuePair> it = nameValuePairs.iterator();
    String signature = null;
    while (it.hasNext()) {
      NameValuePair nameValuePair = it.next();
      String name = nameValuePair.getName();

      if (name.equalsIgnoreCase("signature")) {
        signature = nameValuePair.getValue();
      }
    }
    assertNotNull(signature);
    assertTrue(signature.startsWith("SPH1"));
  }

  /**
   * Test with acceptCvcRequired set to true.
   */
  @Test
  public void testAddCardParameters2() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "http://www.solinor.com";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    Boolean acceptCvcRequired = true;

    // create the payment highway request parameters
    FormBuilder formBuilder = new FormBuilder(method,
        this.signatureKeyId, this.signatureSecret, account, merchant,
        this.serviceUrl);

    FormContainer formContainer = formBuilder.generateAddCardParameters(successUrl, failureUrl, cancelUrl, language, acceptCvcRequired);

    // test that Payment Highway accepts this as a request
    FormAPIConnection formApi = new FormAPIConnection(this.serviceUrl,
        this.signatureKeyId, this.signatureSecret);

    String response = null;
    try {
      response = formApi.addCardRequest(formContainer.getFields());
    } catch (IOException e) {
      e.printStackTrace();
    }

    assertNotNull(response);
    assertTrue(response.contains("card_number_formatted"));
    assertTrue(response.contains("Payment Highway"));
  }

  /**
   * Test with acceptCvcRequired, skipFormNotifications, exitIframeOnResult and exitIframeon3ds set to true.
   */
  @Test
  public void testAddCardParameters3() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "http://www.solinor.com";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    Boolean acceptCvcRequired = true;
    Boolean skipFormNotifications = true;
    Boolean exitIframeOnResult = true;
    Boolean exitIframeOn3ds = true;

    // create the payment highway request parameters
    FormBuilder formBuilder = new FormBuilder(method,
            this.signatureKeyId, this.signatureSecret, account, merchant,
            this.serviceUrl);

    FormContainer formContainer = formBuilder.generateAddCardParameters(successUrl, failureUrl, cancelUrl, language,
            acceptCvcRequired, skipFormNotifications, exitIframeOnResult, exitIframeOn3ds);

    // test that Payment Highway accepts this as a request
    FormAPIConnection formApi = new FormAPIConnection(this.serviceUrl,
            this.signatureKeyId, this.signatureSecret);

    String response = null;
    try {
      response = formApi.addCardRequest(formContainer.getFields());
    } catch (IOException e) {
      e.printStackTrace();
    }

    assertNotNull(response);
    assertTrue(response.contains("card_number_formatted"));
    assertTrue(response.contains("Payment Highway"));
  }

  @Test
  public void testAddCardUse3ds() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "http://www.solinor.com";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    Boolean acceptCvcRequired = null;
    Boolean skipFormNotifications = null;
    Boolean exitIframeOnResult = null;
    Boolean exitIframeOn3ds = null;
    Boolean use3ds = false;

    FormBuilder formBuilder = new FormBuilder(method,
        this.signatureKeyId, this.signatureSecret, account, merchant,
        this.serviceUrl);

    FormContainer formContainer = formBuilder.generateAddCardParameters(successUrl, failureUrl, cancelUrl, language,
        acceptCvcRequired, skipFormNotifications, exitIframeOnResult, exitIframeOn3ds, use3ds);

    FormAPIConnection formApi = new FormAPIConnection(this.serviceUrl,
        this.signatureKeyId, this.signatureSecret);

    String response = null;
    try {
      response = formApi.addCardRequest(formContainer.getFields());
    } catch (IOException e) {
      e.printStackTrace();
    }

    assertNotNull(response);
    assertTrue(response.contains("card_number_formatted"));
    assertTrue(response.contains("Payment Highway"));
  }

  /**
   * Test with only the mandatory parameters.
   */
  @Test
  public void testAddCardParameters4() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "https://paymenthighway.fi/index-en.html";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";

    // create the payment highway request parameters
    FormBuilder formBuilder = new FormBuilder(method,
            this.signatureKeyId, this.signatureSecret, account, merchant,
            this.serviceUrl);

    FormContainer formContainer = formBuilder.generateAddCardParameters(successUrl, failureUrl, cancelUrl, language);

    List<NameValuePair> nameValuePairs = formContainer.getFields();

    // test that the result has a signature
    Iterator<NameValuePair> it = nameValuePairs.iterator();
    String signature = null;
    while (it.hasNext()) {
      NameValuePair nameValuePair = it.next();
      String name = nameValuePair.getName();

      if (name.equalsIgnoreCase("signature")) {
        signature = nameValuePair.getValue();
      }
    }
    assertNotNull(signature);
    assertTrue(signature.startsWith("SPH1"));
  }

  @Test
  public void testPaymentParameters() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String amount = "9999";
    String currency = "EUR";
    String orderId = "1000123A";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "https://paymenthighway.fi/index-en.html";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    String description = "this is payment description";

    // create the payment highway request parameters
    FormBuilder formBuilder = new FormBuilder(method,
        this.signatureKeyId, this.signatureSecret, account, merchant,
        this.serviceUrl);

    FormContainer formContainer = formBuilder.generatePaymentParameters(
        successUrl, failureUrl, cancelUrl, language, amount, currency, orderId, description);

    // test that the result has a signature
    Iterator<NameValuePair> it = formContainer.getFields().iterator();
    String signature = null;
    while (it.hasNext()) {
      NameValuePair nameValuePair = it.next();
      String name = nameValuePair.getName();

      if (name.equalsIgnoreCase("signature")) {
        signature = nameValuePair.getValue();
      }
    }
    assertNotNull(signature);
    assertTrue(signature.startsWith("SPH1"));
  }

  /**
   * Test with only the mandatory parameters.
   */
  @Test
  public void testGetPaymentParameters2() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String amount = "9999";
    String currency = "EUR";
    String orderId = "1000123A";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "http://www.solinor.com";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    String description = "this is payment description";

    // create the payment highway request parameters
    FormBuilder formBuilder = new FormBuilder(method,
        this.signatureKeyId, this.signatureSecret, account, merchant,
        this.serviceUrl);

    FormContainer formContainer = formBuilder.generatePaymentParameters(
        successUrl, failureUrl, cancelUrl, language, amount, currency, orderId, description);

    // test that Payment Highway accepts this as a request
    FormAPIConnection formApi = new FormAPIConnection(this.serviceUrl,
        this.signatureKeyId, this.signatureSecret);

    String response = null;
    try {
      response = formApi.paymentRequest(formContainer.getFields());
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertNotNull(response);
    assertTrue(response.contains("card_number_formatted"));
  }

  /**
   * Test with all the optional parameters present.
   */
  @Test
  public void testGetPaymentParameters3() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String amount = "9999";
    String currency = "EUR";
    String orderId = "1000123A";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "http://www.solinor.com";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    String description = "this is payment description";
    Boolean skipFormNotifications = true;
    Boolean exitIframeOnResult = true;
    Boolean exitIframeOn3ds = true;

    // create the payment highway request parameters
    FormBuilder formBuilder = new FormBuilder(method,
            this.signatureKeyId, this.signatureSecret, account, merchant,
            this.serviceUrl);

    FormContainer formContainer = formBuilder.generatePaymentParameters(
            successUrl, failureUrl, cancelUrl, language, amount, currency, orderId, description,
            skipFormNotifications, exitIframeOnResult, exitIframeOn3ds);

    // test that Payment Highway accepts this as a request
    FormAPIConnection formApi = new FormAPIConnection(this.serviceUrl,
            this.signatureKeyId, this.signatureSecret);

    String response = null;
    try {
      response = formApi.paymentRequest(formContainer.getFields());
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertNotNull(response);
    assertTrue(response.contains("card_number_formatted"));
  }

  @Test
  public void testGetPaymentFormWithUse3ds() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String amount = "9999";
    String currency = "EUR";
    String orderId = "1000123A";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "http://www.solinor.com";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    String description = "this is payment description";
    Boolean skipFormNotifications = null;
    Boolean exitIframeOnResult = null;
    Boolean exitIframeOn3ds = null;
    Boolean use3ds = true;

    FormBuilder formBuilder = new FormBuilder(method,
        this.signatureKeyId, this.signatureSecret, account, merchant,
        this.serviceUrl);

    FormContainer formContainer = formBuilder.generatePaymentParameters(
        successUrl, failureUrl, cancelUrl, language, amount, currency, orderId, description,
        skipFormNotifications, exitIframeOnResult, exitIframeOn3ds, use3ds);

    FormAPIConnection formApi = new FormAPIConnection(this.serviceUrl,
        this.signatureKeyId, this.signatureSecret);

    String response = null;
    try {
      response = formApi.paymentRequest(formContainer.getFields());
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertNotNull(response);
    assertTrue(response.contains("card_number_formatted"));
  }

  @Test
  public void testGetAddCardAndPaymentParameters() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String amount = "9999";
    String currency = "EUR";
    String orderId = "1000123A";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "https://paymenthighway.fi/index-en.html";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    String description = "this is payment description";

    // create the payment highway request parameters
    FormBuilder formBuilder = new FormBuilder(method,
        this.signatureKeyId, this.signatureSecret, account, merchant,
        this.serviceUrl);

    FormContainer formContainer = formBuilder.generateAddCardAndPaymentParameters(
            successUrl, failureUrl, cancelUrl, language, amount, currency, orderId, description);

    // test that the result has a signature
    Iterator<NameValuePair> it = formContainer.getFields().iterator();
    String signature = null;
    while (it.hasNext()) {
      NameValuePair nameValuePair = it.next();
      String name = nameValuePair.getName();

      if (name.equalsIgnoreCase("signature")) {
        signature = nameValuePair.getValue();
      }
    }
    assertNotNull(signature);
    assertTrue(signature.startsWith("SPH1"));
  }

  /**
   * Test without optional parameters.
   */
  @Test
  public void testGetAddCardAndPaymentParameters2() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String amount = "9999";
    String currency = "EUR";
    String orderId = "1000123A";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "http://www.solinor.com";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    String description = "this is payment description";

    // create the payment highway request parameters
    FormBuilder formBuilder = new FormBuilder(method,
        this.signatureKeyId, this.signatureSecret, account, merchant,
        this.serviceUrl);

    FormContainer formContainer = formBuilder.generateAddCardAndPaymentParameters(
            successUrl, failureUrl, cancelUrl, language, amount, currency, orderId, description);

    // test that Payment Highway accepts this as a request
    FormAPIConnection formApi = new FormAPIConnection(this.serviceUrl,
        this.signatureKeyId, this.signatureSecret);

    String response = null;
    try {
      response = formApi.addCardAndPayRequest(formContainer.getFields());
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertNotNull(response);
    assertTrue(response.contains("card_number_formatted"));
  }

  /**
   * Test with optional parameters present.
   */
  @Test
  public void testGetAddCardAndPaymentParameters3() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String amount = "9999";
    String currency = "EUR";
    String orderId = "1000123A";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "http://www.solinor.com";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    String description = "this is payment description";
    Boolean skipFormNotifications = true;
    Boolean exitIframeOnResult = true;
    Boolean exitIframeOn3ds = true;

    // create the payment highway request parameters
    FormBuilder formBuilder = new FormBuilder(method,
            this.signatureKeyId, this.signatureSecret, account, merchant,
            this.serviceUrl);

    FormContainer formContainer = formBuilder.generateAddCardAndPaymentParameters(
            successUrl, failureUrl, cancelUrl, language, amount, currency, orderId, description,
            skipFormNotifications, exitIframeOnResult, exitIframeOn3ds);

    // test that Payment Highway accepts this as a request
    FormAPIConnection formApi = new FormAPIConnection(this.serviceUrl,
            this.signatureKeyId, this.signatureSecret);

    String response = null;
    try {
      response = formApi.addCardAndPayRequest(formContainer.getFields());
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertNotNull(response);
    assertTrue(response.contains("card_number_formatted"));
  }

  @Test
  public void testGetAddCardAndPaymentWithUse3ds() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String amount = "9999";
    String currency = "EUR";
    String orderId = "1000123A";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "http://www.solinor.com";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    String description = "this is payment description";
    Boolean skipFormNotifications = true;
    Boolean exitIframeOnResult = true;
    Boolean exitIframeOn3ds = true;
    Boolean use3ds = true;

    // create the payment highway request parameters
    FormBuilder formBuilder = new FormBuilder(method,
        this.signatureKeyId, this.signatureSecret, account, merchant,
        this.serviceUrl);

    FormContainer formContainer = formBuilder.generateAddCardAndPaymentParameters(
        successUrl, failureUrl, cancelUrl, language, amount, currency, orderId, description,
        skipFormNotifications, exitIframeOnResult, exitIframeOn3ds, use3ds);

    // test that Payment Highway accepts this as a request
    FormAPIConnection formApi = new FormAPIConnection(this.serviceUrl,
        this.signatureKeyId, this.signatureSecret);

    String response = null;
    try {
      response = formApi.addCardAndPayRequest(formContainer.getFields());
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertNotNull(response);
    assertTrue(response.contains("card_number_formatted"));
  }

  /**
   * Test without optional parameters.
   */
  @Test
  public void testGetPayWithTokenAndCvcParameters() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String amount = "9999";
    String currency = "EUR";
    String orderId = "1000123A";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "http://www.solinor.com";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    String description = "this is payment description";
    UUID token = UUID.fromString("71435029-fbb6-4506-aa86-8529efb640b0");

    // create the payment highway request parameters
    FormBuilder formBuilder = new FormBuilder(method,
            this.signatureKeyId, this.signatureSecret, account, merchant,
            this.serviceUrl);

    FormContainer formContainer = formBuilder.generatePayWithTokenAndCvcParameters(
            token, successUrl, failureUrl, cancelUrl, language, amount, currency, orderId, description);

    // test that Payment Highway accepts this as a request
    FormAPIConnection formApi = new FormAPIConnection(this.serviceUrl,
            this.signatureKeyId, this.signatureSecret);

    String response = null;
    try {
      response = formApi.payWithTokenAndCvcRequest(formContainer.getFields());
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertNotNull(response);
    assertTrue(response.contains("card_number_formatted"));
  }

  /**
   * Test with optional parameters present.
   */
  @Test
  public void testGetPayWithTokenAndCvcParameters2() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String amount = "9999";
    String currency = "EUR";
    String orderId = "1000123A";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "http://www.solinor.com";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    String description = "this is payment description";
    UUID token = UUID.fromString("71435029-fbb6-4506-aa86-8529efb640b0");
    Boolean skipFormNotifications = true;
    Boolean exitIframeOnResult = true;
    Boolean exitIframeOn3ds = true;

    // create the payment highway request parameters
    FormBuilder formBuilder = new FormBuilder(method,
            this.signatureKeyId, this.signatureSecret, account, merchant,
            this.serviceUrl);

    FormContainer formContainer = formBuilder.generatePayWithTokenAndCvcParameters(
            token, successUrl, failureUrl, cancelUrl, language, amount, currency, orderId, description,
            skipFormNotifications, exitIframeOnResult, exitIframeOn3ds);

    // test that Payment Highway accepts this as a request
    FormAPIConnection formApi = new FormAPIConnection(this.serviceUrl,
            this.signatureKeyId, this.signatureSecret);

    String response = null;
    try {
      response = formApi.payWithTokenAndCvcRequest(formContainer.getFields());
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertNotNull(response);
    assertTrue(response.contains("card_number_formatted"));
  }

  @Test
  public void testGetPayWithTokenAndCvcWithUse3ds() {

    String method = "POST";
    String account = "test";
    String merchant = "test_merchantId";
    String amount = "9999";
    String currency = "EUR";
    String orderId = "1000123A";
    String successUrl = "https://www.paymenthighway.fi/";
    String failureUrl = "http://www.solinor.com";
    String cancelUrl = "https://solinor.fi";
    String language = "EN";
    String description = "this is payment description";
    UUID token = UUID.fromString("71435029-fbb6-4506-aa86-8529efb640b0");
    Boolean skipFormNotifications = false;
    Boolean exitIframeOnResult = null;
    Boolean exitIframeOn3ds = false;
    Boolean use3ds = false;

    // create the payment highway request parameters
    FormBuilder formBuilder = new FormBuilder(method,
        this.signatureKeyId, this.signatureSecret, account, merchant,
        this.serviceUrl);

    FormContainer formContainer = formBuilder.generatePayWithTokenAndCvcParameters(
        token, successUrl, failureUrl, cancelUrl, language, amount, currency, orderId, description,
        skipFormNotifications, exitIframeOnResult, exitIframeOn3ds, use3ds);

    // test that Payment Highway accepts this as a request
    FormAPIConnection formApi = new FormAPIConnection(this.serviceUrl,
        this.signatureKeyId, this.signatureSecret);

    String response = null;
    try {
      response = formApi.payWithTokenAndCvcRequest(formContainer.getFields());
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertNotNull(response);
    assertTrue(response.contains("card_number_formatted"));
  }
}
