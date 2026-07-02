# Sending via transactional outbox

The `jeap-spring-boot-audit-starter-transactional-outbox` module sends `CreateAuditRecordCommand`
messages over Kafka using the
[jeap-messaging-outbox](https://github.com/jeap-admin-ch/jeap-messaging-outbox) transactional outbox.
Because the command is written to an outbox table inside your business transaction and relayed to Kafka
afterwards, delivery is reliable and at-least-once even across failures.

The starter registers `CreateAuditRecordCommandTransactionOutboxSender` beans through
`AuditTransactionalOutboxAutoConfiguration` (active unless `jeap.audit.enabled=false`). Autowire the
sender and pass a builder lambda; the sender builds the command, sets the trigger and sends it in one
transactional step.

## Sender methods

| Method                                                                                                       | Trigger                                                            |
|--------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------|
| `auditUserTriggeredEvent(timestamp, builderConsumer)`                                                        | User, from the jEAP security token; service/system from properties |
| `auditUserTriggeredEvent(serviceName, systemName, timestamp, builderConsumer)`                               | User, with explicit service/system                                 |
| `auditMessageTriggeredSystemEvent(department, timestamp, message, builderConsumer)`                          | System, from the consumed message; service/system from properties  |
| `auditMessageTriggeredSystemEvent(serviceName, systemName, department, timestamp, message, builderConsumer)` | System, with explicit service/system                               |
| `auditEvent(command)`                                                                                        | None — sends an already-built `CreateAuditRecordCommand`           |

All methods are `@Transactional`.

```java
sender.auditMessageTriggeredSystemEvent(serviceName, systemName, triggeringServiceDepartment,
        Instant.now(), avroDomainEvent,
        builder -> {
            builder.setEventType(AuditEventType.CREATED);
            builder.setContext(USE_CASE, PROCESS_ID);
        });
```

## Required setup

1. **Producer contract.** The audit command is a normal jEAP message, so declare a producer contract
   (e.g. on your application class). To encrypt the command, configure a jeap-crypto key in the
   contract.

   ```java
   @JeapMessageProducerContract(value = CreateAuditRecordCommand.TypeRef.class, topic = "my-topic")
   ```

2. **Target topic.** Configure `jeap.audit.transactional-outbox.topic` (single topic) or
   `jeap.audit.transactional-outbox.topics` (a list). See [Configuration](configuration.md).

3. **Outbox database tables.** The transactional outbox needs its `deferred_message` and `shedlock`
   tables. Add them in your Flyway (or equivalent) migrations, e.g. based on
   [V1__create-outbox-schema.sql](https://github.com/jeap-admin-ch/jeap-messaging-outbox/blob/main/jeap-messaging-outbox/src/test/resources/db/migration/common/V1__create-outbox-schema.sql).

## Single vs. multiple topics

With a single topic the sender keeps the bean name `auditRecordCommandTransactionOutboxSender`, so an
unqualified injection point works:

```java
private final CreateAuditRecordCommandTransactionOutboxSender sender;
```

With multiple topics, one sender bean is registered per topic. In this mode the bean name is the
topic name (not the fixed `auditRecordCommandTransactionOutboxSender` name used for single-topic), and
each bean is also qualified with that topic name. Select one with `@Qualifier`:

```java
MyComponent(@Qualifier("topic-one") CreateAuditRecordCommandTransactionOutboxSender senderOne,
            @Qualifier("topic-two") CreateAuditRecordCommandTransactionOutboxSender senderTwo) { ... }
```

Configuring both `topic` and `topics`, or leaving both unset, fails fast at startup; topics must be
non-empty and unique.

## Related

- [Getting started](getting-started.md)
- [Building the command](building-the-command.md)
- [Configuration reference](configuration.md)
- [jeap-audit](../README.md)
