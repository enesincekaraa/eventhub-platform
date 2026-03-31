package com.enesincekara.eventhub.common.error;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        LocalDateTime timestamp,
        int status,
        String message,
        Map<String,String> errors
) {
}
