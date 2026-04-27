package com.example.cbrmonitoringspring.integration.config;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class SimpleFeignLogger extends Logger {
    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    @Override
    protected void log(String configKey, String format, Object... args) {
        log.info("[Feign:{}] {}", configKey, String.format(format, args));
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        byte[] body = request.body();
        log.info(
                "[Feign:{}] Request: method={}, url={}, headers={}, body={}",
                configKey,
                request.httpMethod(),
                request.url(),
                maskSensitiveHeaders(request.headers()),
                decodeBody(body, request.charset())
        );
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime)
            throws IOException {
        byte[] body = response.body() == null ? new byte[0] : Util.toByteArray(response.body().asInputStream());

        log.info(
                "[Feign:{}] Response: status={}, reason={}, durationMs={}, headers={}, body={}",
                configKey,
                response.status(),
                response.reason(),
                elapsedTime,
                maskSensitiveHeaders(response.headers()),
                decodeBody(body, DEFAULT_CHARSET)
        );

        return response.toBuilder().body(body).build();
    }

    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        log.error(
                "[Feign:{}] I/O error after {} ms: {}",
                configKey,
                elapsedTime,
                ioe.getMessage(),
                ioe
        );
        return ioe;
    }

    private String decodeBody(byte[] body, Charset charset) {
        if (body == null || body.length == 0) {
            return "<empty>";
        }

        Charset resolvedCharset = charset == null ? DEFAULT_CHARSET : charset;
        return new String(body, resolvedCharset);
    }

    private Map<String, Collection<String>> maskSensitiveHeaders(Map<String, Collection<String>> headers) {
        Map<String, Collection<String>> maskedHeaders = new LinkedHashMap<>(headers);
        for (String headerName : headers.keySet()) {
            if (API_KEY_HEADER.equalsIgnoreCase(headerName)) {
                maskedHeaders.put(headerName, java.util.List.of("***masked***"));
            }
        }
        return maskedHeaders;
    }
}