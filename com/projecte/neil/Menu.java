package com.projecte.neil;

import com.projecte.marc.Usuario;
import com.projecte.pablo.Actor;
import com.projecte.pablo.Catalogo;
import com.projecte.pablo.Director;
import com.projecte.pablo.Pelicula;
import com.projecte.utils.ComparadorPorAnyoTitulo;
import com.projecte.utils.ComparadorPorId;
import com.projecte.utils.DatoInvalidoException;
import com.projecte.utils.FiltrarPeliculas;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Scanner;

/**
 * Controla la interacción por consola con el usuario.
 *
 * Gestiona el menú principal, las opciones de catálogo general, la construcción
 * de las listas particulares
 * y las operaciones de administración de elementos.
 */
public class Menu {

    // Scanner para leer datos introducidos por teclado
    private Scanner entrada = new Scanner(System.in);

    private Usuario usuario;
    private Catalogo catalogo;

    /**
     * Construye el menú de la aplicación con el usuario actual y el catálogo
     * general.
     *
     * @param usuario  usuario conectado.
     * @param catalogo catálogo general disponible.
     */
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

    /**
     * Ejecuta el bucle principal del menú y procesa las opciones seleccionadas por
     * el usuario.
     */
    public void inicio() {

        int opcion = 0;

        usuario.actualizarListas(catalogo);

        do {

            try {

                menuPrincipal();
                opcion = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcion > 5 || opcion < 0) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.\n");
                }

                switch (opcion) {
                    case 1:
                        consultarCatalogosGenerales();
                        break;
                    case 2:
                        anyadirElemento();
                        break;
                    case 3:
                        construirListaParticular();
                        break;
                    case 4:
                        eliminarElementos();
                        break;
                    case 5:
                        consultarListasParticulares();
                        break;
                    case 6:
                        System.out.println("\nSaliendo del sistema. Vuelve cuando quieras...\n");
                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.\n");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcion != 0);

    }

    /**
     * Muestra el menú principal con las opciones disponibles.
     */
    public void menuPrincipal() {
        System.out.println("""
                             MENÚ PRINCIPAL
                ========================================
                  1 - Consultar catalogos generales
                  2 - Añadir elementos a la listas generales
                  3 - Construir listas personales
                  4 - Eliminar elementos
                  5 - Ordenacion de las listas personales
                  0 - Cerrar Sesion
                ========================================
                """);
        System.out.print("Elige una opcion: ");
    }

    /**
     * Muestra el submenú para seleccionar el tipo de elemento a consultar o
     * gestionar.
     */
    public void menuSeleccionElementos() {
        System.out.println("""

                            CONSULTAR CATALOGO
                ========================================
                  1 - Pelicula
                  2 - Director
                  3 - Actor
                  0 - Volver atras
                ========================================
                """);

        System.out.print("Elige una opcion: ");
    }

    /**
     * Muestra el submenú para añadir nuevos elementos al catálogo general.
     */
    public void menuanyadirElementos() {
        System.out.println("""

                             AÑADIR ELEMENTO
                ========================================
                  1 - Pelicula
                  2 - Director
                  3 - Actor
                  0 - Volver atras
                ========================================
                """);

        System.out.print("Elige una opcion: ");
    }

    /**
     * Muestra el submenú de ordenación de listas.
     */
    public void menuOrdenacion() {
        System.out.println("""

                            MENÚ DE ORDENACIÓN
                ========================================
                  1 - Ordenar por título
                  2 - Ordenar por duracion
                  3 - Ordenar por año + titulo
                  4 - Ordenar por filtro (duracion + genero)
                  0 - Volver atras
                ========================================
                """);

        System.out.print("Elige una opción: ");
    }

    /**
     * Muestra el submenú para elegir opciones de visualización de detalles.
     */
    public void submenuVisualizaciones() {
        System.out.println("""

                         MENÚ DE VISUALIZACIONES
                ========================================
                  1 - Mostrar detalles
                  0 - Volver atras
                ========================================
                """);

        System.out.print("Elige una opción: ");
    }

    /**
     * Muestra el submenú para eliminar elementos del catálogo o de las listas
     * particulares.
     */
    public void menuEliminarElementos() {

        System.out.println("""

                        MENÚ ELIMINAR ELEMENTOS
                ==========================================
                   1 - Eliminar en lista general
                   2 - Eliminar en lista particular
                   0 - Volver atrás
                ==========================================
                """);

        System.out.print("Elige una opción: ");
    }

    /**
     * Muestra el submenú para seleccionar el tipo de elemento a eliminar.
     */
    public void menuSeleccionEliminarElemento() {

        System.out.println("""

                      ¿QUÉ ELEMENTO DESEAS ELIMINAR?
                ==========================================
                   1 - Película
                   2 - Director
                   3 - Actor
                   0 - Volver atrás
                ==========================================
                """);

        System.out.print("Elige una opción: ");
    }

    /**
     * Solicita los datos de una nueva película y la añade al catálogo.
     *
     * Valida que no exista una película con el mismo título antes de añadirla.
     */
    public void altaPelicula() {

        try {

            System.out.print("\nIntroduce el titulo de la pelicula: ");
            String titulo = entrada.nextLine();

            Pelicula p = catalogo.existePelicula(titulo);

            if (p != null) {
                System.out.println("\n" + p.resumen());
                throw new DatoInvalidoException("INFORMACION: La pelicula " + titulo + " ya existe en el catálogo.\n");
            }

            System.out.print("¿En que año se estreno la pelicula " + titulo + "? ");
            int anyo = Integer.parseInt(entrada.nextLine());

            System.out.print("Introduce la duracion de la pelicula: ");
            int duracion = Integer.parseInt(entrada.nextLine());

            System.out.print("Introduce el genero de la pelicula " + titulo + ": ");
            String texto = entrada.nextLine().toUpperCase();

            Pelicula.Genero genero = Pelicula.Genero.valueOf(texto);

            p = new Pelicula(titulo, anyo, duracion, genero);
            catalogo.anyadirPelicula(p);
            System.out.println("\nPelicula añadida al catalogo.\n");
            catalogo.guardarDatosGeneralesPeliculas();

        } catch (DatoInvalidoException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("\nERROR: El género introducido no es válido.\n");
        }

    }

    /**
     * Solicita los datos de un nuevo director y lo añade al catálogo.
     *
     * Valida que no exista un director con el mismo nombre completo antes de
     * añadirlo.
     */
    public void altaDirector() {

        try {

            System.out.print("\nIntroduce el nombre del director: ");
            String nombre = entrada.nextLine();

            System.out.print("Introduce los apellidos de " + nombre + ": ");
            String apellidos = entrada.nextLine();

            String nombreCompleto = nombre + " " + apellidos;

            Director d = catalogo.existeDirector(nombreCompleto);

            if (d != null) {
                System.out.println("\n" + d.resumen());
                throw new DatoInvalidoException(
                        "INFORMACION: El director " + nombreCompleto + " ya existe en el catálogo.\n");
            }

            System.out.print("Introduce la fecha de nacimiento de " + nombre + " (yyyy/MM/dd): ");
            String fecha = entrada.nextLine();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate fechaNacimiento = LocalDate.parse(fecha, formato);

            System.out.print("Introduce la nacionalidad de " + nombre + ": ");
            String nacionalidad = entrada.nextLine();

            d = new Director(nombre, apellidos, fechaNacimiento, nacionalidad);
            catalogo.anyadirDirector(d);
            System.out.println("\nDirector añadido al catalogo.\n");
            catalogo.guardarDatosGeneralesDirectores();

        } catch (DatoInvalidoException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Solicita los datos de un nuevo actor y lo añade al catálogo.
     *
     * Valida que no exista un actor con el mismo nombre completo antes de añadirlo.
     */
    public void altaActor() {

        try {

            System.out.print("\nIntroduce el nombre del actor: ");
            String nombre = entrada.nextLine();

            System.out.print("Introduce los apellidos de " + nombre + ": ");
            String apellidos = entrada.nextLine();

            String nombreCompleto = nombre + " " + apellidos;

            Actor a = catalogo.existeActor(nombreCompleto);

            if (a != null) {
                System.out.println("\n" + a.resumen());
                throw new DatoInvalidoException(
                        "INFORMACION: El actor " + nombreCompleto + " ya existe en el catálogo.\n");
            }

            System.out.print("Introduce la fecha de nacimiento de " + nombre + " (yyyy/MM/dd): ");
            String fecha = entrada.nextLine();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate fechaNacimiento = LocalDate.parse(fecha, formato);

            System.out.print("Introduce la nacionalidad de " + nombre + ": ");
            String nacionalidad = entrada.nextLine();

            a = new Actor(nombre, apellidos, fechaNacimiento, nacionalidad);
            catalogo.anyadirActor(a);
            System.out.println("\nActor añadido al catalogo.\n");
            catalogo.guardarDatosGeneralesActores();

        } catch (DatoInvalidoException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Controla el flujo de añadir elementos al catálogo general.
     *
     * Solo los administradores pueden acceder a esta opción.
     */
    public void anyadirElemento() {

        int opcionSubmenu = 0;

        if (usuario.getRol().equals(Usuario.Rol.ROL_ADMIN)) {

            try {
                menuanyadirElementos();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu > 3 || opcionSubmenu < 0) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.\n");
                }

                switch (opcionSubmenu) {
                    case 1 -> {
                        altaPelicula();
                    }
                    case 2 -> {
                        altaDirector();
                    }
                    case 3 -> {
                        altaActor();
                    }
                    case 0 -> {
                        System.out.print("\nVolviendo al menu anterior...\n");
                    }
                    default -> {
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.\n");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }
        } else {
            throw new DatoInvalidoException(
                    "\nINFORMACION: El usuario no puede acceder a este apartado. Para acceder se necesita ser administrador del catalogo.\n");
        }
    }

    /**
     * Permite consultar los catálogos generales de películas, directores y actores.
     */
    public void consultarCatalogosGenerales() {

        int opcionSubmenu = 0;

        do {

            try {
                menuSeleccionElementos();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu > 3 || opcionSubmenu < 0) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.");
                }

                switch (opcionSubmenu) {
                    case 1:
                        ordenacionListasGenerales();
                        break;
                    case 2:
                        listaGeneralDirectores(opcionSubmenu);
                        break;
                    case 3:
                        listaGeneralActores(opcionSubmenu);
                        break;
                    case 0:
                        System.out.println("\nVolviendo al menu anterior...\n");
                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcionSubmenu != 0);

    }

    /**
     * Muestra la lista general de películas y permite ver los detalles de una
     * selección.
     *
     * @param opcionSubmenu opción de visualización seleccionada.
     */
    public void listaGeneralPeliculas(int opcionSubmenu) {
        System.out.println("\n----- LISTA GENERAL PELICULAS [Usuario = "
                + usuario.identificador() + "] -----");
        // Si no hay comparador explícito, ordenamos por ID usando ComparadorPorId.
        Collections.sort(catalogo.getPeliculas(), new ComparadorPorId());
        boolean esVacio = catalogo.mostrarDatosGeneralesPeliculas();

        if (esVacio) {
            return;
        }

        do {

            try {

                submenuVisualizaciones();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu != 0 && opcionSubmenu != 1) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.\n");
                }

                switch (opcionSubmenu) {
                    case 0:
                        System.out.println("\nVolviendo al menu anterior...");
                        break;
                    case 1:
                        System.out.println("\n¿Que pelicula deseas ver con detalle?\n");
                        catalogo.mostrarDatosGeneralesPeliculas();
                        System.out.print("\nElige el identificador de la pelicula que desees: ");
                        String detalles = entrada.nextLine();

                        Pelicula p = catalogo.buscarPelicula(detalles);

                        if (p == null) {
                            throw new DatoInvalidoException(
                                    "\nINFORMACION: La pelicula con el identificador " + detalles
                                            + " no esta en el catalogo.\n");
                        }
                        System.out.println();
                        p.mostrarDetalles();
                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.\n");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcionSubmenu != 0);

    }

    /**
     * Muestra la lista general de directores y permite ver los detalles de uno.
     *
     * @param opcionSubmenu opción de visualización seleccionada.
     */
    public void listaGeneralDirectores(int opcionSubmenu) {
        System.out.println("\n----- LISTA GENERAL DIRECTORES [Usuario = "
                + usuario.identificador() + "] -----");
        // Usa la ordenación natural de Director cuando no se proporciona un comparador.
        Collections.sort(catalogo.getDirectores());
        boolean esVacio = catalogo.mostrarDatosGeneralesDirectores();

        if (esVacio) {
            return;
        }

        do {

            try {

                submenuVisualizaciones();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu != 0 && opcionSubmenu != 1) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.\n");
                }

                switch (opcionSubmenu) {
                    case 0:
                        System.out.println("\nVolviendo al menu anterior...");
                        break;
                    case 1:
                        System.out.println("\n¿Que director deseas ver con detalle?\n");
                        catalogo.mostrarDatosGeneralesDirectores();
                        System.out.print("\nElige el identificador del director que desees: ");
                        String detalles = entrada.nextLine();

                        Director d = catalogo.buscarDirector(detalles);

                        if (d == null) {
                            throw new DatoInvalidoException(
                                    "\nINFORMACION: El director con el identificador " + detalles
                                            + " no esta en el catalogo.\n");
                        }
                        System.out.println();
                        d.mostrarDetalles();

                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.\n");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcionSubmenu != 0);

    }

    /**
     * Muestra el submenú de detalles de película y ordena la lista general
     * por identificador antes de seleccionar la película.
     */
    public void mostrarDetallesPelicula() {

        int opcionSubmenu = 0;

        do {

            try {

                submenuVisualizaciones();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu != 0 && opcionSubmenu != 1) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.\n");
                }

                switch (opcionSubmenu) {
                    case 0:
                        System.out.println("\nVolviendo al menu anterior...");
                        break;
                    case 1:
                        Collections.sort(catalogo.getPeliculas(), new ComparadorPorId());
                        System.out.println("\n¿Que pelicula deseas ver con detalle?\n");
                        catalogo.mostrarDatosGeneralesPeliculas();
                        System.out.print("\nElige el identificador de la pelicula que desees: ");
                        String detalles = entrada.nextLine();

                        Pelicula p = catalogo.buscarPelicula(detalles);

                        if (p == null) {
                            throw new DatoInvalidoException(
                                    "\nINFORMACION: La pelicula con el identificador " + detalles
                                            + " no esta en el catalogo.\n");
                        }
                        System.out.println();
                        p.mostrarDetalles();

                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.\n");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcionSubmenu != 0);
    }

    /**
     * Muestra la lista general de actores y permite ver los detalles de uno.
     *
     * @param opcionSubmenu opción de visualización seleccionada.
     */
    public void listaGeneralActores(int opcionSubmenu) {
        System.out.println("\n----- LISTA GENERAL ACTORES [Usuario = "
                + usuario.identificador() + "] -----");
        // Usa la ordenación natural de Actor cuando no se proporciona un comparador.
        Collections.sort(catalogo.getActores());
        boolean esVacio = catalogo.mostrarDatosGeneralesActores();

        if (esVacio) {
            return;
        }

        do {

            try {

                submenuVisualizaciones();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu != 0 && opcionSubmenu != 1) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.\n");
                }

                switch (opcionSubmenu) {
                    case 0:
                        System.out.println("\nVolviendo al menu anterior...");
                        break;
                    case 1:
                        System.out.println("\n¿Que actor deseas ver con detalle?\n");
                        catalogo.mostrarDatosGeneralesActores();
                        System.out.print("\nElige el identificador del actor que desees: ");
                        String detalles = entrada.nextLine();

                        Actor a = catalogo.buscarActor(detalles);

                        if (a == null) {
                            throw new DatoInvalidoException(
                                    "\nINFORMACION: El actor con el identificador " + detalles
                                            + " no esta en el catalogo.\n");
                        }
                        System.out.println();
                        a.mostrarDetalles();
                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.\n");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcionSubmenu != 0);
    }

    /**
     * Muestra y permite consultar las listas particulares del usuario.
     */
    public void consultarListasParticulares() {

        int opcionSubmenu = 0;

        do {

            try {
                menuSeleccionElementos();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu > 3 || opcionSubmenu < 0) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.");
                }

                switch (opcionSubmenu) {
                    case 1:
                        ordenacionListasParticulares();
                        break;
                    case 2:
                        System.out.println("\n----- LISTA PARTICULAR DIRECTORES [Usuario = "
                                + usuario.identificador() + "] -----");
                        // Ordenación por ID cuando no se proporciona otro comparador.
                        Collections.sort(usuario.getDirectores());
                        boolean esVacio = usuario.mostrarDatosParticularesDirectores();

                        if (esVacio) {
                            return;
                        }
                        detallesParticularesDirectores();
                        break;
                    case 3:
                        System.out.println("\n----- LISTA PARTICULAR ACTORES [Usuario = "
                                + usuario.identificador() + "] -----");
                        // Ordenación por ID cuando no se proporciona otro comparador.
                        Collections.sort(usuario.getActores());
                        boolean estaVacio = usuario.mostrarDatosParticularesActores();

                        if (estaVacio) {
                            return;
                        }
                        detallesParticularesActores();
                        break;
                    case 0:
                        System.out.println("\nVolviendo al menu anterior...\n");
                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcionSubmenu != 0);
    }

    /**
     * Gestiona la ordenación de las listas particulares del usuario.
     */
    public void ordenacionListasParticulares() {

        int opcionSubmenu = 0;

        do {

            try {

                menuOrdenacion();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu > 4 || opcionSubmenu < 0) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.\n");
                }

                switch (opcionSubmenu) {
                    case 1: // Ordenación alfabético por título.
                        System.out.println("\n----- ORDENACION POR TITULO [Usuario = "
                                + usuario.identificador() + "] -----");
                        usuario.mostrarOrdenacionPeliculas();
                        detallesParticularesPelicula();
                        break;
                    case 2: // Ordenación por duración
                        System.out.println("\n----- ORDENACION POR DURACION [Usuario = "
                                + usuario.identificador() + "] -----");

                        // Comparator, empleado con una clase anónima para ordenar por duración
                        usuario.mostrarOrdenacionPeliculas((Pelicula o1, Pelicula o2) -> {
                            if (Integer.compare(o1.getDuracion(), o2.getDuracion()) == 0) {
                                return 0;
                            } else if (Integer.compare(o1.getDuracion(), o2.getDuracion()) < 0) {
                                return -1;
                            } else {
                                return 1;
                            }
                        });
                        detallesParticularesPelicula();
                        break;
                    case 3: // Ordenación usando Comparator con una clase externa
                        System.out.println("\n----- ORDENACION POR AÑO Y TITULO [Usuario = "
                                + usuario.identificador() + "] -----");
                        usuario.mostrarOrdenacionPeliculas(new ComparadorPorAnyoTitulo()); // Usa el
                                                                                           // comparador
                                                                                           // personalizado
                        detallesParticularesPelicula();
                        break;
                    case 4: // Filtrado personalizado usando Iterator
                        System.out.print(
                                "\nIntroduce la duracion maxima de la pelicula para seleccionar el filtro: ");
                        int d = Integer.parseInt(entrada.nextLine());

                        System.out.print("Introduce el genero de la Pelicula para escoger el filtro: ");
                        String texto = entrada.nextLine().toUpperCase();
                        Pelicula.Genero g = Pelicula.Genero.valueOf(texto); // Convierte String a enum

                        FiltrarPeliculas fp = usuario.getFiltrarPeliculas(g, d);
                        System.out
                                .println("\n----- ORDENACION POR FILTRO (DURACION Y GENERO) [Usuario = "
                                        + usuario.identificador() + "] -----");

                        if (!fp.hasNext()) {
                            System.out.println("\nINFORMACION: Tu catalogo de peliculas esta vacio.\n");
                        }

                        // Recorre las peliculas que cumple el filtro
                        while (fp.hasNext()) {
                            Pelicula p = fp.next();
                            System.out.println(" - " + p.resumen());
                        }
                        detallesParticularesPelicula();
                        break;
                    case 0:
                        System.out.println("\nVolviendo al menu anterior...\n");
                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.\n");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcionSubmenu != 0);
    }

    /**
     * Gestiona la ordenación de las listas generales del catálogo.
     */
    public void ordenacionListasGenerales() {
        int opcionSubmenu = 0;

        do {

            try {

                menuOrdenacion();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu > 4 || opcionSubmenu < 0) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.\n");
                }

                switch (opcionSubmenu) {
                    case 1: // Ordenación alfabético por título.
                        System.out.println("\n----- ORDENACION POR TITULO [Usuario = "
                                + usuario.identificador() + "] -----");
                        catalogo.mostrarOrdenacionPeliculas();
                        mostrarDetallesPelicula();
                        break;
                    case 2: // Ordenación por duración
                        System.out.println("\n----- ORDENACION POR DURACION [Usuario = "
                                + usuario.identificador() + "] -----");

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
                        mostrarDetallesPelicula();
                        break;
                    case 3: // Ordenación usando Comparator con una clase externa
                        System.out.println("\n----- ORDENACION POR AÑO Y TITULO [Usuario = "
                                + usuario.identificador() + "] -----");
                        catalogo.mostrarOrdenacionPeliculas(new ComparadorPorAnyoTitulo()); // Usa el
                                                                                            // comparador
                                                                                            // personalizado
                        mostrarDetallesPelicula();
                        break;
                    case 4: // Filtrado personalizado usando Iterator
                        System.out.print(
                                "\nIntroduce la duracion maxima de la pelicula para seleccionar el filtro: ");
                        int d = Integer.parseInt(entrada.nextLine());

                        System.out.print("Introduce el genero de la Pelicula para escoger el filtro: ");
                        String texto = entrada.nextLine().toUpperCase();
                        Pelicula.Genero g = Pelicula.Genero.valueOf(texto); // Convierte String a enum

                        FiltrarPeliculas fp = catalogo.getFiltrarPeliculas(g, d);
                        System.out
                                .println("\n----- ORDENACION POR FILTRO (DURACION Y GENERO) [Usuario = "
                                        + usuario.identificador() + "] -----");

                        // Recorre las peliculas que cumple el filtro
                        if (!fp.hasNext()) {
                            System.out.println("\nINFORMACION: Tu catalogo de peliculas esta vacio.\n");
                        }

                        while (fp.hasNext()) {
                            Pelicula p = fp.next();
                            System.out.println(" - " + p.resumen());
                        }
                        mostrarDetallesPelicula();
                        break;
                    case 0:
                        System.out.println("\nVolviendo al menu anterior...\n");
                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.\n");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcionSubmenu != 0);
    }

    /**
     * Gestiona el submenú para eliminar elementos del catálogo o de las listas
     * particulares.
     */
    public void eliminarElementos() {

        int opcionSubmenu = 0;

        do {

            try {
                menuEliminarElementos();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu > 2 || opcionSubmenu < 0) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.");
                }

                switch (opcionSubmenu) {
                    case 1:
                        eliminarListaGeneral();
                        break;
                    case 2:
                        eliminarListaParticular();
                        break;
                    case 0:
                        System.out.println("\nVolviendo al menu anterior...\n");
                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcionSubmenu != 0);

    }

    /**
     * Permite eliminar elementos de las listas particulares del usuario.
     */
    public void eliminarListaParticular() {

        int opcionSubmenu = 0;

        do {

            try {
                menuSeleccionEliminarElemento();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu > 3 || opcionSubmenu < 0) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.");
                }

                switch (opcionSubmenu) {
                    case 1:
                        System.out.println("\n----- LISTA PARTICULAR PELICULAS [Usuario = "
                                + usuario.identificador() + "] -----");
                        Collections.sort(usuario.getPeliculas(), new ComparadorPorId());
                        boolean esVacio = usuario.mostrarDatosParticularesPeliculas();

                        if (esVacio) {
                            return;
                        }

                        System.out.print("\n¿Que pelicula deseas eliminar (Escoge el identificador)? ");
                        String id = entrada.nextLine();

                        Pelicula p = usuario.buscarPelicula(id);

                        if (p == null) {
                            throw new DatoInvalidoException(
                                    "\nINFORMACION: La pelicula con el identificador " + id
                                            + " no esta en el catalogo.\n");
                        }

                        usuario.eliminarPelicula(p);
                        System.out.println("\nPelicula eliminada del catalogo correctamente.\n");
                        usuario.guardarDatosParticularesPeliculas();
                        break;
                    case 2:
                        System.out.println("\n----- LISTA PARTICULAR DIRECTORES [Usuario = "
                                + usuario.identificador() + "] -----");
                        Collections.sort(usuario.getDirectores());
                        boolean vacio = usuario.mostrarDatosParticularesDirectores();

                        if (vacio) {
                            return;
                        }

                        System.out.print("\n¿Que director deseas eliminar (Escoge el identificador)? ");
                        String ide = entrada.nextLine();

                        Director d = usuario.buscarDirector(ide);

                        if (d == null) {
                            throw new DatoInvalidoException(
                                    "\nINFORMACION: El director con el identificador " + ide
                                            + " no esta en el catalogo.\n");
                        }

                        usuario.eliminarDirector(d);
                        System.out.println("\nDirector eliminada del catalogo correctamente.\n");
                        usuario.guardarDatosParticularesDirectores();
                        break;
                    case 3:
                        System.out.println("\n----- LISTA PARTICULAR ACTORES [Usuario = "
                                + usuario.identificador() + "] -----");
                        Collections.sort(usuario.getActores());
                        boolean estaVacio = usuario.mostrarDatosParticularesActores();

                        if (estaVacio) {
                            return;
                        }

                        System.out.print("\n¿Que actor deseas eliminar (Escoge el identificador)? ");
                        String ids = entrada.nextLine();

                        Actor a = usuario.buscarActor(ids);

                        if (a == null) {
                            throw new DatoInvalidoException(
                                    "\nINFORMACION: El actor con el identificador " + ids
                                            + " no esta en el catalogo.\n");
                        }

                        usuario.eliminarActor(a);
                        System.out.println("\nActor eliminada del catalogo correctamente.\n");
                        usuario.guardarDatosParticularesActores();
                        break;
                    case 0:
                        System.out.println("\nVolviendo al menu anterior...\n");
                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcionSubmenu != 0);

    }

    /**
     * Permite eliminar elementos del catálogo general (solo administradores).
     */
    public void eliminarListaGeneral() {

        int opcionSubmenu = 0;

        if (usuario.getRol().equals(Usuario.Rol.ROL_ADMIN)) {

            do {

                try {
                    menuSeleccionEliminarElemento();
                    opcionSubmenu = Integer.parseInt(entrada.nextLine());

                    // Comprueba si la opcion es valida
                    if (opcionSubmenu > 3 || opcionSubmenu < 0) {
                        throw new DatoInvalidoException("\nERROR: Valor fuera de rango.");
                    }

                    switch (opcionSubmenu) {
                        case 1:
                            System.out.println("\n----- LISTA GENERAL PELICULAS [Usuario = "
                                    + usuario.identificador() + "] -----");
                            Collections.sort(catalogo.getPeliculas(), new ComparadorPorId());
                            boolean esVacio = catalogo.mostrarDatosGeneralesPeliculas();

                            if (esVacio) {
                                return;
                            }

                            System.out.print("\n¿Que pelicula deseas eliminar (Escoge el identificador)? ");
                            String id = entrada.nextLine();

                            Pelicula p = catalogo.buscarPelicula(id);

                            if (p == null) {
                                throw new DatoInvalidoException(
                                        "\nINFORMACION: La pelicula con el identificador " + id
                                                + " no esta en el catalogo.\n");
                            }

                            catalogo.eliminarPelicula(p);
                            System.out.println("\nPelicula eliminada del catalogo correctamente.\n");
                            catalogo.guardarDatosGeneralesPeliculas();
                            break;
                        case 2:
                            System.out.println("\n----- LISTA GENERAL DIRECTORES [Usuario = "
                                    + usuario.identificador() + "] -----");
                            Collections.sort(catalogo.getDirectores());
                            boolean vacio = catalogo.mostrarDatosGeneralesDirectores();

                            if (vacio) {
                                return;
                            }

                            System.out.print("\n¿Que director deseas eliminar (Escoge el identificador)? ");
                            String ide = entrada.nextLine();

                            Director d = catalogo.buscarDirector(ide);

                            if (d == null) {
                                throw new DatoInvalidoException(
                                        "\nINFORMACION: El director con el identificador " + ide
                                                + " no esta en el catalogo.\n");
                            }

                            catalogo.eliminarDirector(d);
                            System.out.println("\nDirector eliminada del catalogo correctamente.\n");
                            catalogo.guardarDatosGeneralesDirectores();
                            break;
                        case 3:
                            System.out.println("\n----- LISTA GENERAL ACTORES [Usuario = "
                                    + usuario.identificador() + "] -----");
                            Collections.sort(catalogo.getActores());
                            boolean estaVacio = catalogo.mostrarDatosGeneralesActores();

                            if (estaVacio) {
                                return;
                            }

                            System.out.print("\n¿Que actor deseas eliminar (Escoge el identificador)? ");
                            String ids = entrada.nextLine();

                            Actor a = catalogo.buscarActor(ids);

                            if (a == null) {
                                throw new DatoInvalidoException(
                                        "\nINFORMACION: El actor con el identificador " + ids
                                                + " no esta en el catalogo.\n");
                            }

                            catalogo.eliminarActor(a);
                            System.out.println("\nActor eliminada del catalogo correctamente.\n");
                            catalogo.guardarDatosGeneralesActores();
                            break;
                        case 0:
                            System.out.println("\nVolviendo al menu anterior...\n");
                            break;
                        default:
                            break;
                    }

                } catch (NumberFormatException e) {
                    System.out.println("\nERROR: Valor no numerico.");
                } catch (DatoInvalidoException e) {
                    System.out.println(e.getMessage());
                }

            } while (opcionSubmenu != 0);

        } else {
            throw new DatoInvalidoException(
                    "\nINFORMACION: El usuario no puede acceder a este apartado. Para acceder se necesita ser administrador del catalogo.\n");
        }

    }

    /**
     * Permite al usuario construir sus propias listas particulares de películas,
     * directores o actores.
     */
    public void construirListaParticular() {
        int opcionSubmenu = 0;

        do {

            try {
                menuSeleccionElementos();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu > 3 || opcionSubmenu < 0) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.");
                }

                switch (opcionSubmenu) {
                    case 1:
                        listaParticularPelicula();
                        break;
                    case 2:
                        listaParticularDirector();
                        break;
                    case 3:
                        listaParticularActor();
                        break;
                    case 0:
                        System.out.println("\nVolviendo al menu anterior...\n");
                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcionSubmenu != 0);
    }

    /**
     * Añade una película de la lista general a la lista particular del usuario.
     *
     * Ordena previamente las películas por ID cuando no se usa otro comparador.
     */
    public void listaParticularPelicula() {
        System.out.println("\n----- LISTA GENERAL PELICULAS [Usuario = "
                + usuario.identificador() + "] -----");
        Collections.sort(catalogo.getPeliculas(), new ComparadorPorId());
        boolean esVacio = catalogo.mostrarDatosGeneralesPeliculas();

        if (esVacio) {
            return;
        }

        System.out.print("\n¿Que pelicula deseas añadir a tu lista (Escoge el identificador)? ");
        String id = entrada.nextLine();

        Pelicula p = catalogo.buscarPelicula(id);

        if (p == null) {
            throw new DatoInvalidoException(
                    "\nINFORMACION: La pelicula con el identificador " + id
                            + " no esta en el catalogo.\n");
        }

        Pelicula pelicula = usuario.buscarPelicula(p.getIdentificador());

        if (pelicula != null) {
            throw new DatoInvalidoException(
                    "\nINFORMACION: La pelicula " + p.getTitulo()
                            + " esta ya en la lista.\n");
        }

        usuario.anyadirPelicula(p);
        usuario.guardarDatosParticularesPeliculas();
        System.out.println("\nPelicula añadida a la lista particular de " + usuario.nombreCompleto() + ".\n");
    }

    /**
     * Añade un director de la lista general a la lista particular del usuario.
     *
     * Ordena previamente los directores por su orden natural (ID) al mostrar el catálogo.
     */
    public void listaParticularDirector() {
        System.out.println("\n----- LISTA GENERAL DIRECTORES [Usuario = "
                + usuario.identificador() + "] -----");
        Collections.sort(catalogo.getDirectores());
        boolean esVacio = catalogo.mostrarDatosGeneralesDirectores();

        if (esVacio) {
            return;
        }

        System.out.print("\n¿Que director deseas añadir a tu lista (Escoge el identificador)? ");
        String id = entrada.nextLine();

        Director d = catalogo.buscarDirector(id);

        if (d == null) {
            throw new DatoInvalidoException(
                    "\nINFORMACION: El director con el identificador " + id
                            + " no esta en el catalogo.\n");
        }

        Director director = usuario.buscarDirector(d.getIdentificador());

        if (director != null) {
            throw new DatoInvalidoException(
                    "\nINFORMACION: El director " + d.nombreCompleto()
                            + " esta ya en la lista.\n");
        }

        usuario.anyadirDirector(d);
        usuario.guardarDatosParticularesDirectores();
        System.out.println("\nDirector añadido a la lista particular de " + usuario.nombreCompleto() + ".\n");
    }

    /**
     * Añade un actor de la lista general a la lista particular del usuario.
     *
     * Ordena previamente los actores por su orden natural (ID) al mostrar el catálogo.
     */
    public void listaParticularActor() {
        System.out.println("\n----- LISTA GENERAL ACTORES [Usuario = "
                + usuario.identificador() + "] -----");
        Collections.sort(catalogo.getActores());
        boolean esVacio = catalogo.mostrarDatosGeneralesActores();

        if (esVacio) {
            return;
        }

        System.out.print("\n¿Que actor deseas añadir a tu lista (Escoge el identificador)? ");
        String id = entrada.nextLine();

        Actor a = catalogo.buscarActor(id);

        if (a == null) {
            throw new DatoInvalidoException(
                    "\nINFORMACION: El actor con el identificador " + id
                            + " no esta en el catalogo.\n");
        }

        Actor actor = usuario.buscarActor(a.getIdentificador());

        if (actor != null) {
            throw new DatoInvalidoException(
                    "\nINFORMACION: El actor " + a.nombreCompleto()
                            + " esta ya en la lista.\n");
        }

        usuario.anyadirActor(a);
        usuario.guardarDatosParticularesActores();
        System.out.println("\nActor añadida a la lista particular de " + usuario.nombreCompleto() + ".\n");
    }

    /**
     * Muestra el submenú de detalles para directores en la lista particular.
     *
     * Ordena los directores por su orden natural (ID) antes de mostrar la lista.
     */
    public void detallesParticularesDirectores() {

        int opcionSubmenu = 0;

        do {

            try {

                submenuVisualizaciones();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu != 0 && opcionSubmenu != 1) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.\n");
                }

                switch (opcionSubmenu) {
                    case 0:
                        System.out.println("\nVolviendo al menu anterior...");
                        break;
                    case 1:
                        Collections.sort(usuario.getDirectores());
                        System.out.println("\n¿Que director deseas ver con detalle?\n");
                        usuario.mostrarDatosParticularesDirectores();
                        System.out.print("\nElige el identificador del director que desees: ");
                        String detalles = entrada.nextLine();

                        Director d = usuario.buscarDirector(detalles);

                        if (d == null) {
                            throw new DatoInvalidoException(
                                    "\nINFORMACION: El director con el identificador " + detalles
                                            + " no esta en el catalogo.\n");
                        }
                        System.out.println();
                        d.mostrarDetalles();
                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.\n");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcionSubmenu != 0);
    }

    /**
     * Muestra el submenú de detalles para actores en la lista particular.
     *
     * Ordena los actores por su orden natural (ID) antes de mostrar la lista.
     */
    public void detallesParticularesActores() {

        int opcionSubmenu = 0;

        do {

            try {

                submenuVisualizaciones();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu != 0 && opcionSubmenu != 1) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.\n");
                }

                switch (opcionSubmenu) {
                    case 0:
                        System.out.println("\nVolviendo al menu anterior...");
                        break;
                    case 1:
                        Collections.sort(usuario.getActores());
                        System.out.println("\n¿Que actor deseas ver con detalle?\n");
                        usuario.mostrarDatosParticularesActores();
                        System.out.print("\nElige el identificador del actor que desees: ");
                        String detalles = entrada.nextLine();

                        Actor a = usuario.buscarActor(detalles);

                        if (a == null) {
                            throw new DatoInvalidoException(
                                    "\nINFORMACION: El actor con el identificador " + detalles
                                            + " no esta en el catalogo.\n");
                        }
                        System.out.println();
                        a.mostrarDetalles();
                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.\n");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcionSubmenu != 0);
    }

    /**
     * Muestra el submenú de detalles para películas en la lista particular.
     *
     * Ordena las películas por ID cuando no se especifica otro comparador.
     */
    public void detallesParticularesPelicula() {

        int opcionSubmenu = 0;

        do {

            try {

                submenuVisualizaciones();
                opcionSubmenu = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcionSubmenu != 0 && opcionSubmenu != 1) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.\n");
                }

                switch (opcionSubmenu) {
                    case 0:
                        System.out.println("\nVolviendo al menu anterior...");
                        break;
                    case 1:
                        Collections.sort(usuario.getPeliculas(), new ComparadorPorId());
                        System.out.println("\n¿Que pelicula deseas ver con detalle?\n");
                        usuario.mostrarDatosParticularesPeliculas();
                        System.out.print("\nElige el identificador de la pelicula que desees: ");
                        String detalles = entrada.nextLine();

                        Pelicula p = usuario.buscarPelicula(detalles);

                        if (p == null) {
                            throw new DatoInvalidoException(
                                    "\nINFORMACION: La pelicula con el identificador " + detalles
                                            + " no esta en el catalogo.\n");
                        }
                        System.out.println();
                        p.mostrarDetalles();
                        break;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.\n");
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }

        } while (opcionSubmenu != 0);
    }

}
