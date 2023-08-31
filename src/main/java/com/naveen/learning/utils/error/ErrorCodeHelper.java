package com.naveen.learning.utils.error;

import com.naveen.learning.utils.error.response.ErrorInfo;
import com.naveen.learning.utils.error.response.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:error.properties")
public class ErrorCodeHelper {

    @Autowired
    private ErrorInfo errorInfo;

    @Autowired
    private Environment systemPropertyReader;

    /**
     *Get Error Info and set error code and error description
     *
     * @param errorCode
     * @param errorDescription
     * @return
     */
    public ErrorInfo getErrorInfo(String errorCode, String errorDescription) {
        String responseCode=systemPropertyReader.getProperty(errorCode);
        String responseDescription=systemPropertyReader.getProperty(errorDescription);
        errorInfo.setResponseCode(responseCode);
        errorInfo.setResponseDescription(responseDescription);
        return errorInfo;
    }

    public ErrorInfo getErrorInfo(String errorCode, String errorDescription, String field) {
        String responseCode=systemPropertyReader.getProperty(errorCode);
        String responseDescription=systemPropertyReader.getProperty(errorDescription,field);
        errorInfo.setResponseCode(responseCode);
        errorInfo.setResponseDescription(responseDescription);
        return errorInfo;
    }

    /**
     *
     * @param errorCode
     * @param errorDescription
     * @param jsonDetails
     * @return
     */
    public ErrorInfo getErrorInfoWithDetails(String errorCode, String errorDescription, String jsonDetails){
    String responseCode=systemPropertyReader.getProperty(errorCode);
    String responseDescription=systemPropertyReader.getProperty(errorDescription);
    errorInfo.setResponseCode(responseCode);
    errorInfo.setResponseDescription(responseDescription);
    errorInfo.setResponseDetails(jsonDetails);
    return errorInfo;
}

    public ErrorInfo getErrorInfo(String httpError, ErrorMessage errorMessage) {
        errorInfo.setResponseCode(httpError);
        errorInfo.setErrorMessage(errorMessage);
        return errorInfo;
    }
}


