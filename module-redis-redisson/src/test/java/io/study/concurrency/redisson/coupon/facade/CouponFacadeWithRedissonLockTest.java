package io.study.concurrency.redisson.coupon.facade;

import io.study.concurrency.common.redis.config.AbstractRedisContainerTestBase;
import io.study.concurrency.core.coupon.domain.entity.Coupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Redisson 락을 이용하여 동시에 실행되는 트랜잭션의 실행 순서 제어
 * - Case#1 : 100명의 사용자가 수량이 1개인 쿠폰을 동시에 발급 하는 경우 트랜잭션의 실행 순서 제어
 * - Case#2 : 100명의 사용자가 수량이 100개인 쿠폰을 동시에 발급 하는 경우 트랜잭션의 실행 순서 제어
 */
@DisplayName("Facade:Coupon:Case#5:Redisson Lock")
class CouponFacadeWithRedissonLockTest extends AbstractRedisContainerTestBase {
    private final CouponFacadeWithRedissonLock couponFacadeWithRedissonLock;

    public CouponFacadeWithRedissonLockTest(CouponFacadeWithRedissonLock couponFacadeWithRedissonLock) {
        this.couponFacadeWithRedissonLock = couponFacadeWithRedissonLock;
    }

    @Test
    @DisplayName("Redisson lock을 통해 100명의 사용자가 수량이 1개인 쿠폰을 동시에 발급하는 경우 발생하는 동시성 문제 해결")
    public void concurrentControlWithLettuceSpinLock_WhenCouponWithOneQuantityIsRequested100TimesAtTheSameTime() throws InterruptedException {
        // Given
        Coupon 수량이_1개인_쿠폰 = couponFixtureGenerator.수량이_1개인_쿠폰_저장();

        // When
        동시에_100개의_쿠폰_발급_요청(() -> couponFacadeWithRedissonLock.issueCoupon(수량이_1개인_쿠폰.getId()));

        // Then
        동시에_수량이_1개인_쿠폰_발급시_발생한_동시성_문제_해결_검증(수량이_1개인_쿠폰.getId());
    }

    @Test
    @DisplayName("Redisson lock을 통해 100명의 사용자가 수량이 100개인 쿠폰을 동시에 발급하는 경우 발생하는 동시성 문제 해결")
    public void concurrentControlWithLettuceSpinLock_WhenCouponWithHundredQuantityIsRequested100TimesAtTheSameTime() throws InterruptedException {
        // Given
        Coupon 수량이_100개인_쿠폰 = couponFixtureGenerator.수량이_100개인_쿠폰_저장();

        // When
        동시에_100개의_쿠폰_발급_요청(() -> couponFacadeWithRedissonLock.issueCoupon(수량이_100개인_쿠폰.getId()));

        // Then
        동시에_수량이_100개인_쿠폰_발급시_발생한_동시성_문제_해결_검증(수량이_100개인_쿠폰.getId());
    }
}
