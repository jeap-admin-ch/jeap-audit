package ch.admin.bit.jeap.audit.transactional.outbox.spring;

import ch.admin.bit.jeap.messaging.kafka.KafkaConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;

@AutoConfiguration(before = KafkaConfiguration.class)
@ConditionalOnProperty(name = "jeap.audit.enabled", havingValue = "true", matchIfMissing = true)
@Import(AuditTransactionalOutboxSenderBeanRegistrar.class)
public class AuditTransactionalOutboxAutoConfiguration {
}
