package clases;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Producto {

  private final SimpleIntegerProperty id;
  private final SimpleIntegerProperty idTipo;
  private final SimpleStringProperty nombre;
  private final SimpleStringProperty fechaIngreso;
  private final SimpleStringProperty condicion;

  public Producto(int id, int idTipo, String nombre, String fechaIngreso, String condicion) {
    this.id = new SimpleIntegerProperty(id);
    this.idTipo = new SimpleIntegerProperty(idTipo);
    this.nombre = new SimpleStringProperty(nombre);
    this.fechaIngreso = new SimpleStringProperty(fechaIngreso);
    this.condicion = new SimpleStringProperty(condicion);
  }

  public int getId() {
    return id.get();
  }

  public SimpleIntegerProperty idProperty() {
    return id;
  }

  public void setId(int id) {
    this.id.set(id);
  }

  public int getIdTipo() {
    return idTipo.get();
  }

  public SimpleIntegerProperty idTipoProperty() {
    return idTipo;
  }

  public void setIdTipo(int idTipo) {
    this.idTipo.set(idTipo);
  }

  public String getNombre() {
    return nombre.get();
  }

  public SimpleStringProperty nombreProperty() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre.set(nombre);
  }

  public String getFechaIngreso() {
    return fechaIngreso.get();
  }

  public SimpleStringProperty fechaIngresoProperty() {
    return fechaIngreso;
  }

  public void setFechaIngreso(String fechaIngreso) {
    this.fechaIngreso.set(fechaIngreso);
  }

  public String getCondicion() {
    return condicion.get();
  }

  public SimpleStringProperty condicionProperty() {
    return condicion;
  }

  public void setCondicion(String condicion) {
    this.condicion.set(condicion);
  }
}
