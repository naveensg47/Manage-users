package com.naveen.learning.utils.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.naveen.learning.utils.constant.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 *
 * Class for common response format
 *
 */
@Component
@PropertySource("classpath:success.properties")
@JsonInclude(Include.NON_NULL)
@JsonSerialize(using = ResponseJsonSerializer.class)
@Scope("prototype")
public class ResponseJson {

    @Autowired
    private Environment successProperty;

    private String responseCode;

    private String responseDescription;

    private Object response;

    public String getResponseCode() {
        if (this.responseCode == null) {
            responseCode = successProperty.getProperty(CommonConstants.S0001_SUCCESS_CODE);
        }
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = successProperty.getProperty(responseCode);
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = successProperty.getProperty(responseDescription);
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
        builder.append("ResponseJson [ responseCode=");
        builder.append(responseCode);
        builder.append(", responseDescription=");
        builder.append(responseDescription);
        builder.append(", response=");
        builder.append(response);
        builder.append("]");
        return builder.toString();
    }



}
