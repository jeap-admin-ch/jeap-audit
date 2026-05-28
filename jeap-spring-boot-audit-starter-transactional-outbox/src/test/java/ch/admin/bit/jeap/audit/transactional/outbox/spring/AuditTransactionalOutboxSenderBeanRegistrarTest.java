package ch.admin.bit.jeap.audit.transactional.outbox.spring;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuditTransactionalOutboxSenderBeanRegistrarTest {

    private static final String SINGLE_TOPIC_BEAN_NAME = "auditRecordCommandTransactionOutboxSender";
    private static final String SINGLE_TOPIC_PROPERTY = "jeap.audit.transactional-outbox.topic";
    private static final String TOPICS_PROPERTY = "jeap.audit.transactional-outbox.topics";

    @Test
    void registersLegacyBeanNameForSingleTopicProperty() {
        GenericApplicationContext context = register(new MockEnvironment()
                .withProperty(SINGLE_TOPIC_PROPERTY, "my-topic"));

        assertThat(context.containsBeanDefinition(SINGLE_TOPIC_BEAN_NAME)).isTrue();
        assertThat(context.containsBeanDefinition("my-topic")).isFalse();
    }

    @Test
    void registersTopicBeanNameForSingleEntryTopicsProperty() {
        GenericApplicationContext context = register(new MockEnvironment()
                .withProperty(TOPICS_PROPERTY + "[0]", "my-topic"));

        assertThat(context.containsBeanDefinition("my-topic")).isTrue();
        assertThat(context.containsBeanDefinition(SINGLE_TOPIC_BEAN_NAME)).isFalse();
    }

    @Test
    void rejectsEmptySingleTopicProperty() {
        MockEnvironment environment = new MockEnvironment()
                .withProperty(SINGLE_TOPIC_PROPERTY, "");
        assertThatThrownBy(() -> register(environment))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Property '%s' must not contain empty topics.", SINGLE_TOPIC_PROPERTY);
    }

    private GenericApplicationContext register(MockEnvironment environment) {
        GenericApplicationContext context = new GenericApplicationContext();
        AuditTransactionalOutboxSenderBeanRegistrar registrar = new AuditTransactionalOutboxSenderBeanRegistrar();
        registrar.setEnvironment(environment);
        registrar.registerBeanDefinitions(null, context);
        return context;
    }
}
