package com.projecte.utils;

import com.projecte.pablo.Pelicula;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Iterador personalizado para recorrer películas que cumplen un filtro de género y duración.
 */
public class FiltrarPeliculas implements Iterator<Pelicula> {

    private int posicion = 0; // Posición actual del recorrido
    private int duracion;
    private Pelicula.Genero genero;
    private ArrayList<Pelicula> peliculas;

    /**
     * Construye un iterador que solo devolverá películas del género indicado
     * y con duración menor o igual a la especificada.
     *
     * @param peliculas lista de películas a filtrar.
     * @param genero género que deben cumplir las películas.
     * @param duracion duración máxima en minutos.
     */
    public FiltrarPeliculas(ArrayList<Pelicula> peliculas, Pelicula.Genero genero, int duracion) {
        this.peliculas = peliculas;
        this.genero = genero;
        this.duracion = duracion;
    }

    // Getters y Setters
    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public ArrayList<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(ArrayList<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    /**
     * Determina si existe una siguiente película que cumpla el filtro.
     *
     * @return true si hay más películas válidas, false en caso contrario.
     */
    @Override
    public boolean hasNext() {
        // Avanza mientras la película no cumpla el filtro
        while (posicion < peliculas.size() && (!peliculas.get(posicion).getGenero().equals(genero)
                || peliculas.get(posicion).getDuracion() > duracion)) {
            posicion++;
        }
        // Devuelve true si aún quedan películas válidas
        return posicion < peliculas.size();
    }

    /**
     * Devuelve la siguiente película válida del filtro.
     *
     * @return siguiente película que cumple el filtro.
     */
    @Override
    public Pelicula next() {
        return peliculas.get(posicion++);
    }

}
