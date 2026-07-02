# jEAP Audit

jEAP Audit is a library for the standardized creation and delivery of audit records in Spring Boot
services built on the jEAP framework. Distributed government systems need comprehensive audit trails
for compliance, security and operational transparency, and jEAP Audit provides the building blocks to
produce them consistently across microservices. It offers:

* A fluent builder (`CreateAuditRecordCommandBuilder`) for the Avro `CreateAuditRecordCommand`
* A Spring-managed factory (`CreateAuditRecordCommandBuilderFactory`) that fills the trigger from the
  jEAP security token (user) or from a consumed message (system)
* Reliable, at-least-once delivery of audit commands through the
  [jeap-messaging-outbox](https://github.com/jeap-admin-ch/jeap-messaging-outbox) transactional outbox
* Consumer support that converts a received `CreateAuditRecordCommand` into a plain Java `AuditRecord`
  model for downstream processing and centralized audit-log aggregation

The audit command is an Avro message type (`create-audit-record-command`) sent over Kafka. jEAP Audit
builds on [jeap-messaging](https://github.com/jeap-admin-ch/jeap-messaging) for serialization,
contracts and delivery.

## Documentation

Start with [Getting started](docs/getting-started.md), then follow the links below.

| Topic                                                        | File                                                                 |
|--------------------------------------------------------------|----------------------------------------------------------------------|
| Getting started (add a dependency, build & send a command)   | [docs/getting-started.md](docs/getting-started.md)                   |
| Architecture & audit-record model                            | [docs/architecture.md](docs/architecture.md)                         |
| Building the command (`CreateAuditRecordCommandBuilder`)     | [docs/building-the-command.md](docs/building-the-command.md)         |
| Sending via transactional outbox                             | [docs/transactional-outbox.md](docs/transactional-outbox.md)         |
| Consuming audit commands                                     | [docs/consuming-audit-commands.md](docs/consuming-audit-commands.md) |
| Configuration reference (`jeap.audit.*`)                     | [docs/configuration.md](docs/configuration.md)                       |

## Modules

Group id for all modules is `ch.admin.bit.jeap`; the version is managed by the jEAP Spring Boot parent.
A consuming service depends on the module(s) matching its use case.

| Module                                            | Purpose                                                                                                   |
|---------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| `jeap-audit-command-builder`                      | Fluent `CreateAuditRecordCommandBuilder` and Spring `CreateAuditRecordCommandBuilderFactory`              |
| `jeap-spring-boot-audit-starter-transactional-outbox` | Spring Boot starter registering `CreateAuditRecordCommandTransactionOutboxSender` to send via the outbox |
| `jeap-audit-command-consumer`                     | `AuditRecordFactory` converting a `CreateAuditRecordCommand` into the plain `AuditRecord` model            |
| `jeap-audit-command-builder-nojeapsecurity-test`  | Internal test module verifying the builder works without jEAP security on the classpath                   |

## Changes

This library is versioned using [Semantic Versioning](http://semver.org/) and all changes are documented in
[CHANGELOG.md](./CHANGELOG.md) following the format defined in [Keep a Changelog](http://keepachangelog.com/).

## Note

This repository is part the open source distribution of jEAP.
See [github.com/jeap-admin-ch/jeap](https://github.com/jeap-admin-ch/jeap)
for more information.

## License

This repository is Open Source Software licensed under the [Apache License 2.0](./LICENSE).
