package clases;

public class CalendarioIndividual {
  private String fecha;
  private String name;
  private int productoId;


  public CalendarioIndividual(String fecha, String name, int productoId) {
    this.fecha = fecha;
    this.name = name;
    this.productoId = productoId;
  }
  public String getFecha() {
    return fecha;
  }
  public void setFecha(String fecha) {
    this.fecha = fecha;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public int getProductoId() {
    return productoId;
  }
  public void setProductoId(int productoId) {
    this.productoId = productoId;
  }
}
