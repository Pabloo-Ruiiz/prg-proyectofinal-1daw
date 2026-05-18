package com.projecte.pablo;

import com.projecte.utils.DatoInvalidoException;
import com.projecte.utils.Gestionable;
import java.io.Serializable;

/**
 * Representa una película con título, año, duración y género.
 *
 * Esta clase es serializable para poder guardarla en fichero y se puede ordenar en catálogos.
 */
public class Pelicula implements Serializable, Comparable<Pelicula>, Gestionable {

    // Enum con los diferentes Generos de peliculas
    public enum Genero {
        ACCION, AVENTURA, COMEDIA, DRAMA, TERROR, FICCION, FANTASIA, ROMANTICA, MUSICAL, DOCUMENTAL;
    }

    // Contador estático para generar IDs automáticos
    protected static int contador = 0;

    // Atributos
    private int id;
    private String titulo;
    private int anyo;
    private int duracion;
    private Genero genero;

    /**
     * Crea una nueva película con los datos proporcionados.
     *
     * @param titulo título de la película.
     * @param anyo año de estreno.
     * @param duracion duración en minutos.
     * @param genero género de la película.
     */
    public Pelicula(String titulo, int anyo, int duracion, Genero genero) {
        contador++;
        this.id = contador;

        // Valida los datos mediante setters.
        setTitulo(titulo);
        setAnyo(anyo);
        setDuracion(duracion);
        setGenero(genero);
    }

    // Getters y Setters
    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Pelicula.contador = contador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo != null && !titulo.isEmpty()) {
            this.titulo = titulo;
        } else {
            throw new DatoInvalidoException("\nINFORMACION: El titulo no puede estar vacio.\n");
        }
    }

    public int getAnyo() {
        return anyo;
    }

    public void setAnyo(int anyo) {
        if (anyo > 0) {
            this.anyo = anyo;
        } else {
            throw new DatoInvalidoException("\nINFORMACION: El año no puede ser menor o igual a 0.\n");
        }
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        if (duracion > 0) {
            this.duracion = duracion;
        } else {
            throw new DatoInvalidoException("\nINFORMACION: La duracion no puede ser menor o igual a 0.\n");
        }
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        if (genero != null) {
            this.genero = genero;
        } else {
            throw new DatoInvalidoException("\nINFORMACION: El genero no puede ser null.\n");
        }
    }

    // Convierte la duracion de minutos a horas y minutos
    public String duracionHoras() {
        int horas = duracion / 60;
        int minutos = duracion % 60;

        return horas + "h " + minutos + "min";
    }

    // Comprueba si la película se considera clásica.
    public boolean esClasica() {
        return anyo < 2000;
    }

    // Comprueba si la película se considera larga.
    public boolean esLarga() {
        return duracion >= 120;
    }

    // Ordenacion natural por titulo (sin distinguir mayusculas / minusculas) utilizando Comparable.
    @Override
    public int compareTo(Pelicula o) {
        if (this.titulo.compareToIgnoreCase(o.getTitulo()) == 0) {
            return 0;
        } else if (this.titulo.compareToIgnoreCase(o.getTitulo()) < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public String getIdentificador() {
        return id + "-" + titulo;
    }

    @Override
    public String resumen() {
        return this.id + " | " + this.titulo + " | " + this.anyo + " | " + duracionHoras() + " | " + this.genero;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("""
                ==============================
                         FICHA PELÍCULA
                ==============================
                ID: %d
                Título: %s
                Año: %d
                Duración: %s
                Género: %s
                ==============================
                """.formatted(
                id,
                titulo,
                anyo,
                duracionHoras(),
                genero));
    }

    // toString
    @Override
    public String toString() {
        return getIdentificador() + " [ " + this.id + " | " + this.titulo + " | " + this.anyo + " | " + duracionHoras() + " | "
                + this.genero + " ]";
    }

}
