package com.projecte.main;

import com.projecte.marc.Acceso;
import com.projecte.marc.Usuario;
import com.projecte.neil.Menu;
import com.projecte.pablo.Catalogo;

/**
 * Clase principal de la aplicación.
 *
 * Arranca el sistema de acceso, crea el catálogo general y lanza el menú principal.
 */
public class ProgramaPrincipal {

    public static void main(String[] args) {
        
        ProgramaPrincipal programa = new ProgramaPrincipal();
        programa.inici();
        
    }

    // Inicia el flujo principal del programa.
    public void inici() {
        Usuario u = marc();
        Catalogo c = pablo(u);
        neil(u, c);
    }

    public Catalogo pablo(Usuario u) {
        Catalogo c = new Catalogo();
        return c;
    }

    public void neil(Usuario u, Catalogo c) {
        Menu m = new Menu(u, c);
        m.inicio();
    }

    public Usuario marc() {
        Acceso acceso = new Acceso();
        return acceso.inicio();
    }
    
}