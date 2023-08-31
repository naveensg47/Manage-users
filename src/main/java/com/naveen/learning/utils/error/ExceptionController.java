package com.naveen.learning.utils.error;

import com.naveen.learning.utils.constant.ErrorConstants;
import com.naveen.learning.utils.error.response.ErrorInfo;
import com.naveen.learning.utils.error.response.ErrorMessage;
import com.naveen.learning.utils.error.response.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.DataBinder;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * ExceptionController to catch controller exceptions
 *
 */
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @Autowired
    private ErrorCodeHelper errorCodeHelper;
    @Autowired
    private Environment errorProperty;
    private final Logger log = Logger.getLogger("file");

    /**
     * 
     * Handles cases when required request parameters are missing
     * 
     * @param request
     * @param ex
     * @param headers
     * @param status
     * 
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.OK;
        ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1001_ERROR_CODE,
                ErrorConstants.E1001_ERROR_DESCRIPTION);
        return handleExceptionInternal(ex, errorInfo, headers, httpStatus, request);

    }

    /**
     * Handles unsupported Media type error
     * 
     * @param request
     * @param ex
     * @param headers
     * @param status
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.OK;
        ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1001_ERROR_CODE,
                ErrorConstants.E1001_ERROR_DESCRIPTION);
        return handleExceptionInternal(ex, errorInfo, headers, httpStatus, request);
    }

    /**
     * 
     * Handles cases when invalid input parameter is passed like when input json format is incorrect
     * 
     * @param request
     * @param ex
     * @param headers
     * @param status
     * 
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.OK;
        ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1002_ERROR_CODE,
                ErrorConstants.E1002_ERROR_DESCRIPTION);
        return handleExceptionInternal(ex, errorInfo, headers, httpStatus, request);
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.OK;
        List<String> errorList = ex.getBindingResult().getFieldErrors().stream().map(map -> map.getDefaultMessage())
                .collect(Collectors.toList());
        errorList.addAll(ex.getBindingResult().getGlobalErrors().stream()
                .map(map -> map.getObjectName() + " " + map.getDefaultMessage()).collect(Collectors.toList()));
        ErrorMessage errorMessage = new ErrorMessage(errorList);
        ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(errorProperty.getProperty(ErrorConstants.E1002_ERROR_CODE),
                errorMessage);
        return handleExceptionInternal(ex, errorInfo, headers, httpStatus, request);
    }

    /**
     * 
     * @param e
     * @param request
     * 
     * @return
     */
    @ExceptionHandler({ ServiceException.class })
    protected ResponseEntity<Object> handleServiceException(RuntimeException e, WebRequest request) {
        ServiceException exception = (ServiceException) e;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.OK;
        return handleExceptionInternal(exception, exception.getErrorInfo(), headers, httpStatus, request);
    }

    /**
     * 
     * @param ex
     * @param request
     * 
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleConstrainViolationException(ConstraintViolationException ex,
            WebRequest request) {
        String errorCode = "";
        for (ConstraintViolation<?> s : ex.getConstraintViolations())
            errorCode = s.getMessage();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.OK;
        headers.setContentType(MediaType.APPLICATION_JSON);
        ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1002_ERROR_CODE, errorCode);
        return handleExceptionInternal(ex, errorInfo, headers, httpStatus, request);
    }

    /**
     * 
     * @param ex
     * @param request
     * 
     * @return
     */
    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<Object> unexpectedTypeException(UnexpectedTypeException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.OK;
        headers.setContentType(MediaType.APPLICATION_JSON);
        ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1003_ERROR_CODE,
                ErrorConstants.E1003_ERROR_DESCRIPTION);
        return handleExceptionInternal(ex, errorInfo, headers, httpStatus, request);
    }

    /**
     * 
     * @param e
     * @param request
     * 
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> sqlExceptionHelper(RuntimeException e, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.OK;
        String errorDescription = e.getCause().getCause().getMessage();
        log.info(e);
        log.error(e);
        ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(errorProperty.getProperty(ErrorConstants.E1001_ERROR_CODE),
                errorDescription);
        return handleExceptionInternal(e, errorInfo, headers, httpStatus, request);
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<Object> arrayIndexOutOfBounds(RuntimeException e, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.OK;
        log.info(e);
        log.error(e);
        ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1001_ERROR_CODE,
                ErrorConstants.E1001_ERROR_DESCRIPTION);
        return handleExceptionInternal(e, errorInfo, headers, httpStatus, request);
    }

    /**
     * 
     * @param e
     * @param request
     * 
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(RuntimeException e, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.OK;
        log.info(e);
        log.error(e);
        ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1003_ERROR_CODE,
                ErrorConstants.E1003_ERROR_DESCRIPTION);
        return handleExceptionInternal(e, errorInfo, headers, httpStatus, request);
    }

    @InitBinder
    private void activateDirectFieldAccess(DataBinder dataBinder) {
        dataBinder.initDirectFieldAccess();
    }
}
