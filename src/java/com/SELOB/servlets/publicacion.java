package com.SELOB.servlets;

import java.io.Serializable;

public class publicacion implements Serializable{
    String nombreCred;
    int matriculaCred;
    String facultadCred;
    String descripcion;
    String nombreAutor;
    int matriculaAutor;
    String correoAutor;

    public publicacion(String nombreCred, int matriculaCred, String facultadCred, String descripcion, String nombreAutor, int matriculaAutor, String correoAutor) {
        this.nombreCred = nombreCred;
        this.matriculaCred = matriculaCred;
        this.facultadCred = facultadCred;
        this.descripcion = descripcion;
        this.nombreAutor = nombreAutor;
        this.matriculaAutor = matriculaAutor;
        this.correoAutor = correoAutor;
    }
    

    public String getNombreCred() {
        return nombreCred;
    }

    public void setNombreCred(String nombreCred) {
        this.nombreCred = nombreCred;
    }

    public int getMatriculaCred() {
        return matriculaCred;
    }

    public void setMatriculaCred(int matriculaCred) {
        this.matriculaCred = matriculaCred;
    }

    public String getFacultadCred() {
        return facultadCred;
    }

    public void setFacultadCred(String facultadCred) {
        this.facultadCred = facultadCred;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public int getMatriculaAutor() {
        return matriculaAutor;
    }

    public void setMatriculaAutor(int matriculaAutor) {
        this.matriculaAutor = matriculaAutor;
    }

    public String getCorreoAutor() {
        return correoAutor;
    }

    public void setCorreoAutor(String correoAutor) {
        this.correoAutor = correoAutor;
    }

    @Override
    public String toString() {
        return "publicacion{nombreCred=" + nombreCred + ", matriculaCred=" + matriculaCred + ", facultadCred=" + facultadCred + ", descripcion=" + descripcion + ", nombreAutor=" + nombreAutor + ", matriculaAutor=" + matriculaAutor + ", correoAutor=" + correoAutor + '}';
    }
    
    
}
