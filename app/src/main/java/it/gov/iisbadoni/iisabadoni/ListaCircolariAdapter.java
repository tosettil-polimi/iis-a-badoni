package it.gov.iisbadoni.iisabadoni;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lorenzo on 24/05/2015.
 */
public class ListaCircolariAdapter extends BaseAdapter {

    private List<CircularFromDb> lista;
    private Context context;

    public ListaCircolariAdapter(Context context, List<CircularFromDb> lista) {
        this.lista=lista;
        this.context=context;
    }



    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lista.get(i).hashCode();
    }

    @Override
    public View getView(int position, View v, ViewGroup vg) {
        if (v==null)
        {
            v= LayoutInflater.from(context).inflate(R.layout.riga_lista, null);
        }
        CircularFromDb ai=(CircularFromDb) getItem(position);
        TextView txt=(TextView) v.findViewById(R.id.txt_title);
        txt.setText(Html.fromHtml(ai.getTitolo()));
        txt=(TextView) v.findViewById(R.id.txt_date);
        txt.setText(ai.getData());
        return v;
    }
}
