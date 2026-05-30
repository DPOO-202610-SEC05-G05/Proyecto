package uniandes.dpoo.proyecto.cafe;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Cliente extends Usuario {
    private int puntosFidelidad;
    private List<String> juegosFavoritos;
    private List<String> historialCompras;
    
    public Cliente(int id, String username, String email, String password, int puntosFidelidad, boolean esValido){
        super(id, username, email, password, esValido);
        this.puntosFidelidad = puntosFidelidad;
        this.juegosFavoritos = new ArrayList<>();
        this.historialCompras = new ArrayList<>();
    }

    public int getPuntos(){
        return puntosFidelidad;
    }

    public void setPuntos(int puntos) {
        this.puntosFidelidad = puntos;
    }

    public List<String> getJuegosFavoritos(){
        return juegosFavoritos;
    }

    public List<String> getHistorialDeCompras(){
        return historialCompras;
    }

    public void setJuegosFavoritos(String juego){
        juegosFavoritos.add(juego);
    }

    public void reservarMesa(Mesa mesa, int numPersonas, boolean hayMenores){
        if(mesa == null){
            System.out.println("No se ha recibido ninguna mesa.");
        } else if (numPersonas > mesa.getCapacidad()) {
            System.out.println("La capacidad de las personas supera la capacidad de la mesa.");
        } else {
            mesa.setNumPersonas(numPersonas); 
            mesa.setHayMenores(hayMenores);
            mesa.setOcupada(true);
        }    
    }

    public void reservarJuegos(Juego juego, Mesa mesa, Prestamo prestamo, InventarioPrestamo inventarioPrestamo) {

        if (mesa == null || !mesa.getOcupada()) {
            System.out.println("Debe tener una mesa asignada para pedir juegos.");
        }
    
        if (mesa.getJuegosEnMesa().size() >= 2) {
            System.out.println("Límite alcanzado: No puede tener más de dos juegos en la mesa.");
        }
    
        if (inventarioPrestamo.getCantidadDisponible() <= 0) {
            System.out.println("El juego " + juego.getNombre() + " se encuentra prestado actualmente.");
        }
    
        if (!prestamo.esAptoJugadores(mesa, juego) || !prestamo.esAptoEdad(mesa, juego)) {
            System.out.println("El juego no es apto para los comensales de esta mesa.");
        }
    
        mesa.agregarJuego(juego);
        inventarioPrestamo.setCantidadDisponible(inventarioPrestamo.getCantidadDisponible() - 1);
        prestamo.registrarPrestamo(juego);
        System.out.println("Juego '" + juego.getNombre() + "' prestado con éxito.");
    }
    

    public List<String> juegoDisponibles(List<InventarioPrestamo> inventarioPrestamo) {
        List<String> disponibles = new ArrayList<>();
    
        for (InventarioPrestamo i : inventarioPrestamo) {
            if (i.getCantidadDisponible() > 0) {
                disponibles.add(i.getJuego().getNombre());
            }
        }
    
        return disponibles;
    }
    
    public void regresarJuego(Juego juego, Mesa mesa, Prestamo prestamo, InventarioPrestamo inventarioPrestamo) {
        if (mesa != null && mesa.getJuegosEnMesa().contains(juego.getNombre())) {
            mesa.getJuegosEnMesa().remove(juego.getNombre());
            inventarioPrestamo.setCantidadDisponible(inventarioPrestamo.getCantidadDisponible() + 1);
            prestamo.finalizarPrestamo();
            System.out.println("Juego devuelto correctamente.");
        } 
        
        else {
            System.out.println("Error: El juego no se encuentra registrado en esta mesa.");
        }
    }

    public VentaJuego comprarJuego(Juego juegoVenta, int idVenta, InventarioVenta inventarioVenta) {
        if (inventarioVenta.getCantidadTotal() > 0) {
            VentaJuego venta = new VentaJuego(idVenta, LocalDate.now(), juegoVenta.getPrecio(), 0.0, this, true);
            
            inventarioVenta.setCantidadTotal(inventarioVenta.getCantidadTotal() - 1);
            this.historialCompras.add("Juego: " + juegoVenta.getNombre());
            int puntosGanados = (int) (venta.getTotal() * 0.01);
            this.puntosFidelidad += puntosGanados;
            System.out.println("Compra exitosa. Ganaste " + puntosGanados + " puntos de fidelidad.");
            return venta;
        } else {
            System.out.println("Lo sentimos, no hay copias disponibles para venta.");
            return new VentaJuego(0, LocalDate.now(), 0.0, 0.0, this, false); 
        }
    }

    public VentaCafeteria comprarProducto(ProductoMenu producto, Mesa mesa, int idVenta) {
        if (producto instanceof Bebida) {
            Bebida bebida = (Bebida) producto;
            if (bebida.getEsAlcoholica() && mesa.getHayMenores()) {
                System.out.println("Venta rechazada: No se sirve alcohol en mesas con menores.");
                return new VentaCafeteria(0, LocalDate.now(), 0.0, this, false);
            }
        }
        
        VentaCafeteria venta = new VentaCafeteria(idVenta, LocalDate.now(), producto.getPrecio(), this, true);
        this.historialCompras.add("Producto: " + producto.getNombre());
        
        int puntosGanados = (int) (venta.getTotal() * 0.01);
        this.puntosFidelidad += puntosGanados;
        
        System.out.println("Pedido de cafetería exitoso. Ganaste " + puntosGanados + " puntos.");
        return venta;
    }
}


