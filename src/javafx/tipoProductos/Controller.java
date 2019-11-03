package javafx.tipoProductos;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import connector.Connector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import clases.Padre;

public class Controller extends Padre implements Initializable {

  @FXML AnchorPane anchorTipo;
  @FXML private TextField textFiedlNuevoTipo;
  @FXML private TextArea imprimir;
  @FXML private ComboBox<String> comboBoxTipo;

  private Connection conn;
  private ObservableList<String> seleccionTipo;

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    conn = Connector.getConnection();
    llenarCombo("tipo");

  }

  @FXML
  private void loadInicio(ActionEvent event) throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("/javafx/inicio/sample.fxml"));
    Stage primaryStage = (Stage) anchorTipo.getScene().getWindow();
    primaryStage.setTitle("Vitacura");
    primaryStage.setScene(new Scene(root, 1366, 768));
    primaryStage.show();
  }

  @FXML
  public void registrarTipo(ActionEvent event) {

    String nombre = textFiedlNuevoTipo.getText();
    String query;
    Random r = new Random();
    int id = r.nextInt(9999999) + 1;
    if(!nombre.equals("")) {
      if(!existe("tipo", nombre, "categoria")) {
        query = "INSERT INTO tipo VALUES (" + id + ", '" + nombre + "')";
        executeQuery(query);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("La operacion resulto con exito!");
        alert.showAndWait();
        textFiedlNuevoTipo.clear();
      }else {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("La categoria ya existe");
        alert.showAndWait();
      }
    }else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error Dialog");
      alert.setHeaderText("Campo vacio");
      alert.showAndWait();
    }
    llenarCombo("tipo");
  }

  @FXML
  public void modificarTipo(ActionEvent event) {
    String nombre = (String) comboBoxTipo.getSelectionModel().getSelectedItem();
    String query;
    if(nombre != null) {
      TextInputDialog dialog = new TextInputDialog(nombre);
      dialog.setTitle("Modificar tipo");
      dialog.setHeaderText("Se cambiara la categoria, pero el id se conservara");
      dialog.setContentText("Por favor ingrese el nuevo tipo:");

      Optional<String> result = dialog.showAndWait();

      if(result.isPresent()) {
        if(result.get() != null) {
          if(result.get().equals(nombre)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("La categoria que ingreso es la misma a la anterios\nLa operacion resulto con exito!");
            alert.showAndWait();
          } else {
            if(!existe("tipo", result.get(),"categoria")) {
              query = "UPDATE tipo SET categoria = '"+result.get()+"' WHERE categoria = '"+nombre+"'";
              Alert alert = new Alert(AlertType.INFORMATION);
              alert.setTitle("Information Dialog");
              alert.setHeaderText(null);
              alert.setContentText("La operacion resulto con exito!");
              alert.showAndWait();
              executeQuery(query);
            } else {
              Alert alert = new Alert(AlertType.ERROR);
              alert.setTitle("Error Dialog");
              alert.setHeaderText("La categoria ya existe, intente de nuevo");
              alert.showAndWait();
            }
          }
        }else {
          Alert alert = new Alert(AlertType.ERROR);
          alert.setTitle("Error Dialog");
          alert.setHeaderText("Ingrese una categoria valida");
          alert.showAndWait();
        }
      } else {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("No se aplicaron cambios");
        alert.showAndWait();
      }
    }else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error Dialog");
      alert.setHeaderText("Seleccione una categoria");
      alert.showAndWait();
    }
    llenarCombo("tipo");
  }

  @FXML
  public void eliminarTipo(ActionEvent event) {
    String nombre = (String) comboBoxTipo.getSelectionModel().getSelectedItem();
    String query;
    if(nombre != null) {
      if(existe("producto", ""+buscarId(nombre, "tipo", "categoria"), "tipo_id")) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Existe productos registrados con esta categoria,\nelimine o edite los productos para realizar esta operacion");
        alert.showAndWait();
        return;
      }
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Eliminar tipo");
      alert.setHeaderText("Se eliminara "+nombre+" de forma permanente");
      alert.setContentText("�Estas seguro de esto?");

      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == ButtonType.OK){
        query = "DELETE FROM tipo WHERE categoria = '"+nombre+"'";

        executeQuery(query);
      } else {
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("No se aplicaron cambios");

        alert.showAndWait();
      }
    } else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error Dialog");
      alert.setHeaderText("Seleccione una categoria");
      alert.showAndWait();
    }
    llenarCombo("tipo");
  }

  public void llenarCombo(String tabla) {
    Statement st;
    ResultSet rs;
    seleccionTipo = FXCollections.observableArrayList();

    String query = "select * from "+tabla;

    try {
      st = conn.createStatement();
      rs = st.executeQuery(query);
      while(rs.next()) {
        seleccionTipo.add(rs.getString("categoria"));
      }
    }catch(Exception e) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error Dialog");
      alert.setHeaderText(e.getMessage());
      alert.showAndWait();
    }
    comboBoxTipo.setItems(seleccionTipo);
  }

  public int buscarId(String referencia, String tabla, String condicion) {

    Statement st;
    ResultSet rs;

    String query;

    try {
      query = "select id from "+tabla+" where "+condicion+" = '"+referencia+"'";
      st = conn.createStatement();
      rs = st.executeQuery(query);
      rs.next();
      System.out.println(query);
      return rs.getInt("id");
    } catch(Exception event) {
      return 0;
    }

  }
}
