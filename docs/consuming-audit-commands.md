# Consuming audit commands

The `jeap-audit-command-consumer` module helps a downstream service that receives
`CreateAuditRecordCommand` messages turn them into a plain, immutable Java model. This is typically used
by a central audit-log service that aggregates audit records from many systems.

Receiving the command itself is plain jEAP messaging (a `@KafkaListener` plus a consumer contract);
this module only handles the mapping afterwards.

## Mapping a command

`AuditRecordFactory` is a stateless utility:

```java
AuditRecord auditRecord = AuditRecordFactory.createAuditRecord(command);
```

It throws `IllegalStateException` if the command carries a trigger or object-data entry of an unknown
type.

## The AuditRecord model

`AuditRecord` is a `record` exposing the flattened command:

| Field         | Type                  | Notes                                                            |
|---------------|-----------------------|------------------------------------------------------------------|
| `serviceName` | `String`              | From the command publisher                                       |
| `systemName`  | `String`              | From the command publisher                                       |
| `timestamp`   | `Instant`             | When the audited action happened                                 |
| `auditEvent`  | `AuditEvent`          | `eventType`, `AuditContext`, list of `AuditEventDataElement`     |
| `trigger`     | `AuditTrigger`        | `AuditTriggerUser` or `AuditTriggerSystemComponent`              |
| `auditedData` | `AuditObject`         | The audited object, or `null` if none                            |

### Trigger

`AuditTrigger` is the common supertype; the concrete type tells you who triggered the action:

- `AuditTriggerUser` — `id`, `identityProvider`.
- `AuditTriggerSystemComponent` — `department`, `system`, `component`.

### Audited object data

`AuditObject` holds the object `type`, `id`, optional `version` and an `objectDataList()` of
`AuditObjectData` entries. `AuditObjectData` exposes `name()`, `role()` (`AuditObjectDataRole`,
`NEW`/`OLD`/`null`) and a `type()` discriminator (`AuditObjectDataType.VALUE`, `JSON` or
`S3_REFERENCE`). The concrete subtypes carry the payload:

- `AuditObjectDataValue` — `value()` (a `String`).
- `AuditObjectDataJSON` — `jsonAsUtf8()` (a `ByteBuffer`).
- `AuditObjectDataS3` — `objectReference()` (a `String`).

```java
for (AuditObjectData data : auditRecord.auditedData().objectDataList()) {
    switch (data.type()) {
        case VALUE -> handleValue(((AuditObjectDataValue) data).value());
        case JSON -> handleJson(((AuditObjectDataJSON) data).jsonAsUtf8());
        case S3_REFERENCE -> handleS3(((AuditObjectDataS3) data).objectReference());
    }
}
```

## Related

- [Architecture & audit-record model](architecture.md)
- [Building the command](building-the-command.md)
- [jeap-audit](../README.md)
