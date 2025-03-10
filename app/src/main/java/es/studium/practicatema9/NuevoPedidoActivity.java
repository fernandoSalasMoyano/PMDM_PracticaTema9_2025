package es.studium.practicatema9;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;

public class NuevoPedidoActivity extends AppCompatActivity {

    private EditText editTextFechaEstimada;
    private EditText editTextDescripcion;
    private EditText editTextImporte;
    private Spinner spinnerTiendas;
    private Button btnAceptar;
    private Button btnCancelar;
    private JSONArray tiendas;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_pedido);

        editTextFechaEstimada = findViewById(R.id.edtFechaEstimada);
        editTextDescripcion = findViewById(R.id.edtDescripcionPedido);
        editTextImporte = findViewById(R.id.edtImporte);
        spinnerTiendas = findViewById(R.id.spinner2);
        btnAceptar = findViewById(R.id.btnAceptar);
        btnCancelar = findViewById(R.id.btnCancelar);

        AltaRemota altaRemota = new AltaRemota();

        client = new OkHttpClient();

        // Cargar tiendas
        AccesoTiendas accesoTienda = new AccesoTiendas(this);
        tiendas = accesoTienda.obtenerListadoTiendas();
        cargarTiendasEnSpinner();

        btnAceptar.setOnClickListener(v -> {
            String fechaEstimada = editTextFechaEstimada.getText().toString();
            String descripcion = editTextDescripcion.getText().toString();
            String importeTexto = editTextImporte.getText().toString();
            int idTiendaFK = obtenerIdTiendaSeleccionada();
            if (fechaEstimada.isEmpty() || descripcion.isEmpty() || importeTexto.isEmpty()) {
                Toast.makeText(this, R.string.toast_complete_all_fields, Toast.LENGTH_LONG).show();
                return;
            }

            if(idTiendaFK == -1)
            {
                Toast.makeText(this, R.string.toast_errorTiendaSeleccionado, Toast.LENGTH_LONG).show();
                return;
            }

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

            boolean exito = altaRemota.darAltaPedido(fechaEstimada, descripcion, importe, idTiendaFK);

            if (exito) {
                Toast.makeText(this, R.string.toast_order_registered, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, R.string.toast_error_registering_order, Toast.LENGTH_LONG).show();
            }
        });



        btnCancelar.setOnClickListener(v -> {
            finish();
        });
    }

    private void cargarTiendasEnSpinner() {
        // Crear un array de nombres de tiendas con una opción predeterminada
        String[] nombresTiendas = new String[tiendas.length() + 1];
        nombresTiendas[0] = "Selecciona una tienda";

        for (int i = 0; i < tiendas.length(); i++) {
            try {
                nombresTiendas[i + 1] = tiendas.getJSONObject(i).getString("nombreTienda");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Crear adaptador para el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresTiendas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTiendas.setAdapter(adapter);
    }


    private int obtenerIdTiendaSeleccionada() {
        int posicion = spinnerTiendas.getSelectedItemPosition();
        if (posicion == 0) {
            // La posición 0 es la opción predeterminada "Selecciona una tienda"
            return -1;
        }
        try {
            return tiendas.getJSONObject(posicion - 1).getInt("idTienda");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
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
