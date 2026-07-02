# Configuration reference

All properties use the prefix `jeap.audit`. They are only relevant when using the
`jeap-spring-boot-audit-starter-transactional-outbox` module — the plain builder and consumer modules
need no configuration of their own.

| Name                                       | Default | Description                                                                                                                                                                |
|--------------------------------------------|---------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `jeap.audit.enabled`                       | `true`  | When `true` (the default), the transactional-outbox auto-configuration registers the `CreateAuditRecordCommandTransactionOutboxSender` bean(s). Set to `false` to disable. |
| `jeap.audit.transactional-outbox.topic`    | —       | Single topic the command is sent to. The sender bean keeps the name `auditRecordCommandTransactionOutboxSender`. Mutually exclusive with `topics`.                         |
| `jeap.audit.transactional-outbox.topics`   | —       | List of topics. One `CreateAuditRecordCommandTransactionOutboxSender` bean is registered per topic, each qualified with the topic name. Mutually exclusive with `topic`.   |

Exactly one of `topic` / `topics` must be configured. Setting both, setting neither, or configuring
empty or duplicate topics, fails fast at application startup.

```yaml
jeap:
  audit:
    transactional-outbox:
      topic: mysystem-audit
```

Multiple topics:

```yaml
jeap:
  audit:
    transactional-outbox:
      topics:
        - mysystem-audit
        - mysystem-audit-secondary
```

Beyond these properties the starter relies on standard jEAP messaging configuration: the auditing
service's `service`/`system` name (`jeap.messaging.kafka.*`, used as the command publisher and as
defaults for the builder), a producer contract for `CreateAuditRecordCommand`, and the transactional
outbox's database tables. See [Sending via transactional outbox](transactional-outbox.md).

## Related

- [Sending via transactional outbox](transactional-outbox.md)
- [Getting started](getting-started.md)
- [jeap-audit](../README.md)
