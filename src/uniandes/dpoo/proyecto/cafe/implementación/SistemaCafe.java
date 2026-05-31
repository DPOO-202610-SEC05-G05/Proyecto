package uniandes.dpoo.proyecto.cafe.implementación;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SistemaCafe {
    private GestorPersistencia persistencia;
    private List<Usuario> usuariosSistema;
    private List<InventarioPrestamo> inventarioPrestamo;
    private List<InventarioVenta> inventarioVenta;
    private HistorialVenta historialVentasGlobal;
    private HistorialPrestamo historialPrestamoGlobal;
    private int clientesActuales;
    private List<Torneo> torneos;
    private int capacidadMax;

    public SistemaCafe() {
        this.persistencia = new GestorPersistencia();
        this.usuariosSistema = new ArrayList<Usuario>();
        this.inventarioPrestamo = new ArrayList<InventarioPrestamo>();
        this.inventarioVenta = new ArrayList<InventarioVenta>();
        
        this.historialVentasGlobal = new HistorialVenta();
        this.historialPrestamoGlobal = new HistorialPrestamo();
        this.capacidadMax = 50;
        this.clientesActuales = 0;
        this.torneos = new ArrayList<>();
    }

    public void arrancarSistema() {
        try {
            System.out.println("Iniciando y cargando datos :)");
            this.usuariosSistema = persistencia.cargarUsuarios();
            this.inventarioPrestamo = persistencia.cargarInventarioPrestamo();
            this.inventarioVenta = persistencia.cargarInventarioVenta();

            List<Venta> ventasCargadas = persistencia.cargarVentas(this.usuariosSistema);
            for (Venta v : ventasCargadas) {
                this.historialVentasGlobal.registrarVenta(v);
            }

            List<Prestamo> prestamosCargados = persistencia.cargarPrestamos();
            for (Prestamo p : prestamosCargados) {
                this.historialPrestamoGlobal.registrarPrestamo(p);
            }

            System.out.println("Datos cargados correctamente :D");
        } catch (IOException e) {
            System.out.println("Error al cargar los datos: " + e.getMessage());
        }
    }

    public void apagarSistema() {
        try {
            System.out.println("Guardando ;)");
            persistencia.guardarUsuarios(this.usuariosSistema);
            persistencia.guardarInventarioPrestamo(this.inventarioPrestamo);
            persistencia.guardarInventarioVenta(this.inventarioVenta);
            persistencia.guardarVentas(this.historialVentasGlobal.getVentas());
            persistencia.guardarPrestamos(this.historialPrestamoGlobal.getPrestamos());

            System.out.println("Hecho :D");
        } catch (IOException e) {
            System.out.println("Error de guardado :( " + e.getMessage());
        }
    }

    public int getCapacidadMax(){
        return capacidadMax;
    }

    public void setCapacidadMax(int capacidadMax){
        this.capacidadMax = capacidadMax;
    }

    public void registrarNuevaVenta(Venta venta) {
        if (venta != null && venta.isValida()) {
            this.historialVentasGlobal.registrarVenta(venta);
        }
    }

    public void registrarNuevoPrestamo(Prestamo prestamo) {
        this.historialPrestamoGlobal.registrarPrestamo(prestamo);
    }

    public Pedido crearPedido(int id, Mesa mesa, int cantidadPersonas) {
        boolean pudoEntrar = intentarIngresarClientes(cantidadPersonas);
        if (!pudoEntrar) {
            System.out.println("No se pudo crear. Supera capacidad máxima de personas");
            return new Pedido(0, LocalDate.now(), mesa, 0, false);
        }

        return new Pedido(id, LocalDate.now(), mesa, cantidadPersonas, true);
    }

    public VentaCafeteria cerrarPedido(Pedido pedido, int idVenta) {
        if (pedido == null || !pedido.esValido()) {
            System.out.println("Pedido inválido");
            return new VentaCafeteria(0, LocalDate.now(), 0.0, 0.0, 0.0, false);
        }
        double subtotal = pedido.calcularTotal();
        double impuestoConsumo = subtotal * 0.08;
        double propina = subtotal * 0.10;
        double total = subtotal + impuestoConsumo + propina;

        VentaCafeteria venta = new VentaCafeteria(idVenta, LocalDate.now(), total, impuestoConsumo, propina, true);
        this.historialVentasGlobal.registrarVenta(venta);
        pedido.terminarPedido();

        return venta;
    }

    public Usuario buscarUsuario(int id) {
        for (Usuario u : usuariosSistema) {
            if (u.getId() == id) {
                return u;
            }
        }
        return new Cliente(0, "", "", "", 0, false);
    }

    public List<Juego> juegosDisponiblesPrestamo() {
        List<Juego> disponibles = new ArrayList<>();

        for (InventarioPrestamo inv : inventarioPrestamo) {
            if (inv.getCantidadDisponible() > 0) {
                disponibles.add(inv.getJuego());
            }
        }

        return disponibles;
    }

    public boolean intentarIngresarClientes(int cantidadPersonas) {
    int capacidadMax = getCapacidadMax();

        if (clientesActuales + cantidadPersonas > capacidadMax) {
            System.out.println("Se alcanzó la capacidad máxima. No puede ingresar");
            return false;
        }

        clientesActuales += cantidadPersonas;
        System.out.println("Se pudo ingresar. # Clientes actuales: "+ clientesActuales);
        return true;
    }

    public void salirClientes(int cantidadPersonas) {
        clientesActuales -= cantidadPersonas;

        if (clientesActuales < 0) {
            clientesActuales = 0;
        }
        
        System.out.println("Clientes actuales: "+clientesActuales);
    }

    public void crearTorneo(Juego juego, int cupos, boolean esAmistoso, Turno turno){
        
        Torneo t;
        if(esAmistoso){
            t = new TorneoAmistoso(juego, cupos, turno, true);
        } else {
            t = new TorneoCompetitivo(juego, cupos, turno, true);
        }
        torneos.add(t);
    }

    public List<Torneo> getTorneos(){
        return torneos;
    }

    public Torneo buscarTorneo(Juego juego){
        for(Torneo t : torneos){
            if(t.getJuego() != null && t.getJuego().equals(juego)){
                return t;
            }
        }
        
        return new TorneoAmistoso(null, 0, null, false);
    }

    public static void main(String[] args) {
        SistemaCafe sistema = new SistemaCafe();
        sistema.menuPrincipal();
    }

    public void menuPrincipal(){
        Juego juego = new Juego(1, "Catan", 1995, "Kosmos", "Estrategia", 3, 4, 10, false, "Disponible", 120000);
        Turno turno = new Turno("Lunes", "08:00", "12:00");
        
        
        Torneo torneo = new TorneoAmistoso(juego, 10, turno, true);
        
        usuariosSistema.add(new Cliente(1, "Juan", "juan", "123", 0, true));
        usuariosSistema.add(new Mesero(2, "Carlos", "carlos", "123", turno, new ArrayList<>(), new ArrayList<>(), true));
        usuariosSistema.add(new Administrador(3, "Mariana", "maraina@gmail.com", "123", true));
        Scanner sc = new Scanner(System.in);


        while(true){
            System.out.println("\n== MENU PRINCIPAL ==\n");
            System.out.println("0. Salir");
            System.out.println("1. Iniciar sesion");
            System.out.println("2. Registrarse");
            int opcion = validarEntero(sc, "Seleccione una opción:");
            if (opcion == 0 ){
                try {
                    persistencia.guardarUsuarios(usuariosSistema);
                    persistencia.guardarTorneos(torneos);
                } catch (IOException e) {
                    System.out.println("Error guardando datos :'V");
                }
                break;
            }

            if(opcion == 1){
                Usuario usuario = login(sc);
                if(usuario == null || !usuario.esValido()){
                    continue; //continua, hace las verificaciones pero no logra nada, entonces vuelve al menú... (más costoso pero no retorna null)
                }
                if(usuario instanceof Cliente){
                    menuCliente(sc, (Cliente) usuario, torneo);
                }
                if(usuario instanceof Empleado){
                    menuEmpleado(sc, (Empleado) usuario, torneo);
                }
                if(usuario instanceof Administrador){
                    menuAdmin(sc, juego, turno);
                }
            }

            else if (opcion == 2 ){
                registrarCliente(sc);
            }
            
            else{
                System.out.println("Ocurrió un error, volviendo al menu... ");
            }
        }
        sc.close(); 
        System.out.println("Has salido del programa. Chao :P");
    }

    public void menuCliente(Scanner sc, Cliente cliente, Torneo torneo){
        System.out.println("\n== MENU CLIENTE ==\n");
        System.out.println("1. Inscribirse a un torneo");
        System.out.println("2. Desinscribirse de un torneo");
        int opcion = validarEntero(sc, "Seleccione una opción:");
        if(opcion == 1){
            boolean resultado = torneo.inscribir(cliente, 2);
            System.out.println(resultado ? "Inscripción exitosa :D" : "No se pudo inscribir :(");
        }
        if(opcion == 2){
            boolean resultado = torneo.desinscribir(cliente);
            System.out.println(resultado ? "Desinscripción exitosa :(" : "No se pudo desinscribir");
        }
        if(opcion < 1 || opcion > 2){
            System.out.println("Opcion escogida no es válida.");
        }
    }

    public void menuAdmin(Scanner sc, Juego juego, Turno turno){
        System.out.println("\n== MENU ADMINISTRADOR ==\n");
        System.out.println("1. Crear torneo");
        System.out.println("2. Ver torneos");
        System.out.println("3. Buscar Usuarios");
        System.out.println("4. Registrar Empleado");

        int opcion = validarEntero(sc, "Seleccione una opción:");
        if(opcion == 1){
            int cupos = validarEntero(sc, "Ingrese cupos del torneo:");
            boolean esAmistoso = validarBoolean(sc, "Es amistoso?, ingrese true/false");
            
            crearTorneo(juego, cupos, esAmistoso, turno);

            try{
                persistencia.guardarTorneos(torneos);
                System.out.println("Torneos guardados correctamente.");
            }catch(IOException e){
                System.out.println("Error guardando torneos.");
            }
        }

        if(opcion == 2){
            System.out.println("Cantidad de torneos actuales: " + torneos.size());
        }

        if(opcion == 3){
            boolean ecnontrado = false;
            String username = validarTexto(sc, "Porfavor ingrese el username de la persona: ");
            
            for(Usuario u: usuariosSistema){
                if(username.equals(u.getUsername())){
                    System.out.println("Usuario encontrado: " + u.getUsername());
                    ecnontrado = true;
                }
            }

            if(!ecnontrado){
                System.out.println("El usuario " + username + " no existe. ");   
            }
        }
        if(opcion == 4){
            String tipoEmpleado = validarTexto(sc, "El empleado tendra rol de (Mesero/Cocinero): ");
            int id = validarEntero(sc, "ID del empleado: ");
            String username = validarTexto(sc, "Nombre del empleado: ");
            String email = validarEmail(sc, "Correo del empleado: ");
            String password = validarTexto(sc, "Password del empleado: ");
            
            if (tipoEmpleado.equalsIgnoreCase("Mesero")) {
                usuariosSistema.add(new Mesero(id, username, email, password, turno, new ArrayList<>(), new ArrayList<>(), true));
                System.out.println("Empleado registrado.");
            } 
            
            else if (tipoEmpleado.equalsIgnoreCase("Cocinero")) {
                usuariosSistema.add(new Cocinero(id, username, email, password, turno, true));
                System.out.println("Empleado registrado.");
            } 
            
            else {
                System.out.println("Rol inválido.");
            }
        }
    }

    public void menuEmpleado(Scanner sc, Empleado empleado, Torneo torneo){
        System.out.println("\n==MENU EMPLEADO==\n");
        System.out.println("1. Inscribirse a torneo");
        int opcion = validarEntero(sc, "Seleccione una opción:");

        if(opcion == 1){
            boolean resultado = torneo.inscribir(empleado, 1);
            System.out.println(resultado ? "Empleado inscrito" : "No se pudo inscribir el empleado");
        }
        if(opcion < 1 || opcion > 1){
            System.out.println("Opcion incorrecta");
        }
    }

    public Usuario login(Scanner sc){
        String username = validarTexto(sc, "Username: ");
        String password = validarTexto(sc, "Password: ");

        for(Usuario u : usuariosSistema){
            if(u.getUsername().equals(username) && u.getPassword().equals(password)){
                System.out.println("Login exitoso!");
                return u;
            }
        }
        System.out.println("Credenciales incorrectas");
        return new Cliente(0, "", "", "", 0, false);
    }

    public void registrarCliente(Scanner sc){
        System.out.println("\n== REGISTRO CLIENTE ==");
        String username = validarTexto(sc, "INgresa tu username: ");
        String email = validarEmail(sc, "Ingresa tu correo: ");
        String password = validarTexto(sc, "Password: ");
        Cliente nCliente = new Cliente(usuariosSistema.size() + 1, username, email, password, 0, true);
        usuariosSistema.add(nCliente);

        try{
            persistencia.guardarUsuarios(usuariosSistema);
            System.out.println("Guardado con existo: ");
        }catch(IOException e){
            System.out.println("Error ");

        }
        System.out.println("Felicidades " + username + " te has registrado con exito. ");
    }

    
    private int validarEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = sc.nextInt();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Debes ingresar un número válido.");
                sc.nextLine(); // limpiar buffer
                }
            }
        }
    
    private String validarTexto(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        return sc.next();
    }
        
    private boolean validarBoolean(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje + " (true/false): ");
            String input = sc.next().toLowerCase();
            if (input.equals("true")) return true;
            if (input.equals("false")) return false;
            System.out.println("Entrada inválida. Escribe true o false.");
        }
    }
        
    private String validarEmail(Scanner sc, String mensaje){
        while (true){
            System.out.print(mensaje);
            String email = sc.next();
                
            if(email.contains("@") && email.contains(".")){
                return email;
            }
            System.out.println("Correo inválido. Intente nuevamente.");
            }
        }
}

//!TUNEAR TODO!!!!