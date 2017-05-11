package app.mego.bluetoothsend;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * This Activity to connect the app with the device(MicroController)
 * if it's a already paired devices using MAC address(Media Access Control)
 * then send the MAC address to the parent Activity as a intent result
 */
public class BluetoothDevices extends AppCompatActivity {

    /**
     * UI Element
     */
    private FloatingActionButton searchForNewDevices;
    private ListView DevicesList;

    /**
     * Tag for the log (Debugging)
     */
    private static final String LOG_TAG = BluetoothDevices.class.getSimpleName();


    /**
     * Return Intent extra (The device MAC address)
     */
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    /**
     * The adapter to get all bluetooth services
     */
    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;

    /**
     * request to enable bluetooth form activity result
     */
    public static final int REQUEST_ENABLE_BT = 1;
    public static final int REQUEST_ENABLE_FINE_LOCATION = 1256;

    /**
     * Adapter for the devices list
     */
    private BluetoothDevicesAdapter bluetoothDevicesAdapter;
    private ArrayList<String> bluetoothDevicesNamesList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_devices);
        initializeScreen();

        //check if the device has a bluetooth or not
        //and show Toast message if it does't have
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevicesAdapter = new BluetoothDevicesAdapter(this, bluetoothDevicesNamesList);

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.does_not_have_bluetooth, Toast.LENGTH_LONG).show();
            finish();
        } else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntentBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntentBluetooth, REQUEST_ENABLE_BT);
        } else if (mBluetoothAdapter.isEnabled()) {
            PairedDevicesList();
        }

        setBroadCastReceiver();

        //request location permission for bluetooth scanning for android API 23 and above
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ENABLE_FINE_LOCATION);


        //press the button to start search new Devices
        searchForNewDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForNewDevices.setEnabled(false);
                bluetoothDevicesAdapter.clear();
                PairedDevicesList();
                NewDevicesList();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ENABLE_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted!
                } else {
                    Toast.makeText(this, "Access Location must be allowed for bluetooth Search", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                PairedDevicesList();
            } else {
                finish();
            }
        }
    }

    /**
     * Link the layout element from XML to Java
     */
    private void initializeScreen() {
        searchForNewDevices = (FloatingActionButton) findViewById(R.id.search_fab_button);
        DevicesList = (ListView) findViewById(R.id.devices_list_listView);
    }

    /**
     * to set the BroadCaster Receiver
     */
    private void setBroadCastReceiver() {
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);


        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
    }

    /**
     * scan for new Devices and pair with them
     */
    private void NewDevicesList() {
        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }

    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

                    bluetoothDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                searchForNewDevices.setEnabled(true);
            }
        }
    };

    /**
     * get the paired devices in the phone
     */
    private void PairedDevicesList() {
        pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                //Get the device's name and the address
                bluetoothDevicesAdapter.add(bt.getName() + "\n" + bt.getAddress());
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_paired_devices,
                    Toast.LENGTH_LONG).show();
        }

        DevicesList.setAdapter(bluetoothDevicesAdapter);
        DevicesList.setOnItemClickListener(bluetoothListClickListener);
    }

    /**
     * handle the click for the list view to get the MAC address
     */
    private AdapterView.OnItemClickListener bluetoothListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //Get the device MAC address , the last 17 char in the view
            String info = (String) parent.getItemAtPosition(position);
            String MACAddress = info.substring(info.length() - 17);

            //make an intent to start the parent activity with the MAC address as a result
            Intent intent = new Intent(BluetoothDevices.this, MainActivity.class);
            intent.putExtra(EXTRA_DEVICE_ADDRESS, MACAddress);
            startActivity(intent);
            //finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        unregisterReceiver(mReceiver);
    }
}
