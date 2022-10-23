package io.study.coupon.event;

import io.study.coupon.repo.ExhaustedCouponEventRepo;
import io.study.coupon.repo.IssuedCouponEventRepo;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class CouponEventListener {
    private final IssuedCouponEventRepo issuedCouponEventRepo;
    private final ExhaustedCouponEventRepo exhaustedCouponEventRepo;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SS");

    @Async
    @EventListener
    public void onIssuedEventHandler(IssuedCouponEvent issuedCouponEvent) {
        issuedCouponEventRepo.save(issuedCouponEvent);
        log.info("[time : {}][eventId : {}][couponId : {}][잔여 수량 : quantity : {}] : {}",
            issuedCouponEvent.getEventTime().format(formatter),
            issuedCouponEvent.getId(),
            issuedCouponEvent.getCouponId(),
            issuedCouponEvent.getQuantity(),
            issuedCouponEvent.getMessage()
        );
    }

    @Async
    @TransactionalEventListener
    public void onExhaustEventHandler(ExhaustCouponEvent exhaustCouponEvent) {
        exhaustedCouponEventRepo.save(exhaustCouponEvent);
        log.info("[time : {}][eventId : {}][couponId : {}] : {}",
            exhaustCouponEvent.getEventTime().format(formatter),
            exhaustCouponEvent.getId(),
            exhaustCouponEvent.getCouponId(),
            exhaustCouponEvent.getMessage()
        );
    }
}
