package com.SELOB.servlets;

import static com.SELOB.servlets.svMakePub.agregarObjeto;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
@MultipartConfig
@WebServlet(name = "svGuest", urlPatterns = {"/svGuest"})
public class svGuest extends HttpServlet {

    public static boolean validarCorreo(String correo) {
        // Patrón para validar el formato de un correo electrónico
        String patternString = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(correo);

        return matcher.matches();
    }

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

        //Datos recibidos de la credencial
        String nombre = request.getParameter("nombre");
        String matricula = request.getParameter("matricula");
        String facultad = request.getParameter("facultad");
        String descripcion = request.getParameter("descripcion");
        //Datos del que la encontro
        String nombre2 = request.getParameter("nombre2");
        String correo = request.getParameter("correo");
        //Varificamos que los campos esten llenos
        // Obtener el tamaño máximo permitido en bytes (64 KB = 64 * 1024 bytes)
        final int MAX_SIZE_BYTES = 100 * 1024;   int m;
        Part archivoPart = request.getPart("imagen");
        if ((archivoPart == null)  ) {
            response.sendRedirect("guest.jsp?mensaje=Seleccione+un+archivo+menor+a+(64+KB)");
            
        }else{
            if (nombre.isEmpty() || matricula.isEmpty() || facultad.isEmpty() || descripcion.isEmpty()) {
                response.sendRedirect("guest.jsp?mensaje=Por+favor+completa+todos+los+campos");
            } else {
                if ((matricula.length()) != 9) {
                    response.sendRedirect("guest.jsp?mensaje=Por+favor+ingresa+una+matricula+valida");
                } else {
                    System.out.println("Entro en la condicion");
                    if (matricula.matches("[0-9]+")) { //comprobamos que la metricula solo tenga numeros
                        m = Integer.parseInt(matricula);
                        if ((m >= 201000000) && (m < ((LocalDate.now().getYear()) + 1) * 100000)) {
                            System.out.println("Vamos a crear la publicacion");
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
                                    preparedStatement.setInt(5,Integer.parseInt(matricula));
                                    preparedStatement.setString(6, nombre2);
                                    preparedStatement.setString(7, correo);
                                    preparedStatement.setBytes(8, imagenBytes);
                                    preparedStatement.executeUpdate(); // Ejecutar la inserción
                                    // Cierra los flujos y la conexión a la base de datos
                                    outputStream.close();
                                    inputStream.close();
                                    preparedStatement.close();
                                    Conexion.close(); // Cerrar la conexión
                                    sentenciaSQL.close(); // Cerrar la sentencia
                                    response.sendRedirect("index.jsp");
                                } catch (Exception e) {
                                    response.sendRedirect("guest.jsp?mensaje=ErrorPub+no+se+pudo+concectar+a+la+base+de+datos");
                                }

                            } catch (ClassNotFoundException e) {
                                response.sendRedirect("guest.jsp?menseje=ErrorPer+no+se+pudo+concectar+a+la+base+de+datos");
                            } catch (SQLException ex) {
                                Logger.getLogger(svMakePub.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else {
                            response.sendRedirect("guest.jsp?mensaje=Por+favor+ingresa+una+matricula+valida");
                        }
                    } else {
                        response.sendRedirect("guest.jsp?mensaje=Por+favor+ingresa+una+matricula+valida");
                    }
                }
            }

        }
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
