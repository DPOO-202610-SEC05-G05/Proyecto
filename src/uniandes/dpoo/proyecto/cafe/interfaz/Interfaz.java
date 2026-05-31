package uniandes.dpoo.proyecto.cafe.interfaz;

import uniandes.dpoo.proyecto.cafe.implementación.Administrador;
import uniandes.dpoo.proyecto.cafe.implementación.Cliente;
import uniandes.dpoo.proyecto.cafe.implementación.Empleado;
import uniandes.dpoo.proyecto.cafe.implementación.InventarioPrestamo;
import uniandes.dpoo.proyecto.cafe.implementación.InventarioVenta;
import uniandes.dpoo.proyecto.cafe.implementación.Juego;
import uniandes.dpoo.proyecto.cafe.implementación.Prestamo;
import uniandes.dpoo.proyecto.cafe.implementación.SistemaCafe;
import uniandes.dpoo.proyecto.cafe.implementación.Torneo;
import uniandes.dpoo.proyecto.cafe.implementación.Turno;
import uniandes.dpoo.proyecto.cafe.implementación.Usuario;
import uniandes.dpoo.proyecto.cafe.implementación.VentaJuego;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Interfaz extends JFrame {
    
    private SistemaCafe sistema; 
    private Usuario usuarioActual; 
    
    private JPanel panelPrincipal;
    private CardLayout cardLayout;

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    
    private List<String> solicitudesTurnos = new ArrayList<>();
    private JButton btnVerSolicitudesAdmin; 

    private DefaultTableModel modeloTorneosCliente;
    private DefaultTableModel modeloTorneosEmpleado;
    private DefaultTableModel modeloTorneosAdmin;

    public Interfaz() {
        sistema = new SistemaCafe();
        sistema.arrancarSistema(); 

        setTitle("Dulces & Dados Café");
        setSize(1000, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelMenu = new JPanel(new BorderLayout());
        panelMenu.setBackground(new Color(74, 46, 27)); 
        
        JLabel lblTitulo = new JLabel("Dulces & Dados Café");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(210, 105, 30));
        btnCerrarSesion.setForeground(Color.WHITE);

        panelMenu.add(lblTitulo, BorderLayout.WEST);
        panelMenu.add(btnCerrarSesion, BorderLayout.EAST);
        add(panelMenu, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        panelPrincipal.add(crearPanelLogin(), "Login");
        panelPrincipal.add(crearPanelCliente(), "Cliente");
        panelPrincipal.add(crearPanelAdmin(), "Admin");
        panelPrincipal.add(crearPanelEmpleado(), "Empleado");

        add(panelPrincipal, BorderLayout.CENTER);

        btnCerrarSesion.addActionListener(e -> {
            usuarioActual = null;
            txtUsuario.setText("");
            txtPassword.setText("");
            cardLayout.show(panelPrincipal, "Login");
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                sistema.apagarSistema(); 
            }
        });
    }

    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel form = new JPanel(new GridLayout(3, 2, 15, 15));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        txtUsuario = new JTextField(15);
        txtPassword = new JPasswordField(15);
        JButton btnEntrar = new JButton("Entrar al Café");
        btnEntrar.setBackground(new Color(139, 90, 43));
        btnEntrar.setForeground(Color.WHITE);
        
        form.add(new JLabel("Usuario:"));
        form.add(txtUsuario);
        form.add(new JLabel("Contraseña:"));
        form.add(txtPassword);
        form.add(new JLabel("")); 
        form.add(btnEntrar);
        
        btnEntrar.addActionListener(e -> {
            String user = txtUsuario.getText();
            String pass = new String(txtPassword.getPassword());
            
            Usuario validado = sistema.loginGUI(user, pass);
            
            if (validado != null && validado.esValido()) {
                usuarioActual = validado;
                
                if (validado instanceof Cliente) {
                    actualizarPanelCliente(); 
                    cardLayout.show(panelPrincipal, "Cliente");
                } else if (validado instanceof Administrador) {
                    actualizarPanelAdmin(); 
                    cardLayout.show(panelPrincipal, "Admin");
                } else if (validado instanceof Empleado) {
                    actualizarPanelEmpleado(); 
                    cardLayout.show(panelPrincipal, "Empleado");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error de Login", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(form);
        return panel;
    }

    private JLabel lblBienvenidaCliente;
    private JLabel lblPuntosCliente;
    private DefaultTableModel modeloCatalogo;
    private DefaultTableModel modeloTienda;

    private JPanel crearPanelCliente() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(253, 245, 230));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblBienvenidaCliente = new JLabel("Bienvenido", SwingConstants.CENTER);
        lblBienvenidaCliente.setFont(new Font("Arial", Font.BOLD, 22));
        
        JPanel pnlPuntos = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        pnlPuntos.setOpaque(false);
        lblPuntosCliente = new JLabel("Puntos de Fidelidad: 0");
        lblPuntosCliente.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton btnCanjear = new JButton("Canjear Puntos");
        btnCanjear.setBackground(new Color(46, 139, 87));
        btnCanjear.setForeground(Color.WHITE);
        
        btnCanjear.addActionListener(e -> {
            if (usuarioActual instanceof Cliente) {
                Cliente c = (Cliente) usuarioActual;
                int pts = c.getPuntos();
                
                if (pts < 100) {
                    JOptionPane.showMessageDialog(this, "Tienes " + pts + " puntos. Necesitas al menos 100 para un descuento.");
                    return;
                }
                
                int puntosACanjear = (pts >= 500) ? 500 : (pts / 100) * 100;
                int porcentajeDesc = puntosACanjear / 10;
                
                int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "Puedes canjear " + puntosACanjear + " puntos por un " + porcentajeDesc + "% de descuento.\n" +
                    "Se restarán de tu saldo.\n¿Deseas generar el código de descuento ahora?",
                    "Canjear Puntos", JOptionPane.YES_NO_OPTION);
                    
                if (confirmacion == JOptionPane.YES_OPTION) {
                    c.setPuntos(pts - puntosACanjear);
                    String codigoGenerado = "PUNTOS" + porcentajeDesc;
                    JOptionPane.showMessageDialog(this, "¡Éxito! Tu código es: " + codigoGenerado + "\nÚsalo en tu próxima compra de juegos.");
                    actualizarPanelCliente();
                }
            }
        });
        
        pnlPuntos.add(lblPuntosCliente);
        pnlPuntos.add(btnCanjear);
        
        header.add(lblBienvenidaCliente, BorderLayout.NORTH);
        header.add(pnlPuntos, BorderLayout.CENTER);
        panel.add(header, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();


        JPanel panelCatalogo = new JPanel(new BorderLayout());
        String[] colCatalogo = {"ID", "Nombre", "Categoría", "Jugadores", "Dificultad ( >:)  = Alta)"};
        modeloCatalogo = new DefaultTableModel(colCatalogo, 0);
        JTable tablaCatalogo = new JTable(modeloCatalogo);
        panelCatalogo.add(new JScrollPane(tablaCatalogo), BorderLayout.CENTER);

        JPanel panelAccionesCatalogo = new JPanel();
        JButton btnPedirPrestamo = new JButton("Solicitar Préstamo");
        JButton btnAddFavorito = new JButton(" <3 Añadir a Favoritos");
        
        btnPedirPrestamo.addActionListener(e -> {
            int fila = tablaCatalogo.getSelectedRow();
            if (fila >= 0 && usuarioActual instanceof Cliente) {
                String nombreJuego = (String) modeloCatalogo.getValueAt(fila, 1);
                InventarioPrestamo inv = sistema.buscarInventarioPrestamo(nombreJuego);
                
                if (inv != null && inv.getCantidadDisponible() > 0) {
                    Prestamo nuevoPrestamo = new Prestamo((int)(Math.random()*1000), LocalDate.now(), "activo", null);
                    inv.setCantidadDisponible(inv.getCantidadDisponible() - 1);
                    sistema.registrarNuevoPrestamo(nuevoPrestamo);
                    
                    JOptionPane.showMessageDialog(this, "Préstamo del juego '" + nombreJuego + "' registrado con éxito.\n¡Disfrútalo en tu mesa!");
                    actualizarPanelAdmin(); 
                    actualizarPanelCliente(); 
                } else {
                    JOptionPane.showMessageDialog(this, "No hay copias disponibles para prestar de este juego.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un juego del catálogo primero.");
            }
        });

        btnAddFavorito.addActionListener(e -> {
            int fila = tablaCatalogo.getSelectedRow();
            if (fila >= 0 && usuarioActual instanceof Cliente) {
                String nombreJuego = (String) modeloCatalogo.getValueAt(fila, 1);
                Cliente c = (Cliente) usuarioActual;
                c.setJuegosFavoritos(nombreJuego);
                JOptionPane.showMessageDialog(this, "El juego '" + nombreJuego + "' se ha añadido a tus favoritos.");
            }
        });

        panelAccionesCatalogo.add(btnPedirPrestamo);
        panelAccionesCatalogo.add(btnAddFavorito);
        panelCatalogo.add(panelAccionesCatalogo, BorderLayout.SOUTH); 
        tabbedPane.addTab("🎮 Catálogo (Para Jugar Aquí)", panelCatalogo);


        JPanel panelTienda = new JPanel(new BorderLayout());
        String[] colTienda = {"Nombre", "Categoría", "Precio Base", "Disponibles"};
        modeloTienda = new DefaultTableModel(colTienda, 0);
        JTable tablaTienda = new JTable(modeloTienda);
        panelTienda.add(new JScrollPane(tablaTienda), BorderLayout.CENTER);

        JPanel panelAccionesTienda = new JPanel();
        JButton btnComprar = new JButton("🛒 Comprar Juego");
        
        btnComprar.addActionListener(e -> {
            int fila = tablaTienda.getSelectedRow();
            if (fila >= 0 && usuarioActual instanceof Cliente) {
                String nombreJuego = (String) modeloTienda.getValueAt(fila, 0);
                InventarioVenta invVenta = sistema.buscarInventarioVenta(nombreJuego);
                
                if (invVenta != null && invVenta.getCantidadTotal() > 0) {
                    String codigo = JOptionPane.showInputDialog(this, 
                        "Precio base: $" + invVenta.getJuego().getPrecio() + "\n¿Tienes un código de descuento? (Ej. PUNTOS20, TORNEO10):");
                    
                    double precioFinal = invVenta.getJuego().getPrecio();
                    String msgDescuento = "";
                    
                    if (codigo != null && !codigo.trim().isEmpty()) {
                        codigo = codigo.toUpperCase().trim();
                        if (codigo.equals("TORNEO10")) {
                            precioFinal = precioFinal * 0.90; 
                            msgDescuento = "\n¡Bono de torneo aplicado! (-10%)";
                        } else if (codigo.equals("EMP20")) {
                            precioFinal = precioFinal * 0.80; 
                            msgDescuento = "\n¡Descuento de empleado aplicado! (-20%)";
                        } else if (codigo.startsWith("PUNTOS")) {
                            try {
                                int porcentajeDesc = Integer.parseInt(codigo.substring(6));
                                if (porcentajeDesc >= 10 && porcentajeDesc <= 50) {
                                    precioFinal = precioFinal * (1.0 - (porcentajeDesc / 100.0));
                                    msgDescuento = "\n¡Descuento por Fidelidad aplicado! (-" + porcentajeDesc + "%)";
                                } else {
                                    JOptionPane.showMessageDialog(this, "Código de puntos no válido.");
                                }
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(this, "Formato de código inválido.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Código no reconocido. Se aplicará precio normal.");
                        }
                    }

                    int confirmacion = JOptionPane.showConfirmDialog(this, 
                        "Total a pagar: $" + precioFinal + msgDescuento + "\n¿Deseas confirmar la compra?", 
                        "Confirmar Compra", JOptionPane.YES_NO_OPTION);
                    
                    if (confirmacion == JOptionPane.YES_OPTION) {
                        Cliente c = (Cliente) usuarioActual;
                        int puntosAntes = c.getPuntos();
                        VentaJuego nuevaVenta = c.comprarJuego(invVenta.getJuego(), (int)(Math.random()*1000), invVenta);
                        sistema.registrarNuevaVenta(nuevaVenta);
                        c.setPuntos(puntosAntes + 2); 
                        
                        JOptionPane.showMessageDialog(this, "¡Compra Exitosa!\nHas ganado 2 puntos de fidelidad adicionales por tu compra.");
                        actualizarPanelAdmin(); 
                        actualizarPanelCliente(); 
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Agotado en tienda.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un juego de la tienda.");
            }
        });

        panelAccionesTienda.add(btnComprar);
        panelTienda.add(panelAccionesTienda, BorderLayout.SOUTH);
        tabbedPane.addTab("Tienda de Juegos (Comprar)", panelTienda);

        JPanel panelMesa = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0;

        panelMesa.add(new JLabel("Número de Personas para la mesa:"), gbc);
        
        gbc.gridx = 1;
        JSpinner spinPersonas = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        panelMesa.add(spinPersonas, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        JButton btnReservarMesa = new JButton("Reservar Mesa");
        
        btnReservarMesa.addActionListener(e -> {
            int numPersonas = (int) spinPersonas.getValue();
            boolean exito = sistema.intentarIngresarClientes(numPersonas);
            
            if(exito) {
                JOptionPane.showMessageDialog(this, "¡Mesa reservada exitosamente para " + numPersonas + " personas!\nYa puedes pedir juegos para tu mesa.");
            } else {
                JOptionPane.showMessageDialog(this, "Lo sentimos, se ha alcanzado la capacidad máxima del café.", "Aforo Completo", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        panelMesa.add(btnReservarMesa, gbc);
        tabbedPane.addTab("🪑 Reservar Mesa", panelMesa);

        JPanel panelTorneosC = new JPanel(new BorderLayout());
        String[] colTorneosC = {"Juego", "Tipo", "Horario", "Cupos Restantes"};
        modeloTorneosCliente = new DefaultTableModel(colTorneosC, 0);
        JTable tablaTorneosC = new JTable(modeloTorneosCliente);
        panelTorneosC.add(new JScrollPane(tablaTorneosC), BorderLayout.CENTER);

        JPanel pnlAccionesTC = new JPanel();
        JSpinner spinCuposC = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1)); 
        JButton btnInscribirC = new JButton(" Inscribirse (Solo/Grupal)");
        
        btnInscribirC.addActionListener(e -> {
            int fila = tablaTorneosC.getSelectedRow();
            if(fila >= 0) {
                int cantidad = (int) spinCuposC.getValue();
                Torneo t = sistema.getTorneos().get(fila);
                boolean exito = t.inscribir(usuarioActual, cantidad);
                if(exito) {
                    String modo = (cantidad == 1) ? "Individual (Solo)" : "Grupal ("+cantidad+" personas)";
                    JOptionPane.showMessageDialog(this, "Inscripción " + modo + " exitosa al torneo de " + t.getJuego().getNombre());
                    actualizarPanelCliente();
                } else {
                    JOptionPane.showMessageDialog(this, "Error: No hay cupos suficientes, máximo 3 por usuario, o restricción de fans activa.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un torneo primero.");
            }
        });
        
        pnlAccionesTC.add(new JLabel("Cantidad Cupos:"));
        pnlAccionesTC.add(spinCuposC);
        pnlAccionesTC.add(btnInscribirC);
        panelTorneosC.add(pnlAccionesTC, BorderLayout.SOUTH);
        tabbedPane.addTab("Torneos", panelTorneosC);

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private void actualizarPanelCliente() {
        if (usuarioActual instanceof Cliente) {
            Cliente c = (Cliente) usuarioActual;
            lblBienvenidaCliente.setText("Bienvenido(a), " + c.getUsername());
            lblPuntosCliente.setText("Tus Puntos de Fidelidad: " + c.getPuntos());

            modeloCatalogo.setRowCount(0);
            List<Juego> disponibles = sistema.juegosDisponiblesPrestamo();
            for (Juego j : disponibles) {
                String dificultad = j.isEsDificil() ? " >:) Alta" : " :) Normal";
                Object[] fila = { j.getId(), j.getNombre(), j.getCategoria(), j.getMinJugadores() + "-" + j.getMaxJugadores(), dificultad };
                modeloCatalogo.addRow(fila);
            }

            modeloTienda.setRowCount(0);
            List<InventarioVenta> paraVender = sistema.getInventariosVenta();
            if (paraVender != null) {
                for (InventarioVenta inv : paraVender) {
                    if (inv.getCantidadTotal() > 0) {
                        Juego j = inv.getJuego();
                        Object[] fila = { j.getNombre(), j.getCategoria(), "$" + j.getPrecio(), inv.getCantidadTotal() };
                        modeloTienda.addRow(fila);
                    }
                }
            }

            if (modeloTorneosCliente != null) {
                modeloTorneosCliente.setRowCount(0);
                for (Torneo t : sistema.getTorneos()) {
                    String tipo = t.isAmistoso() ? "Amistoso" : "Competitivo";
                    String horario = (t.getTurno() != null) ? t.getTurno().getDia() + " " + t.getTurno().getHoraInicio() : "N/A";
                    String nombreJuego = (t.getJuego() != null) ? t.getJuego().getNombre() : "Desconocido";
                    int cuposDisp = t.getCuposMaximos() - t.totalCuposTomados();
                    Object[] fila = { nombreJuego, tipo, horario, cuposDisp + " / " + t.getCuposMaximos() };
                    modeloTorneosCliente.addRow(fila);
                }
            }
        }
    }


    private DefaultTableModel modeloInventarioAdmin;
    private JPanel panelDashboardPastel; 

    private JPanel crearPanelAdmin() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel lblTitulo = new JLabel("Centro de Control - Administrador", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JTabbedPane tabbedPaneAdmin = new JTabbedPane();


        tabbedPaneAdmin.addChangeListener(e -> {
            actualizarPanelAdmin();
        });

        JPanel panelGraficas = new JPanel(new GridLayout(1, 2, 10, 10));
        panelGraficas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panelDashboardPastel = crearGraficaPastel(); 
        panelGraficas.add(panelDashboardPastel);
        panelGraficas.add(crearGraficaBarras()); 
        
        JPanel pnlLinea = new JPanel(new BorderLayout());
        pnlLinea.add(crearGraficaLineas(), BorderLayout.CENTER);
        pnlLinea.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        JPanel tabDashboard = new JPanel(new BorderLayout());
        tabDashboard.add(panelGraficas, BorderLayout.CENTER);
        tabDashboard.add(pnlLinea, BorderLayout.SOUTH);
        
        tabbedPaneAdmin.addTab(" Dashboard Estadístico", tabDashboard);


        JPanel tabInventario = new JPanel(new BorderLayout());
        String[] colAdminInv = {"Nombre Juego", "Copias Venta", "Copias Préstamo (Disp/Total)", "Estado General"};
        modeloInventarioAdmin = new DefaultTableModel(colAdminInv, 0);
        JTable tablaInvAdmin = new JTable(modeloInventarioAdmin);
        tabInventario.add(new JScrollPane(tablaInvAdmin), BorderLayout.CENTER);

        JPanel pnlAccionesInv = new JPanel(new GridLayout(2, 2, 5, 5));
        pnlAccionesInv.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnAddVenta = new JButton("Añadir Copias (Venta)");
        JButton btnAddPrestamo = new JButton("Añadir Copias (Préstamo)");
        JButton btnReparar = new JButton("Reparar Juego (Pasa de Venta -> Préstamo)");
        JButton btnEstado = new JButton("Cambiar Estado del Juego");

        btnAddVenta.addActionListener(e -> {
            int fila = tablaInvAdmin.getSelectedRow();
            if(fila >= 0 && usuarioActual instanceof Administrador) {
                String nombre = (String) modeloInventarioAdmin.getValueAt(fila, 0);
                InventarioVenta invV = sistema.buscarInventarioVenta(nombre);
                if(invV != null) {
                    try {
                        int cantidad = Integer.parseInt(JOptionPane.showInputDialog("¿Cuántas copias añadir para la VENTA?"));
                        ((Administrador)usuarioActual).agregarJuegoVenta(invV, cantidad);
                        actualizarPanelAdmin();
                        JOptionPane.showMessageDialog(this, "Inventario de venta actualizado.");
                    } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Número inválido."); }
                }
            } else { JOptionPane.showMessageDialog(this, "Seleccione un juego en la tabla."); }
        });

        btnAddPrestamo.addActionListener(e -> {
            int fila = tablaInvAdmin.getSelectedRow();
            if(fila >= 0 && usuarioActual instanceof Administrador) {
                String nombre = (String) modeloInventarioAdmin.getValueAt(fila, 0);
                InventarioPrestamo invP = sistema.buscarInventarioPrestamo(nombre);
                if(invP != null) {
                    try {
                        int cantidad = Integer.parseInt(JOptionPane.showInputDialog("¿Cuántas copias añadir para el PRÉSTAMO?"));
                        ((Administrador)usuarioActual).agregarJuegoPrestamo(invP, cantidad);
                        actualizarPanelAdmin();
                        JOptionPane.showMessageDialog(this, "Inventario de préstamo actualizado.");
                    } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Número inválido."); }
                }
            } else { JOptionPane.showMessageDialog(this, "Seleccione un juego en la tabla."); }
        });

        btnReparar.addActionListener(e -> {
            int fila = tablaInvAdmin.getSelectedRow();
            if(fila >= 0 && usuarioActual instanceof Administrador) {
                String nombre = (String) modeloInventarioAdmin.getValueAt(fila, 0);
                InventarioVenta invV = sistema.buscarInventarioVenta(nombre);
                InventarioPrestamo invP = sistema.buscarInventarioPrestamo(nombre);
                
                if(invV != null && invP != null) {
                    ((Administrador)usuarioActual).repararJuego(invV, invP);
                    actualizarPanelAdmin();
                    JOptionPane.showMessageDialog(this, "Proceso de reparación/transferencia ejecutado.");
                }
            } else { JOptionPane.showMessageDialog(this, "Seleccione un juego en la tabla."); }
        });

        btnEstado.addActionListener(e -> {
            int fila = tablaInvAdmin.getSelectedRow();
            if(fila >= 0 && usuarioActual instanceof Administrador) {
                String nombre = (String) modeloInventarioAdmin.getValueAt(fila, 0);
                InventarioVenta invV = sistema.buscarInventarioVenta(nombre);
                if(invV != null) {
                    String nuevoEstado = JOptionPane.showInputDialog("Ingrese el nuevo estado (Ej. Disponible, Descatalogado, Dañado):");
                    if(nuevoEstado != null && !nuevoEstado.isEmpty()) {
                        ((Administrador)usuarioActual).setEstadoJuego(invV, nuevoEstado);
                        actualizarPanelAdmin();
                    }
                }
            } else { JOptionPane.showMessageDialog(this, "Seleccione un juego en la tabla."); }
        });

        pnlAccionesInv.add(btnAddVenta);
        pnlAccionesInv.add(btnAddPrestamo);
        pnlAccionesInv.add(btnReparar);
        pnlAccionesInv.add(btnEstado);
        tabInventario.add(pnlAccionesInv, BorderLayout.SOUTH);
        
        tabbedPaneAdmin.addTab("Gestión de Inventarios", tabInventario);


        JPanel tabTurnos = new JPanel(new BorderLayout());
        JPanel pnlTurnosCentro = new JPanel(new GridLayout(2, 1, 10, 10));
        pnlTurnosCentro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        pnlTurnosCentro.add(new JLabel("<html><h3>Gestor de Turnos y Empleados</h3><p>Para asignar nuevos turnos o responder a solicitudes de cambio, seleccione las opciones a continuación.</p></html>"));
        
        JPanel pnlBotonesTurno = new JPanel();
        
        btnVerSolicitudesAdmin = new JButton("Ver Solicitudes de Cambio (" + solicitudesTurnos.size() + " Pendientes)");
        btnVerSolicitudesAdmin.addActionListener(e -> {
            if (solicitudesTurnos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay solicitudes de cambio de turno pendientes.");
            } else {
                StringBuilder sb = new StringBuilder("Solicitudes recibidas:\n\n");
                for (int i = 0; i < solicitudesTurnos.size(); i++) {
                    sb.append((i+1)).append(". ").append(solicitudesTurnos.get(i)).append("\n");
                }
                sb.append("\n¿Desea marcar todas como procesadas/aprobadas?");
                
                int resp = JOptionPane.showConfirmDialog(this, sb.toString(), "Aprobar Solicitudes", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    solicitudesTurnos.clear(); 
                    actualizarPanelAdmin(); 
                    JOptionPane.showMessageDialog(this, "Las solicitudes han sido procesadas.");
                }
            }
        });
        
        JButton btnAsignarTurno = new JButton("Asignar Nuevo Turno a Empleado");
        btnAsignarTurno.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del empleado (Ej. 2 para Carlos):");
                if (idStr != null && !idStr.trim().isEmpty()) {
                    int id = Integer.parseInt(idStr);
                    Usuario u = sistema.buscarUsuario(id);
                    
                    if (u != null && u instanceof Empleado) {
                        Empleado emp = (Empleado) u;
                        
                        JPanel panelTurnoN = new JPanel(new GridLayout(3, 2, 5, 5));
                        JTextField txtDia = new JTextField();
                        JTextField txtInicio = new JTextField("08:00");
                        JTextField txtFin = new JTextField("16:00");
                        
                        panelTurnoN.add(new JLabel("Día (Ej. Lunes):"));
                        panelTurnoN.add(txtDia);
                        panelTurnoN.add(new JLabel("Hora Inicio:"));
                        panelTurnoN.add(txtInicio);
                        panelTurnoN.add(new JLabel("Hora Fin:"));
                        panelTurnoN.add(txtFin);
                        
                        int result = JOptionPane.showConfirmDialog(this, panelTurnoN, 
                                 "Asignar Turno a " + emp.getUsername(), JOptionPane.OK_CANCEL_OPTION);
                                 
                        if (result == JOptionPane.OK_OPTION && !txtDia.getText().trim().isEmpty()) {
                            Turno nuevoTurno = new Turno(txtDia.getText(), txtInicio.getText(), txtFin.getText());
                            emp.setTurno(nuevoTurno);
                            JOptionPane.showMessageDialog(this, "Turno asignado exitosamente a " + emp.getUsername());
                            
                            solicitudesTurnos.removeIf(s -> s.contains(emp.getUsername()));
                            actualizarPanelAdmin();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No se encontró un empleado con el ID " + id + ".", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido. Debe ingresar un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        pnlBotonesTurno.add(btnVerSolicitudesAdmin);
        pnlBotonesTurno.add(btnAsignarTurno);
        pnlTurnosCentro.add(pnlBotonesTurno);
        
        tabTurnos.add(pnlTurnosCentro, BorderLayout.NORTH);
        tabbedPaneAdmin.addTab("Empleados y Turnos", tabTurnos);

        JPanel tabAdminTorneos = new JPanel(new BorderLayout());
        String[] colAdminTor = {"Juego", "Tipo", "Horario", "Cupos Restantes"};
        modeloTorneosAdmin = new DefaultTableModel(colAdminTor, 0);
        JTable tablaAdminTor = new JTable(modeloTorneosAdmin);
        tabAdminTorneos.add(new JScrollPane(tablaAdminTor), BorderLayout.CENTER);

        JPanel pnlCrearTorneo = new JPanel(new FlowLayout());
        JTextField txtJuego = new JTextField(10);
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Amistoso", "Competitivo"});
        JSpinner spinCuposT = new JSpinner(new SpinnerNumberModel(10, 2, 50, 1));
        JButton btnCrearTorneo = new JButton("Crear Torneo");
        
        btnCrearTorneo.addActionListener(e -> {
            String nomJuego = txtJuego.getText();
            InventarioPrestamo invP = sistema.buscarInventarioPrestamo(nomJuego);
            if (invP != null) {
                Turno turnoSimulado = new Turno("Sábado", "15:00", "18:00");
                boolean esAmistoso = (cmbTipo.getSelectedIndex() == 0);
                sistema.crearTorneo(invP.getJuego(), (int)spinCuposT.getValue(), esAmistoso, turnoSimulado);
                JOptionPane.showMessageDialog(this, "Torneo de " + nomJuego + " creado exitosamente.");
                actualizarPanelAdmin();
                actualizarPanelCliente();
                actualizarPanelEmpleado();
            } else {
                JOptionPane.showMessageDialog(this, "Juego no encontrado. Asegúrese de escribir el nombre exacto de un juego en inventario (Ej. Catan).");
            }
        });
        
        pnlCrearTorneo.add(new JLabel("Nombre Juego:"));
        pnlCrearTorneo.add(txtJuego);
        pnlCrearTorneo.add(new JLabel("Tipo:"));
        pnlCrearTorneo.add(cmbTipo);
        pnlCrearTorneo.add(new JLabel("Cupos:"));
        pnlCrearTorneo.add(spinCuposT);
        pnlCrearTorneo.add(btnCrearTorneo);
        
        tabAdminTorneos.add(pnlCrearTorneo, BorderLayout.SOUTH);
        tabbedPaneAdmin.addTab("Gestión Torneos", tabAdminTorneos);

        panel.add(tabbedPaneAdmin, BorderLayout.CENTER);
        return panel;
    }

    private void actualizarPanelAdmin() {
        if (usuarioActual instanceof Administrador) {
            modeloInventarioAdmin.setRowCount(0);
            
            List<InventarioVenta> ventas = sistema.getInventariosVenta();
            if(ventas != null) {
                for (InventarioVenta invV : ventas) {
                    Juego j = invV.getJuego();
                    InventarioPrestamo invP = sistema.buscarInventarioPrestamo(j.getNombre());
                    
                    String strVenta = String.valueOf(invV.getCantidadTotal());
                    String strPrestamo = "No registrado";
                    if(invP != null) {
                        strPrestamo = invP.getCantidadDisponible() + " / " + invP.getCantidadTotal();
                    }
                    
                    Object[] fila = { j.getNombre(), strVenta, strPrestamo, invV.getEstado() };
                    modeloInventarioAdmin.addRow(fila);
                }
            }
            
            if (modeloTorneosAdmin != null) {
                modeloTorneosAdmin.setRowCount(0);
                for (Torneo t : sistema.getTorneos()) {
                    String tipo = t.isAmistoso() ? "Amistoso" : "Competitivo";
                    String horario = (t.getTurno() != null) ? t.getTurno().getDia() + " " + t.getTurno().getHoraInicio() : "N/A";
                    String nombreJuego = (t.getJuego() != null) ? t.getJuego().getNombre() : "Desconocido";
                    int cuposDisp = t.getCuposMaximos() - t.totalCuposTomados();
                    Object[] fila = { nombreJuego, tipo, horario, cuposDisp + " / " + t.getCuposMaximos() };
                    modeloTorneosAdmin.addRow(fila);
                }
            }

            if (btnVerSolicitudesAdmin != null) {
                btnVerSolicitudesAdmin.setText("Ver Solicitudes de Cambio (" + solicitudesTurnos.size() + " Pendientes)");
            }
            
            if(panelDashboardPastel != null) {
                panelDashboardPastel.repaint();
            }
        }
    }

    private JPanel crearGraficaPastel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int copiasVenta = 0;
                int copiasPrestamo = 0;
                
                InventarioVenta invV = sistema.buscarInventarioVenta("Catan");
                InventarioPrestamo invP = sistema.buscarInventarioPrestamo("Catan");
                
                if(invV != null) copiasVenta = invV.getCantidadTotal();
                if(invP != null) copiasPrestamo = invP.getCantidadTotal();
                
                int total = copiasVenta + copiasPrestamo;
                
                if(total > 0) {
                    int anguloVenta = (int) Math.round((copiasVenta * 360.0) / total);
                    int anguloPrestamo = 360 - anguloVenta;
                    
                    g2.setColor(new Color(65, 105, 225)); 
                    g2.fillArc(50, 30, 150, 150, 90, anguloVenta);
                    
                    g2.setColor(new Color(220, 20, 60)); 
                    g2.fillArc(50, 30, 150, 150, 90 + anguloVenta, anguloPrestamo);
                    
                    g2.setColor(Color.WHITE);
                    if(copiasVenta > 0) g2.drawString(copiasVenta+"", 100, 80);
                    if(copiasPrestamo > 0) g2.drawString(copiasPrestamo+"", 120, 140);
                } else {
                    g2.setColor(Color.GRAY);
                    g2.fillOval(50, 30, 150, 150);
                    g2.setColor(Color.WHITE);
                    g2.drawString("Sin Datos", 100, 110);
                }
                
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 11));
                g2.drawString("■ Venta: " + copiasVenta, 220, 90);
                g2.drawString("■ Préstamo: " + copiasPrestamo, 220, 120);
            }
        };
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createTitledBorder("Disponibilidad Dinámica de Catan"));
        return panel;
    }

    private JPanel crearGraficaBarras() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                
                g2.setColor(Color.BLACK);
                g2.drawLine(30, 20, 30, 170); 
                g2.drawLine(30, 170, 400, 170); 
                
                int[] cafe = {20, 40, 60, 50, 30};
                int[] juegos = {50, 30, 90, 40, 80};
                
                for(int i = 0; i < 5; i++) {
                    int x = 50 + (i * 70);
                    g2.setColor(new Color(220, 20, 60)); 
                    g2.fillRect(x, 170 - cafe[i], 15, cafe[i]);
                    g2.setColor(new Color(65, 105, 225)); 
                    g2.fillRect(x + 15, 170 - juegos[i], 15, juegos[i]);
                    
                    g2.setColor(Color.BLACK);
                    g2.drawString("D" + (i+1), x, 185);
                }
            }
        };
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createTitledBorder("Ventas Categoría (Histórico 5 días)"));
        return panel;
    }

    private JPanel crearGraficaLineas() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(Color.BLACK);
                g2.drawLine(40, 20, 40, 150); 
                g2.drawLine(40, 150, 850, 150); 
                
                int[] reservas = {10, 25, 15, 45, 60, 30, 40};
                String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
                
                g2.setColor(new Color(220, 20, 60)); 
                g2.setStroke(new BasicStroke(2));
                
                for(int i = 0; i < 6; i++) {
                    int x1 = 80 + (i * 120);
                    int y1 = 150 - reservas[i];
                    int x2 = 80 + ((i+1) * 120);
                    int y2 = 150 - reservas[i+1];
                    
                    g2.drawLine(x1, y1, x2, y2);
                    g2.fillOval(x1 - 4, y1 - 4, 8, 8);
                }
                g2.fillOval(80 + (6 * 120) - 4, 150 - reservas[6] - 4, 8, 8); 
                
                g2.setColor(Color.BLACK);
                for(int i = 0; i < 7; i++) {
                    g2.drawString(dias[i], 70 + (i * 120), 170);
                }
            }
        };
        panel.setBackground(new Color(245, 245, 245));
        panel.setPreferredSize(new Dimension(900, 220));
        panel.setBorder(BorderFactory.createTitledBorder("Evolución Reservas Semana"));
        return panel;
    }

    private JLabel lblBienvenidaEmpleado;
    private JLabel lblTurnoEmpleado;

    private JPanel crearPanelEmpleado() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(new Color(230, 240, 253));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblBienvenidaEmpleado = new JLabel("Bienvenido", SwingConstants.CENTER);
        lblBienvenidaEmpleado.setFont(new Font("Arial", Font.BOLD, 22));
        
        lblTurnoEmpleado = new JLabel("Turno Actual: No asignado", SwingConstants.CENTER);
        lblTurnoEmpleado.setFont(new Font("Arial", Font.ITALIC, 16));
        
        header.add(lblBienvenidaEmpleado);
        header.add(lblTurnoEmpleado);
        panel.add(header, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panelTurno = new JPanel(new GridBagLayout());
        JButton btnCambiarTurno = new JButton("Solicitar Cambio de Turno");
        btnCambiarTurno.addActionListener(e -> {
            String nuevoDia = JOptionPane.showInputDialog(this, "Ingrese el día del turno que desea (Ej. Martes):");
            if (nuevoDia != null && !nuevoDia.isEmpty() && usuarioActual instanceof Empleado) {
                Empleado emp = (Empleado) usuarioActual;
                Turno tSimulado = new Turno(nuevoDia, "08:00", "16:00");
                
                Turno turnoActual = emp.getTurno();
                if (turnoActual == null) {
                    turnoActual = new Turno("Ninguno", "00:00", "00:00"); 
                }
                
                emp.solicitarCambioTurno(tSimulado, turnoActual, emp.getId());
                
                String msgSolicitud = emp.getUsername() + " (" + emp.getClass().getSimpleName() + ") solicita cambio para el día: " + nuevoDia;
                solicitudesTurnos.add(msgSolicitud);
                
                JOptionPane.showMessageDialog(this, "Solicitud de cambio a " + nuevoDia + " enviada al Administrador.");
            }
        });
        panelTurno.add(btnCambiarTurno);
        tabbedPane.addTab("Mi Turno", panelTurno);

        JPanel panelSugerencias = new JPanel(new GridBagLayout());
        JButton btnSugerir = new JButton("💡 Sugerir Nuevo Platillo");
        btnSugerir.addActionListener(e -> {
            String platillo = JOptionPane.showInputDialog(this, "Ingrese el nombre del nuevo platillo:");
            if (platillo != null && !platillo.trim().isEmpty() && usuarioActual instanceof Empleado) {
                ((Empleado) usuarioActual).sugerirPlatillo(platillo);
                JOptionPane.showMessageDialog(this, "Sugerencia enviada exitosamente al Administrador.");
            }
        });
        panelSugerencias.add(btnSugerir);
        tabbedPane.addTab("Sugerencias", panelSugerencias);

        JPanel panelTorneosE = new JPanel(new BorderLayout());
        String[] colTorneosE = {"Juego", "Tipo", "Horario", "Cupos Restantes"};
        modeloTorneosEmpleado = new DefaultTableModel(colTorneosE, 0);
        JTable tablaTorneosE = new JTable(modeloTorneosEmpleado);
        panelTorneosE.add(new JScrollPane(tablaTorneosE), BorderLayout.CENTER);

        JPanel pnlAccionesTE = new JPanel();
        JButton btnInscribirE = new JButton(" Inscribirse al Torneo");
        
        btnInscribirE.addActionListener(e -> {
            int fila = tablaTorneosE.getSelectedRow();
            if(fila >= 0) {
                Torneo t = sistema.getTorneos().get(fila);
                boolean exito = t.inscribir(usuarioActual, 1); 
                if(exito) {
                    JOptionPane.showMessageDialog(this, "Inscripción exitosa al torneo de " + t.getJuego().getNombre());
                    actualizarPanelEmpleado();
                } else {
                    JOptionPane.showMessageDialog(this, "Error: No se pudo inscribir (Ya inscrito, choque de turno o sin cupos).");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un torneo primero.");
            }
        });
        
        pnlAccionesTE.add(btnInscribirE);
        panelTorneosE.add(pnlAccionesTE, BorderLayout.SOUTH);
        tabbedPane.addTab("Torneos", panelTorneosE);

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private void actualizarPanelEmpleado() {
        if (usuarioActual instanceof Empleado) {
            Empleado emp = (Empleado) usuarioActual;
            lblBienvenidaEmpleado.setText("Bienvenido(a), " + emp.getUsername() + " (Rol: " + emp.getClass().getSimpleName() + ")");
            
            if (emp.getTurno() != null) {
                lblTurnoEmpleado.setText("Turno Actual: " + emp.getTurno().getDia() + " | " + emp.getTurno().getHoraInicio() + " - " + emp.getTurno().getHoraFinal());
            } else {
                lblTurnoEmpleado.setText("Turno Actual: No asignado");
            }

            if (modeloTorneosEmpleado != null) {
                modeloTorneosEmpleado.setRowCount(0);
                for (Torneo t : sistema.getTorneos()) {
                    String tipo = t.isAmistoso() ? "Amistoso" : "Competitivo";
                    String horario = (t.getTurno() != null) ? t.getTurno().getDia() + " " + t.getTurno().getHoraInicio() : "N/A";
                    String nombreJuego = (t.getJuego() != null) ? t.getJuego().getNombre() : "Desconocido";
                    int cuposDisp = t.getCuposMaximos() - t.totalCuposTomados();
                    Object[] fila = { nombreJuego, tipo, horario, cuposDisp + " / " + t.getCuposMaximos() };
                    modeloTorneosEmpleado.addRow(fila);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Interfaz().setVisible(true);
        });
    }
}