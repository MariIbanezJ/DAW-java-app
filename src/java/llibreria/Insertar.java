package llibreria;



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
 * Servlet para manejar la inserción de nuevos libros en la base de datos.
 * Proporciona funcionalidad para mostrar el formulario de inserción (GET)
 * y procesar el envío del formulario (POST).
 * 
 * @author Juan Marí Ibañez
 * @version 1.0
 * @since 1.0
 */
 
@WebServlet(name = "Insertar", urlPatterns = {"/Insertar"})
public class Insertar extends HttpServlet {

    /**
     * Muestra el formulario para insertar un nuevo libro
     * 
     * @param request Objeto HttpServletRequest con la solicitod del cliente
     * @param response Objeto HttlServet Responese para enviar la respuesta
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
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
     * Procesa el envío del formulario para insertar un nuevo libro
     * Inserta los datos en las tablas llibres, llibres_autor y llibres_genere
     * 
     * @param request Objeto HttpServletRequest con los datos del formulario
     * @param response Objeto HttlServet Responese para enviar la respuesta
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conexion = null;
        try {
            // Obtener los parámetros del formulario
            String titol = request.getParameter("titol");
            String isbn = request.getParameter("isbn");
            int any_publicacio = Integer.parseInt(request.getParameter("any_publicacio"));
            int id_editorial = Integer.parseInt(request.getParameter("id_editorial"));
            int id_autor = Integer.parseInt(request.getParameter("id_autor"));
            int id_genere = Integer.parseInt(request.getParameter("id_genere"));

            // Conectar a la base de datos
            Conexion conexionDB = new Conexion();
            conexion = conexionDB.conectar();

            // Insertar el libro
            String sqlLibro = "INSERT INTO llibres (titol, isbn, any_publicacio, id_editorial) VALUES (?, ?, ?, ?)";
            PreparedStatement statementLibro = conexion.prepareStatement(sqlLibro, PreparedStatement.RETURN_GENERATED_KEYS);
            statementLibro.setString(1, titol);
            statementLibro.setString(2, isbn);
            statementLibro.setInt(3, any_publicacio);
            statementLibro.setInt(4, id_editorial);
            statementLibro.executeUpdate();

            // Obtener ID del libro insertado
            int id_llibre;
            try (var generatedKeys = statementLibro.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id_llibre = generatedKeys.getInt(1);
                } else {
                    throw new Exception("No se pudo obtener el ID del libro insertado.");
                }
            }

            statementLibro.close();

            // Insertar en la tabla "llibre_autor"
            String sqlLlibreAutor = "INSERT INTO llibre_autor (id_llibre, id_autor) VALUES (?, ?)";
            PreparedStatement statementLlibreAutor = conexion.prepareStatement(sqlLlibreAutor);
            statementLlibreAutor.setInt(1, id_llibre);
            statementLlibreAutor.setInt(2, id_autor);
            statementLlibreAutor.executeUpdate();
            statementLlibreAutor.close();

            // Insertar en la tabla "llibre_genere"
            String sqlLlibreGenere = "INSERT INTO llibre_genere (id_llibre, id_genere) VALUES (?, ?)";
            PreparedStatement statementLlibreGenere = conexion.prepareStatement(sqlLlibreGenere);
            statementLlibreGenere.setInt(1, id_llibre);
            statementLlibreGenere.setInt(2, id_genere);
            statementLlibreGenere.executeUpdate();
            statementLlibreGenere.close();

            // Redirigir después de insertar
            response.sendRedirect("/la_meva_llibreria/actualizar.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al insertar el libro.");
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
