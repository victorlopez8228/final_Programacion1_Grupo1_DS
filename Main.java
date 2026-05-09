import service.AuthService;
import db.ConnectionDB;
import ui.Dialogs;
import javax.swing.*;
import java.util.List;
import model.User;

// clase principal del programa. Contiene toda la navegación entre
// ventanas (menús JOptionPane) y coordina las acciones del usuario.


public class Main {

    // Servicio con toda la lógica de negocio y seguridad
    static AuthService authService = new AuthService();

    // Usuario actualmente autenticado (null = sin sesión)
    static User currentUser = null;
    
    public static void main(String[] args) {
        
        try {
            javax.swing.UIManager.setLookAndFeel(
            javax.swing.UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {}

        menuStart(); // Lanzar el menú inicial
        db.ConnectionDB.closeConnection(); // Cerrar BD al terminar
    }

    //  MENÚ 1 — INICIO (Sin sesión)
    static void menuStart() {
        while (true) {
            String[] options = {"🔑  Iniciar Sesión", "🚪  Salir"};
            int option = Dialogs.showMenu(
                "Bienvenido al Sistema de Gestión de Usuarios\n\n" +
                "¿Qué deseas hacer?",
                options
            );

            if (option == 0) {
                // Intentar login
                boolean loginOk = login();
                if (loginOk) {
                    mainMenu(); // Ir al menú principal con sesión
                }
                // Si falla el login, el bucle se repite (vuelve al inicio)

            } else {
                // opcion == 1 o -1 (X): salir del programa
                boolean confirm = Dialogs.confirm("¿Estás seguro de que deseas salir?");
                if (confirm) {
                    Dialogs.success("¡Hasta luego! 👋");
                    return; // Termina el método y por tanto el programa
                }
                // Si no confirma, el bucle continúa
            }
        }
    }


    //  FLUJO DE LOGIN

    static boolean login() {
        // Pedir username
        String username = Dialogs.textRequest("👤 Ingresa tu username:");
        if (username == null) return false; // Canceló

        // Pedir contraseña con campo oculto
        String password = Dialogs.passwordRequest("🔒 Ingresa tu contraseña:");
        if (password == null) return false; // Canceló

        // Verificar credenciales (internamente hashea y compara)
        currentUser = authService.login(username, password);

        if (currentUser != null) {
            Dialogs.success("✅ Login exitoso\n\n¡Bienvenido, " + currentUser.getUsername() + "!");
            return true;
        } else {
            Dialogs.error("❌ Credenciales incorrectas\n\nEl usuario no existe, está inactivo,\no la contraseña es incorrecta.");
            return false;
        }
    }

    //  MENÚ 2 — PRINCIPAL (Con sesión iniciada)

        static void mainMenu() {
        while (true) {
            String[] options = {
                "➕  Crear Usuario",
                "📋  Ver Lista de Usuarios",
                "✏️   Modificar Usuario",
                "🔓  Cerrar Sesión"
            };

            int option = Dialogs.showMenu(
                "Panel de Administración\n" +
                "Usuario activo: " + currentUser.getUsername() + "\n\n" +
                "¿Qué deseas hacer?",
                options
            );

            switch (option) {
                case 0: createUserMenu();     break;
                case 1: menuViewUsers();      break;
                case 2: menuUpdateUser(); break;
                case 3:
                    // Cerrar sesión
                    if (Dialogs.confirm("¿Cerrar sesión de " + currentUser.getUsername() + "?")) {
                        currentUser = null;
                        return; // Regresa al menuInicio()
                    }
                    break;
                default:
                    // -1 = cerró con X, preguntar confirmación
                    if (Dialogs.confirm("¿Cerrar sesión?")) {
                        currentUser = null;
                        return;
                    }
            }
        }
    }


    //  MENÚ 3 — CREAR USUARIO

    static void createUserMenu() {
        boolean repeat = true;
        while (true) {

            // Paso 1: Username
            String username = Dialogs.textRequest("👤 Nuevo username:");
            
            while (username == null) {
                Dialogs.error("❌ El nombre de usuario no puede estar vacío.");
                boolean retry = Dialogs.confirm("¿Deseas intentar ingresar el nombre de usuario nuevamente?");
                if (!retry) {
                    Dialogs.error("❌ Operación cancelada. No se creó ningún usuario.");
                    return; // Canceló → volver al menú principal
                }
                username = Dialogs.textRequest("👤 Nuevo username:");
            }

            // Paso 2: Contraseña (campo oculto)
            String password = Dialogs.passwordRequest("🔒 Contraseña (mínimo 6 caracteres):");
             while (password == null) {
                boolean retry = Dialogs.confirm("❌ La contraseña no puede estar vacía.\n¿Deseas intentar ingresar la contraseña nuevamente?");
                if (!retry) {
                    Dialogs.error("❌ Operación cancelada. No se creó ningún usuario.");
                    return; // Canceló → volver al menú principal
                }
                password = Dialogs.passwordRequest("🔒 Contraseña (mínimo 6 caracteres):");
            }

            // Paso 3: Email
            String email = Dialogs.textRequest("📧 Email:");

            // Registrar usuario (el servicio valida y hashea)
            String result = authService.registerUser(username, password, email);

            // El resultado viene con prefijo "OK:" o "ERROR:"
            if (result.startsWith("OK:")) {
                Dialogs.success("✅ " + result.substring(3));
                // Preguntar si desea crear otro usuario
                repeat = Dialogs.confirm("¿Deseas crear otro usuario?");
            } else {
                Dialogs.error("❌ " + result.substring(6));
                // Preguntar si desea intentar de nuevo
                repeat = Dialogs.confirm("¿Deseas intentar de nuevo?");
            }
            if (!repeat) return; // No → salir del bucle, volver al menú principal
            // Sí → el bucle while se repite automáticamente
        }
    }


    //  MENÚ 4 — VER LISTA DE USUARIOS

    static void menuViewUsers() {
        while (true) {
            List<User> users = authService.listUsers();

            String contenido;
            if (users.isEmpty()) {
                contenido = "No hay usuarios registrados en el sistema.";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Total: ").append(users.size()).append(" usuario(s)\n");
                for (int i = 0; i < 50; i++) sb.append("─");
                sb.append("\n\n");
                for (User u : users) {
                    sb.append(u.toString()).append("\n\n");
                }
                contenido = sb.toString();
            }

            // Mostrar en JTextArea con scroll (útil si hay muchos usuarios)
            javax.swing.JTextArea textArea = new javax.swing.JTextArea(contenido);
            textArea.setEditable(false);
            textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 13));
            textArea.setBackground(new java.awt.Color(245, 245, 245));

            javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(480, 280));

            String[] botones = {"🔄 Actualizar lista", "🔙 Volver"};
            int resp = JOptionPane.showOptionDialog(null, scrollPane,
                "📋 Lista de Usuarios",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null, botones, botones[1]);

            if (resp == 0) {
                continue; // Actualizar → el bucle repite y carga de nuevo la lista
            } else {
                return; // Volver al menú principal
            }
        }
    }

    //  MENÚ 5 — MODIFICAR USUARIO
        static void menuUpdateUser() {
        while (true) {
            List<User> users = authService.listUsers();

            if (users.isEmpty()) {
                Dialogs.warning("⚠️ No hay usuarios registrados en el sistema.");
                return;
            }

            // Construir array de opciones para el selector
            String[] options = new String[users.size() + 1];
            for (int i = 0; i < users.size(); i++) {
                options[i] = users.get(i).toString();
            }
            options[users.size()] = "🔙 Volver al menú principal";

            // Mostrar selector de lista
            String selected = Dialogs.selectFromList(
                "Selecciona el usuario que deseas modificar:", options
            );

            // null = canceló con X, o eligió "Volver"
            if (selected == null || selected.equals("🔙 Volver al menú principal")) return;

            // Encontrar el objeto User correspondiente a la opción seleccionada
            User userSelected = null;
            for (User u : users) {
                if (u.toString().equals(selected)) {
                    userSelected = u;
                    break;
                }
            }

            if (userSelected == null) return;

            // Submenú: qué desea cambiar
            menuSelectChange(userSelected);

            // Preguntar si modifica otro
            if (!Dialogs.confirm("¿Deseas modificar otro usuario?")) return;
        }
    }

    static void menuSelectChange(User user) {
        while (true) {
            // Refrescar datos del usuario desde BD antes de mostrar el menú
            User userUpdated = authService.searchById(user.getId());
            if (userUpdated == null) {
                Dialogs.warning("⚠️ Este usuario ya no existe en el sistema.");
                return;
            }

            String infoUser =
                "Usuario seleccionado:\n" +
                "  👤 Username : " + userUpdated.getUsername() + "\n" +
                "  📧 Email    : " + userUpdated.getEmail() + "\n" +
                "  🔘 Estado   : " + (userUpdated.isActive() ? "✅ Activo" : "❌ Inactivo") + "\n\n" +
                "¿Qué deseas cambiar?";

            String[] options = {
                "👤  Cambiar Username",
                "🔒  Cambiar Contraseña",
                "🔘  Cambiar Estado (Activo/Inactivo)",
                "🗑️   Eliminar Usuario",
                "🔙  Volver"
            };

            int option = Dialogs.showMenu(infoUser, options);

            switch (option) {
                case 0: actionChangeUsername(userUpdated); break;
                case 1: actionChangePassword(userUpdated); break;
                case 2: actionChangeStatus(userUpdated);   break;
                case 3:
                    boolean deleted = actionDelete(userUpdated);
                    if (deleted) return; // Ya no existe, salir del submenú
                    break;
                case 4:  // "Volver"
                default:
                    return; // Salir → regresa a menuModificarUsuario()
            }
        }
    }

    //  ACCIONES DE MODIFICACIÓN
    static void actionChangeUsername(User user) {
        String newUsername = Dialogs.textRequest(
            "👤 Cambiar username de: " + user.getUsername() + "\n\nNuevo username:"
        );
        if (newUsername == null) return; // Canceló

        String result = authService.changeUsername(user.getId(), newUsername);
        if (result.startsWith("OK:")) {
            Dialogs.success("✅ " + result.substring(3));
            user.setUsername(newUsername); // actualizar referencia local
        } else {
            Dialogs.error("❌ " + result.substring(6));
        }
    }

    /**
     * Cambia la contraseña del usuario.
     * Usa campo oculto (JPasswordField). El servicio hashea con SHA-256 antes de guardar.
     */
    static void actionChangePassword(User user) {
        String newPassword = Dialogs.passwordRequest(
            "🔒 Cambiar contraseña de: " + user.getUsername() + "\n\nNueva contraseña (mín. 6 caracteres):"
        );
        if (newPassword == null) return;

        String result = authService.changePassword(user.getId(), newPassword);
        if (result.startsWith("OK:")) {
            Dialogs.success("✅ " + result.substring(3));
        } else {
            Dialogs.error("❌ " + result.substring(6));
        }
    }

    /**
     * Alterna el estado activo/inactivo del usuario.
     * Muestra el estado actual y pide confirmación antes de cambiar.
     */
    static void actionChangeStatus(User user) {
        String currentStatus = user.isActive() ? "✅ ACTIVO" : "❌ INACTIVO";
        String newStatus  = user.isActive() ? "❌ INACTIVO" : "✅ ACTIVO";

        boolean confirm = Dialogs.confirm(
            "🔘 Cambiar estado de: " + user.getUsername() + "\n\n" +
            "Estado actual : " + currentStatus + "\n" +
            "Nuevo estado  : " + newStatus + "\n\n" +
            "¿Confirmas el cambio?"
        );

        if (!confirm) return;

        String result = authService.changeStatus(user.getId(), user.isActive());
        if (result.startsWith("OK:")) {
            Dialogs.success("✅ " + result.substring(3));
            user.setActive(!user.isActive()); // actualizar referencia local
        } else {
            Dialogs.error("❌ " + result.substring(6));
        }
    }

    /**
     * Elimina permanentemente al usuario después de doble confirmación.
     * La doble confirmación previene eliminaciones accidentales.
     *
     * @return true si se eliminó, false si canceló
     */
    static boolean actionDelete(User user) {
        // Primera confirmación
        boolean confirm1 = Dialogs.confirm(
            "🗑️ Eliminar usuario: " + user.getUsername() + "\n\n" +
            "⚠️ Esta acción es PERMANENTE e irreversible.\n\n" +
            "¿Estás seguro?"
        );
        if (!confirm1) return false;

        // Segunda confirmación (doble verificación)
        boolean confirm2 = Dialogs.confirm(
            "⚠️ CONFIRMA LA ELIMINACIÓN\n\n" +
            "El usuario '" + user.getUsername() + "' será eliminado para siempre.\n\n" +
            "¿Continuar con la eliminación?"
        );
        if (!confirm2) return false;

        String result = authService.deleteUser(user.getId());
        if (result.startsWith("OK:")) {
            Dialogs.success("✅ " + result.substring(3));
            return true;
        } else {
            Dialogs.error("❌ " + result.substring(6));
            return false;
        }
    }
}
