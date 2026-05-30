package uniandes.dpoo.proyecto.cafe;

import java.util.List;
import java.util.ArrayList;

public class Mesa {
    private int numMesa;
    private int capacidad;
    private int numPersonas;
    private boolean hayMenores;
    private boolean hayJovenes;
    private boolean ocupada;
    private List<String> juegosEnMesa;

    public Mesa(int capacidad){
        this.capacidad = capacidad;
        this.numMesa = 0;
        this.numPersonas = 0;
        this.hayMenores = false;
        this.hayJovenes = false;
        this.ocupada = false;
        this.juegosEnMesa = new ArrayList<>();
    }

    public int getNumMesa(){
        return numMesa;
    }

    public int getCapacidad(){
        return capacidad;
    }

    public int getNumPersonas(){
        return numPersonas;
    }

    public boolean getHayMenores(){
        return hayMenores;
    }

    public boolean getHayJovenes(){
        return hayJovenes;
    }

    public boolean getOcupada(){
        return ocupada;
    }

    public List<String> getJuegosEnMesa(){
        return juegosEnMesa;
    }


    public void setNumPersonas(int numPersonas){
        this.numPersonas = numPersonas;
    }

    public void setHayMenores(boolean hayMenores){
        this.hayMenores = hayMenores;
    }

    public void setOcupada(boolean ocupada){
        this.ocupada = ocupada;
    }

    public void agregarJuego(Juego juego){
        if (juego == null){
            System.out.println("no hay juego");
        }

        if (juegosEnMesa.size() >= 2){
            System.out.println("no se pueden tener más de 2 juegos en la mesa");
        }

        if (!juegoAptoParaMesa(juego)){
            System.out.println("juego no es apto para esta mesa");
        }
        String nombreJuego = juego.getNombre();
        juegosEnMesa.add(nombreJuego);
        System.out.println("agregado correctamente");
    }

    public boolean juegoAptoParaMesa(Juego juego){
        if(this.numPersonas > this.capacidad){
            return false;
        }
        
        if ((this.numPersonas < juego.getMinJugadores()) || (this.numPersonas > juego.getMaxJugadores())){
            return false;
        }

        if (this.hayMenores && juego.getEdadMinima() > 5){
            return false;
        }

        if (this.hayJovenes && juego.getEdadMinima() >= 18){
            return false;
        }

            return true;
    }
}
