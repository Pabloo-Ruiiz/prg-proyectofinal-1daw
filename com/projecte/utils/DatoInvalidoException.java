package com.projecte.utils;

/**
 * Excepción personalizada que se lanza cuando un dato introducido no es válido.
 *
 * Esta excepción hereda de {@link RuntimeException}, por lo que es no comprobada.
 */
public class DatoInvalidoException extends RuntimeException {
    
    /**
     * Construye una excepción con un mensaje de error.
     *
     * @param mensaje descripción del error.
     */
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }

}
