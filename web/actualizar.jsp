<%--
  Página de redirección inicial (index.jsp).
  Redirige automáticamente al servlet Listar para mostrar
  el listado de libros al acceder a la aplicación.
  
  Esta página no contiene contenido visible ya que su única
  función es redirigir al punto de entrada principal de la aplicación.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Redirige automáticamente al servlet Listar
    //response.sendRedirect("/la_meva_llibreria/DAO/Listar");
    response.sendRedirect(request.getContextPath() + "/Listar");
    //response.sendRedirect("http://localhost:8084/la_meva_llibreria/Listar");

  
    //response.sendRedirect(request.getContextPath() + "/DAO/Listar");
%>

