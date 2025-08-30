package com.skytracker.common.exception.integrations;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class DistributedLockTimeoutException extends BusinessException {

    public DistributedLockTimeoutException(String detail) {
        super(ErrorCode.LOCK_TIMEOUT, detail);
    }

}
