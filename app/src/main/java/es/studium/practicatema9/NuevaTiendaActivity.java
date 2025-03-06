package es.studium.practicatema9;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NuevaTiendaActivity extends AppCompatActivity
{
    private EditText edtNmbreTienda;
    private Button btnAceptar;
    private Button btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tienda);

        edtNmbreTienda = findViewById(R.id.edtNombreTienda);
        btnAceptar = findViewById(R.id.btnAceptarTienda);
        btnCancelar = findViewById(R.id.btnCancelarTienda);

        AltaRemota altaRemota = new AltaRemota();

        btnAceptar.setOnClickListener(v -> {
            String nombreTienda = edtNmbreTienda.getText().toString();


            if (nombreTienda.isEmpty()) {
                Toast.makeText(this, R.string.toast_complete_all_fields, Toast.LENGTH_LONG).show();
                return;
            }

            boolean exito = altaRemota.darAltaTienda(nombreTienda);

            if (exito) {
                Toast.makeText(this, R.string.toast_tiendaNuevaCorrecta, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, R.string.toast_tiendaNuevaError, Toast.LENGTH_LONG).show();
            }
        });

        btnCancelar.setOnClickListener(v -> {
            finish();
        });
    }
}