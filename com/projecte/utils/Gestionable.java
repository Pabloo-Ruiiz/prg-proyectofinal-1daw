package com.projecte.utils;

/**
 * Interfaz base para objetos gestionables en el catálogo.
 *
 * Las clases que implementan esta interfaz deben proporcionar un identificador
 * único, un resumen para listados y un método para mostrar detalles completos.
 */
public interface Gestionable {

    /**
     * Obtiene el identificador único del objeto.
     *
     * @return identificador del elemento, por ejemplo título o nombre.
     */
    String getIdentificador();

    /**
     * Obtiene una descripción resumida del objeto.
     *
     * @return resumen breve para mostrarse en listados.
     */
    String resumen();

    /**
     * Muestra los detalles completos del objeto por pantalla.
     */
    void mostrarDetalles();
    
}
