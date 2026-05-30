package uniandes.dpoo.proyecto.cafe;

public class TorneoAmistoso extends Torneo {
    private double bonoDescuento;

    public TorneoAmistoso(Juego juego, int cuposMaximos, Turno turno, boolean esValido) {
        super(juego, cuposMaximos, turno, esValido);
        this.bonoDescuento = 0.10; 
    }

    @Override
    public double calcularPremio() {
        return bonoDescuento;
    }

    @Override
    public void finalizarTorneo(Usuario ganador) {
        if (ganador == null) {
            System.out.println("No hay ganador.");
            return;
        }
        double bono = calcularPremio();
        System.out.println("Torneo Amistoso finalizado. Ganador recibe bono de descuento del " + (bono * 100) + "%.");
    }

    @Override
    public boolean isAmistoso() {
        return true;
    }
}