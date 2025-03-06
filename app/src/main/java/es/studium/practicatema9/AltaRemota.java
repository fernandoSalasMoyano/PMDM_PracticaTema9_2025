package es.studium.practicatema9;

import android.util.Log;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AltaRemota {
    private final OkHttpClient client = new OkHttpClient();

    public boolean darAltaPedido(String fechaEstimada, String descripcion, double importe, int idTiendaFK) {
        boolean correcta;

        // Montamos la petición POST en formato application/x-www-form-urlencoded
        RequestBody formBody = new FormBody.Builder()
                .add("fechaEstimadaPedido", fechaEstimada)
                .add("descripcionPedido", descripcion)
                .add("importePedido", String.valueOf(importe))
                .add("idTiendaFK", String.valueOf(idTiendaFK))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.143/APIpractica/pedidos.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            Log.i("AltaRemota", "Código: " + response.code() + ", Respuesta: " + response.body().string());
            correcta = response.isSuccessful();
        } catch (IOException e) {
            Log.e("AltaRemota", "Error en la solicitud: " + e.getMessage());
            correcta = false;
        }
        return correcta;
    }

    public boolean darAltaTienda(String nombreTienda)
    {
        boolean correcta;

        // Montamos la petición POST en formato application/x-www-form-urlencoded
        RequestBody formBody = new FormBody.Builder()
                .add("nombreTienda", nombreTienda)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.143/APIpractica/tiendas.php") // Reemplaza con la IP de tu servidor
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            Log.i("AltaRemota", "Código: " + response.code() + ", Respuesta: " + response.body().string());
            correcta = response.isSuccessful();
        } catch (IOException e) {
            Log.e("AltaRemota", "Error en la solicitud: " + e.getMessage());
            correcta = false;
        }
        return correcta;
    }

}
