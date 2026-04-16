package ui;

import javax.swing.*;
import java.awt.*;

public class Dialogs {


   
    private static final String TITTLE = "🔐 Sistema de Gestión de Usuarios";

    
   
    public static void success(String message) {
        JOptionPane.showMessageDialog(null, message, TITTLE,
            JOptionPane.INFORMATION_MESSAGE);
    }

   
    public static void error(String message) {
        JOptionPane.showMessageDialog(null, message, TITTLE,
            JOptionPane.ERROR_MESSAGE);
    }

    
    public static void warning(String message) {
        JOptionPane.showMessageDialog(null, message, TITTLE,
            JOptionPane.WARNING_MESSAGE);
    }

      
    public static String textRequest(String tag) {
        String valor = JOptionPane.showInputDialog(null, tag, TITTLE,
            JOptionPane.QUESTION_MESSAGE);
        if (valor == null) return null;
        return valor.trim().isEmpty() ? null : valor.trim();
    }

    public static String passwordRequest(String tag) {
        // Crear campo de contraseña personalizado
        JPasswordField fieldPassword = new JPasswordField(20);
        fieldPassword.setEchoChar('●'); // Carácter de ocultamiento

        // Panel con etiqueta + campo de contraseña
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel(tag), BorderLayout.NORTH);
        panel.add(fieldPassword, BorderLayout.CENTER);

        // Forzar foco en el campo al abrir
        fieldPassword.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent e) {
                fieldPassword.requestFocusInWindow();
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent e) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent e) {}
        });

        int resultado = JOptionPane.showConfirmDialog(null, panel, TITTLE,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (resultado != JOptionPane.OK_OPTION) return null;

        String pass = new String(fieldPassword.getPassword());
        return pass.trim().isEmpty() ? null : pass;
    }

        // ── Confirmaciones 

    /**
     * Muestra una ventana de confirmación con botones Sí / No.
     */
    public static boolean confirm(String ask) {
        int answer = JOptionPane.showConfirmDialog(null, ask, TITTLE,
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return answer == JOptionPane.YES_OPTION;
    }

        // ── Menús con botones personalizados ─────────────────────────────

    /**
     * Muestra un menú con botones personalizados.
     * El usuario hace clic en el botón de la opción deseada.
     *
     * FUNCIONAMIENTO DE showOptionDialog():
     *  - opciones[]:  Array de Strings que se convierten en botones
     *  - Retorna el ÍNDICE del botón presionado (0, 1, 2...)
     *  - Retorna -1 si el usuario cierra la ventana con la X
     */
    public static int showMenu(String message, String[] options) {
        return JOptionPane.showOptionDialog(
            null,
            message,
            TITTLE,
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,       // sin ícono personalizado
            options,   // estos se convierten en botones
            options[0] // botón seleccionado por defecto
        );
    }

    /**
     * Muestra un menú de selección de lista (JList) para elegir entre varios usuarios.
     * Más cómodo que botones cuando hay muchos elementos.     */
    public static String selectFromList(String message, String[] options) {
        return (String) JOptionPane.showInputDialog(
            null,
            message,
            TITTLE,
            JOptionPane.QUESTION_MESSAGE,
            null,       // sin ícono
            options,   // elementos de la lista desplegable
            options.length > 0 ? options[0] : null
        );
    }
}