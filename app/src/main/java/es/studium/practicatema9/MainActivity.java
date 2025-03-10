package es.studium.practicatema9;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private Button btnNuevoPedido;
    private Button btnTiendas;
    private PedidoAdapter adapter;
    private List<Pedido> pedidos;
    AccesoTiendas accesoTiendas = new AccesoTiendas(this);
    BajaRemota bajaRemota = new BajaRemota();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        btnNuevoPedido = findViewById(R.id.btnNuevoPedido);
        btnNuevoPedido.setOnClickListener(this);
        btnTiendas = findViewById(R.id.btnTiendas);
        btnTiendas.setOnClickListener(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Inicializar la lista de pedidos
        pedidos = new ArrayList<>();

        AccesoRemoto accesoRemoto = new AccesoRemoto();
        JSONArray jsonArray = accesoRemoto.obtenerListado();

        pedidos = parseJsonArray(jsonArray);
        adapter = new PedidoAdapter(this, pedidos);
        listView.setAdapter(adapter);

        // Configurar el listener para el pulsado largo
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            mostrarDialogoEliminar(position);
            return true;
        });

        // Configurar el listener para el clic corto
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Pedido pedido = pedidos.get(position);
            Intent intent = new Intent(MainActivity.this, ModificacionPedidoActivity.class);
            intent.putExtra("idPedido", pedido.getIdPedido());
            intent.putExtra("fechaPedido", pedido.getFechaPedido());
            intent.putExtra("fechaEstimadaPedido", pedido.getFechaEstimadaPedido());
            intent.putExtra("descripcionPedido", pedido.getDescripcionPedido());
            intent.putExtra("importePedido", pedido.getImportePedido());
            intent.putExtra("estadoPedido", pedido.getEstadoPedido());
            intent.putExtra("nombreTienda", pedido.getNombreTienda());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPedidos(); // Recargamos la lista cuando volvemos a esta actividad
    }

    private void cargarPedidos() {
        AccesoRemoto accesoRemoto = new AccesoRemoto();
        JSONArray jsonArray = accesoRemoto.obtenerListado();

        pedidos = parseJsonArray(jsonArray);
        adapter = new PedidoAdapter(this, pedidos);
        listView.setAdapter(adapter);
    }

    private List<Pedido> parseJsonArray(JSONArray jsonArray) {
        List<Pedido> pedidos = new ArrayList<>();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Pedido pedido = new Pedido();
                pedido.setIdPedido(jsonObject.getInt("idPedido"));
                pedido.setFechaPedido(jsonObject.getString("fechaPedido"));
                Date fechaEstimadaPedido = inputFormat.parse(jsonObject.getString("fechaEstimadaPedido"));
                pedido.setFechaEstimadaPedido(outputFormat.format(fechaEstimadaPedido));
                pedido.setDescripcionPedido(jsonObject.getString("descripcionPedido"));
                pedido.setImportePedido(jsonObject.getDouble("importePedido"));
                pedido.setEstadoPedido(jsonObject.getInt("estadoPedido"));
                int idTiendaFK = jsonObject.getInt("idTiendaFK");
                // Obtener el nombre de la tienda usando el idTiendaFK
                String nombreTienda = accesoTiendas.obtenerNombreTienda(idTiendaFK);
                pedido.setNombreTienda(nombreTienda);
                pedidos.add(pedido);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
        return pedidos;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNuevoPedido) {
            Intent intent = new Intent(MainActivity.this, NuevoPedidoActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, TiendasActivity.class);
            startActivity(intent);
        }
    }

    private void mostrarDialogoEliminar(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Pedido");
        builder.setMessage(R.string.msgEliminarPedido);
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Pedido pedido = pedidos.get(position);
                int idPedido = pedido.getIdPedido(); // Obtener el id del pedido


                boolean exito = bajaRemota.darBajaPedido(idPedido);

                if (exito) {
                    Toast.makeText(MainActivity.this, R.string.toast_borradoPedidoCorrecto, Toast.LENGTH_LONG).show();
                    pedidos.remove(position);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, R.string.toast_borradoPedidoError, Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
