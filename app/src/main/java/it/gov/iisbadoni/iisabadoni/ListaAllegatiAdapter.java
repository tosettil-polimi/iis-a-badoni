package it.gov.iisbadoni.iisabadoni;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lorenzo on 08/11/2015.
 */
public class ListaAllegatiAdapter extends BaseAdapter {

    private CircolareCreata lista;
    private Context context;

    public ListaAllegatiAdapter(CircolareCreata lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lista.getLinks().length;
    }

    @Override
    public Object getItem(int position) {
        return lista.getLinks()[position];
    }

    @Override
    public long getItemId(int position) {
        return lista.getLinks()[position].hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.riga_lista_mydialog, null);
        }

        String sFormato = lista.getFormati()[position];
        final String sAllegato = lista.getLinks()[position];
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, Utils.formattaPerDownload(lista.getLinks()[position]), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        convertView.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PDFView.class);
                String split[];
                /*String split[] = sAllegato.split(".");
                String fileType = split[(split.length-1)];
                if(fileType.equals("pdf")) {*/
                split = sAllegato.split("/");
                intent.putExtra("url", sAllegato);
                intent.putExtra("title", split[(split.length - 1)]);
                context.startActivity(intent);/*
                } else {
                    new InterClassi.ScaricaAllegato().execute(new InterClassi.OggettiPerDownload(context, sAllegato));
                }*/
            }
        });

        TextView allegato = (TextView) convertView.findViewById(R.id.txt_allegato);
        TextView formato = (TextView) convertView.findViewById(R.id.txt_formato_allegato);

        allegato.setText(Utils.formattaPerDownload(sAllegato));
        formato.setText(sFormato);

        return convertView;
    }
}
