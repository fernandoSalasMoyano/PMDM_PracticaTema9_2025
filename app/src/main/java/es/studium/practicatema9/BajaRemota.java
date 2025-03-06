package es.studium.practicatema9;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BajaRemota
{
    OkHttpClient client = new OkHttpClient();
    BajaRemota() {}
    boolean darBajaPedido(int id)
    {
        boolean correcta = true;
        Request request = new Request.Builder()

                .url("http://192.168.1.143/APIpractica/pedidos.php?idPedido="+id)
                .delete()
                .build();
        Call call = client.newCall(request);
        try
        {
            Response response = call.execute();
            Log.i("BajaRemota", String.valueOf(response));
            correcta = true;
        }
        catch (IOException e)
        {
            Log.e("BajaRemota", e.getMessage());
            correcta = false;
        }
        return correcta;
    }

    boolean darBajaTienda(int id)
    {
        boolean correcta = true;
        Request request = new Request.Builder()

                .url("http://192.168.1.143/APIpractica/tiendas.php?idTienda="+id)
                .delete()
                .build();
        Call call = client.newCall(request);
        try
        {
            Response response = call.execute();
            Log.i("BajaRemota", String.valueOf(response));
            correcta = true;
        }
        catch (IOException e)
        {
            Log.e("BajaRemota", e.getMessage());
            correcta = false;
        }
        return correcta;
    }
}
