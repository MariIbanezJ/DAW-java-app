package llibreria;



import llibreria.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Clase para generar formularios HTML para la gestión de libros.
 * Proporciona métodos para crear formularios de inserción y edición de libros.
 * 
 * @author Juan Marí Ibañez
 * @version 1.0
 * @since 1.0
 */
public class Formulario {
    
    /**
     * Genera un formulario HTML para inserta o editar un libro
     * 
     * @param idLibro ID del libro a editar (null o vacío para nuevo libro)
     * @return String con el codigo HTML del formualrio generado
     */
public String generarFormulario(String idLibro) {
    StringBuilder formBuilder = new StringBuilder();
    ArrayList<String[]> generos = new ArrayList<>();
    ArrayList<String[]> editorials = new ArrayList<>();
    ArrayList<String[]> autors = new ArrayList<>();
    String titol = "", isbn = "", autorId = "", genereId = "", editorialId = "";
    int anyPublicacio = 0;

    try (Connection conexion = new Conexion().conectar()) {
        
         // Obtener géneros
            String sqlGeneros = "SELECT id, nom FROM generes";
            PreparedStatement statementGeneros = conexion.prepareStatement(sqlGeneros);
            ResultSet resultSetGeneros = statementGeneros.executeQuery();
            while (resultSetGeneros.next()) {
                generos.add(new String[]{resultSetGeneros.getString("id"), resultSetGeneros.getString("nom")});
            }
            resultSetGeneros.close();
            statementGeneros.close();

            // Obtener editoriales
            String sqlEditorials = "SELECT id, nom FROM editorials";
            PreparedStatement statementEditorials = conexion.prepareStatement(sqlEditorials);
            ResultSet resultSetEditorials = statementEditorials.executeQuery();
            while (resultSetEditorials.next()) {
                editorials.add(new String[]{resultSetEditorials.getString("id"), resultSetEditorials.getString("nom")});
            }
            resultSetEditorials.close();
            statementEditorials.close();

            // Obtener autores
            String sqlAutors = "SELECT id, nom FROM autors";
            PreparedStatement statementAutors = conexion.prepareStatement(sqlAutors);
            ResultSet resultSetAutors = statementAutors.executeQuery();
            while (resultSetAutors.next()) {
                autors.add(new String[]{resultSetAutors.getString("id"), resultSetAutors.getString("nom")});
            }
            resultSetAutors.close();
            statementAutors.close();

            // Obtener datos del libro solo si el ID está presente
            if (idLibro != null && !idLibro.isEmpty()) {
                String sqlLibro = "SELECT l.titol, l.isbn, l.any_publicacio, l.id_editorial, la.id_autor, lg.id_genere " +
                        "FROM llibres l " +
                        "LEFT JOIN llibre_autor la ON l.id = la.id_llibre " +
                        "LEFT JOIN llibre_genere lg ON l.id = lg.id_llibre " +
                        "WHERE l.id = ?";
                PreparedStatement statementLibro = conexion.prepareStatement(sqlLibro);
                statementLibro.setString(1, idLibro);
                ResultSet resultSetLibro = statementLibro.executeQuery();
                if (resultSetLibro.next()) {
                    titol = resultSetLibro.getString("titol");
                    isbn = resultSetLibro.getString("isbn");
                    anyPublicacio = resultSetLibro.getInt("any_publicacio");
                    editorialId = resultSetLibro.getString("id_editorial");
                    autorId = resultSetLibro.getString("id_autor");
                    genereId = resultSetLibro.getString("id_genere");
                }
                resultSetLibro.close();
                statementLibro.close();
            }
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    // Determinar la acción del formulario (edición o inserción)
    String actionUrl = idLibro != null && !idLibro.isEmpty() 
            ? "/la_meva_llibreria/Editar" 
            : "/la_meva_llibreria/Insertar";

    // Generar el formulario
    formBuilder.append("<!DOCTYPE html>");
    formBuilder.append("<html>");
    formBuilder.append("<head>");
    formBuilder.append("<meta charset='UTF-8'>");
    formBuilder.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
    formBuilder.append("<title>Formulario de Libro</title>");
    formBuilder.append("<link rel='stylesheet' href='./css/bootstrap.css'>");
    formBuilder.append("</head>");
    formBuilder.append("<body>");
    formBuilder.append("<div class='container mt-4'>");
    formBuilder.append("<h1 class='mb-4'>Formulario de Libro</h1>");
    formBuilder.append("<form action='").append(actionUrl).append("' method='POST' class='row g-3'>");

    if (idLibro != null && !idLibro.isEmpty()) {
        formBuilder.append("<input type='hidden' name='id' value='").append(idLibro).append("'>");
    }

    // Campos del formulario
    formBuilder.append("<div class='col-12 col-md-6'>");
    formBuilder.append("<label for='titol' class='form-label'>Título:</label>");
    formBuilder.append("<input type='text' id='titol' name='titol' value='").append(titol).append("' class='form-control' required>");
    formBuilder.append("</div>");

    formBuilder.append("<div class='col-12 col-md-6'>");
    formBuilder.append("<label for='isbn' class='form-label'>ISBN:</label>");
    formBuilder.append("<input type='text' id='isbn' name='isbn' value='").append(isbn).append("' class='form-control' required>");
    formBuilder.append("</div>");

    formBuilder.append("<div class='col-12 col-md-6'>");
    formBuilder.append("<label for='any_publicacio' class='form-label'>Año de Publicación:</label>");
    formBuilder.append("<input type='number' id='any_publicacio' name='any_publicacio' value='")
            .append(anyPublicacio == 0 ? "" : anyPublicacio).append("' class='form-control' required>");
    formBuilder.append("</div>");

    // Selector de editorial
    formBuilder.append("<div class='col-12 col-md-6'>");
    formBuilder.append("<label for='id_editorial' class='form-label'>Editorial:</label>");
    formBuilder.append("<select id='id_editorial' name='id_editorial' class='form-select' required>");
    formBuilder.append("<option value='' ").append(editorialId.isEmpty() ? "selected" : "").append(">Seleccione una editorial</option>");
    for (String[] editorial : editorials) {
        formBuilder.append("<option value='").append(editorial[0]).append("'")
                .append(editorial[0].equals(editorialId) ? " selected" : "").append(">")
                .append(editorial[1]).append("</option>");
    }
    formBuilder.append("</select>");
    formBuilder.append("</div>");

    // Selector de autor
    formBuilder.append("<div class='col-12 col-md-6'>");
    formBuilder.append("<label for='id_autor' class='form-label'>Autor:</label>");
    formBuilder.append("<select id='id_autor' name='id_autor' class='form-select' required>");
    formBuilder.append("<option value='' ").append(autorId.isEmpty() ? "selected" : "").append(">Seleccione un autor</option>");
    for (String[] autor : autors) {
        formBuilder.append("<option value='").append(autor[0]).append("'")
                .append(autor[0].equals(autorId) ? " selected" : "").append(">")
                .append(autor[1]).append("</option>");
    }
    formBuilder.append("</select>");
    formBuilder.append("</div>");

    // Selector de género
    formBuilder.append("<div class='col-12 col-md-6'>");
    formBuilder.append("<label for='id_genere' class='form-label'>Género:</label>");
    formBuilder.append("<select id='id_genere' name='id_genere' class='form-select' required>");
    formBuilder.append("<option value='' ").append(genereId.isEmpty() ? "selected" : "").append(">Seleccione un género</option>");
    for (String[] genero : generos) {
        formBuilder.append("<option value='").append(genero[0]).append("'")
                .append(genero[0].equals(genereId) ? " selected" : "").append(">")
                .append(genero[1]).append("</option>");
    }
    formBuilder.append("</select>");
    formBuilder.append("</div>");

    // Botón submit
    formBuilder.append("<div class='col-12 text-center'>");
    formBuilder.append("<button type='submit' class='btn btn-primary w-50'>Guardar</button>");
    formBuilder.append("</div>");

    formBuilder.append("</form>");
    formBuilder.append("</div>");
    formBuilder.append("<script src='../js/bootstrap.bundle.js'></script>");
    formBuilder.append("</body>");
    formBuilder.append("</html>");

    return formBuilder.toString();
}

}
