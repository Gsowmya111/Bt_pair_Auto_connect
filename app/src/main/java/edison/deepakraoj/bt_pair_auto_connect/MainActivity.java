package edison.deepakraoj.bt_pair_auto_connect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> mDeviceList = new ArrayList<String>();
    private static final UUID DEVICE_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ArrayList<BluetoothDevice> BTdevice = new ArrayList<BluetoothDevice>();

    private ArrayList<String> Macid = new ArrayList<String>();
    private BluetoothAdapter mBluetoothAdapter=null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();


        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            //textview1.append("\n  Device: " + device.getName() + ", " + device);
            String mac=device.getAddress();
            connect(mac);
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BluetoothDevice bd=BTdevice.get(position);
                String mac=Macid.get(position);
                pairDevice(bd,mac);
            }
        });
    }
    public void connect(String address)
    {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {
            BluetoothSocket bs = device.createRfcommSocketToServiceRecord(DEVICE_UUID);
            bs.connect();
            if(bs.isConnected())
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"bt connected",Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice




    }

   /* public void btConnectionOpen() {
        try {
            //address = Static_variables.mac_id;
            //  Log.d("", "Address : " + address);
            // String macId = "20:13:05:24:18:15";
            String macId = "00:06:66:64:B2:00";
            // String macId = "00:06:66:75:B4:41";
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            device = mBluetoothAdapter.getRemoteDevice(macId);
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            mBluetoothAdapter.cancelDiscovery();
            btSocket.connect();
            BT_Connected = true;
            BT_ReadLine = true;
            progressDialog.dismiss();


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this,"bluetooth connected",Toast.LENGTH_LONG).show();
                }
            });

            btInputStream = btSocket.getInputStream();
            if (BT_ReadLine) {
                btBufferedReader = new BufferedReader(new InputStreamReader(
                        btInputStream));
            }
            btOutputStream = btSocket.getOutputStream();


            mReadThread = new brt();
            mReadThread.start();
           *//* if(BT_Connected)
            {
                sb.setLength(0);

                expected_data="OK";
                case5=9;
                next_executable_stmt=2;
                config();
                //tv.setText("");

                Log.d("TAG","Oncrea data1 send");
            }*//*
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    private void pairDevice(BluetoothDevice device,String macid) {
        try {
            // Log.d("pairDevice()", "Start Pairing...");
            Method m = device.getClass().getMethod("createBond", (Class[]) null);
            m.invoke(device, (Object[]) null);

           // Thread.sleep(3000);
           /* runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Toast.makeText(MainActivity.this, "bt paired", Toast.LENGTH_LONG).show();
                }
            });*/
            Toast.makeText(MainActivity.this, "bt paired", Toast.LENGTH_LONG).show();
            connect(macid);



            //Log.d("pairDevice()", "Pairing finished.");
        } catch (Exception e) {
            //Log.e("pairDevice()", e.getMessage());
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Toast.makeText(MainActivity.this, "b not paired", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    //For UnPairing
    private void unpairDevice(BluetoothDevice device) {
        try {
            //Log.d("unpairDevice()", "Start Un-Pairing...");
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
            //Log.d("unpairDevice()", "Un-Pairing finished.");
        } catch (Exception e) {
            //Log.e(TAG, e.getMessage());
        }
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDeviceList.add(device.getName() + "\n" + device.getAddress());
                Macid.add(device.getAddress());
                BTdevice.add(device);
                Log.d("BT", device.getName() + "\n" + device.getAddress());
                listView.setAdapter(new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, mDeviceList));
            }

        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}