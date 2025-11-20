package com.skytracker.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.skytracker.entity.FlightAlert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.skytracker.entity.QFlightAlert.flightAlert;
import static com.skytracker.entity.QUser.user;
import static com.skytracker.entity.QUserFlightAlert.userFlightAlert;

@Repository
@RequiredArgsConstructor
public class FlightAlertRepositoryCustomImpl implements FlightAlertCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<FlightAlert> findFlightAlertByUserIdAndAlertId(Long userId, Long alertId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(flightAlert)
                .join(flightAlert.userFlightAlerts, userFlightAlert)
                .join(userFlightAlert.user, user)
                .where(
                        user.id.eq(userId),
                        flightAlert.id.eq(alertId)
                )
                .fetchOne());
    }
}
