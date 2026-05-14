package com.projecte.pablo;

import java.util.ArrayList;
import java.util.Scanner;

import com.projecte.utils.ComparadorPorAnyoTitulo;
import com.projecte.utils.DatoInvalidoException;
import com.projecte.marc.Usuario;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Catalogo implements Iterable<Pelicula> {

    // Scanner para leer datos introducidos por teclado
    private Scanner entrada = new Scanner(System.in);

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
        cargarDatosListasGenerales("peliculas.datos");
        cargarDatosListasGenerales("directores.datos");
        cargarDatosListasGenerales("actores.datos");
    }

    // Getters y Setters
    public Scanner getEntrada() {
        return entrada;
    }

    public void setEntrada(Scanner entrada) {
        this.entrada = entrada;
    }

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

    // Método principal del catálogo.
    // Controla todo el menú y las opciones disponibles.
    public void inicio(Usuario u) {

        int opcion = 0;
        int opcionSubmenu = 0;

        do {

            try {

                menuPrincipal();
                opcion = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcion > 6 || opcion < 1) {
                    throw new DatoInvalidoException("\nError: Valor fuera de rango.\n");
                }

                switch (opcion) {
                    case 1:

                        break;
                    case 2:

                        if (u.getRol().equals(Usuario.Rol.ROL_ADMIN)) {

                            try {
                                System.out.println("\n  Elige el elemento que deseas añadir");
                                menuSeleccionElementos();
                                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                                // Comprueba si la opcion es valida
                                if (opcionSubmenu > 3 || opcionSubmenu < 1) {
                                    throw new DatoInvalidoException("\nError: Valor fuera de rango.\n");
                                }

                                switch (opcionSubmenu) {
                                    case 1:
                                        altaPelicula();
                                        guardarDatosListasGenerales("peliculas.datos");
                                        break;
                                    case 2:
                                        altaDirector();
                                        guardarDatosListasGenerales("directores.datos");
                                        break;
                                    case 3:
                                        altaActor();
                                        guardarDatosListasGenerales("actores.datos");
                                        break;
                                    default:
                                        break;
                                }

                            } catch (NumberFormatException e) {
                                System.out.println("\nError: Valor no numerico.\n");
                            } catch (DatoInvalidoException e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            throw new DatoInvalidoException("\nEl usuario no puede acceder a este apartado. Para acceder se necesita ser administrador del catalogo.\n");
                        }

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:

                        do {

                            try {

                                menuOrdenacion();
                                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                                // Comprueba si la opcion es valida
                                if (opcionSubmenu > 4 || opcionSubmenu < 1) {
                                    throw new DatoInvalidoException("\nError: Valor fuera de rango.\n");
                                }

                                switch (opcionSubmenu) {
                                    case 1: // Ordenación alfabético por título.
                                        System.out.println("\n----- ORDENACION POR TITULO -----");
                                        Collections.sort(peliculas); // Usa el compareTo de la clase Pelicula
                                        mostrarOrdenacionPeliculas();
                                        break;
                                    case 2: // Ordenación por duración
                                        System.out.println("\n----- ORDENACION POR DURACION -----");

                                        // Comparator, empleado con una clase anónima para ordenar por duración
                                        Collections.sort(peliculas, new Comparator<Pelicula>() {

                                            @Override
                                            public int compare(Pelicula o1, Pelicula o2) {
                                                if (Integer.compare(o1.getDuracion(), o2.getDuracion()) == 0) {
                                                    return 0;
                                                } else if (Integer.compare(o1.getDuracion(), o2.getDuracion()) < 0) {
                                                    return -1;
                                                } else {
                                                    return 1;
                                                }
                                            }

                                        });
                                        mostrarOrdenacionPeliculas();
                                        break;
                                    case 3: // Ordenación usando Comparator con una clase externa
                                        System.out.println("\n----- ORDENACION POR AÑO Y TITULO -----");

                                        Collections.sort(peliculas, new ComparadorPorAnyoTitulo()); // Usa el comparador
                                                                                                    // personalizado
                                        mostrarOrdenacionPeliculas();
                                        break;
                                    case 4: // Filtrado personalizado usando Iterator
                                        System.out.print(
                                                "\nIntroduce la duracion maxima de la pelicula para seleccionar el filtro: ");
                                        int d = Integer.parseInt(entrada.nextLine());

                                        System.out.print("Introduce el genero de la Pelicula para escoger el filtro: ");
                                        String texto = entrada.nextLine();
                                        Pelicula.Genero g = Pelicula.Genero.valueOf(texto); // Convierte String a enum

                                        FiltrarPeliculas fp = new FiltrarPeliculas(peliculas, g, d);
                                        System.out.println("\n----- ORDENACION POR FILTRO (DURACION Y GENERO) -----");

                                        // Recorre las peliculas que cumple el filtro
                                        while (fp.hasNext()) {
                                            Pelicula p = fp.next();
                                            System.out.println(" - " + p.resumen());
                                        }
                                        System.out.println();
                                        break;
                                    default:
                                        break;
                                }

                            } catch (NumberFormatException e) {
                                System.out.println("\nError: Valor no numerico.\n");
                            } catch (DatoInvalidoException e) {
                                System.out.println(e.getMessage());
                            }

                        } while (opcionSubmenu < 1 || opcionSubmenu > 4);

                        break;
                    case 6:
                        System.out.println("\nSaliendo del sistema. Vuelve cuando quieras...\n");
                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nError: Valor no numerico.\n");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcion != 6);

    }

    // Iterator personalizado que filtra películas por género y duración.
    class FiltrarPeliculas implements Iterator<Pelicula> {

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

    // Permite recorrer la lista con for-each.
    @Override
    public Iterator<Pelicula> iterator() {
        return peliculas.iterator();
    }

    public void cargarDatosListasGenerales(String fichero) {

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

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("datos/" + fichero));) {

            if (fichero.equalsIgnoreCase("peliculas.datos")) {
                while (true) {
                    Pelicula p = (Pelicula) in.readObject();
                    peliculas.add(p);
                }
            } else if (fichero.equalsIgnoreCase("directores.datos")) {
                while (true) {
                    Director d = (Director) in.readObject();
                    directores.add(d);
                }
            } else {
                while (true) {
                    Actor a = (Actor) in.readObject();
                    actores.add(a);
                }
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

    public void guardarDatosListasGenerales(String fichero) {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("datos/" + fichero));) {

            if (fichero.equalsIgnoreCase("peliculas.datos")) {
                for (Pelicula p : peliculas) {
                    out.writeObject(p.resumen());
                }
            } else if (fichero.equalsIgnoreCase("directores.datos")) {
                for (Director d : directores) {
                    out.writeObject(d.resumen());
                }
            } else {
                for (Actor a : actores) {
                    out.writeObject(a.resumen());
                }
            }

        } catch (IOException e) {
            System.out.println("\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }

    }

    public void altaPelicula() {

        try {

            System.out.print("\nIntroduce el titulo de la pelicula: ");
            String titulo = entrada.nextLine();

            boolean tituloNoValido = existePelicula(titulo);

            if (tituloNoValido) {
                throw new DatoInvalidoException("\nLa pelicula " + titulo + " ya existe en el catálogo.\n");
            }

            System.out.print("¿En que año se estreno la pelicula " + titulo + "? ");
            int anyo = Integer.parseInt(entrada.nextLine());

            System.out.print("Introduce la duracion de la pelicula: ");
            int duracion = Integer.parseInt(entrada.nextLine());

            System.out.print("Introduce el genero de la pelicula " + titulo + ": ");
            String texto = entrada.nextLine().toUpperCase();

            Pelicula.Genero genero = Pelicula.Genero.valueOf(texto);

            Pelicula p = new Pelicula(titulo, anyo, duracion, genero);
            peliculas.add(p);

        } catch (DatoInvalidoException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: El género introducido no es válido.\n");
        }

    }

    public void altaDirector() {

        try {

            System.out.print("\nIntroduce el nombre del director: ");
            String nombre = entrada.nextLine();

            System.out.print("Introduce los apellidos de " + nombre + ": ");
            String apellidos = entrada.nextLine();

            String nombreCompleto = nombre + " " + apellidos;

            boolean directorNoValido = existeDirector(nombreCompleto);

            if (directorNoValido) {
                throw new DatoInvalidoException("\nEl director " + nombreCompleto + " ya existe en el catálogo.\n");
            }

            System.out.print("Introduce la fecha de nacimiento de " + nombre + " (yyyy/MM/dd): ");
            String fecha = entrada.nextLine();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate fechaNacimiento = LocalDate.parse(fecha, formato);

            System.out.print("Introduce la nacionalidad de " + nombre + ": ");
            String nacionalidad = entrada.nextLine();

            Director d = new Director(nombre, apellidos, fechaNacimiento, nacionalidad);
            directores.add(d);

        } catch (DatoInvalidoException e) {
            System.out.println(e.getMessage());
        }

    }

    public void altaActor() {

        try {

            System.out.print("\nIntroduce el nombre del actor: ");
            String nombre = entrada.nextLine();

            System.out.print("Introduce los apellidos de " + nombre + ": ");
            String apellidos = entrada.nextLine();

            String nombreCompleto = nombre + " " + apellidos;

            boolean actorNoValido = existeActor(nombreCompleto);

            if (actorNoValido) {
                throw new DatoInvalidoException("\nEl actor " + nombreCompleto + " ya existe en el catálogo.\n");
            }

            System.out.print("Introduce la fecha de nacimiento de " + nombre + " (yyyy/MM/dd): ");
            String fecha = entrada.nextLine();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate fechaNacimiento = LocalDate.parse(fecha, formato);

            System.out.print("Introduce la nacionalidad de " + nombre + ": ");
            String nacionalidad = entrada.nextLine();

            Actor a = new Actor(nombre, apellidos, fechaNacimiento, nacionalidad);
            actores.add(a);

        } catch (DatoInvalidoException e) {
            System.out.println(e.getMessage());
        }

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

    // Menú principal del programa
    public void menuPrincipal() {
        System.out.println("""
                             MENÚ PRINCIPAL
                ========================================
                  1 - Consultar catalogos particulares
                  2 - Añadir elementos a la listas generales
                  3 - Construir listas personales
                  4 - Eliminar elementos
                  5 - Ordenacion de las listas personales
                  6 - Salir
                ========================================
                """);
        System.out.print("Elige una opcion: ");
    }

    public void menuSeleccionElementos() {
        System.out.println("""
                ========================================
                  1 - Pelicula
                  2 - Director
                  3 - Actor
                ========================================
                """);
        System.out.print("Elige una opcion: ");
    }

    // Menú de opciones de ordenación
    public void menuOrdenacion() {
        System.out.println("""
                         \nMENÚ DE ORDENACIÓN
                ========================================
                  1 - Ordenar por título
                  2 - Ordenar por duracion
                  3 - Ordenar por año + titulo
                  4 - Ordenar por filtro (duracion + genero)
                ========================================
                """);
        System.out.print("Elige una opción: ");
    }

    // Muestra las películas después de ordenar
    public void mostrarOrdenacionPeliculas() {
        Iterator<Pelicula> it = peliculas.iterator();

        while (it.hasNext()) {
            Pelicula p = it.next();
            System.out.println(" - " + p.resumen());
        }
        System.out.println();
    }

    public void mostrarDatosListasGenerales(String fichero) {
        if (fichero.equalsIgnoreCase("peliculas.datos")) {
                for (Pelicula p : peliculas) {
                    System.out.println(" - " + p.resumen());
                }
            } else if (fichero.equalsIgnoreCase("directores.datos")) {
                for (Director d : directores) {
                    System.out.println(" - " + d.resumen());
                }
            } else {
                for (Actor a : actores) {
                    System.out.println(" - " + a.resumen());
                }
            }
    }

}
