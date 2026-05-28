package ch.admin.bit.jeap.audit.transactional.outbox;

import ch.admin.bit.jeap.audit.command.builder.CreateAuditRecordCommandBuilder;
import ch.admin.bit.jeap.audit.record.create.AuditEventType;
import ch.admin.bit.jeap.audit.record.create.CreateAuditRecordCommand;
import ch.admin.bit.jeap.messaging.kafka.test.KafkaIntegrationTestBase;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApp.class,
        properties = {
                "jeap.audit.transactional-outbox.topics[0]=" + AuditMultiTopicIT.PRIMARY_TOPIC,
                "jeap.audit.transactional-outbox.topics[1]=" + AuditMultiTopicIT.SECONDARY_TOPIC,
                "jeap.messaging.kafka.service-name=todo",
                "jeap.messaging.kafka.system-name=test-system"
        }
)
@Import(AuditMultiTopicIT.TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuditMultiTopicIT extends KafkaIntegrationTestBase {

    static final String PRIMARY_TOPIC = "mysystem-audit";
    static final String SECONDARY_TOPIC = "mysystem-audit-secondary";

    @Autowired
    @Qualifier(PRIMARY_TOPIC)
    private CreateAuditRecordCommandTransactionOutboxSender primaryTopicSender;

    @Autowired
    @Qualifier(SECONDARY_TOPIC)
    private CreateAuditRecordCommandTransactionOutboxSender secondaryTopicSender;

    @Autowired
    private MultiTopicAuditCommandConsumer auditCommandConsumer;

    @Test
    void createsSenderBeanForEachConfiguredTopicQualifiedByTopicName() {
        primaryTopicSender.auditEvent(createCommand("primary-process", Instant.parse("2026-05-28T08:00:00Z")));
        secondaryTopicSender.auditEvent(createCommand("secondary-process", Instant.parse("2026-05-28T08:00:01Z")));

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertThat(auditCommandConsumer.getCommands(PRIMARY_TOPIC)).hasSize(1);
                    assertThat(auditCommandConsumer.getCommands(SECONDARY_TOPIC)).hasSize(1);
                });

        assertThat(auditCommandConsumer.getCommands(PRIMARY_TOPIC).get(0).getProcessId()).isEqualTo("primary-process");
        assertThat(auditCommandConsumer.getCommands(SECONDARY_TOPIC).get(0).getProcessId()).isEqualTo("secondary-process");
    }

    private CreateAuditRecordCommand createCommand(String processId, Instant timestamp) {
        return CreateAuditRecordCommandBuilder.createCommandBuilder("todo", "test-system", timestamp, processId)
                .setEventType(AuditEventType.CREATED)
                .setTriggerSystem("department", "trigger-system", "trigger-service")
                .setContext("my-usecase", processId)
                .build();
    }

    public static class TestConfig {

        @Bean
        public MultiTopicAuditCommandConsumer multiTopicAuditCommandConsumer() {
            return new MultiTopicAuditCommandConsumer();
        }
    }

    public static class MultiTopicAuditCommandConsumer {

        private final Map<String, List<CreateAuditRecordCommand>> commands = new ConcurrentHashMap<>();

        @KafkaListener(topics = PRIMARY_TOPIC)
        public void consumeFromPrimaryTopic(@Payload CreateAuditRecordCommand command,
                                            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                            Acknowledgment ack) {
            consume(command, topic, ack);
        }

        @KafkaListener(topics = SECONDARY_TOPIC)
        public void consumeFromSecondaryTopic(@Payload CreateAuditRecordCommand command,
                                              @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                              Acknowledgment ack) {
            consume(command, topic, ack);
        }

        private void consume(CreateAuditRecordCommand command, String topic, Acknowledgment ack) {
            commands.computeIfAbsent(topic, key -> new CopyOnWriteArrayList<>()).add(command);
            ack.acknowledge();
        }

        List<CreateAuditRecordCommand> getCommands(String topic) {
            return commands.getOrDefault(topic, List.of());
        }
    }
}
