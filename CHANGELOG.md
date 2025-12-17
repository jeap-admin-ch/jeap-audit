# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres
to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [4.9.0] - 2025-12-17

### Changed

- Update parent from 5.16.4 to 5.16.5

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
