package com.SELOB.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "svFound", urlPatterns = {"/svFound"})
public class svFound extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        publicacion encontrada = (publicacion) session.getAttribute("publicacionEncontrada");
        int matricula = encontrada.getMatriculaCred();
        int matriculaAutor = encontrada.getMatriculaAutor();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection Conexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/selob","root","");
            Statement SentenciaSQL = Conexion.createStatement();
            String orden = "DELETE FROM publicaciones WHERE matriculaCred="+matricula;
            SentenciaSQL.executeUpdate(orden);
            String eliminarPub=matricula+",";
            orden = "SELECT publicacion FROM cuentasregistradas WHERE matricula="+matriculaAutor;
            ResultSet autor = SentenciaSQL.executeQuery(orden);
            String publicaciones="";
            if(autor.next()){
                publicaciones=autor.getString("publicacion");
                publicaciones=publicaciones.replace(eliminarPub, "");
                if(publicaciones.equals("")){
                    publicaciones="No Info";
                }
            }
            orden = "UPDATE cuentasregistradas SET publicacion='"+publicaciones+"' WHERE matricula="+matriculaAutor;
            SentenciaSQL.executeUpdate(orden);
            Conexion.close();
            SentenciaSQL.close();
            response.sendRedirect("index.jsp");
        } catch (ClassNotFoundException e) {
            System.out.println("Clase no encontrada");
        } catch (SQLException e) {
            System.out.println(e);
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
