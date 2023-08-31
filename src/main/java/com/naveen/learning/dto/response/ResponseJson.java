package com.naveen.learning.dto.response;

import com.naveen.learning.utils.constant.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class ResponseJson {

    @Autowired
    private Environment successProperty;
    private String responseCode;
    private String responseDescription;
    private Object response;

    public String getResponseCode() {
        if (responseCode == null) {
            responseCode = successProperty.getProperty(CommonConstants.S0001_SUCCESS_CODE);
        }
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        if (responseDescription == null) {
            responseDescription = successProperty.getProperty(CommonConstants.S0001_SUCCESS_DESCRIPTION);
        }
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ResponseJson[successProperty=");
        builder.append(successProperty);
        builder.append(", responseCode=");
        builder.append(responseCode);
        builder.append(", responseDescription=");
        builder.append(responseDescription);
        builder.append(", response=");
        builder.append(response);
        builder.append("]");
        return builder.toString();
    }
}
