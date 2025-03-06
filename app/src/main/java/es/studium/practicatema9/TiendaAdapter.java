package es.studium.practicatema9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TiendaAdapter extends BaseAdapter
{
    private Context context;
    private List<Tienda> tiendas;

    public TiendaAdapter(Context context, List<Tienda> tiendas) {
        this.context = context;
        this.tiendas = tiendas;
    }

    @Override
    public int getCount() {
        return tiendas.size();
    }

    @Override
    public Object getItem(int position) {
        return tiendas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lista_item, parent, false);
        }

        Tienda tienda = (Tienda) getItem(position);

        String tiendaInfo = tienda.getNombreTienda();

        // Asigna la información al TextView
        TextView tiendaTextView = convertView.findViewById(R.id.textViewLista);
        tiendaTextView.setText(tiendaInfo);

        return convertView;
    }
}
