package ch.admin.bit.jeap.audit.command.builder;

import ch.admin.bit.jeap.audit.record.create.*;
import ch.admin.bit.jeap.command.avro.AvroCommandBuilder;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateAuditRecordCommandBuilder extends AvroCommandBuilder<CreateAuditRecordCommandBuilder, CreateAuditRecordCommand> {

    private final String serviceName;
    private final String systemName;

    private String processId;

    private Instant timestamp;
    private AuditEventType eventType = AuditEventType.UNKNOWN;
    private AuditContext context;
    private List<AuditEventDataElement> eventData;
    private AuditUser user;
    private AuditSystemComponent systemComponent;
    private AuditObject auditedData;
    private List<Object> objectData = new ArrayList<>();

    public static CreateAuditRecordCommandBuilder createCommandBuilder(String serviceName, String systemName, Instant timestamp) {
        return createCommandBuilder(serviceName, systemName, timestamp, null);
    }

    public static CreateAuditRecordCommandBuilder createCommandBuilder(String serviceName, String systemName, Instant timestamp, String processId) {
        return new CreateAuditRecordCommandBuilder(serviceName, systemName, timestamp, processId);
    }

    private CreateAuditRecordCommandBuilder(String serviceName, String systemName, Instant timestamp, String processId) {
        super(CreateAuditRecordCommand::new);
        this.serviceName = serviceName;
        this.systemName = systemName;
        this.timestamp = timestamp;
        this.processId = processId;
    }

    public CreateAuditRecordCommandBuilder setEventType(AuditEventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public CreateAuditRecordCommandBuilder setContext(String useCase) {
        this.context = AuditContext.newBuilder()
                .setUseCase(useCase)
                .build();
        return this;
    }

    public CreateAuditRecordCommandBuilder setContext(String useCase, String processId) {
        this.context = AuditContext.newBuilder()
                .setUseCase(useCase)
                .setProcessId(processId)
                .build();
        if (this.processId != null && !Objects.equals(this.processId, processId)) {
            throw new IllegalArgumentException("processId on command and processId on AuditContext must match");
        }
        this.processId = processId;
        return this;
    }

    public CreateAuditRecordCommandBuilder addEventData(String key, String value) {
        if (this.eventData == null) {
            this.eventData = new ArrayList<>();
        }
        this.eventData.add(new AuditEventDataElement(key, value));
        return this;
    }

    public CreateAuditRecordCommandBuilder setTriggerUser(String userId, String identityProvider) {
        this.user = AuditUser.newBuilder()
                .setId(userId)
                .setIdentityProvider(identityProvider)
                .build();
        this.systemComponent = null;
        return this;
    }

    public CreateAuditRecordCommandBuilder setTriggerSystem(String department, String system, String component) {
        this.systemComponent = AuditSystemComponent.newBuilder()
                .setDepartment(department)
                .setSystem(system)
                .setComponent(component)
                .build();
        this.user = null;
        return this;
    }

    public CreateAuditRecordCommandBuilder setAuditObject(String type, String id) {
        return this.setAuditObject(type, id, null);
    }

    public CreateAuditRecordCommandBuilder setAuditObject(String type, String id, String version) {
        this.auditedData = new AuditObject(type, id, version, objectData);
        return this;
    }

    public CreateAuditRecordCommandBuilder addAuditObjectDataValue(AuditObjectDataRole role, String name, String value) {
        this.objectData.add(new AuditObjectDataValue(role, name, value));
        return this;
    }

    public CreateAuditRecordCommandBuilder addAuditObjectDataValue(String name, String value) {
        return this.addAuditObjectDataValue(null, name, value);
    }

    public CreateAuditRecordCommandBuilder addAuditObjectDataJSON(AuditObjectDataRole role, String name, String jsonAsUTF8) {
        this.objectData.add(new AuditObjectDataJSON(role, name, ByteBuffer.wrap(jsonAsUTF8.getBytes(StandardCharsets.UTF_8))));
        return this;
    }

    public CreateAuditRecordCommandBuilder addAuditObjectDataJSON(String name, String jsonAsUTF8) {
        return this.addAuditObjectDataJSON(null, name, jsonAsUTF8);
    }

    public CreateAuditRecordCommandBuilder addAuditObjectDataJSON(AuditObjectDataRole role, String name, ByteBuffer jsonAsUTF8) {
        this.objectData.add(new AuditObjectDataJSON(role, name, jsonAsUTF8));
        return this;
    }

    public CreateAuditRecordCommandBuilder addAuditObjectDataJSON(String name, ByteBuffer jsonAsUTF8) {
        return this.addAuditObjectDataJSON(null, name, jsonAsUTF8);
    }

    public CreateAuditRecordCommandBuilder addAuditObjectDataS3(AuditObjectDataRole role, String name, String objectReference) {
        this.objectData.add(new AuditObjectDataS3(role, name, objectReference));
        return this;
    }

    public CreateAuditRecordCommandBuilder addAuditObjectDataS3(String name, String objectReference) {
        return this.addAuditObjectDataS3(null, name, objectReference);
    }

    @Override
    public CreateAuditRecordCommand build() {
        AuditEventDetails eventDetails = getAuditEventDetails();
        // Build trigger (either user or system)
        Object trigger = getTrigger();

        // Build payload
        CreateAuditRecordCommandPayload.Builder payloadBuilder = CreateAuditRecordCommandPayload.newBuilder()
                .setTimestamp(timestamp)
                .setEvent(eventDetails)
                .setTrigger(trigger);

        if (auditedData != null) {
            payloadBuilder.setAuditedData(auditedData);
        }

        setPayload(payloadBuilder.build());

        // Build references (currently empty)
        CreateAuditRecordCommandReferences references = CreateAuditRecordCommandReferences.newBuilder().build();
        setReferences(references);

        if (idempotenceId == null) {
            idempotenceId(createIdempotenceId());
        }

        CreateAuditRecordCommand command = super.build();
        if (processId != null) {
            command.setProcessId(processId);
        }
        if (objectData.isEmpty() && command.getPayload().getAuditedData() != null) { // for efficiency
            command.getPayload().getAuditedData().setObjectData(null);
        }
        return command;
    }

    private AuditEventDetails getAuditEventDetails() {
        AuditEventDetails.Builder eventDetailsBuilder = AuditEventDetails.newBuilder()
                .setType(eventType);

        if (context != null) {
            eventDetailsBuilder.setContext(context);
        }

        if (eventData != null && !eventData.isEmpty()) {
            eventDetailsBuilder.setEventData(eventData);
        }
        return eventDetailsBuilder.build();
    }

    private Object getTrigger() {
        Object trigger;
        if (user != null) {
            trigger = user;
        } else if (systemComponent != null) {
            trigger = systemComponent;
        } else {
            throw new IllegalStateException("Either user or system trigger must be set");
        }
        return trigger;
    }

    private String createIdempotenceId() {
        String triggerInfo = user != null ? user.getId() : systemComponent.getComponent();
        return String.format("%s-%s-%s-%s-%s",
                systemName,
                serviceName,
                eventType.name(),
                triggerInfo,
                timestamp);
    }

    @Override
    protected String getServiceName() {
        return serviceName;
    }

    @Override
    protected String getSystemName() {
        return systemName;
    }

    @Override
    protected CreateAuditRecordCommandBuilder self() {
        return this;
    }
}
