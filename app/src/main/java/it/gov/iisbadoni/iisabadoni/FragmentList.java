package it.gov.iisbadoni.iisabadoni;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorenzo on 02/11/2015.
 */
public class FragmentList extends android.support.v4.app.Fragment implements TaskListener {

    private List<CircularFromDb> lista;
    private ProgressBar pb;
    private ListaCircolariAdapter adapter;
    private ListView listView;
    private View rootView;
    private Exception eccezione;

    public FragmentList() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.list_layout, container, false);
        listView = (ListView) rootView.findViewById(R.id.lista);
        pb = (ProgressBar) rootView.findViewById(R.id.progressLista);
        settaLista();
        return rootView;
    }

    private void settaLista() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), CircolareActiviry.class);
                intent.putExtra("nome circolare", lista.get(i).getTitolo());        //mettere la roba di tipo String (nome circolare)
                intent.putExtra("link", lista.get(i).getLink());
                intent.putExtra("nProto", lista.get(i).getProtocollo());
                startActivity(intent);
            }
        });
    }

    public void populateList(String categoria) {
        listView.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        pb.setIndeterminate(true);
        new ScaricaArrayJSON(this).execute(categoria);
    }

    public void populateSearchList(String categoria, String query) {
        listView.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        pb.setIndeterminate(true);
        String[] array = new String[2];
        array[0] = categoria;
        array[1] = query;
        new ScaricaArrayJSON(this).execute(array);
    }

    @Override
    public void onTaskCompleted() {
        if(eccezione == null) {
            pb.setVisibility(View.INVISIBLE);
            TextView tw = (TextView) rootView.findViewById(R.id.nessun_risultato_textview);
            if(adapter.getCount() == 0)
                tw.setVisibility(View.VISIBLE);
            else {
                tw.setVisibility(View.INVISIBLE);
                listView.setAdapter(adapter);
                listView.setVisibility(View.VISIBLE);
            }
        }
        else {
            Utils.dialogHome((Activity) getContext(), getContext());
        }
    }

    private class ScaricaArrayJSON extends AsyncTask<String, Void, Void> {

        private TaskListener listener;

        public ScaricaArrayJSON(TaskListener listener) {
            this.listener = listener;
        }

        protected Void doInBackground(String... urls) {

            URL url;
            lista = new ArrayList<>();
            BufferedReader br;
            String circDaCaricare = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("numCircolari", "30cir");
            int nCircDaCaricare = Integer.parseInt(circDaCaricare.split("cir")[0]);
            try {
                if(urls.length == 1) {
                    url = new URL("http://vps208467.ovh.net:8080/Gestione_circolari/Circolari?nCircolari=" + nCircDaCaricare + "&categoria=" + urls[0].replaceAll(" ", "%20"));
                    br = new BufferedReader(new InputStreamReader(url.openStream()));
                    String buffer, file = "";
                    while ((buffer = br.readLine()) != null) {
                        file += buffer;
                    }
                    CircularFromDb circ;
                    JSONArray jArray = new JSONArray(file);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        circ = new CircularFromDb(json_data.getString("titolo"), json_data.getString("link"), json_data.getString("data"), json_data.getString("protocollo"));
                        lista.add(circ);
                    }
                } else {
                    url = new URL("http://vps208467.ovh.net:8080/Gestione_circolari/RicercaCircolari?nCircolari=" + nCircDaCaricare + "&categoria=" + urls[0].replaceAll(" ", "%20") + "&query=" + urls[1]);
                    br = new BufferedReader(new InputStreamReader(url.openStream()));
                    String buffer, file = "";
                    while ((buffer = br.readLine()) != null) {
                        file += buffer;
                    }
                    CircularFromDb circ;
                    JSONArray jArray = new JSONArray(file);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        circ = new CircularFromDb(json_data.getString("titolo"), json_data.getString("link"), json_data.getString("data"), json_data.getString("protocollo"));
                        lista.add(circ);
                    }
                }
            }
            catch (IOException e) {
                eccezione = e;
            }
            catch (JSONException e) {
                eccezione = e;
            }
            catch (Exception e) {
                eccezione = e;
            }

            adapter = new ListaCircolariAdapter(getContext(), lista);

            return null;
        }

        protected void onPostExecute(Void feed) {
            super.onPostExecute(null);
            listener.onTaskCompleted();
        }
    }
}
