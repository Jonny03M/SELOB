package com.SELOB.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import javax.servlet.http.HttpSession;

@WebServlet(name = "svInicioSesion", urlPatterns = {"/svInicioSesion"})
public class svInicioSesion extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email"); // Recuperar el parámetro
	String contrasena = request.getParameter("contrasena"); // Recuperar el parámetro
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection Conexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/selob","root","");
            Statement SentenciaSQL = Conexion.createStatement();
            String orden="SELECT * FROM cuentasregistradas WHERE email='"+email+"' AND contrasena='"+contrasena+"'";
            ResultSet cuentaExistente = SentenciaSQL.executeQuery(orden);
            if(cuentaExistente.next()){
                String nombre = cuentaExistente.getString("nombre");
                int matricula = cuentaExistente.getInt("matricula");
                String facultad = cuentaExistente.getString("facultad");
                String publicacion = cuentaExistente.getString("publicacion");
                HttpSession session = request.getSession();
                estudiante perfil = new estudiante(nombre,matricula,email,contrasena, facultad, publicacion);
                publicacion[] posts=new publicacion[0];
                if(publicacion.equals("No Info")){
                }else{
                    posts=obtenerPublicaciones(publicacion);
                    System.out.println("INICIO EXITOSO"+perfil.toString());
                }
                perfil = new estudiante(nombre,matricula,email,contrasena, facultad, publicacion,posts);
                session.setAttribute("perfilActual", perfil);
                Conexion.close();
                SentenciaSQL.close();
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Conexion= DriverManager.getConnection("jdbc:mysql://localhost:3306/selob", "root", "");
                    SentenciaSQL = Conexion.createStatement();
                    orden = "SELECT * FROM publicaciones WHERE matriculaCred="+perfil.getMatricula();
                    ResultSet publicaciones = SentenciaSQL.executeQuery(orden);
                    if(publicaciones.next()){
                        String nombreCred = publicaciones.getString("nombreCred");
                        int matriculaCred = publicaciones.getInt("matriculaCred");
                        String facultadCred = publicaciones.getString("facultadCred");
                        String descripcion = publicaciones.getString("descripcion");
                        int matriculaAutor = publicaciones.getInt("matriculaAutor");
                        String nombreAutor = publicaciones.getString("nombreAutor");
                        String correoAutor = publicaciones.getString("correoAutor");
                        publicacion encontrada = new publicacion(nombreCred,matriculaCred,facultadCred,descripcion,nombreAutor,matriculaAutor,correoAutor);
                        session.setAttribute("publicacionEncontrada", encontrada);
                        System.out.println("SE ENCOTRO LA PUB: "+encontrada.toString());
                        response.sendRedirect("verification.jsp");
                    }else{
                        response.sendRedirect("verificationFail.jsp");
                    }
                    Conexion.close();
                    SentenciaSQL.close();
                } catch (ClassNotFoundException e) {
                    System.out.println("Clase no encontrada");
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }else{
                response.sendRedirect("index.jsp");
            }
        }catch (ClassNotFoundException e) { // Manejar excepciones
            System.out.println("Clase no encontrada");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error de SQL: " + e.getMessage());
        }
    }
    
    publicacion[] obtenerPublicaciones(String textoConcatenado){
        int[] listaPublicaciones = obtenerMatriculas(textoConcatenado);
        if(listaPublicaciones.length==0){
            return new publicacion[0];
        }else{
            publicacion[] publicaciones = new publicacion[listaPublicaciones.length];
            int i;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection Conexion= DriverManager.getConnection("jdbc:mysql://localhost:3306/selob", "root", "");
                Statement SentenciaSQL = Conexion.createStatement();
                for(i=0;i<listaPublicaciones.length;i++){
                    String orden = "SELECT * FROM publicaciones WHERE matriculaCred="+listaPublicaciones[i];
                    ResultSet publicacionesEncontradas = SentenciaSQL.executeQuery(orden);
                    if(publicacionesEncontradas.next()){
                        String nombreCred = publicacionesEncontradas.getString("nombreCred");
                        int matriculaCred = publicacionesEncontradas.getInt("matriculaCred");
                        String facultadCred = publicacionesEncontradas.getString("facultadCred");
                        String descripcion = publicacionesEncontradas.getString("descripcion");
                        int matriculaAutor = publicacionesEncontradas.getInt("matriculaAutor");
                        String nombreAutor = publicacionesEncontradas.getString("nombreAutor");
                        String correoAutor = publicacionesEncontradas.getString("correoAutor");
                        publicacion encontrada = new publicacion(nombreCred,matriculaCred,facultadCred,descripcion,nombreAutor,matriculaAutor,correoAutor);
                        publicaciones[i]=encontrada;
                    }
                }
                Conexion.close();
                SentenciaSQL.close();
            } catch (ClassNotFoundException e) {
                System.out.println("Clase no encontrada");
            } catch (SQLException e) {
                System.out.println(e);
            }
            return publicaciones;
        }
    }

    public static int[] obtenerMatriculas(String textoConcatenado){
        if(textoConcatenado.equals("No Info")){
            return new int[0];
        }else{
            String[] matriculas = textoConcatenado.split(",");
            int[] matriculasInt = new int[matriculas.length];
            for(int i=0;i<matriculas.length;i++){
                try{
                    matriculasInt[i]=Integer.parseInt(matriculas[i]);
                }catch(NumberFormatException e){System.out.println("ERROR EN LA CONVERSION: "+e);}
            }    
            return matriculasInt;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
