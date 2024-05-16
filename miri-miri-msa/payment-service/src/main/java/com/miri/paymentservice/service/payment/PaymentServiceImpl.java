package com.miri.paymentservice.service.payment;

import com.miri.coremodule.dto.kafka.PaymentRequestEventDto;
import com.miri.paymentservice.domain.payment.Payment;
import com.miri.paymentservice.domain.payment.PaymentRepository;
import com.miri.paymentservice.domain.payment.PaymentStatus;
import com.miri.paymentservice.event.PaymentAbortedEvent;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final Random random = new Random();
    private final ApplicationEventPublisher applicationEventPublisher;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              ApplicationEventPublisher applicationEventPublisher) {
        this.paymentRepository = paymentRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public void processPaymentEvent(PaymentRequestEventDto paymentRequestEventDto) {

        // 결제 화면 (Payment 객체 생성)
        Payment payment = enterPaymentScreen(paymentRequestEventDto);

        if (!attemptPayment(payment) || !processPayment(payment)) {
            log.debug("traceId={}, PaymentAbortedEvent 발행", paymentRequestEventDto.getTraceId());
            applicationEventPublisher.publishEvent(new PaymentAbortedEvent(this, paymentRequestEventDto));
        }
        log.debug("traceId={}, 결제 완료", paymentRequestEventDto.getTraceId());
    }

    @Override
    @Transactional
    public void updatePaymentStatusOnCancelOrder(Long orderId) {
        paymentRepository.updatePaymentStatusByOrderId(orderId, PaymentStatus.REFUNDED);
    }

    private Payment enterPaymentScreen(PaymentRequestEventDto paymentRequestEventDto) {
        return paymentRepository.save(Payment.createFromPaymentRequestEventDto(paymentRequestEventDto));
    }

    private boolean attemptPayment(Payment payment) {
        return processChangePaymentStatus("고객 변심 이탈", payment, PaymentStatus.PAYMENT_IN_PROGRESS);
    }

    private boolean processPayment(Payment payment) {
        return processChangePaymentStatus("결제 중 이탈", payment, PaymentStatus.PAYMENT_COMPLETED);
    }

    private boolean processChangePaymentStatus(String failLogMessage, Payment payment, PaymentStatus paymentStatus) {
        // 20% 확률로 결제 이탈 상황 가정
        if (random.nextDouble() < 0.2) {
            log.debug(failLogMessage);
            payment.changePaymentStatus(PaymentStatus.PAYMENT_ABORTED);
            return false;
        }
        // 이탈하지 않았다면 결제 상태 변경
        payment.changePaymentStatus(paymentStatus);
        return true;
    }
}