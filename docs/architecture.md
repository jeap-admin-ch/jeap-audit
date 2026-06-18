# Architecture & audit-record model

jEAP Audit centers on the Avro message type `CreateAuditRecordCommand` (artifact
`ch.admin.bit.jeap.messagetype.jeap:create-audit-record-command`, generated types under
`ch.admin.bit.jeap.audit.record.create.*`). The three production modules play different roles around
that command:

- `jeap-audit-command-builder` — builds a `CreateAuditRecordCommand`.
- `jeap-spring-boot-audit-starter-transactional-outbox` — sends the command over Kafka with
  at-least-once delivery via the [transactional outbox](transactional-outbox.md).
- `jeap-audit-command-consumer` — converts a received command into the plain Java `AuditRecord` model.

```mermaid
flowchart LR
    A["Service (auditing)"] -->|builds| B[CreateAuditRecordCommand]
    B -->|TransactionalOutbox.sendMessage| C[(Kafka topic)]
    C -->|@KafkaListener| D["Audit-consuming service"]
    D -->|AuditRecordFactory| E[AuditRecord model]
```

## The command structure

A `CreateAuditRecordCommand` carries a publisher (`service` / `system`, set from the Kafka properties or
explicit builder arguments), an optional `processId`, and a `CreateAuditRecordCommandPayload` with:

- `timestamp` — when the audited action happened.
- `event` (`AuditEventDetails`) — the `type` (`AuditEventType`), an optional `context`
  (`useCase` + `processId`) and an optional list of free-form `eventData` key/value elements.
- `trigger` — **either** an `AuditUser` (`id`, `identityProvider`) **or** an `AuditSystemComponent`
  (`department`, `system`, `component`). Exactly one must be set.
- `auditedData` (`AuditObject`, optional) — the object the action applied to: `type`, `id`, optional
  `version` and a list of `objectData` entries (value, JSON or S3 reference; each with an optional
  `AuditObjectDataRole` of `NEW` or `OLD`).

`AuditEventType` values are `CREATED`, `READ`, `LISTED`, `MODIFIED`, `DISCLOSED`, `DESTROYED`,
`DELETED` and `UNKNOWN` (the builder default).

## The consumer model

`AuditRecordFactory.createAuditRecord(command)` flattens the Avro command into an immutable
`AuditRecord` record, hiding the Avro union/builder types behind plain Java:

| Avro source                                   | Consumer model type                                            |
|-----------------------------------------------|----------------------------------------------------------------|
| publisher + payload timestamp                 | `AuditRecord` (`serviceName`, `systemName`, `timestamp`)       |
| `AuditEventDetails`                           | `AuditEvent` (`eventType`, `AuditContext`, event-data list)    |
| `AuditUser` / `AuditSystemComponent` trigger  | `AuditTriggerUser` / `AuditTriggerSystemComponent`             |
| `AuditObject` + its data entries              | `AuditObject` with `AuditObjectDataValue/JSON/S3` subtypes     |

See [Consuming audit commands](consuming-audit-commands.md) for details.

## Related

- [Getting started](getting-started.md)
- [Building the command](building-the-command.md)
- [Consuming audit commands](consuming-audit-commands.md)
- [jeap-audit](../README.md)
