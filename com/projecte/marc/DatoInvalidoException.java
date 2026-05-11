package com.projecte.marc;

//Excepcion personalizada que se lanza cuando un dato introducido no es valido.
//Esta clase hereda de Exception, por lo que es una excepcion comprobada.
public class DatoInvalidoException extends Exception {
    
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }

}
