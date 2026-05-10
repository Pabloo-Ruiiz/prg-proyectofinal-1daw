package com.projecte.marc;

import com.projecte.marc.Usuario.Rol;
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

public class Acceso {

    private Scanner entrada = new Scanner(System.in);
    private ArrayList<Usuario> usuarios;

    public Acceso() {
        this.usuarios = new ArrayList<Usuario>();
        cargarDatos();
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario inicio() {

        int opcion = 0;
        Usuario usuario = null;

        do {
            try {

                menuAcceso();
                opcion = Integer.parseInt(entrada.nextLine());

                if (opcion != 1 || opcion != 2) {
                    throw new DatoInvalidoException("\nError: Valor fuera de rango.\n");
                }

                switch (opcion) {
                    case 1 -> {
                        usuario = iniciarSesion();
                    }
                    case 2 -> {
                        usuario = registro();
                        usuarios.add(usuario);
                        crearCarpetaUsuario(usuario);
                    }
                    default -> {
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("\nError: Valor no numerico.\n");
                opcion = 0;
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
                opcion = 0;
            }

        } while (opcion != 1 && opcion != 2);
        guardarDatos();
        return usuario;
    }

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

    public void cargarDatos() {

        File file = new File("usuarios.llista");

        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("usuarios.llista"))) {

                while (true) {
                    Usuario u = (Usuario) in.readObject();
                    usuarios.add(u);
                }

            } catch (EOFException e) {
                // Fin del fichero
            } catch (IOException e) {
                System.out.println("\n" + e.getMessage() + "\n");
            } catch (ClassNotFoundException e) {
                System.out.println("\n" + e.getMessage() + "\n");
                e.printStackTrace();
            }
        } else {
            Usuario admin = new Usuario("Admin", "Admin", "admin@gmail.com", "admin1234", "Valencia", Rol.ROL_ADMIN, LocalDate.of(1990, 5, 10));
            usuarios.add(admin);
        }
    }

    public void mostrarUsuarios() {
        for (Usuario u : usuarios) {
            System.out.println(" - " + u.toString());
        }
    }

    public void guardarDatos() {

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("usuarios.llista"));) {

            for (Usuario usuario : usuarios) {
                out.writeObject(usuario);
            }

        } catch (IOException e) {
            System.out.println("\n" + e.getMessage() + "\n");
        }

    }

    public void crearCarpetaUsuario(Usuario u) {
        File directori = new File(u.identificador());

        if (directori.mkdir()) {
            System.out.println("\nCarpeta creada correctamente.\n");
        } else {
            System.out.println("\nLa carpeta ya existe o no se pudo crear.\n");
        }
    }

    public Usuario registro() throws DatoInvalidoException {
        System.out.println("""
                \n----------------------------------------
                                 REGISTRO
                  ----------------------------------------
                  """);
        System.out.print(" - Introduce el nombre del nuevo usuario: ");
        String nombre = entrada.nextLine();
        System.out.print(" - Introduce los apellidos del usuario: ");
        String apellidos = entrada.nextLine();

        String nombreCompleto = nombre + " " + apellidos;

        Usuario usuario = buscarUsuario(nombreCompleto);

        if (usuario != null) {
            throw new DatoInvalidoException(
                    "\nError: Ya existe un usuario registrado con el nombre " + nombreCompleto + ".\n");
        }

        System.out.print(" - Introduce el correo de " + nombre + ": ");
        String correo = entrada.nextLine();

        if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(es|com)$")) {
            throw new DatoInvalidoException(
                    "\nError: el correo no es válido. Debe contener '@' y terminar en '.es' o '.com'");
        }

        System.out.print(" - Introduce la poblacion de " + nombre + ": ");
        String poblacion = entrada.nextLine();

        System.out.print(" - Introduce la fecha de nacimiento de " + nombre + ": ");
        String fechaNacimiento = entrada.nextLine();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate fecha = LocalDate.parse(fechaNacimiento, formato);

        Rol rol = Rol.ROL_USUARIO;

        System.out.print(" - Introduce la contraseña: ");
        String contrasenya = entrada.nextLine();

        System.out.print(" - Confirma la contraseña introducida: ");
        String confirmacion = entrada.nextLine();

        if (!contrasenya.equalsIgnoreCase(confirmacion)) {
            throw new DatoInvalidoException("\nError: La contrasenya introducida no coincide.\n");
        }
        return new Usuario(nombre, apellidos, correo, contrasenya, poblacion, rol, fecha);
    }

    public Usuario iniciarSesion() throws DatoInvalidoException {
        System.out.println("""
                \n----------------------------------------
                              INICIO DE SESION
                  ----------------------------------------
                  """);
        System.out.print(" - Introduce el nombre completo del usuario: ");
        String nombre = entrada.nextLine();

        Usuario usuario = buscarUsuario(nombre);

        if (usuario == null) {
            throw new DatoInvalidoException(
                    "\nError: No se ha encontrado ningún usuario registrado con el nombre " + nombre + ".\n");
        }

        System.out.print(" - Introduce la contraseña: ");
        String contrasenya = entrada.nextLine();

        if (!usuario.getContrasenya().equalsIgnoreCase(contrasenya)) {
            throw new DatoInvalidoException("\nError: La contrasenya introducida es incorrecta.\n");
        }

        System.out.print(" - Confirma la contraseña introducida: ");
        String confirmacion = entrada.nextLine();

        if (!usuario.getContrasenya().equalsIgnoreCase(confirmacion)) {
            throw new DatoInvalidoException("\nError: La contrasenya introducida no coincide.\n");
        }
        return usuario;
    }

    public Usuario buscarUsuario(String nombreCompleto) {
        for (Usuario u : usuarios) {
            if (u.nombreCompleto().equalsIgnoreCase(nombreCompleto)) {
                return u;
            }
        }
        return null;
    }

}
