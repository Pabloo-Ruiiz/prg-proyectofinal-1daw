package com.projecte.utils;

import com.projecte.pablo.Pelicula;
import java.util.ArrayList;
import java.util.Iterator;

// Iterator personalizado que filtra películas por género y duración.
public class FiltrarPeliculas implements Iterator<Pelicula> {

    private int posicion = 0; // Posición actual del recorrido
    private int duracion;
    private Pelicula.Genero genero;
    private ArrayList<Pelicula> peliculas;

    // Constructor del iterator
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

    // Comprueba si existe otra película válida
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

    // Devuelve la siguiente película válida
    @Override
    public Pelicula next() {
        return peliculas.get(posicion++);
    }

}
