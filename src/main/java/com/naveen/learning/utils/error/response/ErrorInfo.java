package com.naveen.learning.utils.error.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;

@Component
@Scope("prototype")
@JsonSerialize(using = ErrorInfoSerializer.class)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ErrorInfo implements Serializable {

    private static final long serialVersionUID = 67549136213141491L;

    private String responseCode;
    private String responseDescription;
    private String responseDetails;
    private Integer referenceNumber;
    private ErrorMessage errorMessage;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getResponseDetails() {
        return responseDetails;
    }

    public void setResponseDetails(String responseDetails) {
        this.responseDetails = responseDetails;
    }

    public Integer getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(Integer referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ErrorInfo [responseCode=" + responseCode + ", responseDescription=" + responseDescription
                + ", responseDetails=" + responseDetails + ", referenceNumber=" + referenceNumber + ", errorMessage="
                + errorMessage + "]";
    }

}
