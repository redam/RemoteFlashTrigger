package fr.damongeot.remoteflashtrigger;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    public final static String LISTENING_PORT = "listen_port";
    public final static int LISTENING_PORT_DEF = 8081;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ToggleButton tb = (ToggleButton) findViewById(R.id.button_toggleservice);
        //set button current state
        tb.setChecked(isMyServiceRunning(NetworkListenService.class));

        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Intent intent = new Intent(MainActivity.this, NetworkListenService.class);
                    intent.putExtra(LISTENING_PORT,getListeningPort());
                    startService(intent);
                    Log.d(TAG,"Starting service");
                } else {
                    // The toggle is disabled
                    Intent intent = new Intent(MainActivity.this, NetworkListenService.class);
                    stopService(intent);
                    Log.d(TAG,"Stoping service");
                }
            }
        });
    }

    /**
     * Show menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    /**
     * Handle menu item click
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_preferences:
                Intent i = new Intent(MainActivity.this, fr.damongeot.remoteflashtrigger.PreferenceActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get listening port from shared preferences
     * @return
     */
    private int getListeningPort() {
        SharedPreferences mSP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        return Integer.parseInt(mSP.getString(LISTENING_PORT,LISTENING_PORT_DEF+""));
    }
}
