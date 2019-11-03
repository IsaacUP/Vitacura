package javafx.reportes;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import connector.Connector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller implements Initializable {

  @FXML AnchorPane anchorReportes;
  @FXML private ComboBox<String> comboBoxReportes;
  @FXML private Button botonAceptar;
  @FXML private LineChart<String, Integer> chartBarras;
  @FXML private CategoryAxis xAxis;
  @FXML PieChart pieChart;
  private ObservableList<String> seleccionTipo = FXCollections.observableArrayList();
  private ObservableList<String> leyenda= FXCollections.observableArrayList();
  private XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
  private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
  private Connection conn;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    generarOpciones();
    conn = Connector.getConnection();
    botonAceptar.setVisible(false);


  }
  @FXML
  private void loadInicio() throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("/javafx/inicio/sample.fxml"));
    Stage primaryStage = (Stage) anchorReportes.getScene().getWindow();
    primaryStage.setTitle("Vitacura");
    primaryStage.setScene(new Scene(root, 1366, 768));
    primaryStage.show();
  }

  @FXML
  public void actionCombo(ActionEvent event) {
    botonAceptar.setVisible(true);
  }
  @FXML
  public void generarReportes(ActionEvent event) {
    String reporte = (String) comboBoxReportes.getSelectionModel().getSelectedItem();
    String query = "";

    switch(reporte) {
      case "Cantidad de productos por tipo de producto":
        query = "SELECT t.categoria, count(p.tipo_id) total_productos FROM producto p, tipo t\r\n" +
            "WHERE t.id = p.tipo_id\r\n" +
            "GROUP BY p.tipo_id, t.categoria";
        break;
      case "Cantidad de riego por dia":
        query = "SELECT fecha, COUNT(fecha) total_riego FROM calendario_individual\r\n" +
            "GROUP BY fecha";
        break;
      case "Cantidad de productos por condicion actual":
        query = "SELECT condicion, COUNT(condicion) FROM producto\r\n" +
            "GROUP BY condicion";
        break;
      case "Cantida de fotografias por tipo de producto":
        query = "SELECT nombre, COUNT(producto_id) fotos_total FROM producto p, historial_fotografias f\r\n" +
            "WHERE p.id = f.producto_id\r\n" +
            "GROUP BY nombre, producto_id";
        break;
    }
    System.out.println(reporte+"\n"+query);
    generarGraficas(query, reporte);
    botonAceptar.setVisible(false);
  }
  public void generarOpciones() {
    seleccionTipo.add("Cantidad de productos por tipo de producto");
    seleccionTipo.add("Cantidad de riego por dia");
    seleccionTipo.add("Cantidad de productos por condicion actual");
    seleccionTipo.add("Cantida de fotografias por tipo de producto");

    comboBoxReportes.setItems(seleccionTipo);
  }
  public void generarGraficas(String query, String titulo) {
    chartBarras.getData().clear();
    pieChart.getData().clear();
    Statement st;
    ResultSet rs;

    try {
      st = conn.createStatement();
      rs = st.executeQuery(query);
      java.sql.ResultSetMetaData  columns =  rs.getMetaData();
      while(rs.next()) {

        System.out.println(rs.getString(1)+" "+rs.getInt(2));
        leyenda.add(""+rs.getString(1));
        dataSeries.setName(columns.getColumnName(1));
        dataSeries.getData().add(new XYChart.Data<>(""+rs.getString(1), rs.getInt(2)));
        pieChartData.add(new PieChart.Data(rs.getString(1), rs.getInt(2)));
        xAxis.setCategories(leyenda);
        chartBarras.setTitle(titulo);
        pieChart.setTitle(titulo);
      }
      chartBarras.getData().addAll(dataSeries);
      pieChart.getData().addAll(pieChartData);
    } catch(Exception event) {
      event.printStackTrace();
    }
    reiniciarData();
  }
  public void reiniciarData() {
    seleccionTipo = FXCollections.observableArrayList();
    leyenda= FXCollections.observableArrayList();
    dataSeries = new XYChart.Series<>();
    pieChartData = FXCollections.observableArrayList();
  }

}
