package es.studium.practicatema9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class PedidoAdapter extends BaseAdapter {
    private Context context;
    private List<Pedido> pedidos;

    public PedidoAdapter(Context context, List<Pedido> pedidos) {
        this.context = context;
        this.pedidos = pedidos;
    }

    @Override
    public int getCount() {
        return pedidos.size();
    }

    @Override
    public Object getItem(int position) {
        return pedidos.get(position);
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

        Pedido pedido = (Pedido) getItem(position);

        // Concatena toda la información del pedido en una sola cadena
        String pedidoInfo = "Fecha Estimada: " + pedido.getFechaEstimadaPedido() + "\n" +
                "Descripción: " + pedido.getDescripcionPedido() + "\n" +
                "Importe: " + pedido.getImportePedido() + "\n" +
                "Tienda: " + pedido.getNombreTienda();

        // Asigna la información al TextView
        TextView pedidoTextView = convertView.findViewById(R.id.textViewLista);
        pedidoTextView.setText(pedidoInfo);

        return convertView;
    }

}
