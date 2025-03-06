package es.studium.practicatema9;

import android.util.Log;
import java.io.IOException;
import java.net.URI;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModificacionRemota {
    OkHttpClient client = new OkHttpClient();

    ModificacionRemota() {}

    boolean modificarPedido(int idPedido, String fechaPedido, String fechaEstimada, String descripcionPedido, double importePedido, int estadoPedido, int idTienda) {
        boolean correcta = false;

        HttpUrl.Builder queryUrlBuilder = HttpUrl.get(URI.create("http://192.168.1.143/APIpractica/pedidos.php"))
                .newBuilder();
        queryUrlBuilder.addQueryParameter("idPedido", String.valueOf(idPedido));
        queryUrlBuilder.addQueryParameter("fechaPedido", fechaPedido);
        queryUrlBuilder.addQueryParameter("descripcionPedido", descripcionPedido);
        queryUrlBuilder.addQueryParameter("fechaEstimadaPedido", fechaEstimada);
        queryUrlBuilder.addQueryParameter("importePedido", String.valueOf(importePedido));
        queryUrlBuilder.addQueryParameter("estadoPedido", String.valueOf(estadoPedido));
        queryUrlBuilder.addQueryParameter("idTiendaFK", String.valueOf(idTienda));

        // Las peticiones PUT requieren BODY, aunque sea vacío
        RequestBody formBody = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .put(formBody)
                .build();

        Log.i("ModificacionRemota", "Request URL: " + request.url().toString());

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            Log.i("ModificacionRemota", "Response Code: " + response.code());
            Log.i("ModificacionRemota", "Response Body: " + response.body().string());
            correcta = response.isSuccessful();
        } catch (IOException e) {
            Log.e("ModificacionRemota", "Error: " + e.getMessage());
        }

        return correcta;
    }

    boolean modificarTienda(int idTienda, String nombreTienda) {
        boolean correcta = false;

        HttpUrl.Builder queryUrlBuilder = HttpUrl.get(URI.create("http://192.168.1.143/APIpractica/tiendas.php"))
                .newBuilder();
        queryUrlBuilder.addQueryParameter("idTienda", String.valueOf(idTienda));
        queryUrlBuilder.addQueryParameter("nombreTienda", String.valueOf(nombreTienda));


        // Las peticiones PUT requieren BODY, aunque sea vacío
        RequestBody formBody = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .put(formBody)
                .build();

        Log.i("ModificacionRemota", "Request URL: " + request.url().toString());

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            Log.i("ModificacionRemota", "Response Code: " + response.code());
            Log.i("ModificacionRemota", "Response Body: " + response.body().string());
            correcta = response.isSuccessful();
        } catch (IOException e) {
            Log.e("ModificacionRemota", "Error: " + e.getMessage());
        }

        return correcta;
    }
}
