package market.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import market.MarketServiceApp;
import market.dto.model.payment.PaymentReadDto;
import market.dto.model.report.ReportReadDto;
import market.service.AlertService;
import market.service.CustomerOrderService;
import market.service.PaymentService;
import market.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageListener {
    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @Autowired
    PaymentService paymentService;

    @Autowired
    ReportService reportService;

    @Autowired
    CustomerOrderService customerOrderService;

    @Autowired
    AlertService alertService;

    @RabbitListener(queues = MarketServiceApp.QUEUE_PAYMENT_NAME)
    public void receiveMessageToProcessPayments(final Message message) {
        log.info("Received message as a generic AMQP 'Message' wrapper: {}", message.toString());
        List<PaymentReadDto> paymentReadDtoList = paymentService.getPendingPayments(10);

        for (PaymentReadDto payment : paymentReadDtoList) {
            customerOrderService.checkout(payment.getCustomerOrderId(), payment.getId());
            log.info(String.format("Payment ID {%s} has been successfully processed.", payment.getId().toString()));
        }
    }

    @RabbitListener(queues = MarketServiceApp.QUEUE_REPORT_NAME)
    public void receiveMessageToProcessReports(final Message message) throws JsonProcessingException {
        log.info("Received message and deserialized to 'CustomMessage': {}", message.toString());
        List<ReportReadDto> reportReadDtoList = reportService.getPendingReports(10);

        for (ReportReadDto report : reportReadDtoList) {
            reportService.processPendingReports();
            log.info(String.format("Report ID {%s} has been successfully processed.", report.getId().toString()));
        }
    }

    @RabbitListener(queues = MarketServiceApp.QUEUE_ALERT_NAME)
    public void receiveMessageToProcessAlerts(final Message message) {
        log.info("Received message and deserialized to 'CustomMessage': {}", message.toString());
        alertService.processAlerts();
    }
}