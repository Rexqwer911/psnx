package com.rexqwer.psnx;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class MainController {

    @Value("${token}")
    private String tkn;

    private final MainService mainService;

    @PostMapping("hashFormdataParameters/{operationId}")
    public ResponseEntity<ResponseDTO> hashFormdataParameters(
            HttpServletRequest request,
            @RequestHeader("Token") Optional<String> token) {
        
        if (!token.isPresent() || !token.get().equals(tkn)) {
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ResponseDTO.builder()
                    .status(ResponseStatus.fail)
                    .result(Result.builder().build())
                    .build());
        }

        Map<String, String[]> paramMap = request.getParameterMap();

        if (paramMap.size() == 0) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO.builder()
                    .status(ResponseStatus.fail)
                    .result(Result.builder().build())
                    .build());
        }

        try {
            return ResponseEntity.ok(ResponseDTO.builder()
                .status(ResponseStatus.success)
                .result(Result.builder()
                    .signature(mainService.generateHmacSHA256Hash(mainService.generateSortedParamsString(paramMap)))
                    .build())
                .build());
        } catch (Exception e) {
            return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ResponseDTO.builder()
                .status(ResponseStatus.fail)
                .result(Result.builder().build())
                .build());
        }
    }

    @Data
    @Builder
    public static class ResponseDTO {
    
        private ResponseStatus status;

        private Result result;
        
    }

    @Data
    @Builder
    public static class Result {
        private String signature;
    }

    public enum ResponseStatus {
        success,
        fail
    }
}
