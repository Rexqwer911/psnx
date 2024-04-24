package com.rexqwer.psnx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    @Value("${secretKey}")
    private String secret;

    private static final HmacAlgorithms HMAC_SHA256_ALGORITHM = HmacAlgorithms.HMAC_SHA_256;

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
     */
    public String generateHmacSHA256Hash(String data) {
        return new HmacUtils(HMAC_SHA256_ALGORITHM, secret).hmacHex(data);
    }
}
