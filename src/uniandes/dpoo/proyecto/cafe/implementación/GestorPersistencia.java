package uniandes.dpoo.proyecto.cafe;
import java.io.*;
import java.util.*;
import java.time.LocalDate;

public class GestorPersistencia {
    private final String RUTA_DATOS = "datos_aplicacion/";

    public GestorPersistencia() {
        File folder = new File(RUTA_DATOS);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public void guardarUsuarios(List<Usuario> usuarios) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_DATOS + "usuarios.csv"))) {
            for (Usuario u : usuarios) {
                String tipo = u.getClass().getSimpleName();
                String base = tipo + ";" + u.getId() + ";" + u.getUsername() + ";" + u.getEmail() + ";" + u.getPassword();
                
                if (u instanceof Cliente) {
                    base += ";" + ((Cliente) u).getPuntos() + ";N/A";
                } else if (u instanceof Mesero) {
                    List<String> conocidos = ((Mesero) u).getJuegosConocidos();
                    String juegosStr = conocidos != null && !conocidos.isEmpty() ? String.join(",", conocidos) : "Ninguno";
                    base += ";0;" + juegosStr;
                } else {
                    base += ";0;N/A";
                }
                writer.println(base);
            }
        }
    }

    public List<Usuario> cargarUsuarios() throws IOException {
        List<Usuario> usuarios = new ArrayList<>();
        File file = new File(RUTA_DATOS + "usuarios.csv");
        if (!file.exists()) return usuarios;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] p = linea.split(";");
                String tipo = p[0];
                int id = Integer.parseInt(p[1]);
                String user = p[2];
                String email = p[3];
                String pass = p[4];

                if (tipo.equals("Administrador")) {
                    usuarios.add(new Administrador(id, user, email, pass, true));
                } else if (tipo.equals("Cliente")) {
                    int puntos = Integer.parseInt(p[5]);
                    usuarios.add(new Cliente(id, user, email, pass, puntos, true));
                } else if (tipo.equals("Mesero")) {

                    List<String> juegosConocidos = new ArrayList<>();
                    if (p.length > 6 && !p[6].equals("Ninguno") && !p[6].equals("N/A")) {
                        juegosConocidos.addAll(Arrays.asList(p[6].split(",")));
                    }
                    usuarios.add(new Mesero(id, user, email, pass, null, new ArrayList<>(), juegosConocidos, true));
                } else if (tipo.equals("Cocinero")) {
                    usuarios.add(new Cocinero(id, user, email, pass, null, true));
                }
            }
        }
        return usuarios;
    }

    public void guardarInventarioVenta(List<InventarioVenta> inventarios) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_DATOS + "juegos_venta.csv"))) {
            for (InventarioVenta inv : inventarios) {
                Juego j = inv.getJuego();
                writer.println(j.getId() + ";" + j.getNombre() + ";" + j.getAnioPublicacion() + ";" +
                                j.getEmpresa() + ";" + j.getCategoria() + ";" + j.getMinJugadores() + ";" + j.getMaxJugadores() + ";" +
                                j.getEdadMinima() + ";" + j.isEsDificil() + ";" + j.getEstado() + ";" + j.getPrecio() + ";" +
                                inv.getCantidadTotal() + ";" + inv.getEstado());
            }
        }
    }

    public void guardarInventarioPrestamo(List<InventarioPrestamo> inventarios) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_DATOS + "juegos_prestamo.csv"))) {
            for (InventarioPrestamo inv : inventarios) {
                Juego j = inv.getJuego();
                writer.println(j.getId() + ";" + j.getNombre() + ";" + j.getAnioPublicacion() + ";" +
                j.getEmpresa() + ";" + j.getCategoria() + ";" + j.getMinJugadores() + ";" + j.getMaxJugadores() + ";" +
                j.getEdadMinima() + ";" + j.isEsDificil() + ";" + j.getEstado() + ";" + j.getPrecio() + ";" +
                inv.getCantidadTotal() + ";" + inv.getCantidadDisponible() + ";" + inv.getEstado());
            }
        }
    }

    public List<InventarioVenta> cargarInventarioVenta() throws IOException {
        List<InventarioVenta> inventarios = new ArrayList<>();
        File file = new File(RUTA_DATOS + "juegos_venta.csv");
        if (!file.exists()) return inventarios;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] p = linea.split(";");

                Juego j = new Juego(Integer.parseInt(p[0]), p[1], Integer.parseInt(p[2]), p[3], p[4],
                                    Integer.parseInt(p[5]), Integer.parseInt(p[6]), Integer.parseInt(p[7]),
                                    Boolean.parseBoolean(p[8]), p[9], Double.parseDouble(p[10]));

            InventarioVenta inv = new InventarioVenta(j,Integer.parseInt(p[11]),p[12]);
            inventarios.add(inv);
            }
        }
        return inventarios;
    }

    public List<InventarioPrestamo> cargarInventarioPrestamo() throws IOException {
        List<InventarioPrestamo> inventarios = new ArrayList<>();
        File file = new File(RUTA_DATOS + "juegos_prestamo.csv");
        if (!file.exists()) return inventarios;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] p = linea.split(";");

                Juego j = new Juego(Integer.parseInt(p[0]), p[1], Integer.parseInt(p[2]), p[3], p[4],
                                    Integer.parseInt(p[5]), Integer.parseInt(p[6]), Integer.parseInt(p[7]),
                                    Boolean.parseBoolean(p[8]), p[9], Double.parseDouble(p[10]));

            InventarioPrestamo inv = new InventarioPrestamo(j, Integer.parseInt(p[12]), Integer.parseInt(p[11]), p[13]);
            inventarios.add(inv);
            }
        }
        return inventarios;
    }

    public void guardarVentas(List<Venta> ventas) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_DATOS + "ventas.csv"))) {
            for (Venta v : ventas) {
                if (!v.isValida()) continue;
                String tipo = v.getClass().getSimpleName();
                writer.println(v.getId() + ";" + v.getFecha() + ";" + tipo + ";" + 
                               v.getSubtotal() + ";" + v.getTotal() + ";" + v.getComprador().getId() + ";" + v.isValida());
            }
        }
    }

    public List<Venta> cargarVentas(List<Usuario> usuariosGlobales) throws IOException {
        List<Venta> ventas = new ArrayList<>();
        File file = new File(RUTA_DATOS + "ventas.csv");
        if (!file.exists()) return ventas;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] p = linea.split(";");
                int id = Integer.parseInt(p[0]);
                LocalDate fecha = LocalDate.parse(p[1]);
                String tipo = p[2];
                double subtotal = Double.parseDouble(p[3]);
                int idComprador = Integer.parseInt(p[5]);
                boolean esValida = Boolean.parseBoolean(p[6]);

                Usuario comprador = null;
                for (Usuario u : usuariosGlobales) {
                    if (u.getId() == idComprador) {
                        comprador = u;
                        break;
                    }
                }

                if (tipo.equals("VentaJuego")) {
                    ventas.add(new VentaJuego(id, fecha, subtotal, 0.0, comprador, esValida));
                } else if (tipo.equals("VentaCafeteria")) {
                    ventas.add(new VentaCafeteria(id, fecha, subtotal, comprador, esValida));
                }
            }
        }
        return ventas;
    }

    public void guardarPrestamos(List<Prestamo> prestamos) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_DATOS + "prestamos.csv"))) {
            for (Prestamo p : prestamos) {
                writer.println(p.getId() + ";" + p.getFecha() + ";" + p.getEstado() + ";" + p.getFechaDevolucion());
            }
        }
    }

    public List<Prestamo> cargarPrestamos() throws IOException {
        List<Prestamo> prestamos = new ArrayList<>();
        File file = new File(RUTA_DATOS + "prestamos.csv");
        if (!file.exists()) return prestamos;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] p = linea.split(";");
                int id = Integer.parseInt(p[0]);
                LocalDate fecha = p[1].equals("null") ? null : LocalDate.parse(p[1]);
                String estado = p[2];
                LocalDate fechaDevolucion = p[3].equals("null") ? null : LocalDate.parse(p[3]);

                prestamos.add(new Prestamo(id, fecha, estado, fechaDevolucion));
            }
        }
        return prestamos;
    }

    public void guardarTorneos(List<Torneo> torneos)throws IOException{
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_DATOS + "torneos.csv"))){
            for (Torneo t : torneos){
                if (!t.isValido()) continue;
                writer.println(t.getJuego().getId() + ";" + t.getCuposMaximos() + ";" + t.isAmistoso() + ";" + t.getTurno().getDia() + ";" +
                    t.getTurno().getHoraInicio() + ";" + t.getTurno().getHoraFinal() + ";" + t.isValido()
                );
            }
        }
    }
    
    public List<Torneo> cargarTorneos(List<InventarioPrestamo> inventarioPrestamo)throws IOException{
        List<Torneo> torneos = new ArrayList<>();
        File file = new File(RUTA_DATOS + "torneos.csv");
        if (!file.exists()){
            return torneos;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String linea;
    
            while ((linea = reader.readLine()) != null){
                String[] p = linea.split(";");
                int idJuego = Integer.parseInt(p[0]);
                int cuposMaximos = Integer.parseInt(p[1]);
                boolean esAmistoso = Boolean.parseBoolean(p[2]);
                String dia = p[3];
                String horaInicio = p[4];
                String horaFinal = p[5];
                boolean esValido = Boolean.parseBoolean(p[6]);
                Juego juegoEncontrado = buscarJuegoPorId(idJuego, inventarioPrestamo);

                if (juegoEncontrado != null){
                    Turno turno = new Turno(dia, horaInicio, horaFinal);
                    Torneo torneo;
                    if (esAmistoso) {
                        torneo = new TorneoAmistoso(juegoEncontrado, cuposMaximos, turno, esValido);
                    } else {
                        torneo = new TorneoCompetitivo(juegoEncontrado, cuposMaximos, turno, esValido);
                    }
                    torneos.add(torneo);
                }
            }
        }
    
        return torneos;
    }

    private Juego buscarJuegoPorId(int idJuego, List<InventarioPrestamo> inventario){
        for (InventarioPrestamo inv : inventario){
            if (inv.getJuego().getId() == idJuego){
                return inv.getJuego();
                }
            }
            return null; //Aquí si está justificado el null pq es para una comparación :P
        }

}