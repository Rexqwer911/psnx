package com.rexqwer.psnx;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MainService {

    @Value("${secretKey}")
    private String secret;

    /**
     * Генерирует отсортированную строку из переданных параметров запроса.
     *
     * @param paramMap параметры
     * @return отсортированная строка параметров в виде "name1=value1&name2=value2..."
     */
    public String generateSortedParamsString(Map<String, String[]> paramMap) {
        List<String> sortedKeys = new ArrayList<>(paramMap.keySet());
        Collections.sort(sortedKeys);

        StringBuilder data = new StringBuilder();
        for (String key : sortedKeys) {
            data.append(key).append("=").append(paramMap.get(key)[0]).append("&");
        }
        data.deleteCharAt(data.length() - 1);
        return data.toString();
    }

    /**
     * Генерация хеша HMAC SHA256.
     *
     * @param data строка для хеширования
     * @return хешированная строка
     * @throws Exception в случае ошибки при генерации хеша
     */
    public String generateHmacSHA256Hash(String data) throws Exception {
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256HMAC.init(secretKey);
            byte[] hashByte = sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hashByte);
        } catch (Exception e) {
            log.error("Ошибка при генерации HMAC SHA256 hash", e);
            throw new Exception("Ошибка при генерации HMAC SHA256 hash", e);
        }
    }
}
