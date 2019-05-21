package it.gov.iisbadoni.iisabadoni;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Lorenzo on 25/10/2015.
 */
public interface InterClassi {

    class DialogPerDownload {

        private CircolareCreata oggetti;
        private Context context;

        public DialogPerDownload(Context context, CircolareCreata oggetti) {
            this.oggetti = oggetti;
            this.context = context;
            onMenuItemClick();
        }

        public boolean onMenuItemClick() {
            new ResourceDataDialogBuilder(context, oggetti)
                    .setPositiveButton("Annulla", null)
                    .show();
            return true;
        }
    }

    class OggettiPerCircolare {

        private String link;
        private Activity activity;
        private CircolareCreata creato;
        private Context context;
        private TextView cont;
        private MenuItem linkAllegato;
        private ProgressBar progressBar;

        public OggettiPerCircolare(String link, Activity activity, Context context, TextView cont, MenuItem linkAllegato, ProgressBar progressBar, CircolareCreata creato) {
            this.link = link;
            this.activity = activity;
            this.creato = creato;
            this.context = context;
            this.cont = cont;
            this.linkAllegato = linkAllegato;
            this.progressBar = progressBar;
        }

        public MenuItem getMenuItem() {
            return linkAllegato;
        }

        public String getLink() {
            return link;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        public Activity getActivity() {
            return activity;
        }

        public Context getContext() {
            return context;
        }

        public TextView getCont() {
            return cont;
        }

        public CircolareCreata getCreato() {
            return creato;
        }

        public void setCreato(CircolareCreata creato) {
            this.creato = creato;
        }
    }

    class RichiestaCircolareHTTP extends AsyncTask<OggettiPerCircolare,Void,CircolareCreata> {

        private String contenuto;
        private String linkCirc;
        private OggettiPerCircolare oggettiPerCircolare;
        private Exception e;

        @Override
        protected CircolareCreata doInBackground(OggettiPerCircolare... objects) {

            oggettiPerCircolare = objects[0];
            CircolareCreata circolareCreata = new CircolareCreata();
            try {
                URL url = new URL("http://vps208467.ovh.net:8080/Gestione_circolari/CircolareDaLink?link=" + URLEncoder.encode(oggettiPerCircolare.getLink(), "UTF-8"));
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String json = "";
                String line;

                while((line = br.readLine()) != null)
                    json += line;

                JSONObject obj = new JSONObject(json);
                JSONArray array = obj.getJSONArray("allegati");
                ArrayList<String> linkAllegati = new ArrayList<>();
                ArrayList<String> titoloAllegati = new ArrayList<>();

                for(int i=0;i<array.length();i++) {
                    JSONObject allegato = array.getJSONObject(i);
                    linkAllegati.add(allegato.getString("link"));
                    titoloAllegati.add(allegato.getString("titolo"));
                }

                String[] formati = new String[titoloAllegati.size()];
                //creo un array contenente il formato degli allegati (pdf, xlxs, doc..)
                for (int i = 0; i < titoloAllegati.size(); i++) {
                    String[] split = titoloAllegati.get(i).split("\\.");
                    try {formati[i] = split[(split.length - 1)];} catch(Exception e) {}
                }

                circolareCreata.setFormati(formati);
                circolareCreata.setContenuto(obj.getString("testo"));
                circolareCreata.setLinks(linkAllegati.toArray(new String[0]));
                circolareCreata.setnProto(obj.getString("nproto"));

                contenuto = circolareCreata.getContenuto();
                try {
                    linkCirc = circolareCreata.getLinks()[0];    //primo oggetto dell'array: link della circolare, gli altri link agli allegati
                }
                catch (Exception e) {
                    linkCirc = null;
                }
                oggettiPerCircolare.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        MenuItem imageView = oggettiPerCircolare.getMenuItem();
                        if (linkCirc != null && !linkCirc.equals("")) {
                            imageView.setVisible(true);
                        }

                        ProgressBar pb = oggettiPerCircolare.getProgressBar();
                        pb.setIndeterminate(false);
                        pb.setVisibility(View.INVISIBLE);
                        if (contenuto.length() > 0) {
                            oggettiPerCircolare.getCont().setText(Html.fromHtml(contenuto));
                        } else {
                            oggettiPerCircolare.getCont().setText("Testo non presente...");
                        }
                    }
                });
            } catch (IOException e) {
                this.e = e;
                Log.e("IOException", "RichiestaCircolareHTTP");
            }
            catch (Exception e) {
                this.e = e;
                oggettiPerCircolare.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        oggettiPerCircolare.getCont().setText("Vedi sito");
                        ProgressBar pb = oggettiPerCircolare.getProgressBar();
                        pb.setIndeterminate(false);
                        pb.setVisibility(View.INVISIBLE);
                    }
                });
                Log.e("Exception", "RichiestaCircolareHTTP");
            }

            oggettiPerCircolare.setCreato(circolareCreata);
            return circolareCreata;
        }

        @Override
        protected void onPostExecute(CircolareCreata aVoid) {
            super.onPostExecute(aVoid);
            if(e != null) {
                e.printStackTrace();
                Utils.dialogHome(oggettiPerCircolare.getActivity(), oggettiPerCircolare.getContext());
            }
        }
    }

}
