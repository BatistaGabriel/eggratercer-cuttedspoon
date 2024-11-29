package com.bobathief.UrlShortener;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public final class Main
  implements RequestHandler<Map<String, Object>, Map<String, String>> {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final S3Client S3_CLIENT = S3Client.builder().build();
  private static final int MAX_URL_CODE_SIZE = 8;
  private static final String BUCKET_NAME = "siamang-nihonium";
  private static final Pattern NUMERIC_PATTERN = Pattern.compile("\\d+");

  @Override
  public Map<String, String> handleRequest(
    final Map<String, Object> input,
    final Context context
  ) {
    String body = (String) input.get("body");

    Map<String, String> bodyMap = parseRequestBody(body);
    validateBodyMap(bodyMap);

    String originalUrl = bodyMap.get("originalUrl");
    long expirationTimeInSeconds = parseExpirationTime(bodyMap.get("expirationTime"));
    String shortUrlCode = generateShortUrlCode();

    UrlData urlData = new UrlData(originalUrl, expirationTimeInSeconds);
    saveUrlDataToS3(shortUrlCode, urlData);

    return createResponse(shortUrlCode);
  }

  private Map<String, String> parseRequestBody(String body) {
    try {
      return OBJECT_MAPPER.readValue(body, Map.class);
    } catch (Exception exception) {
      throw new RuntimeException(
        "Error parsing JSON body: " + exception.getMessage(),
        exception
      );
    }
  }

  private void validateBodyMap(Map<String, String> bodyMap) {
    if (bodyMap.get("originalUrl") == null || bodyMap.get("expirationTime") == null) {
      throw new RuntimeException(
        "Missing required fields: originalUrl or expirationTime."
      );
    }
  }

  private long parseExpirationTime(String expirationTime) {
    if (expirationTime == null || !NUMERIC_PATTERN.matcher(expirationTime).matches()) {
      throw new RuntimeException("Invalid expiration time format.");
    }
    return Long.parseLong(expirationTime);
  }

  private String generateShortUrlCode() {
    return UUID.randomUUID().toString().substring(0, MAX_URL_CODE_SIZE);
  }

  private void saveUrlDataToS3(String shortUrlCode, UrlData urlData) {
    try {
      String urlDataJson = OBJECT_MAPPER.writeValueAsString(urlData);
      PutObjectRequest request = PutObjectRequest
        .builder()
        .bucket(BUCKET_NAME)
        .key(shortUrlCode + ".json")
        .build();
      S3_CLIENT.putObject(request, RequestBody.fromString(urlDataJson));
    } catch (Exception exception) {
      throw new RuntimeException(
        "Error saving data to S3: " + exception.getMessage(),
        exception
      );
    }
  }

  private Map<String, String> createResponse(String shortUrlCode) {
    Map<String, String> response = new HashMap<>();
    response.put("urlCode", shortUrlCode);
    return response;
  }
}
