package com.projecte.pablo;

import com.projecte.utils.DatoInvalidoException;
import com.projecte.utils.Gestionable;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa a un actor con sus datos personales.
 *
 * Esta clase es serializable para poder guardarla en fichero y forma parte del catálogo.
 */
public class Actor implements Serializable, Gestionable, Comparable<Actor> {

    // Contador estático para generar IDs automáticos
    protected static int contador = 0;

    // Atributos
    private int id;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String nacionalidad;

    /**
     * Crea un nuevo actor con los datos proporcionados.
     *
     * @param nombre nombre del actor.
     * @param apellidos apellidos del actor.
     * @param fechaNacimiento fecha de nacimiento del actor.
     * @param nacionalidad nacionalidad del actor.
     */
    public Actor(String nombre, String apellidos, LocalDate fechaNacimiento, String nacionalidad) {
        contador++;
        this.id = contador;

        // Valida los datos mediante setters.
        setNombre(nombre);
        setApellidos(apellidos);
        setFechaNacimiento(fechaNacimiento);
        setNacionalidad(nacionalidad);
    }

    // Getters y Setters
    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Actor.contador = contador;
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
            throw new DatoInvalidoException("\nINFORMACION: El nombre del director no puede estar vacío.\n");
        }
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        if (apellidos != null && !apellidos.isEmpty()) {
            this.apellidos = apellidos;
        } else {
            throw new DatoInvalidoException("\nINFORMACION: Los apellidos del director no pueden estar vacíos.\n");
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

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        if (nacionalidad != null && !nacionalidad.isEmpty()) {
            this.nacionalidad = nacionalidad;
        } else {
            throw new DatoInvalidoException("\nINFORMACION: La nacionalidad no puede estar vacía.\n");
        }
    }

    // Devuelve el nombre y los apellidos del usuario
    public String nombreCompleto() {
        return this.nombre + " " + this.apellidos;
    }

    // Devuelve las iniciales del usuario
    public String obtenerIniciales() {
        return "" + this.nombre.charAt(0) + this.apellidos.charAt(0);
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

    @Override
    public String getIdentificador() {
        return id + "-" + nombre;
    }

    @Override
    public String resumen() {
        return this.id + " | " + nombreCompleto() + " | " + calcularEdad() + " | " + this.fechaNacimiento + " | "
                + this.nacionalidad;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("""
                ==============================
                          FICHA ACTOR
                ==============================
                ID: %d
                Nombre: %s
                Edad: %d
                Fecha nacimiento: %s
                Nacionalidad: %s
                ==============================
                """.formatted(
                id,
                nombreCompleto(),
                calcularEdad(),
                fechaNacimiento,
                nacionalidad));
    }

    /**
     * Ordenación natural de actores por su identificador numérico.
     *
     * @param o actor con el que se compara.
     * @return negativo si este actor tiene id menor, cero si son iguales, positivo si es mayor.
     */
    @Override
    public int compareTo(Actor o) {
        if (Integer.compare(id, o.getId()) == 0) {
            return 0;
        } else if (Integer.compare(id, o.getId()) < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    // toString
    @Override
    public String toString() {
        return getIdentificador() + " [ " + this.id + " | " + nombreCompleto() + " | " + calcularEdad() + " | " + this.fechaNacimiento
                + " | " + this.nacionalidad + " ]";
    }

}
