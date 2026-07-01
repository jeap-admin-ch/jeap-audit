# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres
to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


## [8.13.1] - 2026-07-01
### Changed
- update jeap-starter from 23.10.0 to 23.10.1
- Add missing `test` scope to test/mock dependencies (`spring-boot-webmvc-test` in swagger starter, `wiremock-standalone` in security starter)

## [8.13.0] - 2026-06-30

### Changed
- Update parent from 8.3.3 to 8.3.4
- update jeap-starter from 23.9.0 to 23.10.0
- update jeap-messaging from 15.12.1 to 15.13.0
- update jeap-spring-boot-roles-anywhere-starter from 3.8.0 to 3.9.0
- update jeap-crypto from 9.10.0 to 9.11.0
- update jeap-spring-boot-vault-starter from 23.9.0 to 23.10.0
- update jeap-messaging-outbox from 15.12.1 to 15.13.0

## [8.12.1] - 2026-06-25
### Changed
- update jeap-messaging from 15.12.0 to 15.12.1
- Shorten common lib snapshot versions in the avro maven plugin: the snapshot version is now the timestamp-based version followed by `-SNAPSHOT` (`<timestamp-version>-SNAPSHOT`) instead of embedding the branch name
- Stop generating the additional classifier artifact for common (`*-messaging-common`) types in the avro maven plugin,
  as they only ever exist in a single version and are consumed without a classifier
- update jeap-messaging-outbox from 15.12.0 to 15.12.1

## [8.12.0] - 2026-06-23

### Changed
- Update parent from 8.3.2 to 8.3.3
- update jeap-starter from 23.8.0 to 23.9.0
- update jeap-messaging from 15.11.0 to 15.12.0
- update jeap-spring-boot-roles-anywhere-starter from 3.7.0 to 3.8.0
- update jeap-crypto from 9.9.0 to 9.10.0
- update jeap-spring-boot-vault-starter from 23.8.0 to 23.9.0
- update jeap-messaging-outbox from 15.11.0 to 15.12.0

## [8.11.0] - 2026-06-22

### Changed
- Update parent from 8.3.1 to 8.3.2
- update jeap-starter from 23.7.0 to 23.8.0
- update jeap-messaging from 15.10.0 to 15.11.0
- update jeap-spring-boot-roles-anywhere-starter from 3.6.0 to 3.7.0
- update jeap-crypto from 9.8.0 to 9.9.0
- update jeap-spring-boot-vault-starter from 23.7.0 to 23.8.0
- update jeap-messaging-outbox from 15.10.0 to 15.11.0

## [8.10.0] - 2026-06-18
### Changed
- update jeap-messaging from 15.9.0 to 15.10.0
- refactor the upload artifact in the avro maven plugin to ignore errors if the artifact is already present in the repository
- update jeap-messaging-outbox from 15.9.0 to 15.10.0

## [8.9.0] - 2026-06-18

### Changed
- Update parent from 8.3.0 to 8.3.1
- update jeap-starter from 23.6.0 to 23.7.0
- update jeap-messaging from 15.8.0 to 15.9.0
- update jeap-spring-boot-roles-anywhere-starter from 3.5.0 to 3.6.0
- update jeap-crypto from 9.7.0 to 9.8.0
- update jeap-spring-boot-vault-starter from 23.6.0 to 23.7.0
- update jeap-messaging-outbox from 15.8.0 to 15.9.0

## [8.8.0] - 2026-06-17

### Changed
- Update parent from 8.2.0 to 8.3.0
- Deprecated spring boot starter
- Sonar issues
- update jeap-starter from 23.5.0 to 23.5.2
- update jeap-spring-boot-roles-anywhere-starter from 3.4.0 to 3.5.0
- update jeap-crypto from 9.5.0 to 9.6.0
- update jeap-spring-boot-vault-starter from 23.5.0 to 23.5.2
- update jeap-messaging from 15.6.0 to 15.7.0
- update jeap-messaging-outbox from 15.6.0 to 15.7.0
- update jeap-messaging-outbox from 15.7.0 to 15.7.1
- update jeap-starter from 23.5.2 to 23.6.0
- update jeap-spring-boot-vault-starter from 23.5.2 to 23.6.0
- update jeap-crypto from 9.6.0 to 9.7.0
- update jeap-messaging from 15.7.0 to 15.8.0
- update jeap-messaging-outbox from 15.7.1 to 15.8.0

## [8.7.1] - 2026-06-17

### Fixed
- Fixed sonar issues
- Replaced deprecated spring boot starter

## [8.7.0] - 2026-06-12

### Changed
- Update parent from 8.1.0 to 8.2.0
- Remove logstash version because it is managed by the internal parent now
- update jeap-starter from 23.4.0 to 23.5.0
- Update logstash to 9.0 (managed from internal parent)
- update jeap-crypto from 9.4.0 to 9.5.0
- update jeap-messaging from 15.5.0 to 15.6.0
- update jeap-spring-boot-roles-anywhere-starter from 3.3.0 to 3.4.0
- update jeap-spring-boot-vault-starter from 23.4.0 to 23.5.0
- update jeap-messaging-outbox from 15.5.0 to 15.6.0

## [8.6.0] - 2026-06-11
### Changed
  responses (e.g. Spring Boot's welcome page forwarding `/` to `index.html`). ETag content-caching is now disabled
  for FORWARD/INCLUDE dispatches so forwarded responses are served with their full body (without an ETag); regular
  requests keep their ETag unchanged.
  for FORWARD/INCLUDE dispatches so forwarded responses are served with their full body (without an ETag); regular
  responses (e.g. Spring Boot's welcome page forwarding `/` to `index.html`). ETag content-caching is now disabled
  requests keep their ETag unchanged.
  for FORWARD/INCLUDE dispatches so forwarded responses are served with their full body (without an ETag); regular
  responses (e.g. Spring Boot's welcome page forwarding `/` to `index.html`). ETag content-caching is now disabled
  requests keep their ETag unchanged.
- update jeap-starter from 23.3.0 to 23.4.0
- `jeap-spring-boot-web-config-starter`: the ShallowEtag filter no longer swallows the body of `forward:`-ed
- update jeap-spring-boot-vault-starter from 23.3.0 to 23.4.0
- update jeap-crypto from 9.3.0 to 9.4.0
- update jeap-messaging from 15.4.0 to 15.5.0
- update jeap-messaging-outbox from 15.4.0 to 15.5.0

## [8.5.0] - 2026-06-09
### Changed
- update jeap-starter from 23.2.0 to 23.3.0
- Update logstash-logback-encoder from 8.1 to 9.0 (migrates to Jackson 3)
- Update aws-advanced-jdbc-wrapper version to 4.0.1
- update jeap-spring-boot-vault-starter from 23.2.0 to 23.3.0
- update jeap-crypto from 9.2.0 to 9.3.0
- update jeap-messaging from 15.3.0 to 15.4.0
- update jeap-messaging-outbox from 15.3.0 to 15.4.0

## [8.4.0] - 2026-06-04

### Changed
- Update parent from 8.0.1 to 8.1.0
- update jeap-starter from 23.1.0 to 23.2.0
- update jeap-spring-boot-roles-anywhere-starter from 3.2.0 to 3.3.0
- update aws-msk-iam-auth from 2.3.5 to 2.3.7
- update avro-serializer from 8.2.0 to 8.2.1
- update jeap-crypto from 9.1.0 to 9.2.0
- update jeap-messaging from 15.2.1 to 15.3.0
- update jeap-spring-boot-vault-starter from 23.1.0 to 23.2.0
- update maven-plugin-testing-harness from 3.5.0 to 3.5.1
- update maven.api from 3.9.14 to 3.9.16
- update jeap-messaging-outbox from 15.2.1 to 15.3.0

## [8.3.1] - 2026-06-03
### Changed
- update jeap-messaging from 15.2.0 to 15.2.1
- Exclude squareup wire dependency from glue schema registry serde: not used, fixes vulnerability scan (CVE-2026-45799)
- update jeap-messaging-outbox from 15.2.0 to 15.2.1

## [8.3.0] - 2026-06-01

### Changed
- Update parent from 7.0.0 to 8.0.1
- update jeap-starter from 23.0.0 to 23.1.0
- update jeap-spring-boot-roles-anywhere-starter from 3.1.0 to 3.2.0
- update jeap-crypto from 9.0.0 to 9.1.0
- update jeap-messaging from 15.1.0 to 15.2.0
- update jeap-spring-boot-vault-starter from 23.0.0 to 23.1.0
- update jeap-messaging-outbox from 15.1.0 to 15.2.0

## [8.2.0] - 2026-05-29
### Changed
- update jeap-messaging from 15.0.0 to 15.1.0
- update jeap-spring-boot-roles-anywhere-starter from 3.0.0 to 3.1.0
- update jeap-messaging-outbox from 15.0.0 to 15.1.0

## [8.1.0] - 2026-05-28
### Changed

- Support multiple topics in the audit outbox sender configuration
    - Use `jeap.audit.transactional-outbox.topics` to instantiate multiple senders for different topics

## [8.0.0] - 2026-05-27

### Changed

- Official release with spring boot 4

## [6.4.0] - 2026-04-16

### Changed
- Update parent from 6.0.2 to 6.0.3
- update jeap-starter from 21.2.0 to 21.3.0
- update jeap-spring-boot-roles-anywhere-starter from 1.26.0 to 1.27.0
- update jeap-crypto from 7.2.0 to 7.3.0
- update jeap-messaging from 13.3.0 to 13.4.0
- update jeap-spring-boot-vault-starter from 21.2.0 to 21.3.0
- update jeap-messaging-outbox from 13.3.0 to 13.4.0

## [6.3.0] - 2026-04-13

### Changed
- Update parent from 6.0.0 to 6.0.2
- update jeap-starter from 21.1.0 to 21.2.0
- update jeap-spring-boot-roles-anywhere-starter from 1.24.0 to 1.26.0
- update jeap-crypto from 7.1.0 to 7.2.0
- update jeap-messaging from 13.2.1 to 13.3.0
- update jeap-spring-boot-vault-starter from 21.1.0 to 21.2.0
- update jeap-messaging-outbox from 13.2.1 to 13.3.0

## [6.2.1] - 2026-04-09
### Changed
- update jeap-messaging from 13.2.0 to 13.2.1
- Signature is not verified if requireSignature is set to false. 
- update jeap-messaging-outbox from 13.2.0 to 13.2.1

## [6.2.0] - 2026-04-08
### Changed
  retry auth failures instead of stopping, allowing recovery without a restart.
  retry auth failures instead of stopping, allowing recovery without a restart.
- update jeap-messaging from 13.1.0 to 13.2.0
- Multi-cluster Kafka broker health indicator (`jeapKafka`) exposed via Spring Boot Actuator. 
- Configured `spring.kafka.listener.auth-exception-retry-interval=10s` by default so listener containers
- update jeap-messaging-outbox from 13.1.0 to 13.2.0

## [6.1.0] - 2026-04-02

### Changed
- Update parent from 5.20.0 to 6.0.0
- update jeap-starter from 21.0.0 to 21.1.0
- update jeap-spring-boot-roles-anywhere-starter from 1.23.0 to 1.24.0
- update jeap-crypto from 7.0.0 to 7.1.0
- update jeap-messaging from 13.0.0 to 13.1.0
- update jeap-spring-boot-vault-starter from 21.0.0 to 21.1.0
- update jeap-messaging-outbox from 13.0.0 to 13.1.0

## [6.0.0] - 2026-03-30
### Changed
  only (without resource/tenant) now have distinct names to avoid confusion with the role-based overloads:
  | Old method                                  | New method                                              |
  |---------------------------------------------|---------------------------------------------------------|
  | `hasRoleForPartner(operation, partner)`     | `hasOperationForPartner(operation, partner)`            |
  | `hasRoleForAllPartners(operation)`          | `hasOperationForAllPartners(operation)`                 |
  | `getAllRoles(operation)`                    | `getAllRolesForOperation(operation)`                    |
  | `getAllRolesForPartner(operation, partner)` | `getAllRolesForOperationAndPartner(operation, partner)` |
  | `getAllRolesForAllPartners(operation)`      | `getAllRolesForOperationForAllPartners(operation)`      |
  | `getPartnersForRole(operation)`             | `getPartnersForOperation(operation)`                    |
  separator characters (`@`, `%`, `#`, `:`, `!`) are passed as expression parameters instead of decomposed values.
  Access is denied and an error is logged.
  | Old method                                  | New method                                              |
  | `getPartnersForRole(operation)`             | `getPartnersForOperation(operation)`                    |
  | `getAllRolesForPartner(operation, partner)` | `getAllRolesForOperationAndPartner(operation, partner)` |
  | `hasRoleForAllPartners(operation)`          | `hasOperationForAllPartners(operation)`                 |
  | `hasRoleForPartner(operation, partner)`     | `hasOperationForPartner(operation, partner)`            |
  only (without resource/tenant) now have distinct names to avoid confusion with the role-based overloads:
  separator characters (`@`, `%`, `#`, `:`, `!`) are passed as expression parameters instead of decomposed values.
  |---------------------------------------------|---------------------------------------------------------|
  Access is denied and an error is logged.
  | `getAllRolesForAllPartners(operation)`      | `getAllRolesForOperationForAllPartners(operation)`      |
  | `getAllRoles(operation)`                    | `getAllRolesForOperation(operation)`                    |
  | Old method                                  | New method                                              |
  | `getPartnersForRole(operation)`             | `getPartnersForOperation(operation)`                    |
  | `getAllRolesForPartner(operation, partner)` | `getAllRolesForOperationAndPartner(operation, partner)` |
  | `hasRoleForAllPartners(operation)`          | `hasOperationForAllPartners(operation)`                 |
  | `hasRoleForPartner(operation, partner)`     | `hasOperationForPartner(operation, partner)`            |
  only (without resource/tenant) now have distinct names to avoid confusion with the role-based overloads:
  separator characters (`@`, `%`, `#`, `:`, `!`) are passed as expression parameters instead of decomposed values.
  |---------------------------------------------|---------------------------------------------------------|
  Access is denied and an error is logged.
  | `getAllRolesForAllPartners(operation)`      | `getAllRolesForOperationForAllPartners(operation)`      |
  | `getAllRoles(operation)`                    | `getAllRolesForOperation(operation)`                    |
- update jeap-starter from 20.5.0 to 21.0.0
- **Breaking:** Renamed operation-only methods in `SemanticRoleRepository` for clarity. Methods that query by operation
- Added input validation to `SemanticRoleRepository` that detects misuse where full token role strings containing
- update jeap-messaging from 12.5.0 to 13.0.0
- update jeap-spring-boot-vault-starter from 20.5.0 to 21.0.0
- update jeap-crypto from 6.5.0 to 7.0.0
- update jeap-messaging-outbox from 12.5.0 to 13.0.0

## [5.5.0] - 2026-03-26

### Changed
- Update parent from 5.19.4 to 5.20.0
- update jeap-starter from 20.4.0 to 20.5.0
- update jeap-spring-boot-vault-starter from 20.4.0 to 20.5.0
- update jeap-spring-boot-roles-anywhere-starter from 1.22.0 to 1.23.0
- update jeap-messaging from 12.4.0 to 12.5.0
- update jeap-crypto from 6.4.0 to 6.5.0
- update jeap-messaging-outbox from 12.4.0 to 12.5.0

## [5.4.0] - 2026-03-23

### Changed
- Update parent from 5.19.3 to 5.19.4
- update jeap-starter from 20.3.0 to 20.4.0
- update jeap-spring-boot-roles-anywhere-starter from 1.21.0 to 1.22.0
- update jeap-crypto from 6.3.0 to 6.4.0
- update jeap-messaging from 12.3.0 to 12.4.0
- update jeap-spring-boot-vault-starter from 20.3.0 to 20.4.0
- update jeap-messaging-outbox from 12.3.0 to 12.4.0

## [5.3.0] - 2026-03-18
### Changed
- update jeap-starter from 20.2.0 to 20.3.0
- Added an eIAM claim set converter that can adapt eIAM-issued access tokens for jeap security.
- update jeap-spring-boot-vault-starter from 20.2.0 to 20.3.0
- update jeap-crypto from 6.2.0 to 6.3.0
- update jeap-messaging from 12.2.0 to 12.3.0
- update jeap-messaging-outbox from 12.2.0 to 12.3.0

## [5.2.0] - 2026-03-17
### Changed
- update jeap-starter from 20.1.0 to 20.2.0
- Added support for a different set of semantic role parts separators.
- update jeap-spring-boot-vault-starter from 20.1.0 to 20.2.0
- update jeap-crypto from 6.1.0 to 6.2.0
- update jeap-messaging from 12.1.0 to 12.2.0
- update jeap-messaging-outbox from 12.1.0 to 12.2.0

## [5.1.0] - 2026-03-12

### Changed
- Update parent from 5.19.2 to 5.19.3
- update jeap-starter from 20.0.0 to 20.1.0
- update jeap-spring-boot-roles-anywhere-starter from 1.20.0 to 1.21.0
- update jeap-crypto from 6.0.0 to 6.1.0
- update jeap-messaging from 12.0.0 to 12.1.0
- update jeap-spring-boot-vault-starter from 20.0.0 to 20.1.0
- update jeap-messaging-outbox from 12.0.0 to 12.1.0

## [5.0.0] - 2026-03-11
### Changed
  - **Removed**
    - Support for reactive/webflux
    - Support removed from monitoring, tracing, swagger, security web-config starters
    - Support for reactive
    - Support for reactive
- update jeap-starter from 19.16.0 to 20.0.0
-  Breaking Change
- **Removed**
- update jeap-crypto from 5.16.0 to 6.0.0
- update jeap-messaging from 11.18.0 to 12.0.0
- update jeap-messaging-outbox from 11.20.0 to 12.0.0

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
