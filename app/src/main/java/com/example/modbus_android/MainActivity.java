package com.example.modbus_android;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.ReadCoilsRequest;
import com.ghgande.j2mod.modbus.msg.ReadCoilsResponse;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersResponse;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class MainActivity extends AppCompatActivity {

    Button button;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GasLevelTask gasLevelTask = new GasLevelTask();
                gasLevelTask.execute();
            }
        });
    }

    public class GasLevelTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            // Gas Level verisini arka planda al
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
        String ipAddress = "192.168.1.5"; // KBRN cihazının IP adresi
        int port = 502; // Modbus cihazının port numarası (varsayılan olarak 502)
        int slaveId = 1; // Modbus cihazının Slave ID'si (genellikle 1)
        int gasLevelRegister = 40116; // Gas Level değerinin kayıtlı olduğu Modbus veri adresi
        int gasLevel;
        try {
            // Modbus TCP/IP bağlantısı için TCPMasterConnection oluşturun
            InetAddress address = InetAddress.getByName(ipAddress);
            TCPMasterConnection connection = new TCPMasterConnection(address);
            connection.setPort(port);

            // Bağlantıyı açın
            connection.connect();

            // Modbus işlemlerini yapabilmek için ModbusTCPTransaction oluşturun
            ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
            ReadInputRegistersRequest request = new ReadInputRegistersRequest(gasLevelRegister, 1);
            request.setUnitID(slaveId);
            transaction.setRequest(request);

            // Veriyi alın
            transaction.execute();
            ReadInputRegistersResponse response = (ReadInputRegistersResponse) transaction.getResponse();

            // Alınan veriyi INT16 türüne çevirin
            gasLevel = response.getRegisterValue(0);

            // Bağlantıyı kapatın
            connection.close();

            return gasLevel;
        } catch (Exception e) {
            // Modbus işlemleri sırasında oluşabilecek hataları yakalayın
            e.printStackTrace();
        }

        return -1; // Hata durumunda 0 döndürün
    }
}