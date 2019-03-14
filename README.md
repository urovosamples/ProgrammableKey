# ProgrammableKey
Sample for Programmable Key
Export Programmable key config to sdcard/keys_config.txt.

Intent intentService = new Intent("action.PROGRAMMABLE_KEY_SERVICE");
                    Intent eintent = new Intent(getExplicitIntent(MainActivity.this,intentService));
                    eintent.putExtra("programmable",2);
                    startService(eintent);

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
 Import Programmable key config from sdcard/keys_config.txt.
Intent intentService = new Intent("action.PROGRAMMABLE_KEY_SERVICE");
                    Intent eintent = new Intent(getExplicitIntent(MainActivity.this,intentService));
                    eintent.putExtra("programmable",1);
                    startService(eintent);                           
<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>
<KeysConfig>
<KeyRemapEnabled>true</KeyRemapEnabled>
<Key>
<KeyName>VOLUME_DOWN</KeyName>
<KeyCode>66</KeyCode>
<Wakeup>0</Wakeup>
</Key>
<Key>
<KeyName>VOLUME_UP</KeyName>
<KeyCode>com.android.chrome</KeyCode>
<Wakeup>0</Wakeup>
</Key>
</KeysConfig>
