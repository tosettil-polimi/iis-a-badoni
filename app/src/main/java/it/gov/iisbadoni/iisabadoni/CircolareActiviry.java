package it.gov.iisbadoni.iisabadoni;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URLDecoder;

import it.gov.iisbadoni.iisabadoni.InterClassi.*;


public class CircolareActiviry extends AppCompatActivity {

    private String url;
    private String link;
    private String nProto;
    private OggettiPerCircolare oggetti;
    private Menu menu;
    private TextView cont;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_circolare);

            Intent intent = getIntent();

            TextView nProtocollo = (TextView) findViewById(R.id.testo2);
            cont = (TextView) findViewById(R.id.contenutoCirc);
            //cont.setTypeface(Typeface.createFromAsset(getAssets(), "font/NexaRegular.otf"));
            TextView nomeCircolare = (TextView) findViewById(R.id.testo_circolare);

            String nomeCirc = intent.getStringExtra("nome circolare");
            link = intent.getStringExtra("link");
            nProto = intent.getStringExtra("nProto");

            Uri uri = intent.getData();
            if(uri != null) {
                this.uri = uri;
                String[] split = uri.toString().split("/");
                split = split[split.length - 1].split("-");
                String title = "";
                for(String piece: split)
                    title += URLDecoder.decode(piece, "UTF-8").toUpperCase() + " ";
                title = title.trim();
                nomeCircolare.setText(Html.fromHtml(title));
            } else
                nomeCircolare.setText(Html.fromHtml(nomeCirc));

            url = "http://www.iisbadoni.gov.it" + link;


            if (!(nProto == null || nProto.trim().equals("null"))) {
                nProtocollo.setText(nProto);
            }

            startService(new Intent(this, ServizioNuovaCircolare.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.circolare_menu, menu);
        this.menu = menu;
        if(uri != null)
            oggetti = new OggettiPerCircolare(uri.toString().replace("http://www.iisbadoni.gov.it", ""), CircolareActiviry.this, CircolareActiviry.this, cont, menu.findItem(R.id.attach), (ProgressBar) findViewById(R.id.progressCirc), null);
        else
            oggetti = new OggettiPerCircolare(link, CircolareActiviry.this, CircolareActiviry.this, cont, menu.findItem(R.id.attach), (ProgressBar) findViewById(R.id.progressCirc), null);
        new InterClassi.RichiestaCircolareHTTP().execute(oggetti);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.attach:
                if(oggetti.getCreato() != null) {
                    new InterClassi.DialogPerDownload(this, oggetti.getCreato());
                    return true;
                }
                return false;

            case R.id.link_circolare:
                Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                urlIntent.setData(Uri.parse(url));
                startActivity(urlIntent);
                return true;

            case R.id.share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Leggi questa circolare " + url);
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
