package javafx.productos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import clases.*;

public class Controller extends Padre{

  @FXML
  private TextField nombre, condicion, id, eliminar;

  @FXML
  private Button editarBtn, registrarBtn;

  @FXML
  private ComboBox<String> tipo;

  @FXML
  private DatePicker fechaIngreso;

  @FXML
  private AnchorPane anchorProductos;

  @FXML
  private TableView<Producto> tabla;

  @FXML
  private TableColumn<?, ?> idCol, idTipoCol, nombreCol, fechaIngresoCol, condicionCol;

  private ArrayList<String> tipoAL = new ArrayList<>();
  private ObservableList<Producto> data = FXCollections.observableArrayList();

  @FXML
  private void loadProductos(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/javafx/tipoProductos/sample.fxml"));
    cambiarEscena(root);
  }

  @FXML
  private void loadInicio(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/javafx/inicio/sample.fxml"));
    cambiarEscena(root);
  }

  @FXML
  private void loadReportes(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/javafx/reportes/sample.fxml"));
    cambiarEscena(root);
  }

  private void cambiarEscena(Parent root) {
    Stage primaryStage = (Stage) anchorProductos.getScene().getWindow();
    primaryStage.setTitle("Vitacura");
    primaryStage.setScene(new Scene(root, 1366, 768));
    primaryStage.show();
  }

  @FXML
  private void ingresar() throws SQLException, ParseException {
    String query = "select id from tipo where categoria = '" + tipo.getValue() + "'";
    Connection conn = connector.Connector.getConnection();
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery(query);

    int idTipo = 0;
    if (rs.next()) {
      idTipo = rs.getInt(1);
    }

    LocalDate fecha = fechaIngreso.getValue();
    query =
        "insert into producto values(" + idTipo + ",'" + nombre.getText() + "','" + fecha + "','"
            + condicion.getText() + "'," + Integer.parseInt(id.getText()) + ")";
    executeQuery(query);
    try {
      actualizarTabla();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void editar() throws SQLException {
    String query = "select id from tipo where categoria = '" + tipo.getValue() + "'";
    Connection conn = connector.Connector.getConnection();
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery(query);

    int idTipo = 0;
    if (rs.next()) {
      idTipo = rs.getInt(1);
    }

    query = "UPDATE producto SET tipo_id = " + idTipo + ", nombre = '" + nombre.getText()
        + "', fecha_ingreso = '" + fechaIngreso.getValue() + "', condicion = '" + condicion
        .getText() + "' where  id = " + id.getText();
    try {
      actualizarTabla();
    } catch (Exception e) {
      e.printStackTrace();
    }

    nombre.clear();
    id.clear();
    fechaIngreso.setValue(null);
    tipo.setValue(null);
    condicion.clear();
    editarBtn.setVisible(false);
    registrarBtn.setVisible(true);
    eliminar.clear();
  }

  @FXML
  private void eliminar() {
    String query = "delete from producto where id = " + Integer.parseInt(eliminar.getText());
    executeQuery(query);
    try {
      actualizarTabla();
    } catch (Exception e) {
      e.printStackTrace();
    }

    nombre.clear();
    id.clear();
    fechaIngreso.setValue(null);
    tipo.setValue(null);
    condicion.clear();
    editarBtn.setVisible(false);
    registrarBtn.setVisible(true);
    eliminar.clear();
  }

  @FXML
  private void initialize() throws SQLException {

    idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    idTipoCol.setCellValueFactory(new PropertyValueFactory<>("idTipo"));
    nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    fechaIngresoCol.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));
    condicionCol.setCellValueFactory(new PropertyValueFactory<>("condicion"));

    actualizarTabla();

    String query = "select categoria from tipo";
    Connection conn = connector.Connector.getConnection();
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery(query);

    while (rs.next()) {
      tipoAL.add(rs.getString("categoria"));
    }

    tipo.getItems().setAll(tipoAL);

    tabla.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
        if (tabla.getSelectionModel().getSelectedItem() != null) {
          Producto producto = tabla.getSelectionModel().getSelectedItem();

          nombre.setText(producto.getNombre());
          condicion.setText(producto.getCondicion());
          id.setText(Integer.toString(producto.getId()));
          eliminar.setText(Integer.toString(producto.getId()));

          LocalDate localDate = LocalDate.parse(producto.getFechaIngreso());
          fechaIngreso.setValue(localDate);

          String query2 = "SELECT categoria FROM tipo where id = " + producto.getIdTipo();
          try {
            ResultSet rs = st.executeQuery(query2);
            if (rs.next()) {
              tipo.setValue(rs.getString(1));
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }

          editarBtn.setVisible(true);
          registrarBtn.setVisible(false);
        }
      }
    });
  }

  private void actualizarTabla() throws SQLException {
    data.clear();
    String query = "select * from producto";
    Connection conn = connector.Connector.getConnection();
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery(query);

    while (rs.next()) {
      data.add(new Producto(rs.getInt("id"), rs.getInt("tipo_id"), rs.getString("nombre"),
          String.valueOf(rs.getDate("fecha_ingreso")), rs.getString("condicion")));
    }

    tabla.setItems(data);
  }
}
