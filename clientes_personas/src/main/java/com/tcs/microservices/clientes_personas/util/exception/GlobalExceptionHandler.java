package com.tcs.microservices.clientes_personas.util.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Excepciones relacionadas con la Base de Datos
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "Error de acceso a datos",
                "DATABASE_ERROR");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.CONFLICT, "Clave duplicada", "DUPLICATE_KEY_ERROR");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.CONFLICT, "Violación de integridad de datos",
                "DATA_INTEGRITY_VIOLATION");
    }

    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataAccessResourceUsageException(
            InvalidDataAccessResourceUsageException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR,
                "Uso incorrecto de recurso de base de datos", "RESOURCE_USAGE_ERROR");
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLockingFailureException(
            OptimisticLockingFailureException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.CONFLICT, "Error de concurrencia", "LOCKING_ERROR");
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeoutException(TimeoutException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.REQUEST_TIMEOUT, "La operación ha excedido el tiempo",
                "TIMEOUT_ERROR");
    }

    // 2. Excepciones relacionadas con la Validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
            WebRequest request) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        return buildErrorResponseWithDetails(ex, request, HttpStatus.BAD_REQUEST, "Error de validación",
                "VALIDATION_ERROR", details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Violación de restricciones",
                "CONSTRAINT_VIOLATION_ERROR");
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Error de binding", "BIND_ERROR");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Tipo de argumento inválido",
                "ARGUMENT_TYPE_MISMATCH");
    }

    // 3. Excepciones relacionadas con HTTP
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP no soportado",
                "HTTP_METHOD_NOT_SUPPORTED");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Tipo de contenido no soportado",
                "MEDIA_TYPE_NOT_SUPPORTED");
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotAcceptableException(
            HttpMediaTypeNotAcceptableException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.NOT_ACCEPTABLE, "Tipo de contenido no aceptable",
                "MEDIA_TYPE_NOT_ACCEPTABLE");
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse> handleMissingPathVariableException(MissingPathVariableException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Variable de ruta faltante",
                "MISSING_PATH_VARIABLE");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Parámetro de solicitud faltante",
                "MISSING_REQUEST_PARAMETER");
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ErrorResponse> handleServletRequestBindingException(ServletRequestBindingException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Error de binding en solicitud",
                "SERVLET_BINDING_ERROR");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Cuerpo de solicitud no legible",
                "MESSAGE_NOT_READABLE");
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotWritableException(HttpMessageNotWritableException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR,
                "No se pudo escribir la respuesta",
                "MESSAGE_NOT_WRITABLE");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.NOT_FOUND, "No se encontró el controlador",
                "NO_HANDLER_FOUND");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.UNAUTHORIZED, "Error de autenticación",
                "AUTHENTICATION_ERROR");
    }

    // 5. Excepciones Generales
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Argumento inválido",
                "ILLEGAL_ARGUMENT");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Estado inválido", "ILLEGAL_STATE");
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "Error de referencia nula",
                "NULL_POINTER");
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<ErrorResponse> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Índice fuera de los límites",
                "INDEX_OUT_OF_BOUNDS");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.NOT_FOUND, "Entidad no encontrada",
                "ENTITY_NOT_FOUND");
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "Error de I/O", "IO_ERROR");
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFoundException(FileNotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.NOT_FOUND, "Archivo no encontrado", "FILE_NOT_FOUND");
    }

    // 6. Excepciones específicas de Spring Framework
    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<ErrorResponse> handleConversionFailedException(ConversionFailedException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Error de conversión",
                "CONVERSION_FAILED");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.NOT_FOUND, "Elemento no encontrado",
                "NO_SUCH_ELEMENT");
    }

    @ExceptionHandler(UnsatisfiedDependencyException.class)
    public ResponseEntity<ErrorResponse> handleUnsatisfiedDependencyException(UnsatisfiedDependencyException ex,
            WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "Dependencia no satisfecha",
                "UNSATISFIED_DEPENDENCY");
    }

    @ExceptionHandler(BeanCreationException.class)
    public ResponseEntity<ErrorResponse> handleBeanCreationException(BeanCreationException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear bean",
                "BEAN_CREATION_ERROR");
    }

    // Método helper para construir respuestas de error
    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, WebRequest request, HttpStatus status,
            String errorMessage, String errorCode) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(errorMessage)
                .code(errorCode)
                .message(ex.getMessage())
                .details(List.of(ex.getMessage()))
                .path(request.getDescription(false))
                .method(request.getHeader("X-HTTP-Method-Override"))
                .exception(ErrorResponse.ExceptionDetail.builder()
                        .type(ex.getClass().getName())
                        .trace(ex.getStackTrace())
                        .build())
                .userMessage("Ocurrió un error. Intente de nuevo más tarde.")
                .build();

        return new ResponseEntity<>(errorResponse, status);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponseWithDetails(Exception ex, WebRequest request,
            HttpStatus status, String errorMessage, String errorCode, List<String> details) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(errorMessage)
                .code(errorCode)
                .message(ex.getMessage())
                .details(details)
                .path(request.getDescription(false))
                .method(request.getHeader("X-HTTP-Method-Override"))
                .exception(ErrorResponse.ExceptionDetail.builder()
                        .type(ex.getClass().getName())
                        .trace(ex.getStackTrace())
                        .build())
                .userMessage("Ocurrió un error. Intente de nuevo más tarde.")
                .build();

        return new ResponseEntity<>(errorResponse, status);
    }
}
