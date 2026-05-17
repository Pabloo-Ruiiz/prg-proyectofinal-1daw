package com.projecte.utils;

import com.projecte.pablo.Pelicula;
import java.util.Comparator;

/**
 * Comparador que ordena películas primero por año y, si hay empate, por título.
 */
public class ComparadorPorAnyoTitulo implements Comparator<Pelicula> {

    /**
     * Compara dos películas para decidir su orden.
     *
     * @param o1 primera película.
     * @param o2 segunda película.
     * @return valor negativo si o1 va antes de o2, cero si son equivalentes, positivo en caso contrario.
     */
    @Override
    public int compare(Pelicula o1, Pelicula o2) {
        if (Integer.compare(o1.getAnyo(), o2.getAnyo()) == 0) {
            if (o1.getTitulo().compareToIgnoreCase(o2.getTitulo()) == 0) {
                return 0;
            } else if (o1.getTitulo().compareToIgnoreCase(o2.getTitulo()) < 0) {
                return -1;
            } else {
                return 1;
            }
        } else if (Integer.compare(o1.getAnyo(), o2.getAnyo()) < 0) {
            return -1;
        } else {
            return 1;
        }
    }

}