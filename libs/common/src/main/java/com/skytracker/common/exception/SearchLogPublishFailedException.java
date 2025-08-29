package com.skytracker.common.exception;

public class SearchLogPublishFailedException extends BusinessException {
    public SearchLogPublishFailedException(String detail) {
        super(ErrorCode.SEARCH_LOG_PUBLISH_FAILED, detail);
    }
}