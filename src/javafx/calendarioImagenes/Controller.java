package javafx.calendarioImagenes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import clases.Padre;

public class Controller extends Padre {

  @FXML
  private AnchorPane anchorCalendario;

  @FXML
  private TextField productoID, productoTipo, productoFecha, productoNombre, productoCondicion, fotografiaId, fotografiaIdPlanta;

  @FXML
  private DatePicker fotografiaFecha;

  @FXML
  private GridPane galeria;

  @FXML
  private void loadInicio(ActionEvent event) throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("/javafx/inicio/sample.fxml"));
    Stage primaryStage = (Stage) anchorCalendario.getScene().getWindow();
    primaryStage.setTitle("Vitacura");
    primaryStage.setScene(new Scene(root, 1366, 768));
    primaryStage.show();
  }

  private File cargarImagen() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters()
        .addAll(new ExtensionFilter("Images Files", "*.png", "*.jpg", "*.jpeg"));
    File imagen = fileChooser.showOpenDialog(new Stage());
    return imagen;
  }

  @FXML
  private void nuevaImagen() throws SQLException, IOException {
    Random r = new Random();
    int id = r.nextInt(9999999) + 1;

    File imagen = cargarImagen();

    String query =
        "insert into historial_fotografias values(? , '" + fotografiaFecha.getValue() + "', "
            + id + ", " + fotografiaIdPlanta.getText() + ")";
    Connection conn = connector.Connector.getConnection();
    PreparedStatement pst = conn.prepareStatement(query);

    FileInputStream fis = new FileInputStream(imagen);
    pst.setBinaryStream(1, fis);

    pst.executeUpdate();

    fis.close();
  }

  @FXML
  private void modificarImagen() throws SQLException, IOException {
    File imagen = cargarImagen();

    String query =
        "UPDATE historial_fotografias SET fotografia = ? WHERE id = " + fotografiaId.getText();
    Connection conn = connector.Connector.getConnection();
    PreparedStatement pst = conn.prepareStatement(query);

    FileInputStream fis = new FileInputStream(imagen);
    pst.setBinaryStream(1, fis);

    pst.executeUpdate();

    fis.close();
  }

  @FXML
  private void eliminarImagen() {
    String query =
        "delete from historial_fotografias  where id = " + fotografiaId.getText();
    executeQuery(query);
  }

  @FXML
  private void verImagenes() throws SQLException, IOException {

    GridPane galeriaAux = new GridPane();

    String query =
        "SELECT COUNT(*) cantidad FROM historial_fotografias WHERE producto_id = " + productoID
            .getText();
    Connection conn = connector.Connector.getConnection();
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery(query);

    int cantidadFotos = 0;
    if (rs.next()) {
      cantidadFotos = rs.getInt("cantidad");
    }
    int longitud = (int) Math.sqrt(cantidadFotos);

    query = "select * from producto where id = " + productoID.getText();
    rs = st.executeQuery(query);
    if (rs.next()) {
      productoTipo.setText(String.valueOf(rs.getInt(1)));
      productoNombre.setText(rs.getString(2));
      productoFecha.setText(String.valueOf(rs.getDate(3)));
      productoCondicion.setText(rs.getString(4));
    }

    query =
        "Select fotografia, id, producto_id, fecha from historial_fotografias where producto_id = "
            + productoID
            .getText();
    rs = st.executeQuery(query);

    for (int i = 0; i < longitud + 1; i++) {
      for (int j = 0; j < longitud + 1; j++) {

        File file = new File("imagenProducto");
        FileOutputStream fos = new FileOutputStream(file);

        if (rs.next()) {
          try {
            InputStream input = (InputStream) rs.getBinaryStream("fotografia");

            byte[] buffer = new byte[1024];
            while (input.read(buffer) > 0) {

              fos.write(buffer);
            }

            input.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
          fos.close();

          int id = rs.getInt("id");
          String fecha = String.valueOf(rs.getDate("fecha"));
          int idPlanta = rs.getInt("producto_id");

          Image image = new Image(file.toURI().toString(), 100, 100, false, true);
          ImageView imageView = new ImageView(image);
          imageView.setId(String.valueOf(rs.getInt("id")));
          imageView.setOnMouseClicked(e -> {
            fotografiaId.setText(Integer.toString(id));
            fotografiaFecha.setValue(LocalDate.parse(fecha));
            fotografiaIdPlanta.setText(Integer.toString(idPlanta));
          });
          galeriaAux.add(imageView, i, j);
        }
      }
    }
    galeria.getChildren().setAll(galeriaAux);
  }
}
