package ch.admin.bit.jeap.audit.transactional.outbox.spring;

import ch.admin.bit.jeap.audit.command.builder.CreateAuditRecordCommandBuilderFactory;
import ch.admin.bit.jeap.audit.transactional.outbox.CreateAuditRecordCommandTransactionOutboxSender;
import ch.admin.bit.jeap.messaging.kafka.KafkaConfiguration;
import ch.admin.bit.jeap.messaging.transactionaloutbox.outbox.TransactionalOutbox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = KafkaConfiguration.class)
@ConditionalOnProperty(name = "jeap.audit.enabled", havingValue = "true", matchIfMissing = true)
public class AuditTransactionalOutboxAutoConfiguration {

    /**
     * @param transactionalOutbox the transactionalOutbox instance
     * @param topic               the topic to send the CreateAuditRecordCommand to
     */
    @Bean
    public CreateAuditRecordCommandTransactionOutboxSender auditRecordCommandTransactionOutboxSender(
            TransactionalOutbox transactionalOutbox,
            CreateAuditRecordCommandBuilderFactory builderFactory,
            @Value("${jeap.audit.transactional-outbox.topic}") String topic) {
        return new CreateAuditRecordCommandTransactionOutboxSender(transactionalOutbox, builderFactory, topic);
    }
}
