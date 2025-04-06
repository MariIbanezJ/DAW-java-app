package llibreria;



import llibreria.Formulario;
import llibreria.Conexion;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet para editar información de libros en la base de datos.
 * Maneja tanto la solicitud para mostrar el formulario de edición (GET)
 * como el procesamiento de los datos actualizados (POST).
 * 
 * @author Juan Marí Ibañez
 * @version 1.0
 * @since 1.0
 */

@WebServlet(name = "Editar", urlPatterns = {"/Editar"})
public class Editar extends HttpServlet {
    
    
    /**
     * Procesa la solicitud GET para mistrar el formulario de edición
     * 
     * @param request Objeto HttpServletRequest con los parámetros
     * @param response Objeto HttpSevletResponse con la respuesta
     * @throws ServletException Si ocurre un error del servlet
     * @throws IOException Si ocurre un error de E/S
     */
@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Obtener el parámetro `id` del libro
        String idLibro = request.getParameter("id");

        // Crear una instancia de la clase FormularioGenerator
        Formulario formulario = new Formulario();

        // Generar el formulario dinámico
        String formularioHtml = formulario.generarFormulario(idLibro);

        // Enviar el formulario como respuesta al cliente
        response.getWriter().write(formularioHtml);
    }


    /**
     * Procesa la solicitud POST para actualizar los datos del libro.
     * Actualiza la información en las tablas llibres, llibre_autor y llibre_genere.
     * 
     * @param request Objeto HttpServletRequest con los parámetros del formulario
     * @param response Objeto HttpServletResponse con la respuesta
     * @throws ServletException Si ocurre un error del servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conexion = null;
        try {
            // Obtener los parámetros del formulario
            String id = request.getParameter("id");
            String titol = request.getParameter("titol");
            String isbn = request.getParameter("isbn");
            int anyPublicacio = Integer.parseInt(request.getParameter("any_publicacio"));
            int idEditorial = Integer.parseInt(request.getParameter("id_editorial"));
            int idAutor = Integer.parseInt(request.getParameter("id_autor"));
            int idGenere = Integer.parseInt(request.getParameter("id_genere"));

            // Conectar a la base de datos
            Conexion conexionDB = new Conexion();
            conexion = conexionDB.conectar();

            // Actualizar el libro
            String sqlUpdateLibro = "UPDATE llibres SET titol = ?, isbn = ?, any_publicacio = ?, id_editorial = ? WHERE id = ?";
            PreparedStatement statementUpdateLibro = conexion.prepareStatement(sqlUpdateLibro);
            statementUpdateLibro.setString(1, titol);
            statementUpdateLibro.setString(2, isbn);
            statementUpdateLibro.setInt(3, anyPublicacio);
            statementUpdateLibro.setInt(4, idEditorial);
            statementUpdateLibro.setString(5, id);
            statementUpdateLibro.executeUpdate();

            // Actualizar la relación autor-libro
            String sqlUpdateAutor = "UPDATE llibre_autor SET id_autor = ? WHERE id_llibre = ?;";
            PreparedStatement statementUpdateAutor = conexion.prepareStatement(sqlUpdateAutor);
            statementUpdateAutor.setInt(1, idAutor);
            statementUpdateAutor.setString(2, id);
            statementUpdateAutor.executeUpdate();

            // Actualizar la relación género-libro
            String sqlUpdateGenere = "UPDATE llibre_genere SET id_genere = ? WHERE id_llibre = ?;";
            PreparedStatement statementUpdateGenere = conexion.prepareStatement(sqlUpdateGenere);
            statementUpdateGenere.setInt(1, idGenere);
            statementUpdateGenere.setString(2, id);
            statementUpdateGenere.executeUpdate();

            // Redirigir después de la actualización
            response.sendRedirect("/la_meva_llibreria/actualizar.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al actualizar el libro.");
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
