package com.example.ssdemoapp.service.sms;

import com.example.ssdemoapp.exception.error.CustomParameterizedException;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.sms.MessageStatus;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.SmsSubmissionResponseMessage;
import com.nexmo.client.sms.messages.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.example.ssdemoapp.util.Constants.*;

@Service
@Slf4j
public class SmsService {

  @Value("${sms.provider.nexmo.api.key}")
  private String apiKey;
  @Value("${sms.provider.nexmo.api.secret}")
  private String apiSecret;
  @Value("${otp.validity.time}")
  private String validateTime;

  public Boolean sendOtp(String mobileNumber, String otp) {
    log.info("send OTP on SMS to : {}", mobileNumber);
    NexmoClient client = new NexmoClient.Builder()
        .apiKey(apiKey)
        .apiSecret(apiSecret)
        .build();

    String messageText = OTP_MESSAGE_PREFIX.concat(otp).concat(" ").concat(OTP_MESSAGE_EXPIRY_MSG).concat(validateTime).concat("Minutes");
    TextMessage message = new TextMessage(OTP_FROM_NEXMO, mobileNumber, messageText);

    SmsSubmissionResponse response = null;
    try {
      response = client.getSmsClient().submitMessage(message);
    } catch (NexmoClientException e) {
      log.error(UNABLE_TO_SEND_SMS, e.getCause());
      throw new CustomParameterizedException(UNABLE_TO_SEND_SMS, HttpStatus.BAD_REQUEST.value());
    }

    if (response == null) {
      throw new CustomParameterizedException(UNABLE_TO_SEND_SMS, HttpStatus.BAD_REQUEST.value());
    }
    SmsSubmissionResponseMessage responseMessage = response.getMessages().get(0);
    return responseMessage.getStatus().equals(MessageStatus.OK);
  }
}
