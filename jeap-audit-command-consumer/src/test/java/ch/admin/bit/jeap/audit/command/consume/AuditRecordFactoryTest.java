package ch.admin.bit.jeap.audit.command.consume;

import ch.admin.bit.jeap.audit.command.builder.CreateAuditRecordCommandBuilder;
import ch.admin.bit.jeap.audit.command.consume.model.*;
import ch.admin.bit.jeap.audit.record.create.AuditEventType;
import ch.admin.bit.jeap.audit.record.create.AuditObjectDataRole;
import ch.admin.bit.jeap.audit.record.create.CreateAuditRecordCommand;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("java:S5961")
class AuditRecordFactoryTest {

    private static final String SYSTEM_NAME = "MY_SYSTEM";
    private static final String SERVICE_NAME = "MY_SERVICE";
    private static final Instant TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Test
    void build_noProcessId_triggerUser_NoAuditObject_EventNoContextAndNoDataElement() {
        String userId = UUID.randomUUID().toString();
        String identityProvider = UUID.randomUUID().toString();

        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(SERVICE_NAME, SYSTEM_NAME, TIMESTAMP);
        builder.setEventType(AuditEventType.CREATED);
        builder.setTriggerUser(userId, identityProvider);

        CreateAuditRecordCommand command = builder.build();

        AuditRecord auditRecord = AuditRecordFactory.createAuditRecord(command);
        assertNotNull(auditRecord);
        assertEquals(SYSTEM_NAME, auditRecord.systemName());
        assertEquals(SERVICE_NAME, auditRecord.serviceName());
        assertEquals(TIMESTAMP, auditRecord.timestamp());
        assertNull(auditRecord.auditedData());
        AuditTrigger trigger = auditRecord.trigger();
        assertNotNull(trigger);
        assertEquals(AuditTrigger.AuditTriggerType.USER, trigger.type());
        AuditTriggerUser triggerUser = (AuditTriggerUser) trigger;
        assertEquals(userId, triggerUser.id());
        assertEquals(identityProvider, triggerUser.identityProvider());
        AuditEvent auditEvent = auditRecord.auditEvent();
        assertNotNull(auditEvent);
        assertEquals(AuditEventType.CREATED, auditEvent.eventType());
        assertNull(auditEvent.context());
        assertTrue(auditEvent.eventDataElements().isEmpty());
    }

    @Test
    void build_noProcessId_triggerSystem_NoAuditObject_EventNoContextAndNoDataElement() {
        String department = UUID.randomUUID().toString();
        String system = UUID.randomUUID().toString();
        String component = UUID.randomUUID().toString();

        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(SERVICE_NAME, SYSTEM_NAME, TIMESTAMP);
        builder.setEventType(AuditEventType.CREATED);
        builder.setTriggerSystem(department, system, component);

        CreateAuditRecordCommand command = builder.build();

        AuditRecord auditRecord = AuditRecordFactory.createAuditRecord(command);
        assertNotNull(auditRecord);
        assertEquals(SYSTEM_NAME, auditRecord.systemName());
        assertEquals(SERVICE_NAME, auditRecord.serviceName());
        assertEquals(TIMESTAMP, auditRecord.timestamp());
        assertNull(auditRecord.auditedData());
        AuditTrigger trigger = auditRecord.trigger();
        assertNotNull(trigger);
        assertEquals(AuditTrigger.AuditTriggerType.SYSTEM_COMPONENT, trigger.type());
        AuditTriggerSystemComponent triggerSystemComponent = (AuditTriggerSystemComponent) trigger;
        assertEquals(department, triggerSystemComponent.department());
        assertEquals(system, triggerSystemComponent.system());
        assertEquals(component, triggerSystemComponent.component());
        AuditEvent auditEvent = auditRecord.auditEvent();
        assertNotNull(auditEvent);
        assertEquals(AuditEventType.CREATED, auditEvent.eventType());
        assertNull(auditEvent.context());
        assertTrue(auditEvent.eventDataElements().isEmpty());
    }

    @Test
    void build_withEventContext_withoutProcessId() {
        String userId = UUID.randomUUID().toString();
        String identityProvider = UUID.randomUUID().toString();
        String useCase = UUID.randomUUID().toString();

        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(SERVICE_NAME, SYSTEM_NAME, TIMESTAMP);
        builder.setEventType(AuditEventType.CREATED);
        builder.setTriggerUser(userId, identityProvider);

        builder.setContext(useCase);

        CreateAuditRecordCommand command = builder.build();

        AuditRecord auditRecord = AuditRecordFactory.createAuditRecord(command);
        assertNotNull(auditRecord);
        assertEquals(SYSTEM_NAME, auditRecord.systemName());
        assertEquals(SERVICE_NAME, auditRecord.serviceName());
        assertEquals(TIMESTAMP, auditRecord.timestamp());
        assertNull(auditRecord.auditedData());
        AuditTrigger trigger = auditRecord.trigger();
        assertNotNull(trigger);
        assertEquals(AuditTrigger.AuditTriggerType.USER, trigger.type());
        AuditTriggerUser triggerUser = (AuditTriggerUser) trigger;
        assertEquals(userId, triggerUser.id());
        assertEquals(identityProvider, triggerUser.identityProvider());
        AuditEvent auditEvent = auditRecord.auditEvent();
        assertNotNull(auditEvent);
        assertEquals(AuditEventType.CREATED, auditEvent.eventType());
        assertTrue(auditEvent.eventDataElements().isEmpty());
        AuditContext context = auditEvent.context();
        assertNotNull(context);
        assertEquals(useCase, context.useCase());
        assertNull(context.processId());
    }

    @Test
    void build_withEventContext_withAllPropertiesSet() {
        String userId = UUID.randomUUID().toString();
        String identityProvider = UUID.randomUUID().toString();
        String useCase = UUID.randomUUID().toString();
        String processId = UUID.randomUUID().toString();

        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(SERVICE_NAME, SYSTEM_NAME, TIMESTAMP);
        builder.setEventType(AuditEventType.CREATED);
        builder.setTriggerUser(userId, identityProvider);

        builder.setContext(useCase, processId);

        CreateAuditRecordCommand command = builder.build();

        AuditRecord auditRecord = AuditRecordFactory.createAuditRecord(command);
        assertNotNull(auditRecord);
        assertEquals(SYSTEM_NAME, auditRecord.systemName());
        assertEquals(SERVICE_NAME, auditRecord.serviceName());
        assertEquals(TIMESTAMP, auditRecord.timestamp());
        assertNull(auditRecord.auditedData());
        AuditTrigger trigger = auditRecord.trigger();
        assertNotNull(trigger);
        assertEquals(AuditTrigger.AuditTriggerType.USER, trigger.type());
        AuditTriggerUser triggerUser = (AuditTriggerUser) trigger;
        assertEquals(userId, triggerUser.id());
        assertEquals(identityProvider, triggerUser.identityProvider());
        AuditEvent auditEvent = auditRecord.auditEvent();
        assertNotNull(auditEvent);
        assertEquals(AuditEventType.CREATED, auditEvent.eventType());
        assertTrue(auditEvent.eventDataElements().isEmpty());
        AuditContext context = auditEvent.context();
        assertNotNull(context);
        assertEquals(useCase, context.useCase());
        assertEquals(processId, context.processId());
    }

    @Test
    void build_withEventContext_withProcessIdSameAsCommand() {
        String userId = UUID.randomUUID().toString();
        String identityProvider = UUID.randomUUID().toString();
        String useCase = UUID.randomUUID().toString();
        String processId = UUID.randomUUID().toString();

        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(SERVICE_NAME, SYSTEM_NAME, TIMESTAMP, processId);
        builder.setEventType(AuditEventType.CREATED);
        builder.setTriggerUser(userId, identityProvider);

        builder.setContext(useCase, processId);

        CreateAuditRecordCommand command = builder.build();

        AuditRecord auditRecord = AuditRecordFactory.createAuditRecord(command);
        assertNotNull(auditRecord);
        assertEquals(SYSTEM_NAME, auditRecord.systemName());
        assertEquals(SERVICE_NAME, auditRecord.serviceName());
        assertEquals(TIMESTAMP, auditRecord.timestamp());
        assertNull(auditRecord.auditedData());
        AuditTrigger trigger = auditRecord.trigger();
        assertNotNull(trigger);
        assertEquals(AuditTrigger.AuditTriggerType.USER, trigger.type());
        AuditTriggerUser triggerUser = (AuditTriggerUser) trigger;
        assertEquals(userId, triggerUser.id());
        assertEquals(identityProvider, triggerUser.identityProvider());
        AuditEvent auditEvent = auditRecord.auditEvent();
        assertNotNull(auditEvent);
        assertEquals(AuditEventType.CREATED, auditEvent.eventType());
        assertTrue(auditEvent.eventDataElements().isEmpty());
        AuditContext context = auditEvent.context();
        assertNotNull(context);
        assertEquals(useCase, context.useCase());
        assertEquals(processId, context.processId());
    }

    @Test
    void build_WithEventDataElements() {
        String userId = UUID.randomUUID().toString();
        String identityProvider = UUID.randomUUID().toString();
        String key1 = UUID.randomUUID().toString();
        String value1 = UUID.randomUUID().toString();
        String key2 = UUID.randomUUID().toString();
        String value2 = UUID.randomUUID().toString();


        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(SERVICE_NAME, SYSTEM_NAME, TIMESTAMP);
        builder.setEventType(AuditEventType.CREATED);
        builder.addEventData(key1, value1);
        builder.addEventData(key2, value2);
        builder.setTriggerUser(userId, identityProvider);

        CreateAuditRecordCommand command = builder.build();

        AuditRecord auditRecord = AuditRecordFactory.createAuditRecord(command);
        assertNotNull(auditRecord);
        assertEquals(SYSTEM_NAME, auditRecord.systemName());
        assertEquals(SERVICE_NAME, auditRecord.serviceName());
        assertEquals(TIMESTAMP, auditRecord.timestamp());
        assertNull(auditRecord.auditedData());
        AuditTrigger trigger = auditRecord.trigger();
        assertNotNull(trigger);
        assertEquals(AuditTrigger.AuditTriggerType.USER, trigger.type());
        AuditTriggerUser triggerUser = (AuditTriggerUser) trigger;
        assertEquals(userId, triggerUser.id());
        assertEquals(identityProvider, triggerUser.identityProvider());
        AuditEvent auditEvent = auditRecord.auditEvent();
        assertNotNull(auditEvent);
        assertEquals(AuditEventType.CREATED, auditEvent.eventType());
        List<AuditEventDataElement> auditEventDataElements = auditEvent.eventDataElements();
        assertEquals(2, auditEventDataElements.size());
        AuditEventDataElement auditEventDataElement1 = auditEventDataElements.get(0);
        assertEquals(key1, auditEventDataElement1.key());
        assertEquals(value1, auditEventDataElement1.value());
        AuditEventDataElement auditEventDataElement2 = auditEventDataElements.get(1);
        assertEquals(key2, auditEventDataElement2.key());
        assertEquals(value2, auditEventDataElement2.value());
        assertNull(auditEvent.context());
    }

    @Test
    void build_withAuditObject_NoVersionNoDataElements() {
        String userId = UUID.randomUUID().toString();
        String identityProvider = UUID.randomUUID().toString();
        String type = UUID.randomUUID().toString();
        String id = UUID.randomUUID().toString();

        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(SERVICE_NAME, SYSTEM_NAME, TIMESTAMP);
        builder.setEventType(AuditEventType.CREATED);
        builder.setTriggerUser(userId, identityProvider);
        builder.setAuditObject(type, id);

        CreateAuditRecordCommand command = builder.build();

        AuditRecord auditRecord = AuditRecordFactory.createAuditRecord(command);
        assertNotNull(auditRecord);
        assertEquals(SYSTEM_NAME, auditRecord.systemName());
        assertEquals(SERVICE_NAME, auditRecord.serviceName());
        assertEquals(TIMESTAMP, auditRecord.timestamp());
        AuditObject auditObject = auditRecord.auditedData();
        assertNotNull(auditObject);
        assertEquals(type, auditObject.type());
        assertEquals(id, auditObject.id());
        assertNull(auditObject.version());
        assertTrue(auditObject.objectDataList().isEmpty());
        AuditTrigger trigger = auditRecord.trigger();
        assertNotNull(trigger);
        assertEquals(AuditTrigger.AuditTriggerType.USER, trigger.type());
        AuditTriggerUser triggerUser = (AuditTriggerUser) trigger;
        assertEquals(userId, triggerUser.id());
        assertEquals(identityProvider, triggerUser.identityProvider());
        AuditEvent auditEvent = auditRecord.auditEvent();
        assertNotNull(auditEvent);
        assertEquals(AuditEventType.CREATED, auditEvent.eventType());
        assertTrue(auditEvent.eventDataElements().isEmpty());
        assertNull(auditEvent.context());
    }

    @Test
    void build_withAuditObject_WithVersionNoDataElements() {
        String userId = UUID.randomUUID().toString();
        String identityProvider = UUID.randomUUID().toString();
        String type = UUID.randomUUID().toString();
        String id = UUID.randomUUID().toString();
        String version = UUID.randomUUID().toString();

        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(SERVICE_NAME, SYSTEM_NAME, TIMESTAMP);
        builder.setEventType(AuditEventType.CREATED);
        builder.setTriggerUser(userId, identityProvider);
        builder.setAuditObject(type, id, version);

        CreateAuditRecordCommand command = builder.build();

        AuditRecord auditRecord = AuditRecordFactory.createAuditRecord(command);
        assertNotNull(auditRecord);
        assertEquals(SYSTEM_NAME, auditRecord.systemName());
        assertEquals(SERVICE_NAME, auditRecord.serviceName());
        assertEquals(TIMESTAMP, auditRecord.timestamp());
        AuditObject auditObject = auditRecord.auditedData();
        assertNotNull(auditObject);
        assertEquals(type, auditObject.type());
        assertEquals(id, auditObject.id());
        assertEquals(version, auditObject.version());
        assertTrue(auditObject.objectDataList().isEmpty());
        AuditTrigger trigger = auditRecord.trigger();
        assertNotNull(trigger);
        assertEquals(AuditTrigger.AuditTriggerType.USER, trigger.type());
        AuditTriggerUser triggerUser = (AuditTriggerUser) trigger;
        assertEquals(userId, triggerUser.id());
        assertEquals(identityProvider, triggerUser.identityProvider());
        AuditEvent auditEvent = auditRecord.auditEvent();
        assertNotNull(auditEvent);
        assertEquals(AuditEventType.CREATED, auditEvent.eventType());
        assertTrue(auditEvent.eventDataElements().isEmpty());
        assertNull(auditEvent.context());
    }

    @Test
    void build_withAuditObject_WithDataElementsNoRole() {
        String userId = UUID.randomUUID().toString();
        String identityProvider = UUID.randomUUID().toString();
        String type = UUID.randomUUID().toString();
        String id = UUID.randomUUID().toString();
        String version = UUID.randomUUID().toString();
        String nameValue = UUID.randomUUID().toString();
        String valueValue = UUID.randomUUID().toString();
        String nameJson1 = UUID.randomUUID().toString();
        String json1 = "{\"gugu\": \"juhu\"}";
        String nameJson2 = UUID.randomUUID().toString();
        ByteBuffer json2 = ByteBuffer.wrap("{\"gugu\": \"juhu\"}".getBytes(StandardCharsets.UTF_8));
        String nameS3 = UUID.randomUUID().toString();
        String s3 = UUID.randomUUID().toString();

        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(SERVICE_NAME, SYSTEM_NAME, TIMESTAMP);
        builder.setEventType(AuditEventType.CREATED);
        builder.setTriggerUser(userId, identityProvider);
        builder.setAuditObject(type, id, version);
        builder.addAuditObjectDataValue(nameValue, valueValue);
        builder.addAuditObjectDataJSON(nameJson1, json1);
        builder.addAuditObjectDataJSON(nameJson2, json2);
        builder.addAuditObjectDataS3(nameS3, s3);

        CreateAuditRecordCommand command = builder.build();

        AuditRecord auditRecord = AuditRecordFactory.createAuditRecord(command);
        assertNotNull(auditRecord);
        assertEquals(SYSTEM_NAME, auditRecord.systemName());
        assertEquals(SERVICE_NAME, auditRecord.serviceName());
        assertEquals(TIMESTAMP, auditRecord.timestamp());
        AuditObject auditObject = auditRecord.auditedData();
        assertNotNull(auditObject);
        assertEquals(type, auditObject.type());
        assertEquals(id, auditObject.id());
        assertEquals(version, auditObject.version());
        List<AuditObjectData> auditObjectDataList = auditObject.objectDataList();
        assertEquals(4, auditObject.objectDataList().size());
        AuditObjectData auditObjectData1 = auditObjectDataList.get(0);
        assertEquals(AuditObjectData.AuditObjectDataType.VALUE, auditObjectData1.type());

        AuditObjectDataValue auditObjectDataValue = (AuditObjectDataValue) auditObjectData1;
        assertEquals(nameValue, auditObjectDataValue.name());
        assertEquals(valueValue, auditObjectDataValue.value());
        assertNull(auditObjectDataValue.role());

        AuditObjectData auditObjectData2 = auditObjectDataList.get(1);
        assertEquals(AuditObjectData.AuditObjectDataType.JSON, auditObjectData2.type());
        AuditObjectDataJSON auditObjectDataJson1 = (AuditObjectDataJSON) auditObjectData2;
        assertEquals(nameJson1, auditObjectDataJson1.name());
        assertEquals(json1, new String(auditObjectDataJson1.jsonAsUtf8().array()));
        assertNull(auditObjectDataJson1.role());

        AuditObjectData auditObjectData3 = auditObjectDataList.get(2);
        assertEquals(AuditObjectData.AuditObjectDataType.JSON, auditObjectData3.type());
        AuditObjectDataJSON auditObjectDataJson2 = (AuditObjectDataJSON) auditObjectData3;
        assertEquals(nameJson2, auditObjectDataJson2.name());
        assertEquals(json2, auditObjectDataJson2.jsonAsUtf8());
        assertNull(auditObjectDataJson2.role());

        AuditObjectData auditObjectData4 = auditObjectDataList.get(3);
        assertEquals(AuditObjectData.AuditObjectDataType.S3_REFERENCE, auditObjectData4.type());
        AuditObjectDataS3 auditObjectDataS3 = (AuditObjectDataS3) auditObjectData4;
        assertEquals(nameS3, auditObjectDataS3.name());
        assertEquals(s3, auditObjectDataS3.objectReference());
        assertNull(auditObjectDataS3.role());

        AuditTrigger trigger = auditRecord.trigger();
        assertNotNull(trigger);
        assertEquals(AuditTrigger.AuditTriggerType.USER, trigger.type());
        AuditTriggerUser triggerUser = (AuditTriggerUser) trigger;
        assertEquals(userId, triggerUser.id());
        assertEquals(identityProvider, triggerUser.identityProvider());
        AuditEvent auditEvent = auditRecord.auditEvent();
        assertNotNull(auditEvent);
        assertEquals(AuditEventType.CREATED, auditEvent.eventType());
        assertTrue(auditEvent.eventDataElements().isEmpty());
        assertNull(auditEvent.context());
    }

    @Test
    void build_withAuditObject_WithDataElementsWithRole() {
        String userId = UUID.randomUUID().toString();
        String identityProvider = UUID.randomUUID().toString();
        String type = UUID.randomUUID().toString();
        String id = UUID.randomUUID().toString();
        String version = UUID.randomUUID().toString();
        String nameValue = UUID.randomUUID().toString();
        String valueValue = UUID.randomUUID().toString();
        String nameJson1 = UUID.randomUUID().toString();
        String json1 = "{\"gugu\": \"juhu\"}";
        String nameJson2 = UUID.randomUUID().toString();
        ByteBuffer json2 = ByteBuffer.wrap("{\"gugu\": \"juhu\"}".getBytes(StandardCharsets.UTF_8));
        String nameS3 = UUID.randomUUID().toString();
        String s3 = UUID.randomUUID().toString();

        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(SERVICE_NAME, SYSTEM_NAME, TIMESTAMP);
        builder.setEventType(AuditEventType.CREATED);
        builder.setTriggerUser(userId, identityProvider);
        builder.setAuditObject(type, id, version);
        builder.addAuditObjectDataValue(AuditObjectDataRole.OLD, nameValue, valueValue);
        builder.addAuditObjectDataJSON(AuditObjectDataRole.OLD, nameJson1, json1);
        builder.addAuditObjectDataJSON(AuditObjectDataRole.NEW, nameJson2, json2);
        builder.addAuditObjectDataS3(AuditObjectDataRole.NEW, nameS3, s3);

        CreateAuditRecordCommand command = builder.build();

        AuditRecord auditRecord = AuditRecordFactory.createAuditRecord(command);
        assertNotNull(auditRecord);
        assertEquals(SYSTEM_NAME, auditRecord.systemName());
        assertEquals(SERVICE_NAME, auditRecord.serviceName());
        assertEquals(TIMESTAMP, auditRecord.timestamp());
        AuditObject auditObject = auditRecord.auditedData();
        assertNotNull(auditObject);
        assertEquals(type, auditObject.type());
        assertEquals(id, auditObject.id());
        assertEquals(version, auditObject.version());
        List<AuditObjectData> auditObjectDataList = auditObject.objectDataList();
        assertEquals(4, auditObject.objectDataList().size());
        AuditObjectData auditObjectData1 = auditObjectDataList.get(0);
        assertEquals(AuditObjectData.AuditObjectDataType.VALUE, auditObjectData1.type());

        AuditObjectDataValue auditObjectDataValue = (AuditObjectDataValue) auditObjectData1;
        assertEquals(nameValue, auditObjectDataValue.name());
        assertEquals(valueValue, auditObjectDataValue.value());
        assertEquals(AuditObjectDataRole.OLD, auditObjectDataValue.role());

        AuditObjectData auditObjectData2 = auditObjectDataList.get(1);
        assertEquals(AuditObjectData.AuditObjectDataType.JSON, auditObjectData2.type());
        AuditObjectDataJSON auditObjectDataJson1 = (AuditObjectDataJSON) auditObjectData2;
        assertEquals(nameJson1, auditObjectDataJson1.name());
        assertEquals(json1, new String(auditObjectDataJson1.jsonAsUtf8().array()));
        assertEquals(AuditObjectDataRole.OLD, auditObjectDataJson1.role());

        AuditObjectData auditObjectData3 = auditObjectDataList.get(2);
        assertEquals(AuditObjectData.AuditObjectDataType.JSON, auditObjectData3.type());
        AuditObjectDataJSON auditObjectDataJson2 = (AuditObjectDataJSON) auditObjectData3;
        assertEquals(nameJson2, auditObjectDataJson2.name());
        assertEquals(json2, auditObjectDataJson2.jsonAsUtf8());
        assertEquals(AuditObjectDataRole.NEW, auditObjectDataJson2.role());

        AuditObjectData auditObjectData4 = auditObjectDataList.get(3);
        assertEquals(AuditObjectData.AuditObjectDataType.S3_REFERENCE, auditObjectData4.type());
        AuditObjectDataS3 auditObjectDataS3 = (AuditObjectDataS3) auditObjectData4;
        assertEquals(nameS3, auditObjectDataS3.name());
        assertEquals(s3, auditObjectDataS3.objectReference());
        assertEquals(AuditObjectDataRole.NEW, auditObjectDataS3.role());

        AuditTrigger trigger = auditRecord.trigger();
        assertNotNull(trigger);
        assertEquals(AuditTrigger.AuditTriggerType.USER, trigger.type());
        AuditTriggerUser triggerUser = (AuditTriggerUser) trigger;
        assertEquals(userId, triggerUser.id());
        assertEquals(identityProvider, triggerUser.identityProvider());
        AuditEvent auditEvent = auditRecord.auditEvent();
        assertNotNull(auditEvent);
        assertEquals(AuditEventType.CREATED, auditEvent.eventType());
        assertTrue(auditEvent.eventDataElements().isEmpty());
        assertNull(auditEvent.context());
    }
}
