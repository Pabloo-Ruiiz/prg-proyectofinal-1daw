package com.projecte.marc;

import java.io.Serializable;
import java.time.LocalDate;

public class Usuario implements Serializable {

    // Enum para definir los tipos de usuario
    enum Rol {
        ROL_USUARIO, ROL_ADMIN;
    }

    // Contador estático para generar IDs automáticos
    private static int contador = 0;
    
    // Atributos
    private int id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String contrasenya;
    private String poblacion;
    private Rol rol;
    private LocalDate fechaNacimiento;

    //Constructor
    public Usuario(String nombre, String apellidos, String correo, String contrasenya, String poblacion, Rol rol,
            LocalDate fechaNacimiento) {
        // Incrementa el contador y asigna un ID único
        contador++;
        this.id = contador;

        // Inicialización de atributos
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contrasenya = contrasenya;
        this.poblacion = poblacion;
        this.rol = rol;
        this.fechaNacimiento = fechaNacimiento;
    }

    //Getters i Setters
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
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    //Genera un identificador para el usuario
    public String identificador() {
        String [] partes = correo.split("@");
        return this.id + "-" + partes[0];
    }

    //Devuelve el nombre y los apellidos del usuario
    public String nombreCompleto() {
        return this.nombre + " " + this.apellidos;
    }

    //Devuelve si el usuario es administrador
    public boolean esAdmin() {
        return this.rol == Rol.ROL_ADMIN;
    }

    //Devuelve la edad exacta del usuario
    public int calcularEdad() {
        LocalDate hoy = LocalDate.now();
        int edad = hoy.getYear() - this.fechaNacimiento.getYear();

        if (hoy.getMonthValue() < this.fechaNacimiento.getMonthValue() || 
            //O bien estamos en el mismo mes que nacio
            (hoy.getMonthValue() == this.fechaNacimiento.getMonthValue()) &&
            //Y ademas el dia de hoy es anterior al dia que nacio
            hoy.getDayOfMonth() < this.fechaNacimiento.getDayOfMonth()) {
            edad--;
        }
        return edad;
    }

    //Devuelve el correo en minúsculas
    public String correoNormalizado() {
        return this.correo.toLowerCase();
    }

    //Devuelve las iniciales del usuario
    public String obtenerIniciales() {
        return "" + this.nombre.charAt(0) + this.apellidos.charAt(0);
    }

    //toString
    @Override
    public String toString() {
        return this.id + "  | " + nombreCompleto() + " | " + calcularEdad() + " | " + correoNormalizado() + " | " + this.poblacion + " | " + this.rol + " | " + this.fechaNacimiento;
    }

}
