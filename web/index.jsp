<%-- 
  Página JSP para mostrar el listado de libros.
  Muestra una tabla con todos los libros registrados en el sistema,
  incluyendo sus autores y géneros asociados, con opciones para
  añadir, editar y eliminar libros.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList, java.util.Map" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Listado de Libros</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
</head>
<body>
    <div class="container mt-4">
        <!-- Encabezado -->
        <div class="row mb-3">
            <div class="col-12 col-md-8">
                <h1 class="mb-0">Listado de Libros</h1>
            </div>
            <div class="col-12 col-md-4 text-md-end mt-3 mt-md-0">
                <a href="/la_meva_llibreria/Insertar" class="btn btn-success d-flex align-items-center justify-content-center">
                    <span class="me-2">&#43;</span>Añadir Libro
                </a>
            </div>
        </div>

        <!-- Tabla responsiva -->
        <div class="table-responsive">
            <table class="table table-bordered table-hover">
                <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Título</th>
                        <th>ISBN</th>
                        <th>Año de Publicación</th>
                        <th>Autores</th>
                        <th>Géneros</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%-- Iterar sobre la lista de libros --%>
                    <% 
                        ArrayList<Map<String, Object>> libros = (ArrayList<Map<String, Object>>) request.getAttribute("libros");
                        if (libros != null && !libros.isEmpty()) {
                            for (Map<String, Object> libro : libros) {
                    %>
                    <tr>
                        <td><%= libro.get("id") %></td>
                        <td><%= libro.get("titol") %></td>
                        <td><%= libro.get("isbn") %></td>
                        <td><%= libro.get("any_publicacio") %></td>
                        <td><%= libro.get("autors") != null ? libro.get("autors") : "Sin Autores" %></td>
                        <td><%= libro.get("generes") != null ? libro.get("generes") : "Sin Géneros" %></td>
                        <td class="d-flex flex-wrap justify-content-center">
                            <a href="/la_meva_llibreria/Editar?id=<%= libro.get("id") %>" class="btn btn-primary btn-sm me-2 mb-2">Editar</a>
                            <a href="/la_meva_llibreria/Borrar?id=<%= libro.get("id") %>" class="btn btn-danger btn-sm mb-2">Borrar</a>
                        </td>
                    </tr>
                    <% 
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="7" class="text-center">No se encontraron libros.</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.js"></script>
</body>
</html>
