package com.projecte.pablo;

import com.projecte.pablo.Pelicula.Genero;
import com.projecte.utils.FiltrarPeliculas;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Catálogo general de películas, directores y actores.
 *
 * Gestiona la carga, guardado y acceso a los datos generales de la aplicación.
 */
public class Catalogo implements Iterable<Pelicula> {

    // Listas generales de peliculas, directores y actores
    private ArrayList<Pelicula> peliculas;
    private ArrayList<Director> directores;
    private ArrayList<Actor> actores;

    /**
     * Inicializa el catálogo general y carga los datos desde los ficheros.
     */
    public Catalogo() {
        // Inicializa las listas vacías.
        this.peliculas = new ArrayList<Pelicula>();
        this.directores = new ArrayList<Director>();
        this.actores = new ArrayList<Actor>();

        cargarDatosGeneralesPeliculas();
        cargarDatosGeneralesDirectores();
        cargarDatosGeneralesActores();
    }

    // Getters y Setters
    public ArrayList<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(ArrayList<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    public ArrayList<Director> getDirectores() {
        return directores;
    }

    public void setDirectores(ArrayList<Director> directores) {
        this.directores = directores;
    }

    public ArrayList<Actor> getActores() {
        return actores;
    }

    public void setActores(ArrayList<Actor> actores) {
        this.actores = actores;
    }

    /**
     * Devuelve un iterador para recorrer las películas del catálogo.
     *
     * @return iterador de películas.
     */
    @Override
    public Iterator<Pelicula> iterator() {
        return peliculas.iterator();
    }

    /**
     * Crea la carpeta "datos" si no existe antes de guardar los ficheros.
     *
     * @throws IOException si la carpeta no puede crearse.
     */
    public void crearCarpetaDatos() throws IOException {
        File directori = new File("datos");

        if (directori.mkdir()) {
            // Carpeta creada correctamente
        } else {
            if (!directori.exists()) {
                throw new IOException("\nINFORMACION: La carpeta no se pudo crear.\n");
            }
        }
    }

    /**
     * Carga las películas generales desde el fichero "datos/peliculas.datos".
     */
    public void cargarDatosGeneralesPeliculas() {

        File file = new File("datos/peliculas.datos");

        if (file.exists()) {

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("datos/peliculas.datos"));) {

                while (true) {
                    Pelicula p = (Pelicula) in.readObject();
                    peliculas.add(p);
                    Pelicula.contador++;
                }

            } catch (EOFException e) {
                // Fin del fichero
            } catch (IOException e) {
                System.out.println("\n" + e.getMessage() + "\n");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("\n" + e.getMessage() + "\n");
                e.printStackTrace();
            }

        } else {
            try {
                crearCarpetaDatos();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("\n" + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }

    }

    /**
     * Carga los directores generales desde el fichero "datos/directores.datos".
     */
    public void cargarDatosGeneralesDirectores() {

        File file = new File("datos/directores.datos");

        if (file.exists()) {

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("datos/directores.datos"));) {

                while (true) {
                    Director d = (Director) in.readObject();
                    directores.add(d);
                    Director.contador++;
                }

            } catch (EOFException e) {
                // Fin del fichero
            } catch (IOException e) {
                System.out.println("\n" + e.getMessage() + "\n");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("\n" + e.getMessage() + "\n");
                e.printStackTrace();
            }

        } else {
            try {
                crearCarpetaDatos();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("\n" + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }

    }

    /**
     * Carga los actores generales desde el fichero "datos/actores.datos".
     */
    public void cargarDatosGeneralesActores() {

        File file = new File("datos/actores.datos");

        if (file.exists()) {

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("datos/actores.datos"));) {

                while (true) {
                    Actor a = (Actor) in.readObject();
                    actores.add(a);
                    Actor.contador++;
                }

            } catch (EOFException e) {
                // Fin del fichero
            } catch (IOException e) {
                System.out.println("\n" + e.getMessage() + "\n");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("\n" + e.getMessage() + "\n");
                e.printStackTrace();
            }
        } else {
            try {
                crearCarpetaDatos();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("\n" + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
    }

    /**
     * Guarda las películas generales en el fichero "datos/peliculas.datos".
     */
    public void guardarDatosGeneralesPeliculas() {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("datos/peliculas.datos"));) {

            for (Pelicula p : peliculas) {
                out.writeObject(p);
            }

        } catch (IOException e) {
            System.out.println("\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }

    }

    /**
     * Guarda los directores generales en el fichero "datos/directores.datos".
     */
    public void guardarDatosGeneralesDirectores() {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("datos/directores.datos"));) {

            for (Director d : directores) {
                out.writeObject(d);
            }

        } catch (IOException e) {
            System.out.println("\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }

    }

    /**
     * Guarda los actores generales en el fichero "datos/actores.datos".
     */
    public void guardarDatosGeneralesActores() {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("datos/actores.datos"));) {

            for (Actor a : actores) {
                out.writeObject(a);
            }

        } catch (IOException e) {
            System.out.println("\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }

    }

    public void anyadirPelicula(Pelicula p) {
        peliculas.add(p);
    }

    public void anyadirDirector(Director d) {
        directores.add(d);
    }

    public void anyadirActor(Actor a) {
        actores.add(a);
    }

    public void eliminarPelicula(Pelicula p) {
        peliculas.remove(p);
    }

    public void eliminarDirector(Director d) {
        directores.remove(d);
    }

    public void eliminarActor(Actor a) {
        actores.remove(a);
    }

    public FiltrarPeliculas getFiltrarPeliculas(Genero genero, int duracion) {
        return new FiltrarPeliculas(peliculas, genero, duracion);
    }

    public Pelicula existePelicula(String titulo) {
        for (Pelicula p : peliculas) {
            if (p.getTitulo().equalsIgnoreCase(titulo)) {
                return p;
            }
        }
        return null;
    }

    public Director existeDirector(String nombreCompleto) {
        for (Director d : directores) {
            if (d.nombreCompleto().equalsIgnoreCase(nombreCompleto)) {
                return d;
            }
        }
        return null;
    }

    public Actor existeActor(String nombreCompleto) {
        for (Actor a : actores) {
            if (a.nombreCompleto().equalsIgnoreCase(nombreCompleto)) {
                return a;
            }
        }
        return null;
    }

    public void mostrarOrdenacionPeliculas() {
        this.mostrarOrdenacionPeliculas(null);
    }

    public void mostrarOrdenacionPeliculas(Comparator<Pelicula> comparator) {
        if (comparator == null) {
            Collections.sort(peliculas);
        } else {
            Collections.sort(peliculas, comparator);
        }

        Iterator<Pelicula> it = peliculas.iterator();

        if (!it.hasNext()) {
            System.out.println("\nINFORMACION: Tu catalogo de peliculas esta vacio.\n");
        }

        while (it.hasNext()) {
            Pelicula p = it.next();
            System.out.println(" - " + p.resumen());
        }
        System.out.println();
    }

    public boolean mostrarDatosGeneralesPeliculas() {
        if (peliculas.isEmpty()) {
            System.out.println("\nINFORMACION: Tu catalogo de peliculas esta vacio.\n");
            return true;
        }

        for (Pelicula p : peliculas) {
            System.out.println(" - " + p.toString());
        }
        return false;
    }

    public boolean mostrarDatosGeneralesDirectores() {
        if (directores.isEmpty()) {
            System.out.println("\nINFORMACION: Tu catalogo de directores esta vacio.\n");
            return true;
        }

        for (Director d : directores) {
            System.out.println(" - " + d.toString());
        }
        return false;
    }

    public boolean mostrarDatosGeneralesActores() {
        if (actores.isEmpty()) {
            System.out.println("\nINFORMACION: Tu catalogo de actores esta vacio.\n");
            return true;
        }

        for (Actor a : actores) {
            System.out.println(" - " + a.toString());
        }
        return false;
    }

    public Pelicula buscarPelicula(String texto) {
        for (Pelicula p : peliculas) {
            if (p.getIdentificador().equalsIgnoreCase(texto)) {
                return p;
            }
        }
        return null;
    }

    public Director buscarDirector(String texto) {
        for (Director d : directores) {
            if (d.getIdentificador().equalsIgnoreCase(texto)) {
                return d;
            }
        }
        return null;
    }

    public Actor buscarActor(String texto) {
        for (Actor a : actores) {
            if (a.getIdentificador().equalsIgnoreCase(texto)) {
                return a;
            }
        }
        return null;
    }

}
