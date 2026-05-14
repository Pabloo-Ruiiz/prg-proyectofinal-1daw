package com.projecte.main;

import com.projecte.marc.Acceso;
import com.projecte.marc.Usuario;
import com.projecte.pablo.Catalogo;

public class ProgramaPrincipal {

    public static void main(String[] args) {
        
        ProgramaPrincipal programa = new ProgramaPrincipal();
        programa.inici();
        
    }

    public void inici() {
        Usuario u = marc();
        Catalogo c = pablo(u);
        neil(u, c);
    }

    public Catalogo pablo(Usuario u) {
        Catalogo c = new Catalogo();
        c.inicio(u);
        return c;
    }

    public void neil(Usuario u, Catalogo c) {

        
    }

    public Usuario marc() {
        Acceso acceso = new Acceso();
        return acceso.inicio();
    }
    
}