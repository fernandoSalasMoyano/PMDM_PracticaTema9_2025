package es.studium.practicatema9;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccesoTiendas {

    private final Context context;



    public AccesoTiendas(Context context) {
        this.context = context;
    }

    private OkHttpClient client = new OkHttpClient();
    private Request request = new Request.Builder()
            .url("http://192.168.1.143/APIpractica/tiendas.php") // Asegúrate de que esta URL sea correcta
            .build();

    public JSONArray obtenerListadoTiendas() {
        JSONArray resultado = new JSONArray();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                Log.d("API Response", responseData);
                resultado = new JSONArray(responseData);
            } else {
                Log.e("AccesoTiendas", response.message());
            }
        } catch (IOException | JSONException e) {
            Log.e("AccesoTiendas", e.getMessage());
        }
        return resultado;
    }

    public String obtenerNombreTienda(int idTienda) {
        String nombreTienda = null;

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                JSONArray jsonArray = new JSONArray(responseData);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getInt("idTienda") == idTienda) {
                        nombreTienda = jsonObject.getString("nombreTienda");
                        break;
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return nombreTienda;
    }
}
