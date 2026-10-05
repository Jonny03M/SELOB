package com.SELOB.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@MultipartConfig
@WebServlet(name = "svMakePub", urlPatterns = {"/svMakePub"})
public class svMakePub extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Me permite recuperar los datos del perfil que esta haciendo la publicacion
        HttpSession session = request.getSession();
        estudiante perfil = (estudiante) session.getAttribute("perfilActual");

        //Datos recibidos del formulario
        String nombre = request.getParameter("nombre");
        String matricula = request.getParameter("matricula");
        String facultad = request.getParameter("facultad");
        String descripcion = request.getParameter("descripcion");
        String nvalor = "";

        // Obtener el tamaño máximo permitido en bytes (64 KB = 64 * 1024 bytes)
        final int MAX_SIZE_BYTES = 64 * 1024;
        Part archivoPart = request.getPart("imagen");

        if (perfil.getPublicaciones().equals("No Info")) {
            nvalor = (matricula + ",");
            perfil.setPublicaciones("");
        } else {
            nvalor += (perfil.getPublicaciones() + matricula + ",");
        }

        //Varificamos que los campos esten llenos 
        int m;
        if ((archivoPart != null)){ //|| (archivoPart.getSize() <= MAX_SIZE_BYTES)) {
            if (nombre.isEmpty() || matricula.isEmpty() || facultad.isEmpty() || descripcion.isEmpty()) {
                response.sendRedirect("makePublication.jsp?mensaje=Por+favor+completa+todos+los+campos");
            } else {
                if ((matricula.length()) != 9) {
                    response.sendRedirect("makePublication.jsp?mensaje=Por+favor+ingresa+una+matricula+valida");
                } else {
                    if (matricula.matches("[0-9]+")) { //comprobamos que la metricula solo tenga numeros
                        m = Integer.parseInt(matricula);
                        if ((m >= 201000000) && (m < ((LocalDate.now().getYear()) + 1) * 100000)) {

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
                                String orden = "INSERT INTO publicaciones (nombreCred, matriculaCred, facultadCred,descripcion,matriculaAutor,nombreAutor,correoAutor,foto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                                try ( PreparedStatement preparedStatement = Conexion.prepareStatement(orden)) {
                                    preparedStatement.setString(1, nombre);
                                    preparedStatement.setInt(2, m);
                                    preparedStatement.setString(3, facultad);
                                    preparedStatement.setString(4, descripcion);
                                    preparedStatement.setInt(5, perfil.getMatricula());
                                    preparedStatement.setString(6, perfil.getNombre());
                                    preparedStatement.setString(7, perfil.getEmail());
                                    preparedStatement.setBytes(8, imagenBytes);
                                    preparedStatement.executeUpdate(); // Ejecutar la inserción
                                    // Cierra los flujos y la conexión a la base de datos
                                    outputStream.close();
                                    inputStream.close();
                                    preparedStatement.close();
                                    Conexion.close(); // Cerrar la conexión
                                    sentenciaSQL.close(); // Cerrar la sentencia
                                    //-------------------------------------------------------------------
                                    //--------------------- ACTUALIZAR POSTEOS ------------------------- 
                                    //------------------------------------------------------------------
                                    try {
                                        Class.forName("com.mysql.cj.jdbc.Driver");
                                        Conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/selob", "root", "");

                                        // Consulta SQL para actualizar el campo 'publicacion' basado en la matrícula
                                        String consulta = "UPDATE cuentasregistradas SET publicacion = ? WHERE matricula = ?";

                                        // Crear la sentencia preparada
                                        PreparedStatement pstmt = Conexion.prepareStatement(consulta);

                                        // Establecer los valores para la sentencia preparada
                                        pstmt.setString(1, nvalor);
                                        pstmt.setInt(2, perfil.getMatricula());

                                        // Ejecutar la actualización
                                        int filasActualizadas = pstmt.executeUpdate();

                                        // Cerrar la conexión y liberar recursos
                                        pstmt.close();
                                        Conexion.close();
                                        perfil.setPublicaciones(nvalor);
                                        System.out.println("Quedo Listo");

                                    } catch (Exception e) {
                                        response.sendRedirect("makePublication.jsp?mensaje=Error+no+se+pudo+concectar+a+la+base+de+datos");
                                    }
                                    System.out.println("ACABARON LAS INCERSIONES");
                                    publicacion PubAux = new publicacion(nombre, m, facultad, descripcion, perfil.getNombre(), perfil.getMatricula(), perfil.getEmail());
                                    publicacion[] posts=new publicacion[0];
                                    if(perfil.getPosts().length==0){
                                        posts=new publicacion[1];
                                        posts[0]=PubAux;
                                    }else{
                                        posts = agregarObjeto(perfil.getPosts(), PubAux);
                                    }
                                    System.out.println("SE CREO EL POSTS");
                                    estudiante perfilAct = new estudiante(perfil.getNombre(), perfil.getMatricula(), perfil.getEmail(), perfil.getContrasena(), perfil.getFacultad(), nvalor, posts);
                                    session.setAttribute("perfilActual", perfilAct);
                                    System.out.println("SE ACTUALIZO EL PERFIL");
                                    //response.sendRedirect("profileView.jsp?mensaje=Publicación+exitosa");
                                    response.sendRedirect("profileView.jsp");

                                } catch (Exception e) {
                                    response.sendRedirect("makePublication.jsp?mensaje=ErrorPub+no+se+pudo+concectar+a+la+base+de+datos");
                                    e.printStackTrace(); // Proporciona detalles del error
                                }

                            } catch (ClassNotFoundException e) {
                                response.sendRedirect("makePublication.jsp?menseje=ErrorPer+no+se+pudo+concectar+a+la+base+de+datos");
                            } catch (SQLException ex) {
                                Logger.getLogger(svMakePub.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else {
                            response.sendRedirect("makePublication.jsp?mensaje=Por+favor+ingresa+una+matricula+valida");
                        }
                    } else {
                        response.sendRedirect("makePublication.jsp?mensaje=Por+favor+ingresa+una+matricula+valida");
                    }
                }
            }

        } else {
            response.sendRedirect("makePublication.jsp?mensaje=Seleccione+un+archivo+menor+a+(64+KB)");
        }

    }

    public static publicacion[] agregarObjeto(publicacion[] arregloExistente, publicacion nuevoObjeto) {
        // Crear un nuevo arreglo con un tamaño mayor
        publicacion[] nuevoArreglo = new publicacion[arregloExistente.length + 1];

        // Copiar los elementos existentes al nuevo arreglo
        System.arraycopy(arregloExistente, 0, nuevoArreglo, 0, arregloExistente.length);

        // Añadir el nuevo objeto al final del nuevo arreglo
        nuevoArreglo[arregloExistente.length] = nuevoObjeto;

        // Devolver el nuevo arreglo
        return nuevoArreglo;
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
