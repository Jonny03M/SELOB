<%@page import="com.SELOB.servlets.publicacion" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <!--Boostrap-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="css/verificationDesing.css">
        <title>SELOB</title>
    </head>
    <body    
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
    publicacion post = (publicacion) session.getAttribute("publicacionSeleccionada");
    String nombrecred = null;
    int matriculacred = 0;
    String facultadcred = null;
    String descripcion = null;

    String matriculacredS = null;

    nombrecred = post.getNombreCred();
    matriculacred = post.getMatriculaCred();
    facultadcred = post.getFacultadCred();
    descripcion = post.getDescripcion();
    matriculacredS=matriculacred+"";

%>    
<body>
 <div class="container card-0 justify-content-center">
        <div class="card-body px-sm-4 px-0">
            <div class="row justify-content-center mb-4">
                <div class="col-md-6 col">
                    <h3 class="font-weight-bold mx-auto text-center text-sm-left"> MODIFICAR PUBLICACIÓN </h3>
                    <p class="mt-md-2 ml-md-0 ml-2 text-center text-sm-left">INGRESE LOS DATOS DE LA CREDENCIAL QUE DESEA ACTUALIZAR:</p>
                </div>
            </div>
            <div class="row justify-content-center">
                <div class="col-lg-6 col-md-8">
                    <div class="card shadow-lg card-1 mx-auto">
                        <div class="card-body inner-card text-center">
                            <div class="row">
                                <div class="contenedor mx-auto my-auto col-md-6"> <!-- Reducimos el tamaño del box y centrar la imagen -->
                                    <img src="imagenes/selob1.jpg" class="img-fluid rounded" alt="Imagen SELBO">
                                </div>

                                <form action="svActPub" method="POST" enctype="multipart/form-data" class="mx-auto text-center col-md-6">
                                    <div class="form-group">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="5" height="5" fill="currentColor"
                                            class="bi bi-person" viewBox="0 0 16 16">
                                            <!-- ... -->
                                        </svg>
                                        <label for="name">Nombre</label>
                                        <input class="form-control" type="text" name="nombre" placeholder="<%= nombrecred %>">
                                    </div>
                                    <div class="form-group">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                            class="bi bi-mortarboard" viewBox="0 0 8 8">
                                            <!-- ... -->
                                        </svg>
                                        <label for="first-name">Matricula Credencial</label>
                                        <p><%= matriculacredS%></p>
                                        <label for="name">Facultad</label>
                                        <input class="form-control" type="text" name="facultad" placeholder="<%= facultadcred%>">
                                    </div>
                                    <div class="form-group">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                            class="bi bi-blockquote-left" viewBox="0 0 8 8">
                                            <!-- ... -->
                                        </svg>
                                        <label for="name">Descripción</label>
                                        <input class="form-control" type="text" name="descripcion" placeholder="<%= descripcion%>">
                                    </div>
                                    <div class="form-group">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                            class="bi bi-image-alt" viewBox="0 0 8 8">
                                            <!-- ... -->
                                        </svg>
                                        <label for="name">Fotografia</label>
                                        <input type="file" name="imagen" accept=".jpg, .png">
                                    </div>
                                    <h6><br></h6>
                                    <div class="col-md-12 mb-2 mx-auto"> <!-- Centramos el botón horizontalmente -->
                                        <button type="submit" class="btn btn-primary btn-block font-weight-bold mx-auto">Guardar</button>
                                    </div>
                                </form>

                                <div class="col-md-12 mb-2 mx-auto"> <!-- Centramos el botón horizontalmente -->
                                    <form action="svDeletePub" method="GET">
                                        <button type="submit" class="btn btn-danger btn-block font-weight-bold mx-auto">Eliminar</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

<!-- Footer Start -->
<div class="container-fluid bg-dark text-white py-5 px-sm-3 px-lg-5" style="margin-top: 1px;">
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

<script src="animaciones.js"></script>
<script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
<script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>

</body>
</html>
