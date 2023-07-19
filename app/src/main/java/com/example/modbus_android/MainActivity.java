package com.example.modbus_android;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersResponse;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;

import java.net.InetAddress;


public class MainActivity extends AppCompatActivity {

    Button button;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);

        button.setOnClickListener(view -> {
            GasLevelTask gasLevelTask = new GasLevelTask();
            gasLevelTask.execute();
        });
    }

    @SuppressLint("StaticFieldLeak")
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

    }

    public int readGasLevel() {
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

        return -1;
    }
}