package com.priyank.sendsms.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.priyank.sendsms.R;

public class SMSActivity extends AppCompatActivity {
    TextView toNames;
    EditText msgText;
    Button sendBtn;

    SmsManager sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        toNames = findViewById(R.id.to_names);
        msgText = findViewById(R.id.msg);
        sendBtn = findViewById(R.id.send_btn);
        sendBtn.setEnabled(false);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS("","");

                /*if (numberText.getText().toString().trim().isEmpty()) {
                    AppUtils.Toast(MainActivity.this, "Enter Number");
                    return;
                }
                sendSMS(numberText.getText().toString().trim(), "Hello");*/
            }
        });
    }

    public void sendSMS(String toNum, String msg) {
        String numbers[] = {toNum};

        sms = SmsManager.getDefault();

        for(String number : numbers) {
            sms.sendTextMessage(number, null, msg, null, null);
        }
    }
}
