package it.gov.iisbadoni.iisabadoni;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lorenzo on 08/11/2015.
 */
public class ResourceDataDialogBuilder extends Builder {

    private CircolareCreata creato;
    private Context context;

    public ResourceDataDialogBuilder(Context context, CircolareCreata creato) {
        super(context);
        this.context = context;
        this.creato = creato;
        builderSetup(context);
    }

    private void builderSetup(final Context context) {
        View customTitle = View.inflate(context, R.layout.title_mydialog, null);
        View customMessage = View.inflate(context, R.layout.mydialog_message, null);

        TextView textView = (TextView) customTitle.findViewById(R.id.alertTitle);
        Typeface roboto = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Light.ttf");
        textView.setTypeface(roboto);

        final ListView listView =  (ListView) customMessage.findViewById(R.id.list_dialog);
        listView.setAdapter(new ListaAllegatiAdapter(creato, context));
        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ListView lista = listView;
                Toast.makeText(context, (String) lista.getItemAtPosition(v.getVerticalScrollbarPosition()), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        setCustomTitle(customTitle);
        setView(customMessage);
    }
}
