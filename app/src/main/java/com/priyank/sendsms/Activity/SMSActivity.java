package com.priyank.sendsms.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.priyank.sendsms.Constant.AppUtils;
import com.priyank.sendsms.Model.SmsModel;
import com.priyank.sendsms.R;

import java.util.ArrayList;
import java.util.Arrays;

public class SMSActivity extends AppCompatActivity {
    TextView toNames;
    TextView groupName;
    EditText msgText;
    Button sendBtn;

    ArrayList<SmsModel> memberList;

    SmsManager sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        toNames = findViewById(R.id.to_names);
        groupName = findViewById(R.id.group_name);
        msgText = findViewById(R.id.msg);
        sendBtn = findViewById(R.id.send_btn);

        memberList = new ArrayList<>();
        String gName = getIntent().getStringExtra("GroupName");
        memberList = getIntent().getParcelableArrayListExtra("GroupMembers");

        groupName.setText(gName + " (" + memberList.size() + " Contacts)");
        toNames.setText(AppUtils.CommaString(memberList, "Names"));

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (toNames.getText().toString().trim().isEmpty()) {
                    AppUtils.Toast(SMSActivity.this, "Enter Number");
                    return;
                }
                if (msgText.getText().toString().trim().isEmpty()) {
                    AppUtils.Toast(SMSActivity.this, "Type Message to send");
                    return;
                }

                sendSMS(AppUtils.CommaString(memberList, "Numbers"), msgText.getText().toString().trim());
            }
        });
    }

    private void sendSMS(String phoneNumber, String message) {
        String num = phoneNumber.replaceAll(" ","").replaceAll("-","");
        ArrayList<String> items = new ArrayList<>(Arrays.asList(num.split("\\s*,\\s*")));
        sms = SmsManager.getDefault();

        AppUtils.LogE("PhoneNum>> " + num);

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        AppUtils.Toast(SMSActivity.this, "SMS sent");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        AppUtils.Toast(SMSActivity.this, "Generic failure");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        AppUtils.Toast(SMSActivity.this, "No service");
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        AppUtils.Toast(SMSActivity.this, "Null PDU");
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        AppUtils.Toast(SMSActivity.this, "Radio off");
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        AppUtils.Toast(SMSActivity.this, "SMS delivered");
                        break;
                    case Activity.RESULT_CANCELED:
                        AppUtils.Toast(SMSActivity.this, "SMS not delivered");
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        for (String number : items) {
            sms.sendTextMessage(number, null, message, sentPI, deliveredPI);
        }
    }
}
