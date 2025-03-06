package es.studium.practicatema9;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TiendasActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private Button btnVolver;
    private Button btnNuevaTienda;
    private TiendaAdapter adapter;
    private List<Tienda> tiendas;
    BajaRemota bajaRemota = new BajaRemota();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiendas);

        listView = findViewById(R.id.listViewTiendas);
        btnVolver = findViewById(R.id.btnVolver);
        btnNuevaTienda = findViewById(R.id.btnNuevaTienda);
        btnVolver.setOnClickListener(this);
        btnNuevaTienda.setOnClickListener(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Inicializar la lista de tiendas
        tiendas = new ArrayList<>();

        AccesoTiendas accesoTiendas = new AccesoTiendas(this);
        JSONArray jsonArray = accesoTiendas.obtenerListadoTiendas();

        tiendas = parseJsonArray(jsonArray);
        adapter = new TiendaAdapter(this, tiendas);
        listView.setAdapter(adapter);

        // Configurar el listener para el pulsado largo
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            mostrarDialogoEliminar(position);
            return true;
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Tienda tienda = tiendas.get(position);
            Intent intent = new Intent(TiendasActivity.this, ModificarTiendaActivity.class);
            intent.putExtra("idPedido", tienda.getIdTienda());
            intent.putExtra("nombreTienda", tienda.getNombreTienda());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTiendas(); // Recargamos la lista cuando volvemos a esta actividad
    }

    private void cargarTiendas() {
        AccesoTiendas accesoTiendas = new AccesoTiendas(this);
        JSONArray jsonArray = accesoTiendas.obtenerListadoTiendas();

        tiendas = parseJsonArray(jsonArray);
        adapter = new TiendaAdapter(this, tiendas);
        listView.setAdapter(adapter);
    }

    private List<Tienda> parseJsonArray(JSONArray jsonArray) {
        List<Tienda> tiendas = new ArrayList<>();
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
        return tiendas;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnVolver) {
            finish();
        } else {
            Intent intent = new Intent(TiendasActivity.this, NuevaTiendaActivity.class);
            startActivity(intent);
        }
    }

    private void mostrarDialogoEliminar(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Tienda");
        builder.setMessage(R.string.msgEliminarTienda);
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Tienda tienda = tiendas.get(position);
                int idTienda = tienda.getIdTienda(); // Obtener el id de la tienda

                boolean exito = bajaRemota.darBajaTienda(idTienda);

                if (exito) {
                    Toast.makeText(TiendasActivity.this, R.string.toast_borradoTiendaCorrecto, Toast.LENGTH_LONG).show();
                    tiendas.remove(position);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(TiendasActivity.this, R.string.toast_borradoTiendaError, Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}