<%@page import="com.SELOB.servlets.estudiante" %>
<%@page import="com.SELOB.servlets.publicacion" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- Estas las agregue yo -->
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.SELOB.servlets.publicacion" %>
<%@page import="java.util.Base64"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="css/profileDesing.css">
        <title>SELOB</title>
    </head>
    <body>
    <body data-spy="scroll" data-target=".navbar" data-offset="51">
        <!-- Navbar Start -->
        <nav class="navbar fixed-top shadow-sm navbar-expand-lg bg-dark navbar-dark py-1 py-lg-0 px-lg-5">
            <a href="index.jsp" class="navbar-brand d-block d-lg-none">
                <h1 class="display-4 text-white text-uppercase m-0">SELOB</h1>
            </a>
            <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-between" id="navbarCollapse">
                <div class="navbar-nav ml-auto py-0">
                    <a href="makePublication.jsp" class="nav-item nav-link active">PUBLICAR</a>
                </div>
                <a href="index.jsp" class="navbar-brand px-4 mx-3 d-none d-lg-block">
                    <h1 class="display-4 text-white text-uppercase m-0">SELOB</h1>
                </a>
                <div class="navbar-nav mr-auto py-0">
                    <a href="profileView.jsp" class="nav-item nav-link">PERFIL</a>
                </div>
            </div>
        </nav>
    </body>     
    <!-- Navbar End -->

    <%
        estudiante perfil = (estudiante) session.getAttribute("perfilActual");
        String nombre = "";
        int matricula = 0;
        String facultad = "";
        String email = "";

        nombre = perfil.getNombre();
        matricula = perfil.getMatricula();
        facultad = perfil.getFacultad();
        email = perfil.getEmail();

        String publicaciones = perfil.getPublicaciones();
        //verifica si la cadena no está vacía para determinar si el estudiante tiene publicaciones
        boolean tienePublicaciones = perfil.getPublicaciones().equals("No Info");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/selob", "root", "");

            // Consulta para obtener la imagen por su matricula
            String consulta = "SELECT foto FROM imagenes WHERE matricula = ?";
            try ( PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
                pstmt.setInt(1, matricula);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    // Obtener los datos de la imagen como un arreglo de bytes desde la base de datos
                    byte[] imagenBytes = rs.getBytes("foto");

                    // Convertir los bytes de la imagen a formato base64
                    String imagenBase64 = Base64.getEncoder().encodeToString(imagenBytes);

                    // Establecer la imagen en formato base64 como un atributo en el alcance de solicitud
                    request.setAttribute("imagenBase64", imagenBase64);
                    // Redirigir a index.jsp para mostrar la imagen
                    // request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {
                    // Manejar el caso en que no se encuentre ninguna imagen con el ID proporcionado
                }

                // Cerrar recursos (ResultSet, PreparedStatement, Connection) después de su uso
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Cerrar la conexión después de utilizarla
            conexion.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    %>
    
    <div class="container card-0 justify-content-center">
        <div class="card p-4">
            <div class="image d-flex flex-column justify-content-center align-items-center"> 
                <!-- Aqui vamos a mostrar la imagen -->
                <button class="btn btn-secondary"> 
                    <%
                        // Verifica si la imagen Base64 está presente en el request antes de intentar mostrarla
                        String imagenBase64 = (String) request.getAttribute("imagenBase64");
                        if (imagenBase64 != null && !imagenBase64.isEmpty()) {
                    %>
                    <img src="data:image/jpg;base64, <%= imagenBase64%>" height="100" width="100" alt="Imagen Cargada" />
                    <%
                    } else {
                    %>
                    <img src="imagenes/avatar.png" height="100" width="100" />
                    <%
                        }
                    %>



                    
                </button>
                <span class="name mt-3"><%= nombre%></span>
                <span class="idd"><%= email%></span> 
                <div class="d-flex flex-row justify-content-center align-items-center gap-2"> 
                    <span class="idd1"><%= matricula%></span> 
                    <span><i class="fa fa-copy"></i></span> 
                </div>
                <div class="d-flex mt-2"> 
                    <a href="profileCustomize.jsp"><button class="btn1 btn-dark">EDITAR PERFIL</button></a>
                </div>
                <%
                    if ((!tienePublicaciones)&&(perfil.getPosts().length > 0)) {
                        publicacion[] posts = perfil.getPosts();
                %>    


                <script>
                    function submitForm() {
                        document.getElementById("formPublications").submit();
                    }
                </script>
                
                <div class="col-md-6 pt-md-0 pt-3" id="lang">  
                    <form method="POST" id="fomPublication">
                        <select class="selection" name="publications" id="publications">
                            <option disabled selected="">Mis Publicaciones...</option>
                            <% for (int i = 0; i < posts.length; i++) {%>
                            <option value="<%=i%>">
                                <%= "Credencial: " + posts[i].getMatriculaCred()%>
                            </option>
                            <% } %>

                            <%
                                if (request.getMethod().equals("POST")) {
                                    String selectedIndex = request.getParameter("publications");
                                    if (selectedIndex != null) {
                                        int index = Integer.parseInt(selectedIndex);
                                        // Guardar el elemento seleccionado en la sesión
                                        session.setAttribute("publicacionSeleccionada", posts[index]);
                                        response.sendRedirect("managePublication.jsp");
                                    }
                                }
                            %>

                        </select>
                          
                            <input type="hidden" onclick="submitForm()" name="formSubmitted" value="true"> <!-- Campo oculto para indicar que se envió el formulario -->
                        <button type="submit">VER</button>
                    </form>   
                </div>

                <%
                } else {
                %> 
                <div class="col-md-6 pt-md-0 pt-3" id="lang">
                    <select class="selection" name="publications" id="publications">
                        <option disabled selected="">Sin Publicaciones</option>
                        <option value="publi1">- - - - -</option>
                    </select>
                </div>
                <% }%>

            </div> 
        </div>
    </div>   

    <!-- Footer Start -->
    <div class="container-fluid bg-dark text-white py-5 px-sm-3 px-lg-5" style="margin-top: 65px;">
        <div class="row pt-3">
            <div class="col-12 mb-3 px-4">
                <div class="row mb-3 p-4" style="background: rgba(256, 256, 256, .05);">
                    <div class="col-md-4">
                        <div class="text-md-center">
                            <h5 class="text-primary text-uppercase mb-2" style="letter-spacing: 5px;">Localización</h5>
                            <p class="mb-4 m-md-0">Puebla,Puebla</p>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="text-md-center">
                            <h5 class="text-primary text-uppercase mb-2" style="letter-spacing: 5px;">Contactanos</h5>
                            <p class="mb-4 m-md-0">lusu@software.com</p>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="text-md-center">
                            <h5 class="text-primary text-uppercase mb-2" style="letter-spacing: 5px;">Call Us</h5>
                            <p class="m-0">+52 *********</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid bg-dark text-white text-center border-top py-4 px-sm-3 px-md-5" style="border-color: rgba(256, 256, 256, .05) !important;">
        <p class="m-0 text-white">&copy; <a href="#">Lusu Software</a>. Todos los Derechos Reservados. Designed by <a href="https://htmlcodex.com">HTML Codex</a></p>
    </div>
    <!-- Footer End -->

</body>
</html>
