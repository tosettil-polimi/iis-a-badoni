package it.gov.iisbadoni.iisabadoni;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Lorenzo on 24/05/2015.
 */
public class ServizioNuovaCircolare extends Service implements TaskListener {

    private CircularFromDb circularFromDb;
    private String categoriaScelta;
    private boolean isUscita = false;
    private boolean primoAvvio;
    private boolean notifiche;
    private Exception exception;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            primoAvvio = intent.getBooleanExtra("primoAvvio", false);
        }
        catch (NullPointerException e) {
            primoAvvio = false;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("Servizio", "onCreate");
        new UltimaCircolare(this)
            .execute();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskCompleted() {

        if(exception != null) {
            exception.printStackTrace();
            exception = null;
        }
        else
            Log.i("Servizio", "onTaskCompleted()");

        if(isUscita && notifiche && exception == null && !primoAvvio) {
            Intent mainIntent =  new Intent(this, CircolareActiviry.class);

            String categoriaStringa;

            switch (categoriaScelta) {

                case "docenti":
                    categoriaStringa = "Docenti";
                    break;

                case "studenti":
                    categoriaStringa = "Studenti";
                    break;

                case "genitori":
                    categoriaStringa = "Genitori";
                    break;

                case "personale ATA":
                    categoriaStringa = "Personale ATA";
                    break;

                default:
                    categoriaStringa = "Studenti";
                    break;
            }

            if(mainIntent != null) {
                mainIntent.putExtra("nome circolare", circularFromDb.getTitolo());
                mainIntent.putExtra("link", circularFromDb.getLink());
                mainIntent.putExtra("nProto", circularFromDb.getProtocollo());

                NotificationManager notificationManager
                        = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification noti = new Notification.Builder(this)
                        .setAutoCancel(true)
                        .setContentIntent(PendingIntent.getActivity(this, 0, mainIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT))
                        .setContentTitle(Html.fromHtml(circularFromDb.getTitolo()))
                        .setContentText("Tocca per leggere...")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.ic_logobadoni_not)
                        .setTicker("Nuova circolare per " + categoriaStringa + "!")
                        .setWhen(System.currentTimeMillis())
                        .build();
                notificationManager.notify(0, noti);
                Log.i("Servizio", "Notification created");
            }

            isUscita = false;
        }
    }

    private class UltimaCircolare extends AsyncTask<Void,Void,Void> {

        private PreferencesManager preferencesManager;
        private TaskListener listener;
        private String ultimaCircolare;

        public UltimaCircolare(TaskListener listener) {
            this.listener = listener;
        }


        @Override
        protected Void doInBackground(Void... params) {

            circularFromDb = null;

            preferencesManager = new PreferencesManager(ServizioNuovaCircolare.this);

            categoriaScelta = PreferenceManager.getDefaultSharedPreferences(ServizioNuovaCircolare.this).getString("categoria", "studenti");
            notifiche = PreferenceManager.getDefaultSharedPreferences(ServizioNuovaCircolare.this).getBoolean("notifiche", true);
            ultimaCircolare = preferencesManager.getCircolare();

            URL url = null;
            String buffer, file="";
            try {
                switch (categoriaScelta) {
                    case "docenti":
                        url = new URL("http://vps208467.ovh.net:8080/Gestione_circolari/ultima_docenti.json");
                        break;

                    case "genitori":
                        url = new URL("http://vps208467.ovh.net:8080/Gestione_circolari/ultima_genitori.json");
                        break;

                    case "studenti":
                        url = new URL("http://vps208467.ovh.net:8080/Gestione_circolari/ultima_alunni.json");
                        break;

                    case "personale ATA":
                        url = new URL("http://vps208467.ovh.net:8080/Gestione_circolari/ultima_ata.json");
                        break;
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                while ((buffer = br.readLine()) != null) {
                    file+=buffer;
                }
            } catch (MalformedURLException e) {
                exception = e;
                isUscita = false;
            } catch (IOException e) {
                exception = e;
                isUscita = false;
            }
            catch (Exception e) {
                exception = e;
            }
            if(file.equals(ultimaCircolare)) {
                isUscita = false;
            }
            else {
                if(exception == null) {
                    try {
                        JSONObject json_data = new JSONObject(file);
                        circularFromDb = new CircularFromDb(json_data.getString("titolo"), json_data.getString("link"), json_data.getString("data"), json_data.getString("protocollo"));
                        preferencesManager.setCircolare(file);
                        isUscita = true;

                    } catch (JSONException e) {
                        circularFromDb = null;
                        exception = e;
                        isUscita = false;
                    }
                }
                else
                    isUscita = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
            listener.onTaskCompleted();
        }
    }
}
