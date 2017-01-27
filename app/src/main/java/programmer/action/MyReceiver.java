package programmer.action;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    AlertDialogManager alert = new AlertDialogManager();
    MyLocationListener gps;

    @Override
    public void onReceive(Context context, Intent intent) {
        gps = new MyLocationListener(context);
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++)
            {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str += " " + msgs[i].getOriginatingAddress();
                str += " :";
                str += msgs[i].getMessageBody().toString();
                str += "";
                if (gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    sendSMS(context, msgs[i].getOriginatingAddress(), " Уртраг: "+latitude+" Өргөрөг: "+longitude);

                    Toast.makeText(context,"Шинэ мессеж ирлээ \nhttps://www.google.mn/maps/@"+latitude+longitude,Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void sendSMS(Context context, String mobNo, String message) {
        String smsSent = "SMS_SENT";
        String smsDelivered = "SMS_DELIVERED";
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(smsSent), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(smsDelivered), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(mobNo, null, message, sentPI, deliveredPI);
    }
}
