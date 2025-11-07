package ch.admin.bit.jeap.audit.transactional.outbox;

import ch.admin.bit.jeap.audit.record.create.CreateAuditRecordCommand;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.ArrayList;
import java.util.List;

public class AuditCommandConsumer {

    private final List<CreateAuditRecordCommand> commands = new ArrayList<>();

    @KafkaListener(topics = "mysystem-audit")
    public void consume(@Payload CreateAuditRecordCommand command, Acknowledgment ack) {
        commands.add(command);
        ack.acknowledge();
    }

    public boolean hasConsumedCommand() {
        return !commands.isEmpty();
    }

    public CreateAuditRecordCommand getFirstCommand() {
        CreateAuditRecordCommand command = commands.get(0);
        commands.remove(command);
        return command;
    }
}
