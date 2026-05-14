package com.projecte.neil;

import com.projecte.marc.Usuario;
import com.projecte.pablo.Actor;
import com.projecte.pablo.Catalogo;
import com.projecte.pablo.Director;
import com.projecte.pablo.Pelicula;
import com.projecte.utils.ComparadorPorAnyoTitulo;
import com.projecte.utils.DatoInvalidoException;
import com.projecte.utils.FiltrarPeliculas;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Menu {

    // Scanner para leer datos introducidos por teclado
    private Scanner entrada = new Scanner(System.in);

    private Usuario usuario;
    private Catalogo catalogo;

    public Menu(Usuario usuario, Catalogo catalogo) {
        this.usuario = usuario;
        this.catalogo = catalogo;
    }

    // Getters y Setters
    public Scanner getEntrada() {
        return entrada;
    }

    public void setEntrada(Scanner entrada) {
        this.entrada = entrada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    // Método principal del menu.
    // Controla todo el menú y las opciones disponibles.
    public void inicio() {

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

                        if (usuario.getRol().equals(Usuario.Rol.ROL_ADMIN)) {

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
                                        catalogo.guardarDatosGeneralesPeliculas();
                                        break;
                                    case 2:
                                        altaDirector();
                                        catalogo.guardarDatosGeneralesDirectores();
                                        break;
                                    case 3:
                                        altaActor();
                                        catalogo.guardarDatosGeneralesActores();
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
                            throw new DatoInvalidoException(
                                    "\nEl usuario no puede acceder a este apartado. Para acceder se necesita ser administrador del catalogo.\n");
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
                                        catalogo.mostrarOrdenacionPeliculas();
                                        break;
                                    case 2: // Ordenación por duración
                                        System.out.println("\n----- ORDENACION POR DURACION -----");

                                        // Comparator, empleado con una clase anónima para ordenar por duración
                                        catalogo.mostrarOrdenacionPeliculas((Pelicula o1, Pelicula o2) -> {
                                            if (Integer.compare(o1.getDuracion(), o2.getDuracion()) == 0) {
                                                return 0;
                                            } else if (Integer.compare(o1.getDuracion(), o2.getDuracion()) < 0) {
                                                return -1;
                                            } else {
                                                return 1;
                                            }
                                        });
                                        break;
                                    case 3: // Ordenación usando Comparator con una clase externa
                                        System.out.println("\n----- ORDENACION POR AÑO Y TITULO -----");
                                        catalogo.mostrarOrdenacionPeliculas(new ComparadorPorAnyoTitulo()); // Usa el comparador personalizado
                                        break;
                                    case 4: // Filtrado personalizado usando Iterator
                                        System.out.print(
                                                "\nIntroduce la duracion maxima de la pelicula para seleccionar el filtro: ");
                                        int d = Integer.parseInt(entrada.nextLine());

                                        System.out.print("Introduce el genero de la Pelicula para escoger el filtro: ");
                                        String texto = entrada.nextLine();
                                        Pelicula.Genero g = Pelicula.Genero.valueOf(texto); // Convierte String a enum

                                        FiltrarPeliculas fp = catalogo.getFiltrarPeliculas(g, d);
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

    public void altaPelicula() {

        try {

            System.out.print("\nIntroduce el titulo de la pelicula: ");
            String titulo = entrada.nextLine();

            boolean tituloNoValido = catalogo.existePelicula(titulo);

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
            catalogo.anyadirPelicula(p);

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

            boolean directorNoValido = catalogo.existeDirector(nombreCompleto);

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
            catalogo.anyadirDirector(d);

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

            boolean actorNoValido = catalogo.existeActor(nombreCompleto);

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
            catalogo.anyadirActor(a);

        } catch (DatoInvalidoException e) {
            System.out.println(e.getMessage());
        }

    }

}
