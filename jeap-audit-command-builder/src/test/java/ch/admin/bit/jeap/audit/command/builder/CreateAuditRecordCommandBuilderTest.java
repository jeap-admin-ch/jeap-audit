package ch.admin.bit.jeap.audit.command.builder;

import ch.admin.bit.jeap.audit.record.create.*;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("java:S5961")
class CreateAuditRecordCommandBuilderTest {

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

        assertNull(command.getProcessId());
        assertNotNull(command.getIdentity().getIdempotenceId());
        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());

        CreateAuditRecordCommandPayload payload = command.getPayload();
        assertNotNull(payload);
        assertEquals(TIMESTAMP, payload.getTimestamp());

        AuditEventDetails event = payload.getEvent();
        assertNotNull(event);
        assertEquals(AuditEventType.CREATED, event.getType());
        assertNull(event.getContext());
        assertNull(event.getEventData());

        Object trigger = payload.getTrigger();
        assertNotNull(trigger);
        assertTrue(trigger instanceof AuditUser);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(userId, auditUser.getId());
        assertEquals(identityProvider, auditUser.getIdentityProvider());

        assertNull(payload.getAuditedData());
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

        assertNull(command.getProcessId());
        assertNotNull(command.getIdentity().getIdempotenceId());
        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());

        CreateAuditRecordCommandPayload payload = command.getPayload();
        assertNotNull(payload);
        assertEquals(TIMESTAMP, payload.getTimestamp());

        AuditEventDetails event = payload.getEvent();
        assertNotNull(event);
        assertEquals(AuditEventType.CREATED, event.getType());
        assertNull(event.getContext());
        assertNull(event.getEventData());

        Object trigger = payload.getTrigger();
        assertNotNull(trigger);
        assertTrue(trigger instanceof AuditSystemComponent);
        AuditSystemComponent auditSystemComponent = (AuditSystemComponent) trigger;
        assertEquals(department, auditSystemComponent.getDepartment());
        assertEquals(system, auditSystemComponent.getSystem());
        assertEquals(component, auditSystemComponent.getComponent());

        assertNull(payload.getAuditedData());
    }

    @Test
    void build_noTrigger() {
        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(SERVICE_NAME, SYSTEM_NAME, TIMESTAMP);
        builder.setEventType(AuditEventType.CREATED);

        assertThrows(IllegalStateException.class, builder::build);
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

        assertNull(command.getProcessId());
        assertNotNull(command.getIdentity().getIdempotenceId());
        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());

        CreateAuditRecordCommandPayload payload = command.getPayload();
        assertNotNull(payload);
        assertEquals(TIMESTAMP, payload.getTimestamp());

        AuditEventDetails event = payload.getEvent();
        assertNotNull(event);
        assertEquals(AuditEventType.CREATED, event.getType());
        assertNull(event.getEventData());
        AuditContext context = event.getContext();
        assertNotNull(context);
        assertEquals(useCase, context.getUseCase());
        assertNull(context.getProcessId());

        Object trigger = payload.getTrigger();
        assertNotNull(trigger);
        assertTrue(trigger instanceof AuditUser);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(userId, auditUser.getId());
        assertEquals(identityProvider, auditUser.getIdentityProvider());

        assertNull(payload.getAuditedData());
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

        assertEquals(processId, command.getProcessId());
        assertNotNull(command.getIdentity().getIdempotenceId());
        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());

        CreateAuditRecordCommandPayload payload = command.getPayload();
        assertNotNull(payload);
        assertEquals(TIMESTAMP, payload.getTimestamp());

        AuditEventDetails event = payload.getEvent();
        assertNotNull(event);
        assertEquals(AuditEventType.CREATED, event.getType());
        assertNull(event.getEventData());
        AuditContext context = event.getContext();
        assertNotNull(context);
        assertEquals(useCase, context.getUseCase());
        assertEquals(processId, context.getProcessId());

        Object trigger = payload.getTrigger();
        assertNotNull(trigger);
        assertTrue(trigger instanceof AuditUser);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(userId, auditUser.getId());
        assertEquals(identityProvider, auditUser.getIdentityProvider());

        assertNull(payload.getAuditedData());
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

        assertEquals(processId, command.getProcessId());
        assertNotNull(command.getIdentity().getIdempotenceId());
        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());

        CreateAuditRecordCommandPayload payload = command.getPayload();
        assertNotNull(payload);
        assertEquals(TIMESTAMP, payload.getTimestamp());

        AuditEventDetails event = payload.getEvent();
        assertNotNull(event);
        assertEquals(AuditEventType.CREATED, event.getType());
        assertNull(event.getEventData());
        AuditContext context = event.getContext();
        assertNotNull(context);
        assertEquals(useCase, context.getUseCase());
        assertEquals(processId, context.getProcessId());

        Object trigger = payload.getTrigger();
        assertNotNull(trigger);
        assertTrue(trigger instanceof AuditUser);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(userId, auditUser.getId());
        assertEquals(identityProvider, auditUser.getIdentityProvider());

        assertNull(payload.getAuditedData());
    }

    @Test
    void build_withEventContext_withProcessIdNotSameAsCommand() {
        String userId = UUID.randomUUID().toString();
        String identityProvider = UUID.randomUUID().toString();
        String useCase = UUID.randomUUID().toString();
        String processId1 = UUID.randomUUID().toString();
        String processId2 = UUID.randomUUID().toString();

        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(SERVICE_NAME, SYSTEM_NAME, TIMESTAMP, processId1);
        builder.setEventType(AuditEventType.CREATED);
        builder.setTriggerUser(userId, identityProvider);

        assertThrows(IllegalArgumentException.class, () -> builder.setContext(useCase, processId2));
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

        assertNull(command.getProcessId());
        assertNotNull(command.getIdentity().getIdempotenceId());
        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());

        CreateAuditRecordCommandPayload payload = command.getPayload();
        assertNotNull(payload);
        assertEquals(TIMESTAMP, payload.getTimestamp());

        AuditEventDetails event = payload.getEvent();
        assertNotNull(event);
        assertEquals(AuditEventType.CREATED, event.getType());
        assertNull(event.getContext());
        List<AuditEventDataElement> eventData = event.getEventData();
        assertNotNull(eventData);
        assertEquals(2, eventData.size());
        assertEquals(key1, eventData.get(0).getKey());
        assertEquals(value1, eventData.get(0).getValue());
        assertEquals(key2, eventData.get(1).getKey());
        assertEquals(value2, eventData.get(1).getValue());

        Object trigger = payload.getTrigger();
        assertNotNull(trigger);
        assertTrue(trigger instanceof AuditUser);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(userId, auditUser.getId());
        assertEquals(identityProvider, auditUser.getIdentityProvider());

        assertNull(payload.getAuditedData());
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

        assertNull(command.getProcessId());
        assertNotNull(command.getIdentity().getIdempotenceId());
        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());

        CreateAuditRecordCommandPayload payload = command.getPayload();
        assertNotNull(payload);
        assertEquals(TIMESTAMP, payload.getTimestamp());

        AuditEventDetails event = payload.getEvent();
        assertNotNull(event);
        assertEquals(AuditEventType.CREATED, event.getType());
        assertNull(event.getContext());
        assertNull(event.getEventData());

        Object trigger = payload.getTrigger();
        assertNotNull(trigger);
        assertTrue(trigger instanceof AuditUser);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(userId, auditUser.getId());
        assertEquals(identityProvider, auditUser.getIdentityProvider());

        AuditObject auditedData = payload.getAuditedData();
        assertNotNull(auditedData);
        assertEquals(type, auditedData.getType());
        assertEquals(id, auditedData.getId());
        assertNull(auditedData.getVersion());
        assertNull(auditedData.getObjectData());
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

        assertNull(command.getProcessId());
        assertNotNull(command.getIdentity().getIdempotenceId());
        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());

        CreateAuditRecordCommandPayload payload = command.getPayload();
        assertNotNull(payload);
        assertEquals(TIMESTAMP, payload.getTimestamp());

        AuditEventDetails event = payload.getEvent();
        assertNotNull(event);
        assertEquals(AuditEventType.CREATED, event.getType());
        assertNull(event.getContext());
        assertNull(event.getEventData());

        Object trigger = payload.getTrigger();
        assertNotNull(trigger);
        assertTrue(trigger instanceof AuditUser);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(userId, auditUser.getId());
        assertEquals(identityProvider, auditUser.getIdentityProvider());

        AuditObject auditedData = payload.getAuditedData();
        assertNotNull(auditedData);
        assertEquals(type, auditedData.getType());
        assertEquals(id, auditedData.getId());
        assertEquals(version, auditedData.getVersion());
        assertNull(auditedData.getObjectData());
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

        assertNull(command.getProcessId());
        assertNotNull(command.getIdentity().getIdempotenceId());
        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());

        CreateAuditRecordCommandPayload payload = command.getPayload();
        assertNotNull(payload);
        assertEquals(TIMESTAMP, payload.getTimestamp());

        AuditEventDetails event = payload.getEvent();
        assertNotNull(event);
        assertEquals(AuditEventType.CREATED, event.getType());
        assertNull(event.getContext());
        assertNull(event.getEventData());

        Object trigger = payload.getTrigger();
        assertNotNull(trigger);
        assertTrue(trigger instanceof AuditUser);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(userId, auditUser.getId());
        assertEquals(identityProvider, auditUser.getIdentityProvider());

        AuditObject auditedData = payload.getAuditedData();
        assertNotNull(auditedData);
        assertEquals(type, auditedData.getType());
        assertEquals(id, auditedData.getId());
        assertEquals(version, auditedData.getVersion());
        List<Object> objectData = auditedData.getObjectData();
        assertNotNull(objectData);
        assertEquals(4, objectData.size());
        Object objectData1 = objectData.get(0);
        assertTrue(objectData1 instanceof AuditObjectDataValue);
        AuditObjectDataValue auditObjectDataValue = (AuditObjectDataValue) objectData1;
        assertEquals(nameValue, auditObjectDataValue.getName());
        assertEquals(valueValue, auditObjectDataValue.getValue());
        assertNull(auditObjectDataValue.getRole());

        Object objectData2 = objectData.get(1);
        assertTrue(objectData2 instanceof AuditObjectDataJSON);
        AuditObjectDataJSON auditObjectDataJSON1 = (AuditObjectDataJSON) objectData2;
        assertEquals(nameJson1, auditObjectDataJSON1.getName());
        assertEquals(json1, new String(auditObjectDataJSON1.getJsonAsUTF8().array()));
        assertNull(auditObjectDataJSON1.getRole());

        Object objectData3 = objectData.get(2);
        assertTrue(objectData3 instanceof AuditObjectDataJSON);
        AuditObjectDataJSON auditObjectDataJSON2 = (AuditObjectDataJSON) objectData3;
        assertEquals(nameJson2, auditObjectDataJSON2.getName());
        assertEquals(json2, auditObjectDataJSON2.getJsonAsUTF8());
        assertNull(auditObjectDataJSON2.getRole());

        Object objectData4 = objectData.get(3);
        assertTrue(objectData4 instanceof AuditObjectDataS3);
        AuditObjectDataS3 auditObjectDataS3 = (AuditObjectDataS3) objectData4;
        assertEquals(nameS3, auditObjectDataS3.getName());
        assertEquals(s3, auditObjectDataS3.getObjectReference());
        assertNull(auditObjectDataS3.getRole());
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

        assertNull(command.getProcessId());
        assertNotNull(command.getIdentity().getIdempotenceId());
        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());

        CreateAuditRecordCommandPayload payload = command.getPayload();
        assertNotNull(payload);
        assertEquals(TIMESTAMP, payload.getTimestamp());

        AuditEventDetails event = payload.getEvent();
        assertNotNull(event);
        assertEquals(AuditEventType.CREATED, event.getType());
        assertNull(event.getContext());
        assertNull(event.getEventData());

        Object trigger = payload.getTrigger();
        assertNotNull(trigger);
        assertTrue(trigger instanceof AuditUser);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(userId, auditUser.getId());
        assertEquals(identityProvider, auditUser.getIdentityProvider());

        AuditObject auditedData = payload.getAuditedData();
        assertNotNull(auditedData);
        assertEquals(type, auditedData.getType());
        assertEquals(id, auditedData.getId());
        assertEquals(version, auditedData.getVersion());
        List<Object> objectData = auditedData.getObjectData();
        assertNotNull(objectData);
        assertEquals(4, objectData.size());
        Object objectData1 = objectData.get(0);
        assertTrue(objectData1 instanceof AuditObjectDataValue);
        AuditObjectDataValue auditObjectDataValue = (AuditObjectDataValue) objectData1;
        assertEquals(nameValue, auditObjectDataValue.getName());
        assertEquals(valueValue, auditObjectDataValue.getValue());
        assertEquals(AuditObjectDataRole.OLD, auditObjectDataValue.getRole());

        Object objectData2 = objectData.get(1);
        assertTrue(objectData2 instanceof AuditObjectDataJSON);
        AuditObjectDataJSON auditObjectDataJSON1 = (AuditObjectDataJSON) objectData2;
        assertEquals(nameJson1, auditObjectDataJSON1.getName());
        assertEquals(json1, new String(auditObjectDataJSON1.getJsonAsUTF8().array()));
        assertEquals(AuditObjectDataRole.OLD, auditObjectDataJSON1.getRole());

        Object objectData3 = objectData.get(2);
        assertTrue(objectData3 instanceof AuditObjectDataJSON);
        AuditObjectDataJSON auditObjectDataJSON2 = (AuditObjectDataJSON) objectData3;
        assertEquals(nameJson2, auditObjectDataJSON2.getName());
        assertEquals(json2, auditObjectDataJSON2.getJsonAsUTF8());
        assertEquals(AuditObjectDataRole.NEW, auditObjectDataJSON2.getRole());

        Object objectData4 = objectData.get(3);
        assertTrue(objectData4 instanceof AuditObjectDataS3);
        AuditObjectDataS3 auditObjectDataS3 = (AuditObjectDataS3) objectData4;
        assertEquals(nameS3, auditObjectDataS3.getName());
        assertEquals(s3, auditObjectDataS3.getObjectReference());
        assertEquals(AuditObjectDataRole.NEW, auditObjectDataS3.getRole());
    }
}
