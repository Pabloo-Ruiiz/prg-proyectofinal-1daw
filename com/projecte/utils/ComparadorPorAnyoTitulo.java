package com.projecte.utils;

import com.projecte.pablo.Pelicula;
import java.util.Comparator;

//Comparator que compara primero por año y en caso de empate compara por titulo.
public class ComparadorPorAnyoTitulo implements Comparator<Pelicula> {

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