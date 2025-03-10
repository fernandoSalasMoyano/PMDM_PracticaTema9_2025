package es.studium.practicatema9;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ModificacionPedidoActivity extends AppCompatActivity {

    private TextView txtPedido;
    private EditText editTextFechaEstimada;
    private EditText editTextDescripcion;
    private EditText editTextImporte;
    private CheckBox checkBoxRecibido;
    private Spinner spinnerTiendas;
    private Button btnAceptar;
    private Button btnCancelar;

    ModificacionRemota modificacionRemota = new ModificacionRemota();

    private List<Tienda> tiendas;
    private int idPedido;
    private String fechaPedido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificacion_pedido);

        // Inicializar vistas
        txtPedido = findViewById(R.id.txtPedido);
        editTextFechaEstimada = findViewById(R.id.editTextText);
        editTextDescripcion = findViewById(R.id.editTextText2);
        editTextImporte = findViewById(R.id.editTextText3);
        checkBoxRecibido = findViewById(R.id.checkBox);
        spinnerTiendas = findViewById(R.id.spinner5);
        btnAceptar = findViewById(R.id.button2);
        btnCancelar = findViewById(R.id.button);

        // Obtener datos del Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idPedido = extras.getInt("idPedido");
            txtPedido.setText("Pedido Nº: " + idPedido);
            fechaPedido = extras.getString("fechaPedido");
            editTextFechaEstimada.setText(extras.getString("fechaEstimadaPedido"));
            editTextDescripcion.setText(extras.getString("descripcionPedido"));
            editTextImporte.setText(String.valueOf(extras.getDouble("importePedido")));
            checkBoxRecibido.setChecked(extras.getInt("estadoPedido") == 1);

            // Cargar tiendas en el Spinner
            cargarTiendasEnSpinner();
            // Seleccionar la tienda actual en el Spinner
            String nombreTiendaActual = extras.getString("nombreTienda");
            seleccionarTiendaEnSpinner(nombreTiendaActual);
        }

        // Configurar listeners
        btnAceptar.setOnClickListener(v -> guardarCambios());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void cargarTiendasEnSpinner() {
        tiendas = new ArrayList<>();
        AccesoTiendas accesoTiendas = new AccesoTiendas(this);
        JSONArray jsonArray = accesoTiendas.obtenerListadoTiendas();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Tienda tienda = new Tienda();
                tienda.setIdTienda(jsonObject.getInt("idTienda"));
                tienda.setNombreTienda(jsonObject.getString("nombreTienda"));
                tiendas.add(tienda);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear adaptador para el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        for (Tienda tienda : tiendas) {
            adapter.add(tienda.getNombreTienda());
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTiendas.setAdapter(adapter);
    }

    private void seleccionarTiendaEnSpinner(String nombreTienda) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerTiendas.getAdapter();
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).equals(nombreTienda)) {
                    spinnerTiendas.setSelection(i);
                    break;
                }
            }
        }
    }

    private void guardarCambios() {
        // Obtener datos actualizados
        String fechaEstimada = editTextFechaEstimada.getText().toString();
        String descripcion = editTextDescripcion.getText().toString();
        String importeTexto = editTextImporte.getText().toString();
        int estadoPedido = checkBoxRecibido.isChecked() ? 1 : 0;
        int idTiendaFK = obtenerIdTiendaSeleccionada();
        if (!esFechaValida(fechaEstimada)) {
            Toast.makeText(this, R.string.toast_valid_date_format, Toast.LENGTH_LONG).show();
            return;
        }

        double importe;
        try {
            importe = Double.parseDouble(importeTexto);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.toast_valid_amount, Toast.LENGTH_LONG).show();
            return;
        }

        // Convertir la fecha a formato americano
        SimpleDateFormat formatoEuropeo = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat formatoAmericano = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date fechaParsed = formatoEuropeo.parse(fechaEstimada);
            fechaEstimada = formatoAmericano.format(fechaParsed);
        } catch (ParseException e) {
            Toast.makeText(this, R.string.toast_valid_date_format, Toast.LENGTH_LONG).show();
            return;
        }

        boolean exito = modificacionRemota.modificarPedido(idPedido, fechaPedido, fechaEstimada, descripcion, importe, estadoPedido, idTiendaFK);

        if (exito) {
            Toast.makeText(this, R.string.toast_ModificarPedidoCorrecto, Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, R.string.toast_ModificarPedidoError, Toast.LENGTH_LONG).show();
        }
    }

    private int obtenerIdTiendaSeleccionada() {
        int posicion = spinnerTiendas.getSelectedItemPosition();
        return tiendas.get(posicion).getIdTienda();
    }

    private boolean esFechaValida(String fecha) {
        SimpleDateFormat formatoEuropeo = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formatoEuropeo.setLenient(false); // Evita que acepte fechas inválidas como "30/02/2024"
        try {
            formatoEuropeo.parse(fecha);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
