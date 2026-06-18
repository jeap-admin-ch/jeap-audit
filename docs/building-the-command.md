# Building the command

The `jeap-audit-command-builder` module provides the fluent `CreateAuditRecordCommandBuilder` and the
Spring-managed `CreateAuditRecordCommandBuilderFactory`.

## CreateAuditRecordCommandBuilder

Use the builder directly when you construct the command yourself (and send it however you like). Create
it with the auditing service's name, its system name and a timestamp; an optional `processId` can be
passed as a fourth argument.

```java
CreateAuditRecordCommand command = CreateAuditRecordCommandBuilder
        .createCommandBuilder(serviceName, systemName, Instant.now())
        .setEventType(AuditEventType.CREATED)
        .addEventData("key1", "value1")
        .addEventData("key2", "value2")
        .setTriggerUser(userId, identityProvider)
        .build();
```

### Builder methods

| Method                                                    | Purpose                                                                                   |
|-----------------------------------------------------------|-------------------------------------------------------------------------------------------|
| `setEventType(AuditEventType)`                            | Event type; defaults to `AuditEventType.UNKNOWN` if not set                                |
| `setContext(useCase)` / `setContext(useCase, processId)`  | Sets the audit context; the two-arg form must agree with any `processId` already on the command |
| `addEventData(key, value)`                                | Adds a free-form key/value event-data element (repeatable)                                 |
| `setTriggerUser(userId, identityProvider)`                | Sets a user trigger (clears any system trigger)                                            |
| `setTriggerSystem(department, system, component)`         | Sets a system trigger (clears any user trigger)                                            |
| `setAuditObject(type, id)` / `setAuditObject(type, id, version)` | Sets the audited object                                                              |
| `addAuditObjectDataValue([role,] name, value)`            | Adds a plain string value entry to the audited object                                     |
| `addAuditObjectDataJSON([role,] name, jsonAsUTF8)`        | Adds a JSON entry (accepts a `String` or `ByteBuffer`)                                     |
| `addAuditObjectDataS3([role,] name, objectReference)`     | Adds an S3 object-reference entry                                                          |
| `idempotenceId(id)`                                       | Overrides the generated idempotence id (inherited from `AvroCommandBuilder`)              |
| `build()`                                                 | Validates and returns the `CreateAuditRecordCommand`                                       |

The `role` argument on the `addAuditObjectData*` methods is an optional `AuditObjectDataRole`
(`NEW` or `OLD`), used to distinguish before/after values; overloads without it pass `null`.

### Rules enforced by `build()`

- Exactly one trigger must be set (`setTriggerUser` **or** `setTriggerSystem`), otherwise `build()`
  throws `IllegalStateException`.
- If no `idempotenceId` was set, one is generated from `systemName`, `serviceName`, the event type, the
  trigger (user id or system component) and the timestamp.
- `setContext(useCase, processId)` throws `IllegalArgumentException` if its `processId` differs from a
  `processId` already supplied to `createCommandBuilder`.

## CreateAuditRecordCommandBuilderFactory

The factory is auto-configured as a Spring bean. It pre-fills the trigger so callers do not have to
extract it themselves. `serviceName`/`systemName` default to the values from the jEAP messaging Kafka
properties; overloads let you pass them explicitly.

```java
// User trigger taken from the current jEAP security token (token subject + issuer):
CreateAuditRecordCommandBuilder builder = factory.createWithUserTrigger(Instant.now());

// System trigger taken from a consumed message (its publisher system & service):
CreateAuditRecordCommandBuilder builder =
        factory.createWithSystemTriggerFromMessage(triggeringDepartment, Instant.now(), message);

CreateAuditRecordCommand command = builder.build();
```

`createWithUserTrigger` requires jEAP security on the classpath; without it the factory throws an
`AuditException` ("You cannot use user convenience method without jEAP security"). The user id is the
token subject (the PAMS ID in the ePortal context) and the identity provider is the token issuer.
`createWithSystemTriggerFromMessage` derives the idempotence id from the source message
(`audit-<message idempotence id>`).

## Related

- [Getting started](getting-started.md)
- [Architecture & audit-record model](architecture.md)
- [Sending via transactional outbox](transactional-outbox.md)
- [jeap-audit](../README.md)
