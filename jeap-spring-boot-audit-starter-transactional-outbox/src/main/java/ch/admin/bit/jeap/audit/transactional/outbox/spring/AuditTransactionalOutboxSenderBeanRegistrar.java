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

/**
 * Registers {@link CreateAuditRecordCommandTransactionOutboxSender} beans from the audit transactional outbox topic
 * configuration.
 * <p>
 * Applications can configure either a single sender with {@value #SINGLE_TOPIC_PROPERTY} or multiple senders with
 * {@value #TOPICS_PROPERTY}. With the single-topic configuration, the sender keeps the established bean name so existing
 * unqualified injection points continue to work. With the multi-topic configuration, one sender bean is registered for
 * every configured topic and each bean is qualified with its topic name, allowing injection points to select a sender via
 * {@link Qualifier @Qualifier("topic-name")}.
 */
class AuditTransactionalOutboxSenderBeanRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final String SINGLE_TOPIC_BEAN_NAME = "auditRecordCommandTransactionOutboxSender";
    private static final String SINGLE_TOPIC_PROPERTY = "jeap.audit.transactional-outbox.topic";
    private static final String TOPICS_PROPERTY = "jeap.audit.transactional-outbox.topics";

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        TopicConfiguration topicConfiguration = resolveTopicConfiguration();

        for (String topic : topicConfiguration.topics()) {
            registerSenderBeanDefinition(registry, topic, topicConfiguration.usesSingleTopicBeanName());
        }
    }

    private TopicConfiguration resolveTopicConfiguration() {
        String singleTopic = environment.getProperty(SINGLE_TOPIC_PROPERTY);
        List<String> topics = getTopicList();

        if (singleTopic != null && !topics.isEmpty()) {
            throw new IllegalStateException(String.format("Configure either '%s' or '%s', not both.", SINGLE_TOPIC_PROPERTY, TOPICS_PROPERTY));
        }
        if (!topics.isEmpty()) {
            return new TopicConfiguration(validateTopics(topics, TOPICS_PROPERTY), false);
        }
        if (singleTopic != null) {
            return new TopicConfiguration(validateTopics(List.of(singleTopic), SINGLE_TOPIC_PROPERTY), true);
        }

        throw new IllegalStateException(String.format("Missing audit transactional outbox topic configuration. Configure either '%s' or '%s'.", SINGLE_TOPIC_PROPERTY, TOPICS_PROPERTY));
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

    private void registerSenderBeanDefinition(BeanDefinitionRegistry registry, String topic, boolean singleTopicConfiguration) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(CreateAuditRecordCommandTransactionOutboxSender.class);
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(new RuntimeBeanReference(TransactionalOutbox.class));
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(new RuntimeBeanReference(CreateAuditRecordCommandBuilderFactory.class));
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(topic);
        beanDefinition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, topic));

        String beanName = singleTopicConfiguration ? SINGLE_TOPIC_BEAN_NAME : topic;
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    private record TopicConfiguration(List<String> topics, boolean usesSingleTopicBeanName) {
    }
}
