package programmer.action;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class Call extends AppCompatActivity {
    private static final String TAG = "Залгах";

    EditText _phoneText;
    Button _callButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        _phoneText = (EditText)findViewById(R.id.call_input_phone);
        _callButton = (Button)findViewById(R.id.call_btn_call);
        _callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calling();
            }
        });
    }
    public void calling() {
        Log.d(TAG, "Calling");

        if (!validate()) {
            onCallFailed();
            return;
        }

        _callButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Call.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Залгаж байна...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onCallSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
        String phoneNumber = _phoneText.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
        startActivity(callIntent);
    }
    public void onCallSuccess() {
        _callButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }
    public void onCallFailed() {
        Toast.makeText(getBaseContext(), "Залгаж чадсангүй", Toast.LENGTH_LONG).show();
        _callButton.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;
        String phoneNumber = _phoneText.getText().toString();
        if (phoneNumber.isEmpty()) {
            _phoneText.setError("Утасны дугаараа оруулна уу");
            valid = false;
        } else {
            _phoneText.setError(null);
        }
        return valid;
    }
}