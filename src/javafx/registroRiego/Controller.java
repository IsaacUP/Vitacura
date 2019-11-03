package javafx.registroRiego;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import connector.Connector;
import clases.Padre;
import clases.CalendarioIndividual;

public class Controller extends Padre implements Initializable {
  @FXML
  AnchorPane anchorRegistro;

  @FXML private DatePicker calendario;

  @FXML private ComboBox<String> comboBoxProductos;

  @FXML private TableView<CalendarioIndividual> tablaRegistro;

  @FXML private Button botonEliminar;
  @FXML private TextField textFieldFecha;
  @FXML private TextField textFieldNombre;
  @FXML private TextField textFieldId;


  private Connection conn;

  private ObservableList<String> seleccionProducto = FXCollections.observableArrayList();
  private  ObservableList<CalendarioIndividual> data = FXCollections.observableArrayList();

  @FXML
  private void loadInicio() throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("/javafx/inicio/sample.fxml"));
    Stage primaryStage = (Stage) anchorRegistro.getScene().getWindow();
    primaryStage.setTitle("Vitacura");
    primaryStage.setScene(new Scene(root, 1366, 768));
    primaryStage.show();
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    conn = Connector.getConnection();

    seleccionProducto = llenarCombo("producto","nombre");
    comboBoxProductos.setItems(seleccionProducto);

    data = llenarComboEditar();
    tablaRegistro.getItems().setAll(this.data);

    deleteTools(false);

    tablaRegistro.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
        if(tablaRegistro.getSelectionModel().getSelectedItem() != null) {
          CalendarioIndividual aux = tablaRegistro.getSelectionModel().getSelectedItem();
          deleteTools(true);
          textFieldFecha.setText(aux.getFecha());
          textFieldNombre.setText(aux.getName());
          textFieldId.setText(""+aux.getProductoId());
        }
      }
    });
  }


  @FXML
  public void registroRiego(ActionEvent event) {

    Random r = new Random();
    int id = r.nextInt(9999999) + 1;
    LocalDate fecha = calendario.getValue();
    String producto = (String) comboBoxProductos.getSelectionModel().getSelectedItem();
    String query;
    System.out.println(fecha);
    if(fecha != null && producto != null) {
      String idProducto = encontrarId("id", "producto","nombre",producto);
      if(idProducto != null) {
        query = "INSERT INTO calendario_individual VALUES ('"+fecha+"', " + id + ", " + idProducto+")";
        executeQuery(query);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("La operacion resulto con exito!\nSe registro el riego del "+ fecha);
        alert.showAndWait();
      }
    } else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error Dialog");
      alert.setHeaderText("Campo/s vacio/s");
      alert.showAndWait();
    }
    data = llenarComboEditar();
    tablaRegistro.getItems().setAll(this.data);
    seleccionProducto = llenarCombo("producto","nombre");
  }
  @FXML
  public void eliminar(ActionEvent event) {

    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Eliminar tipo");
    alert.setHeaderText("Se eliminara el riego de "+textFieldNombre.getText()+" con la fecha de "+textFieldFecha.getText()+" e identificacion "+textFieldId.getText()+" de forma permanente");
    alert.setContentText("�Estas seguro de esto?");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK){
      String query = "DELETE FROM calendario_individual WHERE id = "+textFieldId.getText();
      executeQuery(query);
      data = llenarComboEditar();
      tablaRegistro.getItems().setAll(this.data);
      alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Information Dialog");
      alert.setHeaderText(null);
      alert.setContentText("La operacion resulto con exito!");
      alert.showAndWait();
      deleteTools(false);
    } else {
      alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Information Dialog");
      alert.setHeaderText(null);
      alert.setContentText("No se aplicaron cambios");
      alert.showAndWait();;
    }
  }

  public ObservableList<String> llenarCombo(String tabla, String columna) {
    Statement st;
    ResultSet rs;
    ObservableList<String >list = FXCollections.observableArrayList();

    String query = "select * from "+tabla;

    try {
      st = conn.createStatement();
      rs = st.executeQuery(query);
      while(rs.next()) {
        list.add(rs.getString(columna));
      }
    }catch(Exception e) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error Dialog");
      alert.setHeaderText(e.getMessage());
      alert.showAndWait();
    }
    return list;
  }

  public ObservableList<CalendarioIndividual> llenarComboEditar() {
    Statement st;
    ResultSet rs;
    ObservableList<CalendarioIndividual >list = FXCollections.observableArrayList();

    String query = "SELECT fecha, nombre, c.id FROM calendario_individual c, producto p\r\n" +
        "WHERE p.id = c.producto_id";

    try {
      st = conn.createStatement();
      rs = st.executeQuery(query);
      while(rs.next()) {
        CalendarioIndividual calendario = new CalendarioIndividual(rs.getString(1), rs.getString(2), rs.getInt(3));
        list.add(calendario);
      }
    }catch(Exception e) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error Dialog");
      alert.setHeaderText(e.getMessage());
      alert.showAndWait();
    }
    return list;
  }

  public String encontrarId(String encontrar, String tabla, String condicion, String value) {
    Statement st;
    ResultSet rs;

    String query = "select "+encontrar+" from "+tabla+" where "+condicion+" = '"+value+"'";
    try {
      st = conn.createStatement();
      rs = st.executeQuery(query);
      if(rs.next()) {
        return rs.getString(encontrar);
      } else {
        return null;
      }
    } catch(Exception e) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error Dialog");
      alert.setHeaderText(e.getMessage());
      alert.showAndWait();
      return null;
    }
  }

  public void actualizarTabla() {
    data = llenarComboEditar();
    tablaRegistro.getItems().setAll(this.data);
  }

  public void deleteTools(boolean estado) {
    botonEliminar.setVisible(estado);
    textFieldFecha.setVisible(estado);
    textFieldNombre.setVisible(estado);
    textFieldId.setVisible(estado);
    if(!estado) {
      textFieldFecha.clear();
      textFieldNombre.clear();
      textFieldId.clear();
    }
  }
}
