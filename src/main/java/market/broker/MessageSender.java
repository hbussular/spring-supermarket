package market.broker;

import market.MarketServiceApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {
    private static final Logger log = LoggerFactory.getLogger(MessageSender.class);
    private final RabbitTemplate rabbitTemplate;

    public MessageSender(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 5000L)
    public void processAllPendingReports() {
        rabbitTemplate.convertAndSend(MarketServiceApp.EXCHANGE_NAME, MarketServiceApp.REPORT_ROUTING_KEY, "");
    }

    @Scheduled(fixedDelay = 10000L)
    public void processAllPendingPayments() {
        rabbitTemplate.convertAndSend(MarketServiceApp.EXCHANGE_NAME, MarketServiceApp.PAYMENT_ROUTING_KEY, "");
    }

    @Scheduled(fixedDelay = 15000L)
    public void processAllAlerts() {
        rabbitTemplate.convertAndSend(MarketServiceApp.EXCHANGE_NAME, MarketServiceApp.ALERT_ROUTING_KEY, "");
    }
}