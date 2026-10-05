<%@page import="com.SELOB.servlets.estudiante" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html lang="es">
<head>
    
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
    %>
    <!--Boostrap-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/customizeDesing.css">
    <title>SELOB</title>
</head>

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
    
 <div class="container card-0 justify-content-center">
       <div class="wrapper bg-white mt-sm-5">
           <form action="svProfileCustom" method="POST" enctype="multipart/form-data">
                <h4 class="pb-4 border-bottom">Editar información</h4>
           <div class="d-flex align-items-start py-3 border-bottom">
                <img src="imagenes/camara-fotografica.png" class="img" alt="">
                <div class="pl-sm-4 pl-2" id="img-section">
                    <b>Foto de perfil</b>
                    <input type="file" name="imagen" accept=".jpg, .png, .jpeg">
                </div>
            </div>
            <div class="py-2">
                <div class="row py-2">
                    <div class="col-md-6">
                        <label for="matricula">Matricula</label>
             <!-- solo que se muestre la matricula, que no se pueda cambiar-->
                            <div class="box">
                               <%= perfil.getMatricula() %>
                            </div>
                    </div>
                    <div class="col-md-6 pt-md-0 pt-3">
                        <label for="Nombre Apellido">Nombre</label>
                        <input type="text" class="bg-light form-control" placeholder="<%= perfil.getNombre() %>">
                    </div>
                </div>
                <div class="row py-2">
                    <div class="col-md-6">
                         <label for="matricula">Correo</label>
             <!-- solo que se muestre el correo, que no se pueda cambiar-->
                            <div class="box">
                                <%= perfil.getEmail() %>
                            </div>
                    </div>
                    <div class="col-md-6 pt-md-0 pt-3" id="lang">
                        <label for="contraseña">Contraseña</label>
                        <div class="arrow">
                            <input type="password" class="bg-light form-control" name="contrasena" placeholder="**********">
                        </div>
                    </div>
                </div>
                <div class="row py-2">
                    <div class="col-md-6">
                        <label for="facultad">Facultad</label>
                        <input type="text" class="bg-light form-control" name="facultad" placeholder="<%= perfil.getFacultad() %>">
                    </div>
                </div>
                <div class="py-3 pb-4 border-bottom">
                    <button type="submit" class="btn btn-primary mr-3">GUARDAR CAMBIOS</button>
                    <button  class="btn border button"><a href="profileView.jsp">CANCELAR</button>
                </div>
                    <!-- <div class="d-sm-flex align-items-center pt-3" id="deactivate">
                    <div>
                        <b>Desactivar cuenta</b>
                        <p>Es una pena que tengas que irte</p>
                    </div>
                    <div class="ml-auto">
                        <button class="btn danger">DESACTIVAR</button>
                    </div>
                </div> --> 
            </div>
           </form>
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