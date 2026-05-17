package com.projecte.marc;

import com.projecte.pablo.Actor;
import com.projecte.pablo.Catalogo;
import com.projecte.pablo.Director;
import com.projecte.pablo.Pelicula;
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
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Usuario implements Serializable {

    // Enum para definir los tipos de usuario
    public enum Rol {
        ROL_USUARIO, ROL_ADMIN;
    }

    // Contador estático para generar IDs automáticos
    protected static int contador = 0;

    // Atributos
    private int id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String contrasenya;
    private String poblacion;
    private Rol rol;
    private LocalDate fechaNacimiento;

    private transient ArrayList<Pelicula> peliculas;
    private transient ArrayList<Director> directores;
    private transient ArrayList<Actor> actores;

    // Constructor
    public Usuario(String nombre, String apellidos, String correo, String contrasenya, String poblacion, Rol rol,
            LocalDate fechaNacimiento) {
        // Incrementa el contador y asigna un ID único
        contador++;
        this.id = contador;

        // Valida los datos mediante setters.
        setNombre(nombre);
        setApellidos(apellidos);
        setCorreo(correo);
        setContrasenya(contrasenya);
        setPoblacion(poblacion);
        setRol(rol);
        setFechaNacimiento(fechaNacimiento);

        this.peliculas = new ArrayList<Pelicula>();
        this.directores = new ArrayList<Director>();
        this.actores = new ArrayList<Actor>();

        cargarDatosParticularesPeliculas();
        cargarDatosParticularesDirectores();
        cargarDatosParticularesActores();
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {

        in.defaultReadObject();

        this.peliculas = new ArrayList<Pelicula>();
        this.directores = new ArrayList<Director>();
        this.actores = new ArrayList<Actor>();
    }

    // Getters i Setters
    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Usuario.contador = contador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.isEmpty()) {
            this.nombre = nombre;
        } else {
            throw new DatoInvalidoException("\nEl nombre no puede estar vacio.\n");
        }
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        if (apellidos != null && !apellidos.isEmpty()) {
            this.apellidos = apellidos;
        } else {
            throw new DatoInvalidoException("\nLos apellidos no pueden estar vacios.\n");
        }
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        if (correo != null && !correo.isEmpty()) {
            this.correo = correo;
        } else {
            throw new DatoInvalidoException("\nEl correo no puede estar vacio.\n");
        }
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        if (contrasenya != null && !contrasenya.isEmpty()) {
            this.contrasenya = contrasenya;
        } else {
            throw new DatoInvalidoException("\nLa contraseña no puede estar vacia.\n");
        }
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        if (poblacion != null && !poblacion.isEmpty()) {
            this.poblacion = poblacion;
        } else {
            throw new DatoInvalidoException("\nLa poblacion no puede estar vacia.\n");
        }
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        if (rol != null) {
            this.rol = rol;
        } else {
            throw new DatoInvalidoException("\nEl rol no puede ser null.\n");
        }
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new DatoInvalidoException("\nLa fecha de nacimiento no puede ser null.\n");
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new DatoInvalidoException("\nLa fecha de nacimiento no puede ser futura.\n");
        }

        this.fechaNacimiento = fechaNacimiento;
    }

    // Genera un identificador para el usuario
    public String identificador() {
        String[] partes = correo.split("@");
        return this.id + "-" + partes[0];
    }

    // Devuelve el nombre y los apellidos del usuario
    public String nombreCompleto() {
        return this.nombre + " " + this.apellidos;
    }

    // Devuelve si el usuario es administrador
    public boolean esAdmin() {
        return this.rol == Rol.ROL_ADMIN;
    }

    // Devuelve la edad exacta del usuario
    public int calcularEdad() {
        LocalDate hoy = LocalDate.now();
        int edad = hoy.getYear() - this.fechaNacimiento.getYear();

        if (hoy.getMonthValue() < this.fechaNacimiento.getMonthValue() ||
        // O bien estamos en el mismo mes que nacio
                (hoy.getMonthValue() == this.fechaNacimiento.getMonthValue()) &&
                // Y ademas el dia de hoy es anterior al dia que nacio
                        hoy.getDayOfMonth() < this.fechaNacimiento.getDayOfMonth()) {
            edad--;
        }
        return edad;
    }

    // Devuelve el correo en minúsculas
    public String correoNormalizado() {
        return this.correo.toLowerCase();
    }

    // Devuelve las iniciales del usuario
    public String obtenerIniciales() {
        return "" + this.nombre.charAt(0) + this.apellidos.charAt(0);
    }

    // Crea carpeta personalizada para un usuario.
    public void crearCarpetaUsuario() throws IOException {
        File directori = new File(identificador());

        if (directori.mkdir()) {
            // Carpeta creada correctamente
        } else {
            if (!directori.exists()) {
                throw new IOException("\nLa carpeta no se pudo crear.\n");
            }
        }
    }

    public void cargarDatosParticularesPeliculas() {

        File file = new File(identificador() + "/peliculas.lista");

        if (file.exists()) {

            try (ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(identificador() + "/peliculas.lista"));) {

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

        } else {
            try {
                crearCarpetaUsuario();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("\n" + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
    }

    public void cargarDatosParticularesDirectores() {

        File file = new File(identificador() + "/directores.lista");

        if (file.exists()) {

            try (ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(identificador() + "/directores.lista"));) {

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

        } else {
            try {
                crearCarpetaUsuario();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("\n" + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
    }

    public void cargarDatosParticularesActores() {

        File file = new File(identificador() + "/actores.lista");

        if (file.exists()) {

            try (ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(identificador() + "/actores.lista"));) {

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

        } else {
            try {
                crearCarpetaUsuario();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("\n" + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
    }

    public void guardarDatosParticularesPeliculas() {

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(identificador() + "/peliculas.lista"));) {

            for (Pelicula p : peliculas) {
                out.writeObject(p.resumen());
            }

        } catch (IOException e) {
            System.out.println("\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }

    }

    public void guardarDatosParticularesDirectores() {

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(identificador() + "/directores.lista"));) {

            for (Director d : directores) {
                out.writeObject(d.resumen());
            }

        } catch (IOException e) {
            System.out.println("\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }

    }

    public void guardarDatosParticularesActores() {

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(identificador() + "/actores.lista"));) {

            for (Actor a : actores) {
                out.writeObject(a.resumen());
            }

        } catch (IOException e) {
            System.out.println("\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }

    }

    public boolean mostrarDatosParticularesPeliculas() {
        if (peliculas.isEmpty()) {
            System.out.println("\nTu catalogo de peliculas esta vacio.\n");
            return true;
        }

        for (Pelicula p : peliculas) {
            System.out.println(" - " + p.toString());
        }
        return false;
    }

    public boolean mostrarDatosParticularesDirectores() {
        if (directores.isEmpty()) {
            System.out.println("\nTu catalogo de directores esta vacio.\n");
            return true;
        }

        for (Director d : directores) {
            System.out.println(" - " + d.toString());
        }
        return false;
    }

    public boolean mostrarDatosParticularesActores() {
        if (actores.isEmpty()) {
            System.out.println("\nTu catalogo de actores esta vacio.\n");
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

    public void eliminarPelicula(Pelicula p) {
        peliculas.remove(p);
    }

    public void eliminarDirector(Director d) {
        directores.remove(d);
    }

    public void eliminarActor(Actor a) {
        actores.remove(a);
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

    public void actualizarListas(Catalogo c) {
        if (peliculas != null) {
            for (Pelicula p : peliculas) {
                if (c.existePelicula(p.getTitulo()) == null) {
                    peliculas.remove(p);
                }
            }
        }
        if (directores != null) {
            for (Director d : directores) {
                if (c.existeDirector(d.nombreCompleto()) == null) {
                    directores.remove(d);
                }
            }
        }
        if (actores != null) {
            for (Actor a : actores) {
                if (c.existeDirector(a.nombreCompleto()) == null) {
                    directores.remove(a);
                }
            }
        }

    }

    public void mostrarOrdenacionPeliculas() {
        this.mostrarOrdenacionPeliculas(null);
    }

    // Muestra las películas después de ordenar
    public void mostrarOrdenacionPeliculas(Comparator<Pelicula> comparator) {
        if (comparator == null) {
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

    public FiltrarPeliculas getFiltrarPeliculas(Genero genero, int duracion) {
        return new FiltrarPeliculas(peliculas, genero, duracion);
    }

    // toString
    @Override
    public String toString() {
        return this.id + "  | " + nombreCompleto() + " | " + calcularEdad() + " | " + this.fechaNacimiento + " | "
                + correoNormalizado() + " | " + this.poblacion + " | " + this.rol;
    }

}
