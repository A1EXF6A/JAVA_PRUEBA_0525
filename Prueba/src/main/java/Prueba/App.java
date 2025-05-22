package Prueba;

import com.google.gson.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Vector;

public class App extends JFrame {

    private static final String API_URL = "http://localhost:81/app/api.php";

    private JTable tableEstudiantes;
    private JTable tableCursos;
    private JTextField txtCedulaBuscar;
    private JTextArea txtAreaResultado;
    private JTextField txtCedulaRelacion;
    private JTextField txtCodigoCursoRelacion;

    public App() {
        setTitle("Gestión Estudiantes y Cursos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel(new BorderLayout(10,10));
        getContentPane().add(panel);

        // Panel de tablas y resultados
        JPanel panelCentro = new JPanel(new GridLayout(1, 3, 10, 10));

        // Panel para agregar estudiante
JPanel panelAgregarEstudiante = new JPanel(new FlowLayout(FlowLayout.LEFT));
panelAgregarEstudiante.setBorder(BorderFactory.createTitledBorder("Agregar nuevo estudiante"));
JTextField txtNuevaCedula = new JTextField(8);
JTextField txtNuevoNombre = new JTextField(10);
JTextField txtNuevoEmail = new JTextField(12);
JButton btnAgregarEstudiante = new JButton("Agregar Estudiante");
panelAgregarEstudiante.add(new JLabel("Cédula:"));
panelAgregarEstudiante.add(txtNuevaCedula);
panelAgregarEstudiante.add(new JLabel("Nombre:"));
panelAgregarEstudiante.add(txtNuevoNombre);
panelAgregarEstudiante.add(new JLabel("Email:"));
panelAgregarEstudiante.add(txtNuevoEmail);
panelAgregarEstudiante.add(btnAgregarEstudiante);

// Panel para agregar curso
JPanel panelAgregarCurso = new JPanel(new FlowLayout(FlowLayout.LEFT));
panelAgregarCurso.setBorder(BorderFactory.createTitledBorder("Agregar nuevo curso"));
JTextField txtNuevoCodigo = new JTextField(8);
JTextField txtNuevoNombreCurso = new JTextField(10);
JTextField txtNuevaDescripcion = new JTextField(12);
JButton btnAgregarCurso = new JButton("Agregar Curso");
panelAgregarCurso.add(new JLabel("Código:"));
panelAgregarCurso.add(txtNuevoCodigo);
panelAgregarCurso.add(new JLabel("Nombre:"));
panelAgregarCurso.add(txtNuevoNombreCurso);
panelAgregarCurso.add(new JLabel("Descripción:"));
panelAgregarCurso.add(txtNuevaDescripcion);
panelAgregarCurso.add(btnAgregarCurso);

// Añadir estos nuevos paneles arriba del panelAcciones, por ejemplo:
JPanel panelNuevos = new JPanel(new GridLayout(2,1,5,5));
panelNuevos.add(panelAgregarEstudiante);
panelNuevos.add(panelAgregarCurso);

panel.add(panelNuevos, BorderLayout.NORTH);

        // Tabla Estudiantes
        tableEstudiantes = new JTable();
        JScrollPane scrollEstudiantes = new JScrollPane(tableEstudiantes);
        JPanel panelEstudiantes = new JPanel(new BorderLayout());
        panelEstudiantes.setBorder(BorderFactory.createTitledBorder("Estudiantes"));
        panelEstudiantes.add(scrollEstudiantes, BorderLayout.CENTER);

        // Tabla Cursos
        tableCursos = new JTable();
        JScrollPane scrollCursos = new JScrollPane(tableCursos);
        JPanel panelCursos = new JPanel(new BorderLayout());
        panelCursos.setBorder(BorderFactory.createTitledBorder("Cursos"));
        panelCursos.add(scrollCursos, BorderLayout.CENTER);

        // Área de resultados (texto)
        txtAreaResultado = new JTextArea();
        txtAreaResultado.setEditable(false);
        JScrollPane scrollResultados = new JScrollPane(txtAreaResultado);
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        panelResultados.add(scrollResultados, BorderLayout.CENTER);

        panelCentro.add(panelEstudiantes);
        panelCentro.add(panelCursos);
        panelCentro.add(panelResultados);

        panel.add(panelCentro, BorderLayout.CENTER);

        // Panel de acciones abajo
        JPanel panelAcciones = new JPanel(new GridLayout(5,1,5,5));

        // Buscar estudiante por cédula
        JPanel buscarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscarPanel.setBorder(BorderFactory.createTitledBorder("Buscar estudiante por cédula"));
        txtCedulaBuscar = new JTextField(10);
        JButton btnBuscar = new JButton("Buscar");
        buscarPanel.add(new JLabel("Cédula:"));
        buscarPanel.add(txtCedulaBuscar);
        buscarPanel.add(btnBuscar);

        // Listar cursos relacionados a estudiante
        JPanel relPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        relPanel.setBorder(BorderFactory.createTitledBorder("Listar cursos relacionados a estudiante"));
        JTextField txtCedulaRel = new JTextField(10);
        JButton btnListarRel = new JButton("Listar cursos");
        relPanel.add(new JLabel("Cédula:"));
        relPanel.add(txtCedulaRel);
        relPanel.add(btnListarRel);

        // Crear relación estudiante-curso
        JPanel crearRelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        crearRelPanel.setBorder(BorderFactory.createTitledBorder("Crear relación estudiante-curso"));
        txtCedulaRelacion = new JTextField(10);
        txtCodigoCursoRelacion = new JTextField(10);
        JButton btnCrearRel = new JButton("Crear relación");
        crearRelPanel.add(new JLabel("Cédula estudiante:"));
        crearRelPanel.add(txtCedulaRelacion);
        crearRelPanel.add(new JLabel("Código curso:"));
        crearRelPanel.add(txtCodigoCursoRelacion);
        crearRelPanel.add(btnCrearRel);

        // Botones para listar estudiantes y cursos
        JPanel listarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnListarEstudiantes = new JButton("Listar Estudiantes");
        JButton btnListarCursos = new JButton("Listar Cursos");
        listarPanel.add(btnListarEstudiantes);
        listarPanel.add(btnListarCursos);

        // Salir
        JPanel salirPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalir = new JButton("Salir");
        salirPanel.add(btnSalir);

        panelAcciones.add(buscarPanel);
        panelAcciones.add(relPanel);
        panelAcciones.add(crearRelPanel);
        panelAcciones.add(listarPanel);
        panelAcciones.add(salirPanel);

        panel.add(panelAcciones, BorderLayout.SOUTH);

        // Listeners botones

        btnListarEstudiantes.addActionListener(e -> {
            try {
                listar("estudiantes", tableEstudiantes);
                txtAreaResultado.setText("Listado de estudiantes actualizado.\n");
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnListarCursos.addActionListener(e -> {
            try {
                listar("cursos", tableCursos);
                txtAreaResultado.setText("Listado de cursos actualizado.\n");
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnBuscar.addActionListener(e -> {
            String cedula = txtCedulaBuscar.getText().trim();
            if (cedula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese una cédula para buscar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                String resultado = buscarPorId("estudiantes", "cedula", cedula);
                txtAreaResultado.setText(resultado);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnListarRel.addActionListener(e -> {
            String cedula = txtCedulaRel.getText().trim();
            if (cedula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese una cédula para listar cursos relacionados.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                String resultado = listarRelacionadosTexto("estudiante_curso", "cedula", cedula);
                txtAreaResultado.setText(resultado);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnCrearRel.addActionListener(e -> {
            String cedula = txtCedulaRelacion.getText().trim();
            String codigoCurso = txtCodigoCursoRelacion.getText().trim();

            if (cedula.isEmpty() || codigoCurso.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese cédula y código de curso para crear la relación.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String resp = crearRelacion(cedula, codigoCurso);
                txtAreaResultado.setText("Respuesta servidor: " + resp);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnSalir.addActionListener(e -> System.exit(0));

        // Al iniciar, lista estudiantes y cursos automáticamente
        try {
            listar("estudiantes", tableEstudiantes);
            listar("cursos", tableCursos);
        } catch (Exception ex) {
            mostrarError(ex);
        }
        
        btnAgregarEstudiante.addActionListener(e -> {
    String cedula = txtNuevaCedula.getText().trim();
    String nombre = txtNuevoNombre.getText().trim();
    String email = txtNuevoEmail.getText().trim();

    if (cedula.isEmpty() || nombre.isEmpty() || email.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Complete todos los campos para agregar estudiante.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    try {
        String resp = crearEstudiante(cedula, nombre, email);
        txtAreaResultado.setText("Respuesta: " + resp);
        listar("estudiantes", tableEstudiantes); // refrescar tabla
    } catch (Exception ex) {
        mostrarError(ex);
    }
});

btnAgregarCurso.addActionListener(e -> {
    String codigo = txtNuevoCodigo.getText().trim();
    String nombre = txtNuevoNombreCurso.getText().trim();
    String descripcion = txtNuevaDescripcion.getText().trim();

    if (codigo.isEmpty() || nombre.isEmpty() || descripcion.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Complete todos los campos para agregar curso.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    try {
        String resp = crearCurso(codigo, nombre, descripcion);
        txtAreaResultado.setText("Respuesta: " + resp);
        listar("cursos", tableCursos); // refrescar tabla
    } catch (Exception ex) {
        mostrarError(ex);
    }
});

    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }

    private void listar(String tabla, JTable tablaComponent) throws Exception {
        String urlWithParams = API_URL + "?table=" + URLEncoder.encode(tabla, "UTF-8");
        String response = sendGetRequest(urlWithParams);

        JsonElement parsed = JsonParser.parseString(response);
        if (parsed.isJsonArray()) {
            JsonArray arr = parsed.getAsJsonArray();

            if (arr.size() == 0) {
                tablaComponent.setModel(new DefaultTableModel());
                txtAreaResultado.append("No hay datos en " + tabla + ".\n");
                return;
            }

            // Obtener columnas (claves del primer objeto)
            JsonObject firstObj = arr.get(0).getAsJsonObject();
            Vector<String> columnas = new Vector<>(firstObj.keySet());

            // Construir filas
            Vector<Vector<Object>> data = new Vector<>();
            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                Vector<Object> fila = new Vector<>();
                for (String col : columnas) {
                    fila.add(getSafeString(obj, col));
                }
                data.add(fila);
            }

            DefaultTableModel model = new DefaultTableModel(data, columnas) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tablaComponent.setModel(model);

        } else {
            throw new Exception("Respuesta inesperada: " + parsed);
        }
    }

    private String buscarPorId(String tabla, String idField, String idValue) throws Exception {
        String urlWithParams = API_URL + "?table=" + URLEncoder.encode(tabla, "UTF-8")
                + "&idField=" + URLEncoder.encode(idField, "UTF-8")
                + "&idValue=" + URLEncoder.encode(idValue, "UTF-8");

        String response = sendGetRequest(urlWithParams);

        JsonElement parsed = JsonParser.parseString(response);
        if (parsed.isJsonObject()) {
            JsonObject obj = parsed.getAsJsonObject();
            return jsonObjectToString(obj);
        } else {
            return "Respuesta inesperada: " + parsed;
        }
    }

    private String listarRelacionadosTexto(String tablaPivote, String foreignKey, String id) throws Exception {
        String urlParams = String.format(
                "?relation=true&pivotTable=%s&foreignKey=%s&id=%s",
                URLEncoder.encode(tablaPivote, "UTF-8"),
                URLEncoder.encode(foreignKey, "UTF-8"),
                URLEncoder.encode(id, "UTF-8")
        );

        String response = sendGetRequest(API_URL + urlParams);

        JsonElement parsed = JsonParser.parseString(response);
        if (parsed.isJsonArray()) {
            JsonArray arr = parsed.getAsJsonArray();
            if (arr.size() == 0) {
                return "No se encontraron cursos relacionados para la cédula: " + id;
            }
            StringBuilder sb = new StringBuilder();
            for (JsonElement el : arr) {
                if (el.isJsonObject()) {
                    sb.append(jsonObjectToString(el.getAsJsonObject())).append("\n------------------\n");
                } else {
                    sb.append(el.toString()).append("\n");
                }
            }
            return sb.toString();
        } else {
            return "Respuesta inesperada: " + parsed;
        }
    }

    private String crearRelacion(String cedula, String codigoCurso) throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("relation", true);
        json.addProperty("pivotTable", "estudiante_curso");
        json.addProperty("foreignKey1", "cedula");
        json.addProperty("foreignKey2", "codigo_curso");
        json.addProperty("id1", cedula);
        json.addProperty("id2", codigoCurso);

        return sendRequest("POST", json.toString());
    }

    private String sendRequest(String method, String jsonInput) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
        conn.setRequestMethod(method.equals("GET") ? "POST" : method); // fallback
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes());
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();

        return response.toString();
    }

    private String sendGetRequest(String fullUrl) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(fullUrl).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();

        return response.toString();
    }

    private String getSafeString(JsonObject obj, String key) {
        if (obj.has(key) && !obj.get(key).isJsonNull()) {
            return obj.get(key).getAsString();
        } else {
            return "";
        }
    }

    private String jsonObjectToString(JsonObject obj) {
        StringBuilder sb = new StringBuilder();
        for (String key : obj.keySet()) {
            sb.append(key).append(": ").append(getSafeString(obj, key)).append("\n");
        }
        return sb.toString();
    }
    
    private String crearEstudiante(String cedula, String nombre, String telefono) throws Exception {
    JsonObject json = new JsonObject();
    json.addProperty("table", "estudiantes");
    JsonObject data = new JsonObject();
    data.addProperty("cedula", cedula);
    data.addProperty("nombre", nombre);
    data.addProperty("telefono", telefono);
    json.add("data", data);

    return sendRequest("POST", json.toString());
}

private String crearCurso(String codigo, String nombre, String descripcion) throws Exception {
    JsonObject json = new JsonObject();
    json.addProperty("table", "cursos");
    JsonObject data = new JsonObject();
    data.addProperty("codigo_curso", codigo);
    data.addProperty("nombre_curso", nombre);
    data.addProperty("descripcion", descripcion);
    json.add("data", data);

    return sendRequest("POST", json.toString());
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new App().setVisible(true);
        });
    }
}
