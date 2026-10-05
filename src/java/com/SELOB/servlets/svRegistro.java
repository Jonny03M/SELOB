package com.SELOB.servlets;

import java.sql.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "svRegistro", urlPatterns = {"/svRegistro"}) //nombre del servlet, importante por que se usa para hacer la conexion
public class svRegistro extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {   
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) //metodo si se va a hacer busqueda o consulta
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override //metodo si se va a hacer insercion o modificacion
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correoAlumno="@alumno.buap.mx";
        String nombre = request.getParameter("nombre"); // Recuperar el parámetro del formulario
        String email = request.getParameter("email"); // Son los datos que se reciben del formulario
        int matricula = Integer.parseInt(request.getParameter("matricula")); // En los distintos casos
	String contrasena = request.getParameter("contrasena"); //
        String facultad="No Info"; //Aunque no se usa, se debe de poner para que no marque error
        String publicacion="No Info"; //Meter todos los datos en la base de datos a la vez
        if(email.contains(correoAlumno) && (matricula>201000000) && (matricula<202399999)){
            try {
                Class.forName("com.mysql.jdbc.Driver"); // Cargar el driver
                Connection Conexion= DriverManager.getConnection("jdbc:mysql://localhost:3306/selob", "root", ""); // Conectarse a la base de datos
                Statement SentenciaSQL = Conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); // Crear una sentencia SQL
                //Se redacta la orden SQL dependiedo de lo que se quiera hacer, en este caso es una insercion
                String orden = "INSERT INTO cuentasregistradas (nombre, matricula, email,contrasena,facultad,publicacion) VALUES (?, ?, ?, ?, ?, ?)";
                //la anterior orden se escribe usando los ? para evitar concatenar los datos
                try (PreparedStatement preparedStatement = Conexion.prepareStatement(orden)) { // Preparar la inserción
                    preparedStatement.setString(1, nombre); // Sustituir las ? por los valores correspondientes
                    preparedStatement.setInt(2, matricula); 
                    preparedStatement.setString(3, email);
                    preparedStatement.setString(4, contrasena);
                    preparedStatement.setString(5, facultad);
                    preparedStatement.setString(6, publicacion);
                    preparedStatement.executeUpdate(); // Ejecutar la inserción
            }
                Conexion.close(); // Cerrar la conexión
                SentenciaSQL.close(); // Cerrar la sentencia
                publicacion[] posts = new publicacion[0];
                estudiante nuevaCuenta = new estudiante(nombre,matricula,email,contrasena,facultad,publicacion,posts);
                HttpSession session = request.getSession();
                session.setAttribute("perfilActual", nuevaCuenta);
            } catch (ClassNotFoundException e) { // Manejar excepciones
                System.out.println("Clase no encontrada");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error de SQL: " + e.getMessage());
            }
            
            try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection Conexion= DriverManager.getConnection("jdbc:mysql://localhost:3306/selob", "root", "");
            Statement SentenciaSQL = Conexion.createStatement();
            String orden="SELECT * FROM publicaciones WHERE matriculaCred="+matricula;
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
                HttpSession session = request.getSession();
                session.setAttribute("publicacionEncontrada", encontrada);
                response.sendRedirect("verification.jsp");
            }else{
                response.sendRedirect("verificationFail.jsp");
            }
            Conexion.close();
            SentenciaSQL.close();
        }catch (ClassNotFoundException e) { // Manejar excepciones
                System.out.println("Clase no encontrada");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error de SQL: " + e.getMessage());
            }
        }else{
            response.sendRedirect("index.jsp");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
