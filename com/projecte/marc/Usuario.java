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

/**
 * Representa un usuario del sistema con listas particulares de películas,
 * directores y actores.
 *
 * Esta clase es serializable y gestiona la carga y el guardado de las listas
 * particulares en ficheros.
 */
public class Usuario implements Serializable {

    /**
     * Tipos de rol disponibles para un usuario.
     */
    public enum Rol {
        ROL_USUARIO, ROL_ADMIN;
    }

    private static final long serialVersionUID = 1L;

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

    /**
     * Construye un nuevo usuario con los datos personales y rol indicados.
     *
     * @param nombre          nombre del usuario.
     * @param apellidos       apellidos del usuario.
     * @param correo          correo electrónico del usuario.
     * @param contrasenya     contraseña del usuario.
     * @param poblacion       población del usuario.
     * @param rol             rol del usuario.
     * @param fechaNacimiento fecha de nacimiento del usuario.
     */
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

        try {
            crearCarpetaUsuario();
        } catch (IOException e) {
            System.out.println("\nERROR: " + e.getMessage() + "\n");
        }

        this.peliculas = new ArrayList<Pelicula>();
        this.directores = new ArrayList<Director>();
        this.actores = new ArrayList<Actor>();
    }

    /**
     * Restauración personalizada de la serialización del usuario.
     *
     * @param in objeto de entrada para lectura.
     * @throws IOException            si se produce un error de E/S.
     * @throws ClassNotFoundException si no se encuentra una clase durante la
     *                                lectura.
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {

        in.defaultReadObject();

        this.peliculas = new ArrayList<Pelicula>();
        this.directores = new ArrayList<Director>();
        this.actores = new ArrayList<Actor>();
        cargarDatosParticularesPeliculas();
        cargarDatosParticularesDirectores();
        cargarDatosParticularesActores();
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
            throw new DatoInvalidoException("\nINFORMACION: El nombre no puede estar vacio.\n");
        }
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        if (apellidos != null && !apellidos.isEmpty()) {
            this.apellidos = apellidos;
        } else {
            throw new DatoInvalidoException("\nINFORMACION: Los apellidos no pueden estar vacios.\n");
        }
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        if (correo != null && !correo.isEmpty()) {
            this.correo = correo;
        } else {
            throw new DatoInvalidoException("\nINFORMACION: El correo no puede estar vacio.\n");
        }
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        if (contrasenya != null && !contrasenya.isEmpty()) {
            this.contrasenya = contrasenya;
        } else {
            throw new DatoInvalidoException("\nINFORMACION: La contraseña no puede estar vacia.\n");
        }
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        if (poblacion != null && !poblacion.isEmpty()) {
            this.poblacion = poblacion;
        } else {
            throw new DatoInvalidoException("\nINFORMACION: La poblacion no puede estar vacia.\n");
        }
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        if (rol != null) {
            this.rol = rol;
        } else {
            throw new DatoInvalidoException("\nINFORMACION: El rol no puede ser null.\n");
        }
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new DatoInvalidoException("\nINFORMACION: La fecha de nacimiento no puede ser null.\n");
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new DatoInvalidoException("\nINFORMACION: La fecha de nacimiento no puede ser futura.\n");
        }

        this.fechaNacimiento = fechaNacimiento;
    }

    public ArrayList<Pelicula> getPeliculas() {
        return peliculas;
    }

    public ArrayList<Director> getDirectores() {
        return directores;
    }

    public ArrayList<Actor> getActores() {
        return actores;
    }

    /**
     * Genera un identificador único para el usuario usando su ID y la parte local
     * del correo.
     *
     * @return identificador de usuario.
     */
    public String identificador() {
        String[] partes = correo.split("@");
        return this.id + "-" + partes[0];
    }

    /**
     * Devuelve el nombre completo del usuario.
     *
     * @return nombre y apellidos concatenados.
     */
    public String nombreCompleto() {
        return this.nombre + " " + this.apellidos;
    }

    /**
     * Comprueba si el usuario tiene rol administrador.
     *
     * @return true si el usuario es administrador, false en caso contrario.
     */
    public boolean esAdmin() {
        return this.rol == Rol.ROL_ADMIN;
    }

    /**
     * Calcula la edad actual del usuario en años.
     *
     * @return edad del usuario.
     */
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

    /**
     * Normaliza el correo electrónico a minúsculas.
     *
     * @return correo en minúsculas.
     */
    public String correoNormalizado() {
        return this.correo.toLowerCase();
    }

    /**
     * Obtiene las iniciales del nombre y apellidos del usuario.
     *
     * @return iniciales del usuario.
     */
    public String obtenerIniciales() {
        return "" + this.nombre.charAt(0) + this.apellidos.charAt(0);
    }

    /**
     * Crea la carpeta personal del usuario si no existe.
     *
     * @throws IOException si la carpeta no se puede crear.
     */
    public void crearCarpetaUsuario() throws IOException {
        File directori = new File(identificador());

        if (directori.mkdir()) {
            // Carpeta creada correctamente
        } else {
            if (!directori.exists()) {
                throw new IOException("\nERROR: La carpeta no se pudo crear.\n");
            }
        }
    }

    /**
     * Carga las películas particulares del usuario desde su fichero de lista.
     */
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

    /**
     * Carga los directores particulares del usuario desde su fichero de lista.
     */
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

    /**
     * Carga los actores particulares del usuario desde su fichero de lista.
     */
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

    /**
     * Guarda las películas particulares del usuario en su fichero de lista.
     */
    public void guardarDatosParticularesPeliculas() {

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(identificador() + "/peliculas.lista"));) {

            for (Pelicula p : peliculas) {
                out.writeObject(p);
            }

        } catch (IOException e) {
            System.out.println("\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }

    }

    /**
     * Guarda los directores particulares del usuario en su fichero de lista.
     */
    public void guardarDatosParticularesDirectores() {

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(identificador() + "/directores.lista"));) {

            for (Director d : directores) {
                out.writeObject(d);
            }

        } catch (IOException e) {
            System.out.println("\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }

    }

    /**
     * Guarda los actores particulares del usuario en su fichero de lista.
     */
    public void guardarDatosParticularesActores() {

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(identificador() + "/actores.lista"));) {

            for (Actor a : actores) {
                out.writeObject(a);
            }

        } catch (IOException e) {
            System.out.println("\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }

    }

    /**
     * Muestra la lista particular de películas del usuario.
     *
     * @return true si la lista está vacía, false en caso contrario.
     */
    public boolean mostrarDatosParticularesPeliculas() {
        if (peliculas.isEmpty()) {
            System.out.println("\nINFORMACION: Tu catalogo de peliculas esta vacio.\n");
            return true;
        }

        for (Pelicula p : peliculas) {
            System.out.println(" - " + p.toString());
        }
        return false;
    }

    /**
     * Muestra la lista particular de directores del usuario.
     *
     * @return true si la lista está vacía, false en caso contrario.
     */
    public boolean mostrarDatosParticularesDirectores() {
        if (directores.isEmpty()) {
            System.out.println("\nINFORMACION: Tu catalogo de directores esta vacio.\n");
            return true;
        }

        for (Director d : directores) {
            System.out.println(" - " + d.toString());
        }
        return false;
    }

    /**
     * Muestra la lista particular de actores del usuario.
     *
     * @return true si la lista está vacía, false en caso contrario.
     */
    public boolean mostrarDatosParticularesActores() {
        if (actores.isEmpty()) {
            System.out.println("\nINFORMACION: Tu catalogo de actores esta vacio.\n");
            return true;
        }

        for (Actor a : actores) {
            System.out.println(" - " + a.toString());
        }
        return false;
    }

    /**
     * Busca una película en la lista particular del usuario.
     *
     * @param texto identificador de la película.
     * @return película encontrada o null si no existe.
     */
    public Pelicula buscarPelicula(String texto) {
        for (Pelicula p : peliculas) {
            if (p.getIdentificador().equalsIgnoreCase(texto)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Busca un director en la lista particular del usuario.
     *
     * @param id identificador del director.
     * @return director encontrado o null si no existe.
     */
    public Director buscarDirector(String id) {
        for (Director d : directores) {
            if (d.getIdentificador().equalsIgnoreCase(id)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Busca un actor en la lista particular del usuario.
     *
     * @param id identificador del actor.
     * @return actor encontrado o null si no existe.
     */
    public Actor buscarActor(String id) {
        for (Actor a : actores) {
            if (a.getIdentificador().equalsIgnoreCase(id)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Elimina una película de la lista particular del usuario.
     *
     * @param p película a eliminar.
     */
    public void eliminarPelicula(Pelicula p) {
        peliculas.remove(p);
    }

    /**
     * Elimina un director de la lista particular del usuario.
     *
     * @param d director a eliminar.
     */
    public void eliminarDirector(Director d) {
        directores.remove(d);
    }

    /**
     * Elimina un actor de la lista particular del usuario.
     *
     * @param a actor a eliminar.
     */
    public void eliminarActor(Actor a) {
        actores.remove(a);
    }

    /**
     * Añade una película a la lista particular del usuario.
     *
     * @param p película a añadir.
     */
    public void anyadirPelicula(Pelicula p) {
        peliculas.add(p);
    }

    /**
     * Añade un director a la lista particular del usuario.
     *
     * @param d director a añadir.
     */
    public void anyadirDirector(Director d) {
        directores.add(d);
    }

    /**
     * Añade un actor a la lista particular del usuario.
     *
     * @param a actor a añadir.
     */
    public void anyadirActor(Actor a) {
        actores.add(a);
    }

    /**
     * Elimina de las listas particulares los elementos que ya no existen en el
     * catálogo general.
     *
     * @param c catálogo general de referencia.
     */
    public void actualizarListas(Catalogo c) {
        if (peliculas != null) {
            Iterator<Pelicula> it = peliculas.iterator();
            while (it.hasNext()) {
                Pelicula p = it.next();
                if (c.existePelicula(p.getTitulo()) == null) {
                    it.remove();
                }
            }
        }
        if (directores != null) {
            Iterator<Director> it = directores.iterator();
            while (it.hasNext()) {
                Director d = it.next();
                if (c.existeDirector(d.nombreCompleto()) == null) {
                    it.remove();
                }
            }
        }
        if (actores != null) {
            Iterator<Actor> it = actores.iterator();
            while (it.hasNext()) {
                Actor a = it.next();
                if (c.existeActor(a.nombreCompleto()) == null) {
                    it.remove();
                }
            }
        }

    }

    /**
     * Muestra la lista de películas ordenada usando el orden natural de Pelicula.
     */
    public void mostrarOrdenacionPeliculas() {
        this.mostrarOrdenacionPeliculas(null);
    }

    /**
     * Muestra la lista de películas ordenada con el comparador proporcionado.
     *
     * @param comparator comparador para ordenar las películas; si es null usa el
     *                   orden natural.
     */
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

    /**
     * Crea un filtro para la lista particular de películas del usuario.
     *
     * @param genero   genero de búsqueda.
     * @param duracion duración máxima en minutos.
     * @return filtro de películas preparado.
     */
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
