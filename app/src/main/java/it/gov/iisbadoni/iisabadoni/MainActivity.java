package it.gov.iisbadoni.iisabadoni;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PreferencesManager preferencesManager;

    private String currentPage = null;

    private boolean onAvvio = true;
    private boolean isConnesso;
    private String searchQuery;
    private boolean primoAvvio;
    private DrawerLayout drawer;
    private FragmentList myFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        handleIntent(getIntent());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        //drawer.setDrawerListener(toggle); deprecated
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        preferencesManager = new PreferencesManager(this);
        prendiDaPreferences();



        settaService();

        isConnesso = Utils.dialogHome(this, this);

        if (savedInstanceState == null) {
            myFragment = new FragmentList();
            myFragment.onAttach(getBaseContext());

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmenthome, myFragment)
                    .commit();
        }

        if(primoAvvio) {
            Log.i("Info", "primo avvio!");
            preferencesManager.setPrimoAvvio(false);
            new AlertDialog.Builder(this)
                    .setPositiveButton("OK", null)
                    .setTitle("Benvenuto!")
                    .setMessage("Per iniziare scegli la categoria\ne la classe di cui fai parte dalle\nimpostazioni!\nSpero che questa app ti sia utile.")
                    .show();

            Intent intent = new Intent(this, ServizioNuovaCircolare.class);
            intent.putExtra("primoAvvio", true);
            startService(intent);
        }
        else {
            Intent intent = new Intent(this, ServizioNuovaCircolare.class);
            startService(intent);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //necessario per la ricerca
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            //necessario per la ricerca
            searchQuery = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        // Associate searchable configuration with the SearchView

        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                searchQuery = query;
                searchQuery = searchQuery.replaceAll("'", "''");
                searchQuery = searchQuery.replaceAll("\"","");
                searchQuery = searchQuery.replaceAll("%", "");
                searchQuery = searchQuery.replaceAll("_", "");
                searchQuery = searchQuery.replace(' ', '_');
                myFragment.populateSearchList(currentPage, searchQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.trim().equals("")) {
                    myFragment.populateList(currentPage);
                    searchQuery = "";
                }
                return false;
            }


        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    private void settaService() {

        Intent notificationIntent = new Intent(getBaseContext(), ServizioNuovaCircolare.class);
        PendingIntent contentIntent = PendingIntent.getService(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(contentIntent);

        String intervallo = PreferenceManager.getDefaultSharedPreferences(this).getString("syncTime", "15min");
        int tInter = Integer.parseInt(intervallo.split("min")[0]) * 60 * 1000;


        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + tInter, tInter, contentIntent); //AlarmManager.INTERVAL_FIFTEEN_MINUTES

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(onAvvio && isConnesso) {
            try {
                switch (preferencesManager.getCategoria()) {

                    case "studenti":
                        currentPage = "alunni";
                        myFragment.populateList("alunni");
                        setTitle("Studenti");
                        break;

                    case "genitori":
                        currentPage = "genitori";
                        myFragment.populateList("genitori");
                        setTitle("Genitori");
                        break;

                    case "docenti":
                        currentPage = "docenti";
                        myFragment.populateList("docenti");
                        setTitle("Docenti");
                        break;

                    case "personale ATA":
                        currentPage = "personale ATA";
                        myFragment.populateList("personale ATA");
                        setTitle("Personale ATA");
                        break;

                    default:
                        currentPage = "studenti";
                        myFragment.populateList("alunni");
                        setTitle("Studenti");
                        break;
                }
            }
            catch (NullPointerException e) {
                Log.e("NullPointerException", "onPostCreate");
            }
            onAvvio = false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_studenti && !currentPage.equals("alunni")) {
            currentPage = "alunni";
            setTitle("Studenti");
            myFragment.populateList("alunni");
        } else if (id == R.id.nav_genitori && !currentPage.equals("genitori")) {
            currentPage = "genitori";
            setTitle("Genitori");
            myFragment.populateList("genitori");
        } else if (id == R.id.nav_docenti && !currentPage.equals("docenti")) {
            currentPage = "docenti";
            setTitle("Docenti");
            myFragment.populateList("docenti");
        } else if (id == R.id.nav_ata && !currentPage.equals("personale ATA")) {
            currentPage = "personale ATA";
            setTitle("Personale ATA");
            myFragment.populateList("personale ATA");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prendiDaPreferences() {
        if(preferencesManager!=null) {
            primoAvvio = preferencesManager.getPrimoAvvio();
        }
    }

    public void impostazioni(MenuItem menuItem) {
        Intent apriImpostazioni = new Intent(this, Settings.class);
        startActivity(apriImpostazioni);
    }

    public void informazioni(MenuItem menuItem) {
        startActivity(new Intent(this, Informazioni.class));
    }

    public void registroElettronico(MenuItem menuItem) {
        Uri uri = Uri.parse("http://www.sg26426.scuolanext.info");
        startActivity(new Intent( Intent.ACTION_VIEW, uri));
    }

    public void sito(MenuItem menuItem) {
        Uri uri = Uri.parse("http://www.iisbadoni.gov.it/");
        startActivity(new Intent( Intent.ACTION_VIEW, uri));
    }

    public void orario(MenuItem menuItem) {
        Intent intent = new Intent(this, ScegliClasse.class);
        startActivity(intent);
    }
}
