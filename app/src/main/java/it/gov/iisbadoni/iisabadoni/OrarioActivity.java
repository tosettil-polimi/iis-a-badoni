package it.gov.iisbadoni.iisabadoni;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrarioActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private ArrayList<OraClasseJSON> ore;
    private JSONArray array;
    private boolean isStudente;
    private int numPage;
    public static ArrayList<ArrayList<Materia>> orario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_orario);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /* Mettimi dopo la fine del task */
        inizializzaOrario();
        /* ---------------------------- */
    }

    private void inizializzaOrario() {

        RequestQueue queue = Volley.newRequestQueue(OrarioActivity.this);
        String scelta = getIntent().getStringExtra("scelta");
        isStudente = getIntent().getBooleanExtra("isStudente", false);
        String url;

        if(isStudente)
            url = "http://vps208467.ovh.net:8080/Orario_classi/orarioClasse.jsp?codice=Applicazione25012016&classe=";
        else
            url = "http://vps208467.ovh.net:8080/Orario_classi/orarioDocente.jsp?codice=Applicazione25012016&docente=";

        if(scelta != null) {
            setTitle(scelta);
            scelta = scelta.replaceAll(" ", "%20");
            url += scelta;
        }
        else {
            //TODO: settare preferenza classe o docente
        }
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            array = new JSONArray(response.trim());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        OrarioActivity.this.onResponse();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrarioActivity.this, "Qualcosa con la connessione\nè andato storto...", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    private void onResponse() {
        ore = new ArrayList<>();

        if(array != null) {
            try {
                JSONObject obj;

                for (int i = 0; ; i++) {
                    obj = array.getJSONObject(i);
                    if(isStudente) {
                        ore.add(new OraClasseJSON(obj.getString("nomemateria"), obj.getString("nomedocente"), obj.getInt("durata"), obj.getInt("idgiorno"), obj.getInt("idora")));
                    }
                    else {
                        ore.add(new OraClasseJSON(obj.getString("nomemateria"), obj.getString("nomeclasse"), obj.getInt("durata"), obj.getInt("idgiorno"), obj.getInt("idora")));
                    }
                }
            } catch (JSONException e) {
            }
        }

        orario = Utils.creaOrarioCompleto(ore, this);
        numPage = orario.size();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return numPage;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            /*
            switch (position) {
                case 0:
                    return "LUNEDI";            //fare macchina a stati per i docenti in modo da tornare il giorno esatto dio banana LOOL
                case 1:
                    return "MARTEDI";
                case 2:
                    return "MERCOLEDI";
                case 3:
                    return "GIOVEDI";
                case 4:
                    return "VENERDI";
                case 5:
                    return "SABATO";
            }
            return null;
            */
            return Utils.tornaGiornoEsatto(ore, position);
        }
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_orario, container, false);

            RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.cardList);
            recList.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recList.setLayoutManager(linearLayoutManager);

            int numPage = this.getArguments().getInt(ARG_SECTION_NUMBER);

            OrarioAdapter orarioAdapter = new OrarioAdapter(orario.get(numPage - 1));

            recList.setAdapter(orarioAdapter);
            return rootView;
        }

        public String getDay (int sectionNumber) {
            String giorno = null;

            switch (sectionNumber) {
                case 1:
                    giorno = "Lunedi";
                    break;
                case 2:
                    giorno = "Martedi";
                    break;
                case 3:
                    giorno = "Mercoledi";
                    break;
                case 4:
                    giorno = "Giovedi";
                    break;
                case 5:
                    giorno = "Venerdi";
                    break;
                case 6:
                    giorno = "Sabato";
                    break;
            }

            return giorno;
        }
    }
}
