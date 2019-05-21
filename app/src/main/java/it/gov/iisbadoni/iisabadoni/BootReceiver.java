package it.gov.iisbadoni.iisabadoni;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;

/**
 * Created by Lorenzo on 02/11/2015.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BootReceiver", "onReceive");
        Intent startupBootIntent = new Intent(context, ServizioNuovaCircolare.class);
        startupBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(startupBootIntent);
    }
}
