package com.skytracker.repository.custom;


import com.skytracker.entity.FlightAlert;

import java.util.Optional;

public interface FlightAlertCustom {

    Optional<FlightAlert> findFlightAlertByUserIdAndAlertId(Long userId, Long alertId);
}
