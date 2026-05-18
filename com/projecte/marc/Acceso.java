package com.projecte.marc;

import com.projecte.marc.Usuario.Rol;
import com.projecte.utils.DatoInvalidoException;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Gestiona el acceso y el registro de usuarios en el sistema.
 *
 * Esta clase carga y guarda los usuarios desde el fichero principal
 * y permite iniciar sesión o crear nuevos usuarios.
 */
public class Acceso {

    // Scanner para leer datos introducidos por teclado
    private Scanner entrada = new Scanner(System.in);
    // Lista que almacena todos los usuarios registrados
    private ArrayList<Usuario> usuarios;

    // Constructor
    public Acceso() {
        this.usuarios = new ArrayList<Usuario>();
        cargarDatos();
    }

    // Getters y Setters
    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /**
     * Muestra el menú de acceso y permite al usuario iniciar sesión o registrarse.
     * 
     * @return el usuario registrado o con el que se ha iniciado sesion.
     */
    public Usuario inicio() {

        int opcion = 0;
        Usuario usuario = null;

        do {
            try {

                menuAcceso();
                opcion = Integer.parseInt(entrada.nextLine());

                // Comprueba si la opcion es valida
                if (opcion != 1 && opcion != 2) {
                    throw new DatoInvalidoException("\nERROR: Valor fuera de rango.\n");
                }

                switch (opcion) {
                    // Inicio de sesion
                    case 1 -> {
                        usuario = iniciarSesion();
                    }
                    // Registro de usuario
                    case 2 -> {
                        usuario = registro();
                        usuarios.add(usuario);
                    }
                    default -> {
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("\nERROR: Valor no numerico.\n");
                opcion = 0;
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
                opcion = 0;
            }

        } while (opcion != 1 && opcion != 2);
        guardarDatos();
        bienvenida(usuario);
        return usuario;
    }

    // Muestra el menú principal de acceso en consola.
    public void menuAcceso() {
        System.out.println("""
                ----------------------------------------
                          GESTOR DE PELÍCULAS
                ----------------------------------------

                    1 - Iniciar sesión
                    2 - Registrarse

                ----------------------------------------
                """);
        System.out.print("Elige una opcion: ");
    }

    /**
     * Carga los usuarios registrados desde el fichero "usuarios.llista".
     *
     * Si el fichero no existe, se crea un usuario administrador por defecto.
     */
    public void cargarDatos() {

        File file = new File("usuarios.llista");

        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("usuarios.llista"))) {

                // Lee todos los usuarios del fichero
                while (true) {
                    Usuario u = (Usuario) in.readObject();
                    usuarios.add(u);
                    Usuario.contador++;
                }

            } catch (EOFException e) {
                // Fin del fichero
            } catch (IOException e) {
                System.out.println("\nERROR: " + e.getMessage() + "\n");
            } catch (ClassNotFoundException e) {
                System.out.println("\nERROR: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        } else {
            // Crea un usuario administrador
            Usuario admin = new Usuario("Admin", "Admin", "admin@gmail.com", "admin1234", "Valencia", Rol.ROL_ADMIN,
                    LocalDate.of(1990, 5, 10));
            usuarios.add(admin);
        }
    }

    // Muestra por consola todos los usuarios registrados.
    public void mostrarUsuarios() {
        for (Usuario u : usuarios) {
            System.out.println(" - " + u.toString());
        }
    }

    // Guarda todos los usuarios actuales en el fichero "usuarios.llista".
    public void guardarDatos() {

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("usuarios.llista"));) {

            // Guarda cada usuario en el fichero
            for (Usuario usuario : usuarios) {
                out.writeObject(usuario);
            }

        } catch (IOException e) {
            System.out.println("\nERROR: " + e.getMessage() + "\n");
        }

    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @return el usuario registrado.
     * @throws DatoInvalidoException si algún dato introducido no es válido.
     */
    public Usuario registro() throws DatoInvalidoException {
        System.out.println("""

                ----------------------------------------
                                REGISTRO
                ----------------------------------------
                  """);
        System.out.print(" - Introduce el nombre del nuevo usuario: ");
        String nombre = entrada.nextLine();
        System.out.print(" - Introduce los apellidos del usuario: ");
        String apellidos = entrada.nextLine();

        String nombreCompleto = nombre + " " + apellidos;

        // Combrueba si el usuario ya existe
        Usuario usuario = buscarUsuario(nombreCompleto);

        if (usuario != null) {
            throw new DatoInvalidoException(
                    "\nERROR: Ya existe un usuario registrado con el nombre " + nombreCompleto + ".\n");
        }

        System.out.print(" - Introduce el correo de " + nombre + ": ");
        String correo = entrada.nextLine();

        // Validacion del correo electronico
        if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(es|com)$")) {
            throw new DatoInvalidoException(
                    "\nERROR: el correo no es válido. Debe contener '@' y terminar en '.es' o '.com'");
        }

        System.out.print(" - Introduce la poblacion de " + nombre + ": ");
        String poblacion = entrada.nextLine();

        System.out.print(" - Introduce la fecha de nacimiento de " + nombre + " (yyyy/MM/dd): ");
        String fechaNacimiento = entrada.nextLine();

        // Conversion de String a LocalDate
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate fecha = LocalDate.parse(fechaNacimiento, formato);

        // Rol por defecto para nuevos usuarios
        Rol rol = Rol.ROL_USUARIO;

        System.out.print(" - Introduce la contraseña: ");
        String contrasenya = entrada.nextLine();

        System.out.print(" - Confirma la contraseña introducida: ");
        String confirmacion = entrada.nextLine();

        // Comprueba si las contraseñas coinciden
        if (!contrasenya.equalsIgnoreCase(confirmacion)) {
            throw new DatoInvalidoException("\nERROR: La contrasenya introducida no coincide.\n");
        }
        return new Usuario(nombre, apellidos, correo, contrasenya, poblacion, rol, fecha);
    }

    /**
     * Permite iniciar sesión a un usuario registrado.
     *
     * @return el usuario autenticado.
     * @throws DatoInvalidoException si los datos de acceso son incorrectos.
     */
    public Usuario iniciarSesion() throws DatoInvalidoException {
        System.out.println("""

                ----------------------------------------
                            INICIO DE SESION
                ----------------------------------------
                  """);

        System.out.print(" - Introduce el nombre completo del usuario: ");
        String nombre = entrada.nextLine();

        // Busca el usuario
        Usuario usuario = buscarUsuario(nombre);

        if (usuario == null) {
            throw new DatoInvalidoException(
                    "\nERROR: No se ha encontrado ningún usuario registrado con el nombre " + nombre + ".\n");
        }

        System.out.print(" - Introduce la contraseña: ");
        String contrasenya = entrada.nextLine();

        // Verifica la contraseña
        if (!usuario.getContrasenya().equalsIgnoreCase(contrasenya)) {
            throw new DatoInvalidoException("\nERROR: La contrasenya introducida es incorrecta.\n");
        }

        System.out.print(" - Confirma la contraseña introducida: ");
        String confirmacion = entrada.nextLine();

        // Comprueba la confirmación
        if (!usuario.getContrasenya().equalsIgnoreCase(confirmacion)) {
            throw new DatoInvalidoException("\nERROR: La contrasenya introducida no coincide.\n");
        }
        return usuario;
    }

    /**
     * Busca un usuario por su nombre completo.
     *
     * @param nombreCompleto nombre completo del usuario.
     * @return usuario encontrado o null si no existe.
     */
    public Usuario buscarUsuario(String nombreCompleto) {
        for (Usuario u : usuarios) {
            if (u.nombreCompleto().equalsIgnoreCase(nombreCompleto)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Muestra un mensaje de bienvenida para el usuario autenticado.
     *
     * @param u usuario autenticado.
     */
    public void bienvenida(Usuario u) {
        System.out.println("""
            
                ========================================
                   BIENVENIDO AL GESTOR DE PELÍCULAS
                ========================================
                        Usuario: %s
                ========================================\n
                """.formatted(u.nombreCompleto()));
    }

}
