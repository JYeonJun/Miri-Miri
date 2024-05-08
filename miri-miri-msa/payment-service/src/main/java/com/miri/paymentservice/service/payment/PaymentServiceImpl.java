package com.miri.paymentservice.service.payment;

import com.miri.coremodule.dto.kafka.OrderUpdateEventDto;
import com.miri.coremodule.dto.kafka.PaymentRequestEventDto;
import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
import com.miri.coremodule.vo.KafkaVO;
import com.miri.paymentservice.domain.payment.Payment;
import com.miri.paymentservice.domain.payment.PaymentRepository;
import com.miri.paymentservice.domain.payment.PaymentStatus;
import com.miri.paymentservice.service.kafka.KafkaSender;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final Random random = new Random();

    private final KafkaSender kafkaSender;

    public PaymentServiceImpl(PaymentRepository paymentRepository, KafkaSender kafkaSender) {
        this.paymentRepository = paymentRepository;
        this.kafkaSender = kafkaSender;
    }

    @Override
    @Transactional
    public void processPaymentEvent(PaymentRequestEventDto paymentRequestEventDto) {

        // 결제 화면 (Payment 객체 생성)
        Payment payment = enterPaymentScreen(paymentRequestEventDto);

        if (!attemptPayment(payment) || !processPayment(payment)) {
            handlePaymentAborted(paymentRequestEventDto);
        }
    }

    private Payment enterPaymentScreen(PaymentRequestEventDto paymentRequestEventDto) {
        return paymentRepository.save(Payment.createFromPaymentRequestEventDto(paymentRequestEventDto));
    }

    private boolean attemptPayment(Payment payment) {
        return processChangePaymentStatus("고객 변심 이탈", payment, PaymentStatus.PAYMENT_IN_PROGRESS);
    }

    private boolean processPayment(Payment payment) {
        return processChangePaymentStatus("결제 실패 이탈", payment, PaymentStatus.PAYMENT_COMPLETED);
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

    private void handlePaymentAborted(PaymentRequestEventDto paymentRequestEventDto) {
        // 주문 상태 변경 이벤트 발행
        kafkaSender.sendOrderUpdateRequestEvent(KafkaVO.ORDER_UPDATE_TOPIC, new OrderUpdateEventDto(paymentRequestEventDto));
        // 상품 재고 롤백 이벤트 발행
        kafkaSender.sendRollbackRequestEvent(KafkaVO.STOCK_ROLLBACK_TOPIC, new StockRollbackEventDto(paymentRequestEventDto));
    }
}