package programmer.action;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity{

    public ImageButton mailSenderButton;
    public ImageButton callerButton;
    public ImageButton webButton;
    public ImageButton smsButton;
    public ImageButton mapButton;
    public ImageButton marketButton;

    MyLocationListener gps;
    MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mailSenderButton = (ImageButton) findViewById(R.id.sendButton);
        callerButton = (ImageButton) findViewById(R.id.callButton);
        webButton = (ImageButton) findViewById(R.id.webButton);
        smsButton = (ImageButton) findViewById(R.id.msgButton);
        mapButton = (ImageButton) findViewById(R.id.mapButton);
        marketButton = (ImageButton) findViewById(R.id.marketButton);


        marketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.bodybuilding.mobile"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left, R.anim.slide_left);
            }
        });
        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sms = new Intent(MainActivity.this, Message.class);
                startActivity(sms);
                overridePendingTransition(R.anim.slide_right,R.anim.slide_right);
            }
        });
        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(MainActivity.this, Web.class);
                startActivity(webIntent);
                overridePendingTransition(R.anim.fade,R.anim.fade);
            }
        });
        mailSenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(MainActivity.this, SendMail.class);
                startActivity(callIntent);
                overridePendingTransition(R.anim.wave_scale,R.anim.wave_scale);
            }
        });
        callerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callerIntent = new Intent(MainActivity.this, Call.class);
                startActivity(callerIntent);
                overridePendingTransition(R.anim.push_up_out,R.anim.push_up_out);
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                gps = new MyLocationListener(MainActivity.this);
                receiver = new MyReceiver();
                if(gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    Toast.makeText(getApplicationContext(),"Таны одоогийн байршил: \nӨргөрөг: "+latitude+"\nУртраг:"+longitude,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("geo:" + latitude + "," + longitude + ""));
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_up_in,R.anim.push_up_in);
                }
                else {
                    gps.showSettingsAlert();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this,R.style.AlertDialog)
                .setIcon(R.drawable.exit)
                .setTitle("Лаборатори 5")
                .setMessage("Та програмаас гарах уу?")
                .setPositiveButton("Тийм", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Үгүй", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
