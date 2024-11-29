package com.bobathief.UrlShortener;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public final class Main implements RequestHandler<
    Map<String, Object>,
    Map<String, String>> {
    /**
    * ObjectMapper instance.
    */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
    * S3Client instance.
    */
    private final S3Client s3Client = S3Client.builder().build();

    /**
    * Defines the max size of the url code.
    */
    private static final int MAX_URL_CODE_SIZE = 8;

    @Override
    public Map<String, String> handleRequest(
        final Map<String, Object> input,
        final Context context
    ) {
        String body = input.get("body").toString();

        Map<String, String> bodyMap;
        try {
            bodyMap = objectMapper.readValue(body, Map.class);
        } catch (Exception exception) {
            throw new RuntimeException(
            String.format(
                "Error parsing JSON body: %s",
                exception.getMessage()
            ),
            exception
            );
        }

        String originalUrl = bodyMap.get("originalUrl");
        String expirationTime = bodyMap.get("expirationTime");
        long expirationTimeInSeconds = Long.parseLong(expirationTime);
        String shortUrlCode = UUID.randomUUID()
                .toString()
                .substring(0, MAX_URL_CODE_SIZE);

        UrlData urlData = new UrlData(originalUrl, expirationTimeInSeconds);

        try {
            String urlDataJson = objectMapper.writeValueAsString(urlData);

            PutObjectRequest request = PutObjectRequest
            .builder()
            .bucket("siamang-nihonium")
            .key(String.format("%s.json", shortUrlCode))
            .build();

            s3Client.putObject(request, RequestBody.fromString(urlDataJson));
        } catch (Exception exception) {
            throw new RuntimeException(
            String.format(
                "Error saving data to S3: %s",
                exception.getMessage()
            ),
            exception
            );
        }

        Map<String, String> response = new HashMap<>();
        response.put("urlCode", shortUrlCode);

        return response;
    }
}
