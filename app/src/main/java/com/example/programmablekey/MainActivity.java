package com.example.programmablekey;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.device.KeyMapManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * create on 20190314 by Rocky
 *
 * sdcard/keys_config.txt
 * <?xml version='1.0' encoding='UTF-8' standalone='yes' ?>
 * <KeysConfig>
 * <KeyRemapEnabled>true</KeyRemapEnabled>
 * <Key>
 * <KeyName>VOLUME_DOWN</KeyName>
 * <KeyCode>66</KeyCode>
 * <Wakeup>0</Wakeup>
 * </Key>
 * <Key>
 * <KeyName>VOLUME_UP</KeyName>
 * <KeyCode>com.android.chrome</KeyCode>
 * <Wakeup>0</Wakeup>
 * </Key>
 * </KeysConfig>
 */
public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    TextView eventText;
    //Android keyevent keycode
    public static final int KEYCODE_KEYBOARD_TALK = 514;
    public static final int KEYCODE_SCAN_1 = 520;
    public static final int KEYCODE_SCAN_2 = 521;
    public static final int KEYCODE_ENTER           = 66;
    public static final int KEYCODE_VOLUME_UP       = 24;
    /** Key code constant: Volume Down key.
     * Adjusts the speaker volume down. */
    public static final int KEYCODE_VOLUME_DOWN     = 25;
    private KeyMapManager mKeyMap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mKeyMap = new KeyMapManager(this);
        Button enableRemapkey = (Button) findViewById(R.id.enable_remapkey);
        enableRemapkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeyMap.disableInterception(true);
            }
        });
        Button disableRemapkey = (Button) findViewById(R.id.disable_remapkey);
        disableRemapkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeyMap.disableInterception(false);
            }
        });
        Button Remapscankey = (Button) findViewById(R.id.remapScankey);
        Remapscankey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = SystemClock.uptimeMillis();
                final KeyEvent downEvent = new KeyEvent(now, now, KeyEvent.ACTION_DOWN,
                        KEYCODE_SCAN_1, 0);
                mKeyMap.mapKeyEntry(downEvent, KeyMapManager.KEY_TYPE_KEYCODE,
                        Integer.toString(KEYCODE_ENTER));
            }
        });
        Button RemapVOLUME_DOWNkey = (Button) findViewById(R.id.remapVOLUME_DOWNkey);
        RemapVOLUME_DOWNkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = SystemClock.uptimeMillis();
                final KeyEvent downEvent = new KeyEvent(now, now, KeyEvent.ACTION_DOWN,
                        KEYCODE_VOLUME_DOWN, 0);
                //
                // remap type
                // app packagename
                mKeyMap.mapKeyEntry(downEvent, KeyMapManager.KEY_TYPE_STARTAC,"com.android.chrome");
            }
        });
        Button delremapkey = (Button) findViewById(R.id.delremapkey);
        delremapkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeyMap.delKeyEntry(KEYCODE_SCAN_1);
            }
        });
        eventText = (TextView) findViewById(R.id.eventText);
        Button exportkey = (Button) findViewById(R.id.exportkey);
        exportkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intentService = new Intent("action.PROGRAMMABLE_KEY_SERVICE");
                    Intent eintent = new Intent(getExplicitIntent(MainActivity.this,intentService));
                    eintent.putExtra("programmable",2);
                    startService(eintent);
                } catch (Exception e) {
                    Log.e("ProgrammableKeyExport",
                            "BackendService failed:" + e.getMessage());
                }
            }
        });
        Button importkey = (Button) findViewById(R.id.importkey);
        importkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intentService = new Intent("action.PROGRAMMABLE_KEY_SERVICE");
                    Intent eintent = new Intent(getExplicitIntent(MainActivity.this,intentService));
                    eintent.putExtra("programmable",1);
                    startService(eintent);
                } catch (Exception e) {
                    Log.e("ProgrammableKeyExport",
                            "BackendService failed:" + e.getMessage());
                }
            }
        });

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        eventText.setText(event.toString());
        return super.dispatchKeyEvent(event);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v(TAG, "onKeyDown keycode " + keyCode);

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.v(TAG, "onKeyUp keycode " + keyCode);
        return super.onKeyUp(keyCode, event);
    }


    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }
}
