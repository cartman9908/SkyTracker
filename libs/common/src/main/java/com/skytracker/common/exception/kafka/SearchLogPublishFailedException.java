package com.skytracker.common.exception.kafka;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class SearchLogPublishFailedException extends BusinessException {
    public SearchLogPublishFailedException(String detail) {
        super(ErrorCode.SEARCH_LOG_PUBLISH_FAILED, detail);
    }
}