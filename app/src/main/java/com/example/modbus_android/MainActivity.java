package com.example.modbus_android;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zgkxzx.modbus4And.requset.ModbusParam;
import com.zgkxzx.modbus4And.requset.ModbusReq;
import com.zgkxzx.modbus4And.requset.OnRequestBack;

import java.net.InetAddress;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    Button button;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);


        button.setOnClickListener(view -> {
            /*GasLevelTask gasLevelTask = new GasLevelTask();
            gasLevelTask.execute();*/

            ModbusReq.getInstance().setParam(new ModbusParam()
                            .setHost("192.168.1.5")
                            .setPort(502)
                            .setEncapsulated(false)
                            .setKeepAlive(true)
                            .setTimeout(2000)
                            .setRetries(0))
                    .init(new OnRequestBack<String>() {
                        @Override
                        public void onSuccess(String s) {
                            Log.d(TAG, "onSuccess " + s);
                            Toast.makeText(MainActivity.this, "OnSuccess", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailed(String s) {
                            Log.d(TAG, "onFailed " + s);
                            Toast.makeText(MainActivity.this, "onFailed", Toast.LENGTH_SHORT).show();
                        }
                    });

            ModbusReq.getInstance().readCoil(new OnRequestBack<boolean[]>() {
                @Override
                public void onSuccess(boolean[] booleen) {
                    Log.d(TAG, "readCoil onSuccess " + Arrays.toString(booleen));
                    Toast.makeText(MainActivity.this, "readCoil onSuccess", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(String msg) {
                    Log.e(TAG, "readCoil onFailed " + msg);
                    Toast.makeText(MainActivity.this, "readCoil onFailed", Toast.LENGTH_SHORT).show();
                }
            }, 1, 1, 2);


            ModbusReq.getInstance().readDiscreteInput(new OnRequestBack<boolean[]>() {
                @Override
                public void onSuccess(boolean[] booleen) {
                    Log.d(TAG, "readDiscreteInput onSuccess " + Arrays.toString(booleen));
                    Toast.makeText(MainActivity.this, "readDiscreteInput onSuccess", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(String msg) {
                    Log.e(TAG, "readDiscreteInput onFailed " + msg);
                    Toast.makeText(MainActivity.this, "readDiscreteInput onFailed", Toast.LENGTH_SHORT).show();
                }
            },1,1,5);

            ModbusReq.getInstance().readHoldingRegisters(new OnRequestBack<short[]>() {
                @Override
                public void onSuccess(short[] data) {
                    Log.d(TAG, "readHoldingRegisters onSuccess " + Arrays.toString(data));
                    Toast.makeText(MainActivity.this, "readHoldingRegisters onSuccess", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(String msg) {
                    Log.e(TAG, "readHoldingRegisters onFailed " + msg);
                    Toast.makeText(MainActivity.this, "readHoldingRegisters onFailed", Toast.LENGTH_SHORT).show();
                }
            }, 1, 2, 8);

        });
    }

    /*@SuppressLint("StaticFieldLeak")
    public class GasLevelTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            return readGasLevel();
        }

        @Override
        protected void onPostExecute(Integer gasLevel) {
            String mesaj;
            if (gasLevel >= 0) {
                mesaj = "Gas Level: " + gasLevel;
            } else {
                mesaj = "Failed to get Gas Level";
            }
            Toast.makeText(MainActivity.this, mesaj, Toast.LENGTH_SHORT).show();
        }

    }*/

   /* public int readGasLevel() {
        String ipAddress = "192.168.1.5";
        int port = 502;
        int slaveId = 1;
        int gasLevelRegister = 40009;
        int gasLevel;
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            TCPMasterConnection connection = new TCPMasterConnection(address);
            connection.setPort(port);

            connection.connect();

            ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
            ReadInputRegistersRequest request = new ReadInputRegistersRequest(gasLevelRegister, 1);
            request.setUnitID(slaveId);
            transaction.setRequest(request);

            transaction.execute();
            ReadInputRegistersResponse response = (ReadInputRegistersResponse) transaction.getResponse();

            gasLevel = response.getRegisterValue(0);

            connection.close();

            return gasLevel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        ModbusReq.getInstance().setParam(new ModbusParam()
                        .setHost("192.168.1.5")
                        .setPort(502)
                        .setEncapsulated(false)
                        .setKeepAlive(true)
                        .setTimeout(2000)
                        .setRetries(0))
                .init(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onFailed(String s) {

                    }
                });



        return -1;
    }*/
}