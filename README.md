# ACCESIUM - Sistema de Gestión de Usuarios

🔐 Sistema de Gestión de Usuarios
Sistema de escritorio desarrollado en Java con interfaz gráfica (JOptionPane), diseñado para gestionar usuarios con autenticación segura mediante cifrado SHA-256. Permite registrar, listar, modificar y eliminar usuarios conectándose a una base de datos PostgreSQL a través de JDBC.
---

## 🚀 Características
* 🔑 Autenticación segura con cifrado SHA-256
* 👥 Registro y gestión completa de usuarios
* 🖥️ Interfaz gráfica con ventanas (JOptionPane)
* 🗄️ Persistencia de datos con PostgreSQL vía JDBC
* 🔄 Activar / desactivar usuarios
* 🛡️ Validación de campos y reglas de negocio
* 🔒 Contraseñas almacenadas siempre como hash (nunca en texto plano)

---
## 🧱 Arquitectura

```
Usuario (Interfaz gráfica JOptionPane)
              ↓
          Main.java
              ↓
        AuthService.java  (Lógica de negocio + SHA-256)
              ↓
          UserDAO.java    (Operaciones SQL)
              ↓
        ConexionDB.java   (JDBC → PostgreSQL)
```

---

## 🛠️ Tecnologías
* Java 11+
* Java JOptionPane
* JDBC (Java Database Connectivity)
* PostgreSQL
* SHA-256 (java.security.MessageDigest)
* Visual Studio Code + Extension Pack for Java

---
## 📦 Instalación

1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/final_programacion1_Grupo1.git
cd final_programacion1_Grupo1
```
2. Descargar el driver JDBC de PostgreSQL
Ir a: https://jdbc.postgresql.org/download/
Descargar `postgresql-42.x.x.jar`
Colocarlo dentro de la carpeta `lib/`

3. Configurar credenciales
Editar `src/db/ConexionDB.java`:
```java
private static final String URL      = "jdbc:postgresql://localhost:5432/user_management_db";
private static final String USUARIO  = "postgres";  // ← tu usuario
private static final String PASSWORD = "";           // ← tu contraseña

```

4. Configurar VS Code
Instalar la extensión Extension Pack for Java (Microsoft)
Abrir la carpeta del proyecto: `File → Open Folder`
Presionar `Ctrl+Shift+P` → Java: Configure Classpath
En Referenced Libraries hacer clic en "+" y seleccionar el `.jar` de `lib/`
6. Ejecutar
Abrir `src/Main.java` y presionar F5
---

## 📁 Estructura del proyecto
```
user-management/
├── src/
│   ├── Main.java               → Punto de entrada y navegación de menús
│   ├── db/
│   │   └── ConexionDB.java     → Conexión JDBC a PostgreSQL
│   ├── model/
│   │   └── User.java           → Clase modelo del usuario
│   ├── dao/
│   │   └── UserDAO.java        → Operaciones SQL (INSERT, SELECT, UPDATE, DELETE)
│   ├── service/
│   │   └── AuthService.java    → Lógica de negocio y cifrado SHA-256
│   └── ui/
│       └── Dialogs.java        → Utilidades de interfaz 
├── lib/
│   └── postgresql-42.x.x.jar  → Driver JDBC de PostgreSQL
└── README.md
```

---

## 🧠 Flujo de la aplicación
* El usuario abre el programa → aparece el menú de inicio
* Ingresa username y contraseña
* El sistema hashea la contraseña con SHA-256 y compara con la BD
* Si las credenciales son válidas y el usuario está activo → accede al panel
* Desde el panel puede: crear usuarios, ver la lista, modificar o eliminar
* Al cerrar sesión regresa al menú inicial
---

## 🎯 Funcionalidades

| Función            | Descripción                                                               |
|--------------------|---------------------------------------------------------------------------|
| Login              | Autenticación con validación de estado activo/inactivo                    |
| Crear usuario      | Registro con validaciones y hash SHA-256 automático                       |
| Listar usuarios    | Vista en tabla con scroll (ID, username, email, estado)                   |
| Cambiar username   | Validación de unicidad antes de actualizar                                |
| Cambiar contraseña | Nueva contraseña hasheada antes de guardar                                |
| Cambiar estado     | Toggle activo ↔ inactivo con confirmación                                 |
| Eliminar usuario   | Doble confirmación antes de eliminar permanentemente  
---

## 🔐 Seguridad
* Las contraseñas nunca se almacenan en texto plano
* Se usa SHA-256 (algoritmo unidireccional): la contraseña original no se puede recuperar
* Para verificar el login se hashea el intento y se compara con el hash almacenado
* El campo `password` en la BD contiene siempre 64 caracteres hexadecimales
* Los campos de contraseña usan `JPasswordField` (caracteres ocultos con ●)
---

### 🔑 Usuario de prueba

| Campo    | Valor               |
|----------|---------------------|
| Username | admin               |
| Password | admin123            |
| Email    | admin@sistema.com   |
| Estado   | Activo              |
---

## 📈 Futuras mejoras
Roles de usuario (administrador / usuario estándar)
Registro de auditoría (log de acciones)
Recuperación de contraseña por email
Exportar lista de usuarios a CSV o PDF
Soporte para múltiples bases de datos
---

### 📦 Versionado
Versión actual: v1.0.0
---

## 👨‍💻 Autor
Desarrollado por Ramses Rodriguez, Nestor Pachay y Víctor López (Desarrolladores de Software)
---

## 📄 Licencia
MIT License
