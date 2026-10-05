package com.SELOB.servlets;

import java.io.Serializable;

public class estudiante implements Serializable{
    String nombre;
    int matricula;
    String email;
    String contrasena;
    String facultad;
    String publicaciones;
    publicacion[] posts; 

    public estudiante(String nombre, int matricula, String email, String contrasena) {
        this.nombre = nombre;
        this.matricula = matricula;
        this.email = email;
        this.contrasena = contrasena;
    }

    public estudiante(String nombre, int matricula, String email, String contrasena, String facultad, String publicaciones, publicacion[] posts) {
        this.nombre = nombre;
        this.matricula = matricula;
        this.email = email;
        this.contrasena = contrasena;
        this.facultad = facultad;
        this.publicaciones = publicaciones;
        this.posts=posts;
    }
    
    public estudiante(String nombre, int matricula, String email, String contrasena, String facultad, String publicaciones) {
        this.nombre = nombre;
        this.matricula = matricula;
        this.email = email;
        this.contrasena = contrasena;
        this.facultad = facultad;
        this.publicaciones = publicaciones;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(String publicaciones) {
        this.publicaciones = publicaciones;
    }

    public publicacion[] getPosts() {
        return posts;
    }

    public void setPosts(publicacion[] posts) {
        this.posts = posts;
    }
   
    

    @Override
    public String toString() {
        return "estudiante{" + "nombre=" + nombre + ", matricula=" + matricula + ", email=" + email + ", contrasena=" + contrasena + ", facultad=" + facultad + ", publicaciones=" + publicaciones + '}';
    }
    
    
}
