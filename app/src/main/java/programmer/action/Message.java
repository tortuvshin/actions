package programmer.action;

import android.app.Activity;
import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

public class Message extends AppCompatActivity {
    private static final String TAG = "Message";
    EditText phoneNo;
    EditText textMessage;
    TextView mCounter;
    Button sendSMS;
    AlertDialogManager alert = new AlertDialogManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        phoneNo = (EditText) findViewById(R.id.sms_input_phone);
        textMessage = (EditText) findViewById(R.id.sms_input_sms);
        sendSMS = (Button) findViewById(R.id.sms_btn_send);
        mCounter = (TextView) findViewById(R.id.sms_text_counter);
        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobNo = phoneNo.getText().toString();
                String message = textMessage.getText().toString();
                if (mobNo.length()>0 && message.length()>0)
                    sendSMS(mobNo, message);
                else
                    alert.showAlertDialog(Message.this, "Уучлаарай", "Утасны дугаар болон илгээх зурвасаа оруулна уу.", true);
            }
        });
    }

    private void sendSMS(String mobNo, String message) {
        String smsSent = "SMS_SENT";
        String smsDelivered = "SMS_DELIVERED";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(smsSent), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(smsDelivered), 0);

        // Receiver for Sent SMS.
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        alert.showAlertDialog(Message.this,"Амжилттай","Зурвас амжилттай илгээлээ",true);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        alert.showAlertDialog(Message.this, "Generic failure", "Generic failure", true);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        alert.showAlertDialog(Message.this,"No service","No service",true);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        alert.showAlertDialog(Message.this, "Null PDU", "Null PDU", true);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        alert.showAlertDialog(Message.this,"Radio off","Radio off",true);
                        break;
                }
            }
        }, new IntentFilter(smsSent));

        // Receiver for Delivered SMS.
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        alert.showAlertDialog(Message.this, "SMS delivered", "SMS delivered", true);
                        break;
                    case Activity.RESULT_CANCELED:
                        alert.showAlertDialog(Message.this,"SMS not delivered","SMS not delivered",true);
                        break;
                }
            }
        }, new IntentFilter(smsDelivered));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(mobNo, null, message, sentPI, deliveredPI);
    }
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
            //This sets a textview to the current length
            String smsNo;
            if(s.length() == 0)
                smsNo = "0";
            else
                smsNo = String.valueOf(s.length()/999 + 1);
            String smsLength = String.valueOf(999-(s.length()%999));
            mCounter.setText(smsLength+"/"+smsNo);
        }
        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
        }
    };

}
