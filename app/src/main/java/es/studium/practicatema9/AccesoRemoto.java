package es.studium.practicatema9;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccesoRemoto {
    private OkHttpClient client = new OkHttpClient();
    private Request request = new Request.Builder()
            .url("http://192.168.1.143/APIpractica/pedidos.php")
            .build();

    public AccesoRemoto() {}

    public JSONArray obtenerListado() {
        JSONArray resultado = new JSONArray();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                Log.d("API Response", responseData);
                resultado = new JSONArray(responseData);
            } else {
                Log.e("AccesoRemoto", response.message());
            }
        } catch (IOException | JSONException e) {
            Log.e("AccesoRemoto", e.getMessage());
        }
        return resultado;
    }
}
