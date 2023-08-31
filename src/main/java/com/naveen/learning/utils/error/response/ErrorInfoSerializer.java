package com.naveen.learning.utils.error.response;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ErrorInfoSerializer extends StdSerializer<ErrorInfo> {

    private static final long serialVersionUID = 1L;

    public ErrorInfoSerializer(Class<ErrorInfo> responseJson) {
        super(responseJson);
    }

    public ErrorInfoSerializer() {
        this(null);

    }

    @Override
    public void serialize(ErrorInfo value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("errorCode", value.getResponseCode());
        if (value.getResponseDescription() != null) {
            gen.writeStringField("errorDescription", value.getResponseDescription());
        }
        if (value.getReferenceNumber() != null) {
            gen.writeObjectField("errorReferenceNumber", value.getReferenceNumber());
        }
        if (value.getResponseDetails() != null) {
            gen.writeObjectField("errorResponseDetails", value.getResponseDetails());
        }
        if (value.getErrorMessage() != null) {
            gen.writeObjectField("errorMessage", value.getErrorMessage());
        }
        gen.writeEndObject();
    }
}