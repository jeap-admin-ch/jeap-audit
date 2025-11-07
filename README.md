# jEAP Audit Library

jEAP Audit is a library that provides standardized creation and delivery of audit records within the jEAP framework.
In distributed government systems, maintaining comprehensive audit trails is essential for compliance, security,
and operational transparency.
This library simplifies the process of creating and dispatching audit records by offering a fluent builder API
for the CreateAuditRecordCommand, ensuring consistent audit record structure across all jEAP-based services.
The library supports reliable audit record delivery through integration with the messaging-outbox pattern,
guaranteeing that audit commands are persistently stored and eventually delivered even in the face of system failures.
Consumer support enables downstream services to extract and process CreateAuditRecordCommand messages from the
message stream, facilitating centralized audit log aggregation and analysis.
By providing these building blocks, jEAP Audit helps Swiss government applications meet regulatory requirements
while maintaining system reliability and consistency in audit record handling across microservices architectures.

## Changes

This library is versioned using [Semantic Versioning](http://semver.org/) and all changes are documented in
[CHANGELOG.md](./CHANGELOG.md) following the format defined in [Keep a Changelog](http://keepachangelog.com/).

## Note

This repository is part the open source distribution of jEAP.
See [github.com/jeap-admin-ch/jeap](https://github.com/jeap-admin-ch/jeap)
for more information.

## License

This repository is Open Source Software licensed under the [Apache License 2.0](./LICENSE).

## Usage of transactional-outbox audit starter

If you use the transactional outbox you have to create the deferred_message and shedlock tables in your flyway 
(or something else) scripts. See [V1__create-outbox-schema.sql](https://github.com/jeap-admin-ch/jeap-messaging-outbox/blob/main/jeap-messaging-outbox/src/test/resources/db/migration/common/V1__create-outbox-schema.sql). 
