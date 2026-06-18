# Getting started

This page shows how to add jEAP Audit to a Spring Boot service and emit an audit record. Pick the
module that matches your use case; for the bigger picture see [Architecture](architecture.md).

## 1. Add a dependency

The version of every jEAP Audit artifact is managed by the jEAP Spring Boot parent, so no `<version>`
is needed.

If you only want to build a `CreateAuditRecordCommand` and send it yourself:

```xml
<dependency>
    <groupId>ch.admin.bit.jeap</groupId>
    <artifactId>jeap-audit-command-builder</artifactId>
</dependency>
```

If you want to send the command over Kafka through the transactional outbox (this transitively
includes the builder):

```xml
<dependency>
    <groupId>ch.admin.bit.jeap</groupId>
    <artifactId>jeap-spring-boot-audit-starter-transactional-outbox</artifactId>
</dependency>
```

If you want to consume the command and convert it into a plain Java model:

```xml
<dependency>
    <groupId>ch.admin.bit.jeap</groupId>
    <artifactId>jeap-audit-command-consumer</artifactId>
</dependency>
```

## 2. Build a command

The fluent `CreateAuditRecordCommandBuilder` produces a `CreateAuditRecordCommand`. The minimal command
needs an event type and a trigger (user or system):

```java
CreateAuditRecordCommand command = CreateAuditRecordCommandBuilder
        .createCommandBuilder(serviceName, systemName, Instant.now())
        .setEventType(AuditEventType.CREATED)
        .setContext("my-usecase", processId)
        .addEventData("key", "value")
        .setTriggerUser(userId, identityProvider)
        .build();
```

See [Building the command](building-the-command.md) for the full builder API.

## 3. Send the command (transactional outbox)

With the outbox starter you do not build and send by hand: autowire the
`CreateAuditRecordCommandTransactionOutboxSender` and pass a builder lambda. The trigger is filled
automatically from the jEAP security token (`auditUserTriggeredEvent`) or from a consumed message
(`auditMessageTriggeredSystemEvent`).

```java
@Component
@RequiredArgsConstructor
class MyAuditer {
    private final CreateAuditRecordCommandTransactionOutboxSender sender;

    void audit() {
        sender.auditUserTriggeredEvent(Instant.now(), builder -> {
            builder.setEventType(AuditEventType.CREATED);
            builder.setContext("my-usecase", "my-process-id");
        });
    }
}
```

A `@JeapMessageProducerContract` for `CreateAuditRecordCommand` and the configured target topic are
required. See [Sending via transactional outbox](transactional-outbox.md).

## 4. Consume the command

On the receiving side, map the Avro command to the plain `AuditRecord` model:

```java
AuditRecord auditRecord = AuditRecordFactory.createAuditRecord(command);
```

See [Consuming audit commands](consuming-audit-commands.md).

## Related

- [Architecture](architecture.md)
- [Building the command](building-the-command.md)
- [Sending via transactional outbox](transactional-outbox.md)
- [Consuming audit commands](consuming-audit-commands.md)
- [Configuration reference](configuration.md)
