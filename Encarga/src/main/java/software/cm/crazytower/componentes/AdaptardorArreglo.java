package software.cm.crazytower.componentes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import software.cm.crazytower.R;
import software.cm.crazytower.modelo.Nodo;

public class AdaptardorArreglo extends ArrayAdapter<Nodo> {
    private final Context context;
    private final int resourceID;

    public AdaptardorArreglo(Context context, int resource, List<Nodo> bah) {
        super(context, resource, bah);

        this.context = context;
        this.resourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resourceID, parent, false);

        TextView tt1 = (TextView) rowView.findViewById(R.id.texto);
        Nodo nodo = this.getItem(position);

        if (nodo.getMac() != null && !nodo.getMac().trim().isEmpty()) {
            tt1.setText(String.format("%s (%s)", nodo.getIp(), nodo.getMac()));
        } else {
            tt1.setText(String.format("%s", nodo.getIp()));
        }

        return rowView;
    }
}