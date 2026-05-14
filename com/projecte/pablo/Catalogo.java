package com.projecte.pablo;

import com.projecte.pablo.Pelicula.Genero;
import com.projecte.utils.DatoInvalidoException;
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

public class Catalogo implements Iterable<Pelicula> {

    // Listas generales de peliculas, directores y actores
    private ArrayList<Pelicula> peliculas;
    private ArrayList<Director> directores;
    private ArrayList<Actor> actores;

    // Constructor
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

    // Permite recorrer la lista con for-each.
    @Override
    public Iterator<Pelicula> iterator() {
        return peliculas.iterator();
    }

    public void cargarDatosGeneralesPeliculas() {

        File file = new File("datos");

        if (!file.exists()) {
            if (file.mkdir()) {
                // Carpeta creada correctamente
                return;
            } else {
                if (!file.exists()) {
                    throw new DatoInvalidoException("\nLa carpeta no se pudo crear.\n");
                }
            }
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("datos/peliculas.datos"));) {

            while (true) {
                Pelicula p = (Pelicula) in.readObject();
                peliculas.add(p);
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

    }

    public void cargarDatosGeneralesDirectores() {

        File file = new File("datos");

        if (!file.exists()) {
            if (file.mkdir()) {
                // Carpeta creada correctamente
                return;
            } else {
                if (!file.exists()) {
                    throw new DatoInvalidoException("\nLa carpeta no se pudo crear.\n");
                }
            }
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("datos/directores.datos"));) {

            while (true) {
                Director d = (Director) in.readObject();
                directores.add(d);
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

    }

    public void cargarDatosGeneralesActores() {

        File file = new File("datos");

        if (!file.exists()) {
            if (file.mkdir()) {
                // Carpeta creada correctamente
                return;
            } else {
                if (!file.exists()) {
                    throw new DatoInvalidoException("\nLa carpeta no se pudo crear.\n");
                }
            }
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("datos/actores.datos"));) {

            while (true) {
                Actor a = (Actor) in.readObject();
                actores.add(a);
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

    }

    public void guardarDatosGeneralesPeliculas() {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("datos/peliculas.datos"));) {

            for (Pelicula p : peliculas) {
                out.writeObject(p.resumen());
            }

        } catch (IOException e) {
            System.out.println("\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }

    }

    public void guardarDatosGeneralesDirectores() {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("datos/directores.datos"));) {

            for (Director d : directores) {
                out.writeObject(d.resumen());
            }

        } catch (IOException e) {
            System.out.println("\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }

    }

    public void guardarDatosGeneralesActores() {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("datos/actores.datos"));) {

            for (Actor a : actores) {
                out.writeObject(a.resumen());
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

    public FiltrarPeliculas getFiltrarPeliculas(Genero genero, int duracion) {
        return new FiltrarPeliculas(peliculas, genero, duracion);
    }

    public boolean existePelicula(String titulo) {
        for (Pelicula p : peliculas) {
            if (p.getTitulo().equalsIgnoreCase(titulo)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeDirector(String nombreCompleto) {
        for (Director d : directores) {
            if (d.nombreCompleto().equalsIgnoreCase(nombreCompleto)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeActor(String nombreCompleto) {
        for (Actor a : actores) {
            if (a.nombreCompleto().equalsIgnoreCase(nombreCompleto)) {
                return true;
            }
        }
        return false;
    }

    public void mostrarOrdenacionPeliculas() {
        this.mostrarOrdenacionPeliculas(null);
    }

    // Muestra las películas después de ordenar
    public void mostrarOrdenacionPeliculas(Comparator<Pelicula> comparator) {
        if(comparator == null) {
            Collections.sort(peliculas); // Usa el compareTo de la clase Pelicula
        } else {
            Collections.sort(peliculas, comparator); 
        }

        Iterator<Pelicula> it = peliculas.iterator();

        while (it.hasNext()) {
            Pelicula p = it.next();
            System.out.println(" - " + p.resumen());
        }
        System.out.println();
    }

    public void mostrarDatosGeneralesPeliculas() {
        for (Pelicula p : peliculas) {
            System.out.println(" - " + p.resumen());
        }
    }

    public void mostrarDatosGeneralesDirectores() {
        for (Director d : directores) {
            System.out.println(" - " + d.resumen());
        }
    }

    public void mostrarDatosGeneralesActores() {
        for (Actor a : actores) {
            System.out.println(" - " + a.resumen());
        }
    }

}
