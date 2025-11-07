package ch.admin.bit.jeap.audit.transactional.outbox;

import ch.admin.bit.jeap.audit.record.create.CreateAuditRecordCommand;
import ch.admin.bit.jeap.messaging.annotations.JeapMessageConsumerContract;
import ch.admin.bit.jeap.messaging.annotations.JeapMessageProducerContract;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@JeapMessageProducerContract(value = CreateAuditRecordCommand.TypeRef.class, topic = "mysystem-audit")
@JeapMessageConsumerContract(value = CreateAuditRecordCommand.TypeRef.class, topic = "mysystem-audit")
class TestApp {
}
