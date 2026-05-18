package com.projecte.utils;

import com.projecte.pablo.Pelicula;
import java.util.Comparator;

/**
 * Comparador de películas que ordena por identificador numérico.
 *
 * Se puede usar cuando no hay otro comparador específico y se desea
 * un orden de inserción estable basado en el ID de cada película.
 */
public class ComparadorPorId implements Comparator<Pelicula>{

    @Override
    public int compare(Pelicula o1, Pelicula o2) {
        // Ordena películas por su id ascendente.
        if (Integer.compare(o1.getId(), o2.getId()) == 0) {
            return 0;
        } else if (Integer.compare(o1.getId(), o2.getId()) < 0) {
            return -1;
        } else {
            return 1;
        }
    }

}
