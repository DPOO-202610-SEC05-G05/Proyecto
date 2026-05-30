package uniandes.dpoo.proyecto.cafe;

import java.util.Map;

public class TorneoCompetitivo extends Torneo {
    private double tarifaEntrada;

    public TorneoCompetitivo(Juego juego, int cuposMaximos, Turno turno, boolean esValido) {
        super(juego, cuposMaximos, turno, esValido);
        this.tarifaEntrada = 20000;
    }

    public double calcularRecaudo() {
        double total = 0;
        for (Map.Entry<Usuario, Integer> entry : cuposUsuario.entrySet()) {
            if (!(entry.getKey() instanceof Empleado)) {
                total += entry.getValue() * tarifaEntrada;
            }
        }
        return total;
    }

    @Override
    public double calcularPremio() {
        return calcularRecaudo() * 0.70;
    }

    @Override
    public void finalizarTorneo(Usuario ganador) {
        if (ganador == null) {
            System.out.println("No hay ganador.");
            return;
        }
        if (ganador instanceof Empleado) {
            System.out.println("Torneo Competitivo finalizado. El ganador es empleado, felicitaciones, pero NO recibe premio en dinero.");
            return;
        }

        double premio = calcularPremio();
        System.out.println("Torneo Competitivo finalizado. Premio entregado al ganador: $" + premio);
    }

    @Override
    public boolean isAmistoso() {
        return false;
    }
}