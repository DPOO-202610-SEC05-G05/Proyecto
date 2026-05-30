package uniandes.dpoo.proyecto.cafe;
import java.util.List;

public class Administrador extends Usuario {

    public Administrador(int id, String username, String email, String password, boolean esValido){
        super(id, username, email, password, esValido);
    }

    public String getEstadoJuego(Juego juego){
        return juego.getEstado();
    }

    public void setEstadoJuego(Inventario inventario, String newEstado){
        inventario.setEstado(newEstado);
    }

    public void agregarJuegoPrestamo(InventarioPrestamo inventario, int cantidad){
        inventario.setCantidadTotal(inventario.getCantidadTotal() + cantidad);
        inventario.setCantidadDisponible(inventario.getCantidadDisponible() + cantidad);
    }
    public void agregarJuegoVenta(InventarioVenta inventario, int cantidad){
        inventario.setCantidadTotal(inventario.getCantidadTotal() + cantidad);
    }

    public List<Prestamo> verHistorialPrestamo(HistorialPrestamo historial){
        return historial.getPrestamos();
    }

    public void repararJuego(InventarioVenta inventarioVenta, InventarioPrestamo inventarioPrestamo){
        if(inventarioVenta.getCantidadTotal() > 0) {
            inventarioVenta.setCantidadTotal(inventarioVenta.getCantidadTotal() - 1);
            inventarioPrestamo.setCantidadTotal(inventarioPrestamo.getCantidadTotal() + 1);
            inventarioPrestamo.setCantidadDisponible(inventarioPrestamo.getCantidadDisponible() + 1);
            inventarioPrestamo.setEstado("Nuevo");
        } else {
            System.out.println("No hay copias en el inventario de venta para reparar este juego.");
        }
    }

    public boolean aprobarSugerenciaPlatillo(String sugerencia){
        return true;
    }

    public List<Venta> ventasDetalladas(List<Venta> historialVentas, String rubroSeleccionado, String fecha){
        return historialVentas;
    }

}