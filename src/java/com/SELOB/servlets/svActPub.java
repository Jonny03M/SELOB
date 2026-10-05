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
@WebServlet(name = "svActPub", urlPatterns = {"/svActPub"})
public class svActPub extends HttpServlet {

 
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
        publicacion pubSelecc = (publicacion) session.getAttribute("publicacionSeleccionada");

        //Datos recibidos del formulario
        String nombreAct = request.getParameter("nombre");
        String facultadAct = request.getParameter("facultad");
        String descripcionAct = request.getParameter("descripcion");
         Part archivoPart = request.getPart("imagen");
        
        int matriculaCred = pubSelecc.getMatriculaCred();
        
        // Validación de campos
if (nombreAct == null || nombreAct.trim().isEmpty()) {
    nombreAct = pubSelecc.getNombreCred();  // Mantener el valor existente si no se proporciona un nuevo nombre
}
if (facultadAct == null || facultadAct.trim().isEmpty()) {
    facultadAct = pubSelecc.getFacultadCred();  // Mantener el valor existente si no se proporciona una nueva facultad
}
if (descripcionAct == null || descripcionAct.trim().isEmpty()) {
    descripcionAct = pubSelecc.getDescripcion();  // Mantener el valor existente si no se proporciona una nueva descripción
}
       
        //Varificamos que los campos esten llenos 
        if (nombreAct.isEmpty() || facultadAct.isEmpty() || descripcionAct.isEmpty()) {
            response.sendRedirect("managePublication.jsp?mensaje=Por+favor+completa+todos+los+campos");
        }  
            //Aqui vamos a mandar la informacion de la base de datos
            //Actualizamos primero el perfil del usuario 
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection Conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/selob", "root", "");
                // Consulta SQL para actualizar el campo 'publicacion' basado en la matrícula      
                String consulta = "UPDATE publicaciones SET nombreCred = ?, facultadCred = ?, descripcion = ?, foto = ? WHERE matriculaCred = ?";
                // Crear la sentencia preparada202138543
                PreparedStatement pstmt = Conexion.prepareStatement(consulta);
                // Establecer los valores para la sentencia preparada
                pstmt.setString(1, nombreAct);
                pstmt.setString(2, facultadAct);
                pstmt.setString(3, descripcionAct);
                
                // Actualizar la imagen solo si se proporcionó una nueva
    if (archivoPart != null && archivoPart.getSize() > 0 && archivoPart.getSize() <= 100 * 1024) {
        try (InputStream inputStream = archivoPart.getInputStream()) {
            // Convertir la imagen en bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] imagenBytes = outputStream.toByteArray();

            // Establecer la imagen en la consulta
            pstmt.setBytes(4, imagenBytes);  // Utilizar el índice 4 para la columna 'foto'
        } catch (IOException e) {
            e.printStackTrace(); // Manejo de errores de lectura de la imagen
        }
    } else {
        // En caso de que no se haya proporcionado una nueva imagen válida
        pstmt.setNull(4, java.sql.Types.LONGVARBINARY);  // Utilizar el índice 4 para la columna 'foto'
    }

                pstmt.setInt(5, matriculaCred);  // ESTA VA A IDENTIFICAR LA MATRICULA ORA ENCONTRAR LA PUBLICACION 


// Ejecutar la actualización
int filasActualizadas = pstmt.executeUpdate();

// Verificar si la actualización fue exitosa
    if (filasActualizadas > 0) {
        // Actualización exitosa, establecer mensaje y redirigir a la página de publicación
        publicacion pubAct = new publicacion(nombreAct, pubSelecc.getMatriculaCred(),facultadAct,descripcionAct,pubSelecc.getNombreAutor(), pubSelecc.getMatriculaAutor(), pubSelecc.getCorreoAutor());
        estudiante perfil = (estudiante) session.getAttribute("perfilActual");
        for(int i=0;i<perfil.getPosts().length;i++){
            if(perfil.getPosts()[i].matriculaCred==pubAct.getMatriculaCred()){
                perfil.getPosts()[i]=pubAct;
            }
        }
        session.setAttribute("perfilAct", perfil);
        response.sendRedirect("profileView.jsp?mensaje=Actualizacion+exitosa");
    } else {
        // No se actualizó ninguna fila, probablemente debido a que no se encontró la matrícula
        response.sendRedirect("profileView.jsp?mensaje=No+se+pudo+actualizar");
    }
        // Cerrar la conexión y liberar recursos
        pstmt.close();
        Conexion.close();
        ;
        } catch (Exception e) {
    e.printStackTrace();
    response.sendRedirect("profileView.jsp");
    }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

} 