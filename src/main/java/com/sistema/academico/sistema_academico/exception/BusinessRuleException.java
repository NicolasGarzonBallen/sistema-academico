package com.sistema.academico.sistema_academico.exception;

/** Se usa para reglas de negocio válidas sintácticamente pero inválidas semánticamente -> HTTP 422 */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
