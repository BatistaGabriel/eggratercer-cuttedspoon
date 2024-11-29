package com.bobathief.UrlRedirector;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Slf4j
public final class Main
  implements RequestHandler<Map<String, Object>, Map<String, Object>> {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final S3Client s3Client = S3Client.builder().build();
  private static final int MILLISECONDS = 1000;

  @Override
  public Map<String, Object> handleRequest(
    final Map<String, Object> input,
    final Context context
  ) {
    String pathParams = (String) input.get("rawPath");
    String shortUrlCode = pathParams != null ? pathParams.replace("/", "") : "";

    if (shortUrlCode.isEmpty()) {
      throw new IllegalArgumentException("Invalid input: 'shortUrlCode' is required.");
    }

    InputStream s3ObjectStream;
    try {
      GetObjectRequest request = GetObjectRequest
        .builder()
        .bucket("siamang-nihonium")
        .key(String.format("%s.json", shortUrlCode))
        .build();

      s3ObjectStream = s3Client.getObject(request);
    } catch (Exception exception) {
      log.error("Error fetching URL data from S3: {}", exception.getMessage());
      throw new RuntimeException("Error fetching URL data from S3", exception);
    }

    UrlData urlData;
    try {
      urlData = objectMapper.readValue(s3ObjectStream, UrlData.class);
    } catch (Exception exception) {
      log.error("Error deserializing URL data: {}", exception.getMessage());
      throw new RuntimeException("Error deserializing URL data", exception);
    }

    Map<String, Object> response = new HashMap<>();
    long currentTimeInSeconds = System.currentTimeMillis() / MILLISECONDS;

    if (urlData.getExpirationDate() < currentTimeInSeconds) {
      response.put("statusCode", "410");
      response.put("body", "Oh Snap! This URL has expired.");
      return response;
    }

    response.put("statusCode", "302");
    Map<String, String> headers = new HashMap<>();
    headers.put("Location", urlData.getOriginalUrl());
    response.put("headers", headers);

    return response;
  }
}
