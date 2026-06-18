# AGENTS.md

Guidance for AI coding agents working **in this repository**. For how to *use* the library in a
consuming service, read [README.md](README.md) and the [docs/](docs/) folder instead.

## Project

jEAP Audit is a multi-module Maven library for creating and delivering audit records in jEAP Spring
Boot services. Its core is a fluent builder for the Avro `CreateAuditRecordCommand` message type, a
Spring factory that fills the audit trigger from the security token or a consumed message, a
transactional-outbox starter that sends the command over Kafka with at-least-once delivery, and a
consumer helper that maps a received command into a plain Java `AuditRecord` model. It builds on
[jeap-messaging](https://github.com/jeap-admin-ch/jeap-messaging) and
[jeap-messaging-outbox](https://github.com/jeap-admin-ch/jeap-messaging-outbox).

## Repository layout

```
pom.xml                                                # Parent POM (packaging=pom); declares the modules below
jeap-audit-command-builder/                            # CreateAuditRecordCommandBuilder + CreateAuditRecordCommandBuilderFactory, auto-configuration
jeap-spring-boot-audit-starter-transactional-outbox/   # Starter: CreateAuditRecordCommandTransactionOutboxSender, bean registrar, auto-configuration
jeap-audit-command-consumer/                           # AuditRecordFactory + plain AuditRecord model
jeap-audit-command-builder-nojeapsecurity-test/        # Test-only module: builder must work without jeap-security on the classpath
Jenkinsfile, publiccode.yml, CHANGELOG.md, LICENSE
```

The `CreateAuditRecordCommand` Avro types (`ch.admin.bit.jeap.audit.record.create.*`) come from the
external `ch.admin.bit.jeap.messagetype.jeap:create-audit-record-command` artifact (the
[Message Type Registry](https://github.com/jeap-admin-ch/jeap-message-type-registry)), not from this
repo. Dependency direction: `command-builder` ← `command-consumer` and `command-builder` ←
`transactional-outbox-starter`.

## Build & test

```bash
./mvnw -pl <module> -am install      # build a module and its dependencies
./mvnw verify                        # full build incl. tests
./mvnw -pl jeap-spring-boot-audit-starter-transactional-outbox test
```

- Parent: `ch.admin.bit.jeap:jeap-internal-spring-boot-parent` (Spring Boot 4 aligned).
- Integration tests in the outbox starter extend `KafkaIntegrationTestBase`
  (`jeap-messaging-infrastructure-kafka-test`) with `@EmbeddedKafka`; the H2 + Flyway test setup
  provisions the outbox/shedlock tables. Every feature must have integration tests.
- Spring Boot 3 maintenance happens on the `release/springboot3` branch; `master` targets Spring Boot 4.

## jEAP conventions

- Java packages live under `ch.admin.bit.jeap.audit...`.
- Configuration properties use the prefix `jeap.audit.*` (outbox topics under
  `jeap.audit.transactional-outbox.*`).
- Auto-configuration is registered via
  `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.
- The `CreateAuditRecordCommandBuilderFactory` user-trigger feature is conditional on jEAP security
  being on the classpath (`@ConditionalOnClass(JeapAuthenticationToken.class)`); the builder itself must
  keep working without it (guarded by the `-nojeapsecurity-test` module).
- The audit command is a normal jEAP message: a `@JeapMessageProducerContract` is required to send it,
  and it can be encrypted by declaring a jeap-crypto key in the contract.

## Docs

When changing public behaviour, update the matching focused file under [docs/](docs/) (one topic per
file) and the documentation index in the README.

## Versioning

- Semantic Versioning; all changes documented in [CHANGELOG.md](./CHANGELOG.md) (Keep a Changelog format).
- `setPomVersions.sh <version>` updates the version across all module POMs.
- When working on a feature branch, increase the version to `x.y.z-SNAPSHOT` in the POMs.
- Always keep the -SNAPSHOT postfix in the POMs, CI removes it when releasing. Do not use the SNAPSHOT
  postfix elsewhere (CHANGELOG, publiccode.yml etc).
- Keep changelog entries concise and follow existing patterns.
- Keep commit messages short, use the JIRA ID from the branch name as a prefix, do not use conventional
  commits (for example: "JEAP-1234 Added feature X").
- When bumping the version, also update the changelog and the version/date in `publiccode.yml`.
