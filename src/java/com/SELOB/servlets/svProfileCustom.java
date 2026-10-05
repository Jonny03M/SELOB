package com.SELOB.servlets;

import java.io.ByteArrayOutputStream;
import java.sql.*;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@MultipartConfig
@WebServlet(name = "svProfileCustom", urlPatterns = {"/svProfileCustom"})
public class svProfileCustom extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        estudiante perfil = (estudiante) session.getAttribute("perfilActual");

        String nombre = perfil.getNombre();
        String contrasena = request.getParameter("contrasena");
        if(contrasena.isEmpty()){
            contrasena=perfil.getContrasena();
        }
        String email = perfil.getEmail();
        int matricula = perfil.getMatricula();
        String facultad = request.getParameter("facultad");
        if(facultad.isEmpty()){
            facultad=perfil.getFacultad();
        }
        String publicacion = perfil.getPublicaciones();
        publicacion[] posts = perfil.getPosts();
        Part archivoPart = request.getPart("imagen");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/selob", "root", "");

            // En este caso, se realiza una actualización en lugar de una inserción
            String orden = "UPDATE cuentasregistradas SET nombre=?, email=?, contrasena=?, facultad=?, publicacion=? WHERE matricula=?";

            try ( PreparedStatement preparedStatement = conexion.prepareStatement(orden)) {
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, contrasena);
                preparedStatement.setString(4, facultad);
                preparedStatement.setString(5, publicacion);
                preparedStatement.setInt(6, matricula);
                preparedStatement.executeUpdate();
            }
            conexion.close();
            estudiante perfilActualizado = new estudiante(nombre, matricula, email, contrasena, facultad, publicacion, posts);
            session.setAttribute("perfilActual", perfilActualizado);
            //-------------------------------------------------------------------------------------------------
            //--------------------------------- ESTO LO PUSO JONNY --------------------------------------------
            //--------------------------------------------------------------------------------------------------
            if ((archivoPart != null) && (archivoPart.getSize() > 0)) {
                
                //Busca si ya hay una foto de perfil y si si la elimina para colocar la nueva 
                Class.forName("com.mysql.cj.jdbc.Driver");
                try ( Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/selob", "root", "")) {
                    String consulta = "DELETE FROM imagenes WHERE matricula = ?";
                    try ( PreparedStatement preparedStatement = c.prepareStatement(consulta)) {
                        // Establecer el valor del parámetro (matricula)
                        preparedStatement.setInt(1, matricula);
                        // Ejecutar la consulta para eliminar el registro
                        int filasAfectadas = preparedStatement.executeUpdate();
                        preparedStatement.close();
                        c.close();
                    }
                }
                try {
                    //Convertimos la imagen en bytes 
                    InputStream inputStream = archivoPart.getInputStream();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    byte[] imagenBytes = outputStream.toByteArray();

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection Conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/selob", "root", "");
                    Statement sentenciaSQL = Conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    String orden2 = "INSERT INTO imagenes (matricula,foto) VALUES (?, ?)";
                    try ( PreparedStatement preparedStatement = Conexion.prepareStatement(orden2)) {
                        preparedStatement.setInt(1, matricula);
                        preparedStatement.setBytes(2, imagenBytes);
                        preparedStatement.executeUpdate(); // Ejecutar la inserción
                        // Cierra los flujos y la conexión a la base de datos
                        outputStream.close();
                        inputStream.close();
                        preparedStatement.close();
                        Conexion.close(); // Cerrar la conexión
                        sentenciaSQL.close(); // Cerrar la sentencia
                    } catch (Exception e) {}
                } catch (Exception e) {}
            } 
            response.sendRedirect("profileView.jsp");
        } catch (ClassNotFoundException e) {
            System.out.println("Clase no encontrada");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error de SQL: " + e.getMessage());
        }
    }

    publicacion[] obtenerPublicaciones(String textoConcatenado) {
        int[] listaPublicaciones = obtenerMatriculas(textoConcatenado);
        if (listaPublicaciones.length == 0) {
            return new publicacion[0];
        } else {
            publicacion[] publicaciones = new publicacion[listaPublicaciones.length];
            int i;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection Conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/selob", "root", "");
                Statement SentenciaSQL = Conexion.createStatement();
                for (i = 0; i < listaPublicaciones.length; i++) {
                    String orden = "SELECT * FROM publicaciones WHERE matriculaCred=" + listaPublicaciones[i];
                    ResultSet publicacionesEncontradas = SentenciaSQL.executeQuery(orden);
                    if (publicacionesEncontradas.next()) {
                        String nombreCred = publicacionesEncontradas.getString("nombreCred");
                        int matriculaCred = publicacionesEncontradas.getInt("matriculaCred");
                        String facultadCred = publicacionesEncontradas.getString("facultadCred");
                        String descripcion = publicacionesEncontradas.getString("descripcion");
                        int matriculaAutor = publicacionesEncontradas.getInt("matriculaAutor");
                        String nombreAutor = publicacionesEncontradas.getString("nombreAutor");
                        String correoAutor = publicacionesEncontradas.getString("correoAutor");
                        publicacion encontrada = new publicacion(nombreCred, matriculaCred, facultadCred, descripcion, nombreAutor, matriculaAutor, correoAutor);
                        publicaciones[i] = encontrada;
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

    public static int[] obtenerMatriculas(String textoConcatenado) {
        if (textoConcatenado.equals("No Info")) {
            return new int[0];
        } else {
            String[] matriculas = textoConcatenado.split(",");
            int[] matriculasInt = new int[matriculas.length - 1];
            for (int i = 0; i < matriculas.length - 1; i++) {
                try {
                    matriculasInt[i] = Integer.parseInt(matriculas[i]);
                } catch (NumberFormatException e) {
                    System.out.println("ERROR EN LA CONVERSION: " + e);
                }
            }
            return matriculasInt;
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
