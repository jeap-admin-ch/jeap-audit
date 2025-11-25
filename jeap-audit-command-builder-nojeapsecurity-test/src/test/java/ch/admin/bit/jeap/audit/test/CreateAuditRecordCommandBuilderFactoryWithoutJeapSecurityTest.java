package ch.admin.bit.jeap.audit.test;

import ch.admin.bit.jeap.audit.command.builder.AuditException;
import ch.admin.bit.jeap.audit.command.builder.CreateAuditRecordCommandBuilder;
import ch.admin.bit.jeap.audit.command.builder.CreateAuditRecordCommandBuilderFactory;
import ch.admin.bit.jeap.audit.record.create.AuditSystemComponent;
import ch.admin.bit.jeap.audit.record.create.CreateAuditRecordCommand;
import ch.admin.bit.jeap.messaging.model.Message;
import ch.admin.bit.jeap.messaging.model.MessageIdentity;
import ch.admin.bit.jeap.messaging.model.MessagePublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test verifies that the createWithSystemTriggerFromMessage method of CreateAuditRecordCommandBuilderFactory can
 * be used when no Jeap Security is present on the class path. At the same time, the test verifies that the user trigger
 * convenience methods throw the expected AuditException when no Jeap Security is present.
 */
@SpringBootTest
public class CreateAuditRecordCommandBuilderFactoryWithoutJeapSecurityTest {

    private static final String SYSTEM_NAME_MESSAGE = "systemNameMessage";
    private static final String SERVICE_NAME_MESSAGE = "systemNameMessage";
    private static final String SYSTEM_NAME = "systemName";
    private static final String SERVICE_NAME = "systemName";
    private static final String DEPARMENT_NAME = "departmentName";

    @Autowired
    private CreateAuditRecordCommandBuilderFactory createAuditRecordCommandBuilderFactory;

    @Test
    void testFactoryCreateWithSystemTriggerFromMessage() {
        Message message = createMessage();

        CreateAuditRecordCommandBuilder createAuditRecordCommandBuilder =
                createAuditRecordCommandBuilderFactory.createWithSystemTriggerFromMessage(
                SERVICE_NAME,
                SYSTEM_NAME,
                DEPARMENT_NAME,
                Instant.now(),
                message);

        CreateAuditRecordCommand createAuditRecordCommand = createAuditRecordCommandBuilder.build();
        assertThat(createAuditRecordCommand.getPayload().getTrigger()).isInstanceOf(AuditSystemComponent.class);
        AuditSystemComponent trigger = (AuditSystemComponent) createAuditRecordCommand.getPayload().getTrigger();
        assertThat(trigger.getSystem()).isEqualTo(SYSTEM_NAME_MESSAGE);
        assertThat(trigger.getComponent()).isEqualTo(SERVICE_NAME_MESSAGE);
    }

    @Test
    void testFactoryCreateWithUserTriggerThrowsAuditException() {
        assertThatThrownBy(
                () -> createAuditRecordCommandBuilderFactory.createWithUserTrigger(Instant.now()))
        .isInstanceOf(AuditException.class)
        .hasMessageContaining("You cannot use user convenience method without jEAP security");
    }

    @Test
    void testFactoryCreateWithUserTriggerAndSystemComponentThrowsAuditException() {
        assertThatThrownBy(
                () -> createAuditRecordCommandBuilderFactory.createWithUserTrigger(
                        SERVICE_NAME, SYSTEM_NAME, Instant.now()))
        .isInstanceOf(AuditException.class)
        .hasMessageContaining("You cannot use user convenience method without jEAP security");
    }

    private static Message createMessage() {
        MessagePublisher publisher = mock(MessagePublisher.class);
        when(publisher.getSystem()).thenReturn(SYSTEM_NAME_MESSAGE);
        when(publisher.getService()).thenReturn(SERVICE_NAME_MESSAGE);
        MessageIdentity identity = mock(MessageIdentity.class);
        when(identity.getIdempotenceId()).thenReturn(UUID.randomUUID().toString());
        Message message = mock(Message.class);
        when(message.getPublisher()).thenReturn(publisher);
        when(message.getIdentity()).thenReturn(identity);
        return message;
    }

}
