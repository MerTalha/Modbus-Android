package com.example.modbus_android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.ReadCoilsRequest;
import com.ghgande.j2mod.modbus.msg.ReadCoilsResponse;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            TCPMasterConnection connection = new TCPMasterConnection(InetAddress.getByName("192.168.1.100"));
            connection.setPort(502);

            connection.connect();

            // Cihazdan 1 adet tutulan bobin (coil) okuma işlemi
            int slaveId = 1; // Modbus cihazının Slave ID'si (genellikle 1)
            int coilAddress = 0; // Okunacak bobin adresi
            int coilValue = 0;

            // Modbus işlemlerini yapabilmek için ModbusTCPTransaction oluşturun
            ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
            ReadCoilsRequest req = new ReadCoilsRequest(coilAddress, 1);
            req.setUnitID(slaveId);
            transaction.setRequest(req);

            // Okuma işlemini gerçekleştirin
            transaction.execute();
            ReadCoilsResponse res = (ReadCoilsResponse) transaction.getResponse();
            if (res != null) {
                coilValue = res.getCoilStatus(0) ? 1 : 0;
                System.out.println("Coil Value: " + coilValue);
            } else {
                System.out.println("Read Coils response is null!");
            }

            // Diğer Modbus işlemlerini burada gerçekleştirin...

            // Bağlantıyı kapatın
            connection.close();


        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}