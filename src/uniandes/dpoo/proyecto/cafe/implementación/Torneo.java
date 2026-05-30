package uniandes.dpoo.proyecto.cafe;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Torneo {
    protected Juego juego;
    protected int cuposMaximos;
    protected List<Usuario> inscritos;
    protected int cuposFan;
    protected Map<Usuario, Integer> cuposUsuario;
    protected Turno turno;
    protected boolean esValido;

    public Torneo(Juego juego, int cuposMaximos, Turno turno, boolean esValido){
        this.juego = juego;
        this.cuposMaximos = cuposMaximos;
        this.cuposFan = (int) Math.ceil(cuposMaximos * 0.20);
        this.inscritos = new ArrayList<>();
        this.cuposUsuario = new HashMap<>();
        this.turno = turno;
        this.esValido = esValido;
    }

    public Juego getJuego(){ return juego; }
    public Turno getTurno(){ return turno; }
    public int getCuposMaximos(){ return cuposMaximos; }
    public List<Usuario> getInscritos(){ return inscritos; }
    public int getCuposFan(){ return cuposFan; }
    public boolean isValido(){ return esValido; }

    public int totalCuposTomados(){
        int total = 0;
        for(int cupos : cuposUsuario.values()){
            total += cupos;
        }
        return total;
    }

    protected boolean isFan(Usuario usuario){
        if(usuario instanceof Cliente){
            Cliente c = (Cliente) usuario;
            return c.getJuegosFavoritos().contains(juego.getNombre());
        }
        return false;
    }

    protected int totalCuposNoFanTomados(){
        int total = 0;
        for(Usuario u: cuposUsuario.keySet()){
            if(!isFan(u)){
                total += cuposUsuario.get(u);
            }
        }
        return total;
    }

    public boolean inscribir(Usuario usuario, int cantidad){
        if(usuario == null){
            System.out.println("Lo sentimos, el Usuario no existe. ");
            return false;
        }
        
        int max = cuposUsuario.getOrDefault(usuario, 0);

        if(totalCuposTomados() + cantidad > cuposMaximos){
            System.out.println("Lo sentimos, ya no hay cupos disponibles. ");
            return false;
        }
        if(max + cantidad > 3){
            System.out.println("Lo sentimos, solo admitimos que estés inscrito a máximo 3 cupos.");
            return false;
        }
        if(usuario instanceof Empleado){
            Empleado e = (Empleado) usuario;
            if(e.enTurno(turno)){
                System.out.println("El empleado no puede inscribirse porque está en turno.");
                return false;
            }
        }
        
        if(!isFan(usuario)){
            int cuposRegulares = cuposMaximos - cuposFan;
            if(totalCuposNoFanTomados() + cantidad > cuposRegulares){
                System.out.println("Lo sentimos, los cupos restantes están reservados para los fans de este juego.");
                return false;
            }
        }
        
        inscritos.add(usuario);
        cuposUsuario.put(usuario, cuposUsuario.getOrDefault(usuario, 0) + cantidad);
        return true;
    }

    public boolean desinscribir(Usuario usuario){
        if(usuario == null){
            System.out.println("Lo sentimos, el Usuario no existe. ");
            return false;
        }

        int cupos = cuposUsuario.getOrDefault(usuario, 0);

        if (cupos == 0){
            System.out.println("Lo sentimos, usted no tiene inscripciones. ");
            return false;
        }

        inscritos.remove(usuario);
        cuposUsuario.remove(usuario);
        System.out.println("El usuario se ha desinscrito satisfactoriamente. ");
        return true;
    }

    public abstract double calcularPremio();
    public abstract void finalizarTorneo(Usuario ganador);
    public abstract boolean isAmistoso();
}