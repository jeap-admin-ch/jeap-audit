# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres
to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [4.24.0] - 2026-03-10

### Changed
- Update parent from 5.19.0 to 5.19.2
- update jeap-starter from 19.15.0 to 19.16.0
- update jeap-spring-boot-roles-anywhere-starter from 1.19.0 to 1.20.0
- update jeap-crypto from 5.15.0 to 5.16.0
- update jeap-messaging from 11.17.0 to 11.18.0
- update jeap-spring-boot-vault-starter from 19.15.0 to 19.16.0
- update jeap-messaging-outbox from 11.19.0 to 11.20.0

## [4.23.0] - 2026-03-02

### Changed
- Update parent from 5.18.0 to 5.19.0
- update jeap-starter from 19.14.0 to 19.15.0
- update jeap-spring-boot-roles-anywhere-starter from 1.18.0 to 1.19.0
- update jeap-crypto from 5.14.0 to 5.15.0
- update jeap-messaging from 11.16.0 to 11.17.0
- update jeap-spring-boot-vault-starter from 19.14.0 to 19.15.0
- update jeap-messaging-outbox from 11.18.0 to 11.19.0

## [4.22.0] - 2026-02-25

### Changed
- Update parent from 5.17.1 to 5.18.0
- update jeap-starter from 19.13.0 to 19.14.0
- Improved template path resolution in the annotation processor for robust, cross-platform path handling and avoids issues with illegal characters in file paths.
- update jeap-messaging from 11.15.0 to 11.15.1
- update jeap-spring-boot-roles-anywhere-starter from 1.17.0 to 1.18.0
- update jeap-crypto from 5.13.0 to 5.14.0
- update jeap-messaging from 11.15.1 to 11.16.0
- update jeap-spring-boot-vault-starter from 19.13.0 to 19.14.0
- update jeap-messaging-outbox from 11.16.0 to 11.18.0

## [4.21.0] - 2026-02-13
### Changed
- update jeap-messaging-outbox from 11.15.0 to 11.16.0
- refactoring of the housekeeping: delete messages in batches using paging

## [4.20.0] - 2026-01-27

### Changed
- Update parent from 5.17.0 to 5.17.1
- update jeap-starter from 19.12.0 to 19.13.0
- update jeap-spring-boot-vault-starter from 19.12.0 to 19.13.0
- update jeap-spring-boot-roles-anywhere-starter from 1.16.0 to 1.17.0
- update jeap-messaging from 11.14.0 to 11.15.0
- update jeap-crypto from 5.12.0 to 5.13.0
- update jeap-messaging-outbox from 11.14.0 to 11.15.0

## [4.19.0] - 2026-01-26
### Changed
- update jeap-messaging from 11.13.0 to 11.14.0
- added ErrorHandlingTargetFilter to filter out messages not intended for the consuming service (Header: jeap_eh_target_service)
- update jeap-messaging-outbox from 11.13.0 to 11.14.0

## [4.18.0] - 2026-01-23
### Changed
- update jeap-messaging from 11.12.0 to 11.13.0
- remove v from tag version in comparison of jeap-messaging-avro-maven-plugin GitClient
- update jeap-messaging-outbox from 11.12.0 to 11.13.0

## [4.17.0] - 2026-01-21
### Changed
- update jeap-starter from 19.11.0 to 19.12.0
- Removed X-XSS-Protection header as recommended in https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/X-XSS-Protection
- update jeap-spring-boot-vault-starter from 19.11.0 to 19.12.0
- update jeap-crypto from 5.11.0 to 5.12.0
- update jeap-messaging from 11.11.0 to 11.12.0

## [4.16.0] - 2026-01-20
### Changed
- update jeap-starter from 19.10.0 to 19.11.0
- Default server.forward-headers-strategy to NATIVE
- update jeap-spring-boot-vault-starter from 19.10.0 to 19.11.0
- update jeap-crypto from 5.10.0 to 5.11.0
- update jeap-messaging from 11.10.0 to 11.11.0
- update jeap-messaging-outbox from 11.10.0 to 11.11.0

## [4.15.0] - 2026-01-16
### Changed
  Enable via the `jeap.health.metric.contributor-metrics.enabled` property.
  Enable via the `jeap.health.metric.contributor-metrics.enabled` property.
  Enable via the `jeap.health.metric.contributor-metrics.enabled` property.
- update jeap-starter from 19.9.0 to 19.10.0
- Added support for exposing additional metrics about application health contributors.
- AvroMessageBuilder now validates the presence of the `variant` field in the message type before setting it, throwing an exception if it is undefined.
- update jeap-messaging from 11.8.1 to 11.9.0
- update jeap-messaging-outbox from 11.8.0 to 11.9.0
- update jeap-crypto from 5.9.0 to 5.10.0
- update jeap-messaging from 11.9.0 to 11.10.0
- update jeap-spring-boot-vault-starter from 19.9.0 to 19.10.0
- update jeap-messaging-outbox from 11.9.0 to 11.10.0

## [4.14.0] - 2026-01-16

### Changed
- Refactoring and improving method names in CreateAuditRecordCommandTransactionOutboxSender

## [4.13.0] - 2026-01-14

### Changed
- Update parent from 5.16.8 to 5.17.0
- update jeap-starter from 19.8.0 to 19.9.0
- update jeap-messaging from 11.7.0 to 11.8.1
- update jeap-messaging-outbox from 11.7.0 to 11.8.0
- update commons-io from 2.20.0 to 2.21.0

## [4.12.0] - 2026-01-07

### Changed
- Update parent from 5.16.7 to 5.16.8
- update jeap-starter from 19.7.0 to 19.8.0
- update jeap-spring-boot-roles-anywhere-starter from 1.14.0 to 1.15.0
- update jeap-crypto from 5.7.0 to 5.8.0
- update jeap-messaging from 11.6.0 to 11.7.0
- update jeap-spring-boot-vault-starter from 19.7.0 to 19.8.0
- update jeap-messaging-outbox from 11.6.0 to 11.7.0

## [4.11.0] - 2025-12-22

### Changed
- Update parent from 5.16.6 to 5.16.7
- update jeap-starter from 19.6.0 to 19.7.0
- update jeap-spring-boot-roles-anywhere-starter from 1.13.0 to 1.14.0
- update jeap-crypto from 5.6.0 to 5.7.0
- update jeap-messaging from 11.5.0 to 11.6.0
- update jeap-spring-boot-vault-starter from 19.6.0 to 19.7.0

## [4.10.0] - 2025-12-19

### Changed
- Update parent from 5.16.5 to 5.16.6
- update jeap-starter from 19.5.0 to 19.6.0
- update jeap-spring-boot-vault-starter from 19.5.0 to 19.6.0
- update jeap-spring-boot-roles-anywhere-starter from 1.12.0 to 1.13.0
- update jeap-messaging from 11.4.0 to 11.5.0
- update jeap-crypto from 5.5.0 to 5.6.0
- update jeap-messaging-outbox from 11.4.0 to 11.5.0

## [4.9.0] - 2025-12-17

### Changed
- Update parent from 5.16.4 to 5.16.5
- update jeap-starter from 19.4.1 to 19.5.0
- update jeap-spring-boot-roles-anywhere-starter from 1.11.0 to 1.12.0
- update jeap-crypto from 5.4.1 to 5.5.0
- update jeap-messaging from 11.3.1 to 11.4.0
- update jeap-spring-boot-vault-starter from 19.4.1 to 19.5.0
- update jeap-messaging-outbox from 11.3.1 to 11.4.0

## [4.8.1] - 2025-12-16
### Changed
- update jeap-starter from 19.4.0 to 19.4.1
- Fix logback warnings due to deprecated features being used in the configuration
- update jeap-spring-boot-vault-starter from 19.4.0 to 19.4.1
- update jeap-crypto from 5.4.0 to 5.4.1
- update jeap-messaging from 11.3.0 to 11.3.1
- update jeap-messaging-outbox from 11.3.0 to 11.3.1

## [4.8.0] - 2025-12-15

### Changed
- Update parent from 5.16.3 to 5.16.4
- update jeap-starter from 19.3.0 to 19.4.0
- update jeap-messaging from 11.2.0 to 11.3.0
- update jeap-messaging-outbox from 9.3.1 to 11.3.0

## [4.7.0] - 2025-12-08
### Changed
- update jeap-messaging from 11.1.0 to 11.2.0
- Update parent from 5.16.1 to 5.16.3
  
- update jeap-spring-boot-roles-anywhere-starter from 1.8.0 to 1.10.0
- update jeap-spring-boot-vault-starter from 19.2.0 to 19.3.0
- Update parent from 5.16.2 to 5.16.3
- update jeap-crypto from 5.1.0 to 5.3.0


## [4.6.0] - 2025-12-08
### Changed
- update jeap-starter from 19.2.0 to 19.3.0
- Update parent from 5.16.2 to 5.16.3


## [4.5.0] - 2025-12-08

### Changed

- Update parent from 5.16.2 to 5.16.3

## [4.4.0] - 2025-12-08
### Changed
- update jeap-starter from 19.1.0 to 19.2.0
- Update parent from 5.16.1 to 5.16.2


## [4.3.0] - 2025-12-08

### Changed

- Update parent from 5.16.1 to 5.16.2

## [4.2.0] - 2025-12-04
### Changed
- update jeap-messaging from 11.0.0 to 11.1.0
- Update parent from 5.16.0 to 5.16.1
  
- update jeap-spring-boot-roles-anywhere-starter from 1.7.0 to 1.8.0
- update jeap-spring-boot-vault-starter from 19.0.0 to 19.1.0
- update jeap-crypto from 5.0.0 to 5.1.0


## [4.1.0] - 2025-12-04

### Changed
- Update parent from 5.16.0 to 5.16.1
- update jeap-starter from 19.0.0 to 19.1.0

## [4.0.0] - 2025-12-03
### Changed
- update jeap-messaging from 10.3.0 to 11.0.0
- update jeap-crypto from 4.5.0 to 5.0.0
- update jeap-spring-boot-vault-starter from 18.5.0 to 19.0.0
-  Breaking Change
    - **Removed**
      - jeap-spring-boot-cloud-autoconfig-starter
      - jeap-spring-boot-config-starter
      - other cloudfoundry specifics


## [3.0.0] - 2025-12-03
### Changed
- update jeap-starter from 18.5.0 to 19.0.0
-  Breaking Change
    - **Removed**
      - jeap-spring-boot-cloud-autoconfig-starter
      - jeap-spring-boot-config-starter
      - other cloudfoundry specifics


## [2.6.0] - 2025-12-01
### Changed
- update jeap-messaging from 10.2.0 to 10.3.0
- Use fully qualified name of avro schema record when determining Glue schema names


## [2.5.0] - 2025-11-28
### Changed
- update jeap-messaging from 10.1.0 to 10.2.0
- Update parent from 5.15.1 to 5.16.0
  
- update jeap-spring-boot-roles-anywhere-starter from 1.6.0 to 1.7.0
- update jeap-spring-boot-vault-starter from 18.4.0 to 18.5.0
- update jeap-crypto from 4.4.0 to 4.5.0


## [2.4.0] - 2025-11-28
### Changed
- update jeap-starter from 18.4.0 to 18.5.0
- Update parent from 5.15.1 to 5.16.0


## [2.3.0] - 2025-11-28

### Changed

- Update parent from 5.15.0 to 5.16.0

## [2.2.1] - 2025-11-26
### Changed
- CreateAuditRecordCommandBuilderFactory now can also be used without the jeap-security-starter on the classpath.
- CreateAuditRecordCommandBuilderFactory now accepts Messages int the general Message interface instead of just AvroDomainEvent.

## [2.2.0] - 2025-11-14
### Changed
- update jeap-messaging from 10.0.1 to 10.1.0
- update jeap-crypto from 4.3.0 to 4.4.0
- update jeap-spring-boot-vault-starter from 18.3.0 to 18.4.0
- Update aws-advanced-jdbc-wrapper from 2.5.4 to 2.6.6


## [2.1.0] - 2025-11-14
### Changed
- update jeap-starter from 18.2.0 to 18.4.0
- Update aws-advanced-jdbc-wrapper from 2.5.4 to 2.6.6


## [2.0.0] - 2025-11-14
### Changed
- update jeap-messaging from 9.3.1 to 10.0.1
- Fix parsing of boolean decryption properties in JeapGlueAvroDeserializer


## [1.0.0] - 2025-11-13

### Changed
- Initial audit starter
