package llibreria;

import llibreria.Conexion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet que maneja la eliminación de libros y sus relaciones asociadas.
 * Elimina un libro de la base de datos junto con sus relaciones en las tablas
 * llibre_autor y llibre_genere.
 * 
 * @author Juan Marí Ibañez
 * @version 1.0
 * @since 1.0
 * 
 */
@WebServlet(name = "Borrar", urlPatterns = {"/Borrar"})
public class Borrar extends HttpServlet {

    /**
     * Maneja las solicitudes GET para eliminar un libro.
     * 
     * @param request El objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response El objeto HttpServletResponse que contiene la respuesta al cliente.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idLibro = request.getParameter("id");
        Connection conexion = null;

        try {
            
            //Conectar a la base de datos             
            Conexion conexionDB = new Conexion();
            conexion = conexionDB.conectar();

            //Elimina las relaciones del libro con autores en la tabla llibre_autor.
            
            String sqlDeleteLlibreAutor = "DELETE FROM llibre_autor WHERE id_llibre = ?";
            PreparedStatement statementLlibreAutor = conexion.prepareStatement(sqlDeleteLlibreAutor);
            statementLlibreAutor.setString(1, idLibro);
            statementLlibreAutor.executeUpdate();
            statementLlibreAutor.close();

            //Elimina las relaciones del libro con géneros en la tabla llibre_genere.
             
            String sqlDeleteLlibreGenere = "DELETE FROM llibre_genere WHERE id_llibre = ?";
            PreparedStatement statementLlibreGenere = conexion.prepareStatement(sqlDeleteLlibreGenere);
            statementLlibreGenere.setString(1, idLibro);
            statementLlibreGenere.executeUpdate();
            statementLlibreGenere.close();

            //Elimina el libro de la tabla llibres.          
            String sqlDeleteLibro = "DELETE FROM llibres WHERE id = ?";
            PreparedStatement statementDeleteLibro = conexion.prepareStatement(sqlDeleteLibro);
            statementDeleteLibro.setString(1, idLibro);
            statementDeleteLibro.executeUpdate();
            statementDeleteLibro.close();

            // Redirigir después de borrar            
            response.sendRedirect("/la_meva_llibreria/actualizar.jsp");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al eliminar el libro.");
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