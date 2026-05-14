package com.projecte.utils;

//Excepcion personalizada que se lanza cuando un dato introducido no es valido.
//Esta clase hereda de RuntimeException, por lo que es una excepcion no comprobada.
public class DatoInvalidoException extends RuntimeException {
    
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }

}
