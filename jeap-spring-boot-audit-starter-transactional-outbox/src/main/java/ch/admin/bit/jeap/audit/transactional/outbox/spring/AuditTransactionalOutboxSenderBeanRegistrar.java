package ch.admin.bit.jeap.audit.transactional.outbox.spring;

import ch.admin.bit.jeap.audit.command.builder.CreateAuditRecordCommandBuilderFactory;
import ch.admin.bit.jeap.audit.transactional.outbox.CreateAuditRecordCommandTransactionOutboxSender;
import ch.admin.bit.jeap.messaging.transactionaloutbox.outbox.TransactionalOutbox;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

class AuditTransactionalOutboxSenderBeanRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final String LEGACY_BEAN_NAME = "auditRecordCommandTransactionOutboxSender";
    private static final String LEGACY_TOPIC_PROPERTY = "jeap.audit.transactional-outbox.topic";
    private static final String TOPICS_PROPERTY = "jeap.audit.transactional-outbox.topics";

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        List<String> topics = resolveTopics();
        boolean legacySingleTopicConfiguration = isLegacySingleTopicConfiguration(topics);

        for (String topic : topics) {
            registerSenderBeanDefinition(registry, topic, legacySingleTopicConfiguration);
        }
    }

    private List<String> resolveTopics() {
        String legacyTopic = environment.getProperty(LEGACY_TOPIC_PROPERTY);
        List<String> topics = getTopicList();

        if (legacyTopic != null && !topics.isEmpty()) {
            throw new IllegalStateException(String.format("Configure either '%s' or '%s', not both.", LEGACY_TOPIC_PROPERTY, TOPICS_PROPERTY));
        }
        if (!topics.isEmpty()) {
            return validateTopics(topics, TOPICS_PROPERTY);
        }
        if (legacyTopic != null) {
            return validateTopics(List.of(legacyTopic), LEGACY_TOPIC_PROPERTY);
        }

        throw new IllegalStateException(String.format("Missing audit transactional outbox topic configuration. Configure either '%s' or '%s'.", LEGACY_TOPIC_PROPERTY, TOPICS_PROPERTY));
    }

    private List<String> getTopicList() {
        List<String> topicListBindResult = Binder.get(environment)
                .bind(TOPICS_PROPERTY, Bindable.listOf(String.class))
                .orElse(List.of());
        return Objects.requireNonNullElse(topicListBindResult, List.of());
    }

    private List<String> validateTopics(List<String> topics, String propertyName) {
        List<String> sanitizedTopics = topics.stream().map(topic -> topic == null ? null : topic.trim()).toList();

        if (sanitizedTopics.stream().anyMatch(topic -> !StringUtils.hasText(topic))) {
            throw new IllegalStateException(String.format("Property '%s' must not contain empty topics.", propertyName));
        }

        Set<String> uniqueTopics = new LinkedHashSet<>(sanitizedTopics);
        if (uniqueTopics.size() != sanitizedTopics.size()) {
            throw new IllegalStateException(String.format("Property '%s' must not contain duplicate topics.", propertyName));
        }

        return List.copyOf(uniqueTopics);
    }

    private boolean isLegacySingleTopicConfiguration(List<String> topics) {
        return topics.size() == 1 && environment.getProperty(LEGACY_TOPIC_PROPERTY) != null;
    }

    private void registerSenderBeanDefinition(BeanDefinitionRegistry registry, String topic, boolean legacySingleTopicConfiguration) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(CreateAuditRecordCommandTransactionOutboxSender.class);
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(new RuntimeBeanReference(TransactionalOutbox.class));
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(new RuntimeBeanReference(CreateAuditRecordCommandBuilderFactory.class));
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(topic);
        beanDefinition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, topic));

        String beanName = legacySingleTopicConfiguration ? LEGACY_BEAN_NAME : topic;
        registry.registerBeanDefinition(beanName, beanDefinition);
    }
}
