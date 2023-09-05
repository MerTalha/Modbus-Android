package com.example.modbus_android;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView3);
        byte b = 0x64;
        button.setOnClickListener(view -> new ReceiveDataTask().execute(b));
    }

    @SuppressLint("StaticFieldLeak")
    private class ReceiveDataTask extends AsyncTask<Byte, Void, Integer> {


        @Override
        protected Integer doInBackground(Byte... bytes) {
            byte address = bytes[0];
            int value = 0;

            //short address = 0x301; // 301 adresini temsil ediyor

            /*byte[] data = {
                    0x00, 0x01,             // Transaction Identifier
                    0x00, 0x00,             // Protocol Identifier (Modbus)
                    0x00, 0x06,             // PDU length
                    0x00, 0x11,             // Unit Identifier (17 decimal, 0x11 hex)
                    0x03,                   // Read register command
                    (byte) ((address >> 8) & 0xFF),  // High byte of address
                    (byte) (address & 0xFF),         // Low byte of address
                    0x00, 0x01              // Quantity of Registers (2 bytes)
            };*/


            byte[] data = {
                    0x00, 0x01, // transaction identifier
                    0x00, 0x00, // Protocol Identifier (modbus)
                    0x00, 0x06, // PDU length
                    0x11,       // Address (17 decimals)
                    0x03,       // read register command
                    0x00, address, // from the entered register
                    0x00, 0x01  // Only 1 register (2 bytes)
            };

            byte[] rData = new byte[12];
            try (Socket socket = new Socket("192.168.3.5", 502)) {
                if (socket.isConnected()) {
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(data);

                    InputStream inputStream = socket.getInputStream();
                    int bytesRead = inputStream.read(rData);

                    if (bytesRead >= 9) {
                        int pduLength = rData[8] & 0xFF; // Read the PDU length field
                        int expectedDataLength = pduLength - 2; // Subtract 2 bytes of process identifier from PDU length

                        if (bytesRead >= expectedDataLength + 9) { // Check if the incoming data is greater than or equal to the expected data size
                            byte[] incomingValue = new byte[]{rData[10], rData[9]}; // Order changed
                            value = ((incomingValue[1] & 0xFF) << 8) | (incomingValue[0] & 0xFF);
                            System.out.println("Incoming Value: " + value);
                        } else {
                            System.out.println("Missing data received.");
                        }
                    } else {
                        System.out.println("Data could not be read or missing data was received.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

            return value;
        }

        @Override
        protected void onPostExecute(Integer result) {
            textView.setText(String.valueOf(result));
        }
    }
}

