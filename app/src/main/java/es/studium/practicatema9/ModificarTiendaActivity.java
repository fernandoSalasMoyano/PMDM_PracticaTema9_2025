package es.studium.practicatema9;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ModificarTiendaActivity extends AppCompatActivity
{

    private EditText editTextNombreTienda;
    private Button btnAceptar;
    private Button btnCancelar;
    private int idTienda;

    ModificacionRemota modificacionRemota = new ModificacionRemota();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_tienda);


        editTextNombreTienda = findViewById(R.id.editTextText4);
        btnAceptar = findViewById(R.id.button3);
        btnCancelar = findViewById(R.id.button4);

        // Obtener datos del Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idTienda = extras.getInt("idPedido");

            editTextNombreTienda.setText(extras.getString("nombreTienda"));
        }

        // Configurar listeners
        btnAceptar.setOnClickListener(v -> guardarCambios());
        btnCancelar.setOnClickListener(v -> finish());

    }

    private void guardarCambios() {
        // Obtener datos actualizados
        String nombreTienda = editTextNombreTienda.getText().toString();

        boolean exito = modificacionRemota.modificarTienda(idTienda, nombreTienda);

        if (exito) {
            Toast.makeText(this, R.string.toast_ModificarPedidoCorrecto, Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, R.string.toast_ModificarTiendaError, Toast.LENGTH_LONG).show();
        }

        finish();
    }
}