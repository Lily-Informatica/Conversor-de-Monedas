import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ConversorMonedas extends JFrame {

    private JTextField campoCantidad;
    private JComboBox<String> monedaOrigen;
    private JComboBox<String> monedaDestino;
    private JLabel resultado;
    private JLabel tasaLabel;

    private final Map<String, Double> tasas = new HashMap<>();

    public ConversorMonedas() {
        setTitle("Conversor de Monedas");
        setSize(480, 440);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Icono de la app
        try {
            setIconImage(new ImageIcon("src/icons/icono.png").getImage());
        } catch (Exception ignored) {}

        inicializarTasas();
        crearUI();
    }

    private void crearUI() {

        Color fondo = new Color(245, 248, 255);
        Color azul = new Color(52, 120, 246);

        JPanel panel = new JPanel(new GridLayout(10, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.setBackground(fondo);

        campoCantidad = new JTextField();
        campoCantidad.setHorizontalAlignment(JTextField.CENTER);

        // Enter para convertir
        campoCantidad.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    convertir();
                }
            }
        });

        String[] monedas = {
                "USD - Dólar 🇺🇸",
                "EUR - Euro 🇪🇺",
                "MXN - Peso Mexicano 🇲🇽",
                "ARS - Peso Argentino 🇦🇷",
                "CLP - Peso Chileno 🇨🇱",
                "COP - Peso Colombiano 🇨🇴",
                "PEN - Sol Peruano 🇵🇪",
                "BRL - Real Brasileño 🇧🇷"
        };

        monedaOrigen = new JComboBox<>(monedas);
        monedaDestino = new JComboBox<>(monedas);

        monedaOrigen.setSelectedIndex(0);
        monedaDestino.setSelectedIndex(2);

        JButton btnIntercambiar = new JButton(
                "Intercambiar",
                iconoEscalado("src/icons/intercambiar.png", 24, 24)
        );

        JButton btnConvertir = new JButton(
                "Convertir",
                iconoEscalado("src/icons/convertir.png", 24, 24)
        );

        JButton btnLimpiar = new JButton(
                "Limpiar",
                iconoEscalado("src/icons/limpiar.png", 24, 24)
        );

        // Estilo botones
        JButton[] botones = {btnIntercambiar, btnConvertir, btnLimpiar};
        for (JButton b : botones) {
            b.setFocusPainted(false);
            b.setIconTextGap(10);
            b.setHorizontalAlignment(SwingConstants.LEFT);
        }

        btnConvertir.setBackground(azul);
        btnConvertir.setForeground(Color.WHITE);

        resultado = new JLabel("Resultado: ", SwingConstants.CENTER);
        resultado.setFont(new Font("Arial", Font.BOLD, 15));

        tasaLabel = new JLabel("", SwingConstants.CENTER);
        tasaLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(new JLabel("Cantidad:", SwingConstants.CENTER));
        panel.add(campoCantidad);

        panel.add(new JLabel("Moneda de origen:", SwingConstants.CENTER));
        panel.add(monedaOrigen);

        panel.add(new JLabel("Moneda de destino:", SwingConstants.CENTER));
        panel.add(monedaDestino);

        panel.add(btnIntercambiar);
        panel.add(btnConvertir);
        panel.add(btnLimpiar);
        panel.add(resultado);
        panel.add(tasaLabel);

        add(panel);

        // Acciones
        btnConvertir.addActionListener(e -> convertir());
        btnIntercambiar.addActionListener(e -> intercambiar());
        btnLimpiar.addActionListener(e -> limpiar());
    }

    private void convertir() {
        try {
            double cantidad = Double.parseDouble(campoCantidad.getText());

            if (cantidad <= 0) {
                mostrarError("La cantidad debe ser mayor que 0");
                return;
            }

            String origen = (String) monedaOrigen.getSelectedItem();
            String destino = (String) monedaDestino.getSelectedItem();

            String codOrigen = origen.substring(0, 3);
            String codDestino = destino.substring(0, 3);

            double tasa = tasas.get(codDestino) / tasas.get(codOrigen);
            double total = cantidad * tasa;

            DecimalFormat df = new DecimalFormat("#,##0.00");

            resultado.setText("Resultado: " + df.format(total) + " " + codDestino);
            tasaLabel.setText("1 " + codOrigen + " = " + df.format(tasa) + " " + codDestino);

        } catch (NumberFormatException e) {
            mostrarError("Ingrese un número válido");
        }
    }

    private void intercambiar() {
        int o = monedaOrigen.getSelectedIndex();
        int d = monedaDestino.getSelectedIndex();
        monedaOrigen.setSelectedIndex(d);
        monedaDestino.setSelectedIndex(o);
    }

    private void limpiar() {
        campoCantidad.setText("");
        resultado.setText("Resultado: ");
        tasaLabel.setText("");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void inicializarTasas() {
        tasas.put("USD", 1.0);
        tasas.put("EUR", 0.93);
        tasas.put("MXN", 17.0);
        tasas.put("ARS", 820.0);
        tasas.put("CLP", 950.0);
        tasas.put("COP", 3900.0);
        tasas.put("PEN", 3.7);
        tasas.put("BRL", 5.0);
    }

    private ImageIcon iconoEscalado(String ruta, int ancho, int alto) {
        Image img = new ImageIcon(ruta).getImage();
        Image esc = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(esc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConversorMonedas().setVisible(true));
    }
}








