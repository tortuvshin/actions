package programmer.action;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class SendMail extends AppCompatActivity {
    private static final String TAG = "И-мэйл илгээх";

    EditText _fromText;
    EditText _toText;
    EditText _subjectText;
    EditText _composeEmailText;
    Button _sendButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        _fromText = (EditText)findViewById(R.id.mail_input_from);
        _toText = (EditText)findViewById(R.id.mail_input_to);
        _subjectText = (EditText)findViewById(R.id.mail_input_subject);
        _composeEmailText = (EditText)findViewById(R.id.mail_input_composemail);
        _sendButton = (Button)findViewById(R.id.mail_btn_send);
        _sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sending();
            }
        });

    }

    public void sending() {
        Log.d(TAG, "Sending");

        if (!validate()) {
            onSendFailed();
            return;
        }

        _sendButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SendMail.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("И-мэйл илгээж байна...");
        progressDialog.show();

        String from = _fromText.getText().toString();
        String to = _toText.getText().toString();
        String subject = _subjectText.getText().toString();
        String composeEmail = _composeEmailText.getText().toString();
        Intent sendingMailIntent = new Intent(Intent.ACTION_SEND);
        sendingMailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
        sendingMailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendingMailIntent.putExtra(Intent.EXTRA_TEXT,composeEmail);
        sendingMailIntent.setType("message/rfc822");
        startActivity(sendingMailIntent);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSendSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSendSuccess() {
        _sendButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSendFailed() {
        Toast.makeText(getBaseContext(), "Илгээж чадсангүй", Toast.LENGTH_LONG).show();
        _sendButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String from = _fromText.getText().toString();
        String to = _toText.getText().toString();
        String subject = _subjectText.getText().toString();
        String composeEmail = _composeEmailText.getText().toString();

        if (to.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(to).matches()) {
            _fromText.setError("Зөв и-мэйл хаяг оруулна уу");
            valid = false;
        } else {
            _fromText.setError(null);
        }
        if (from.isEmpty() || to.length() < 3) {
            _toText.setError("3-аас доошгүй тэмдэгт");
            valid = false;
        } else {
            _toText.setError(null);
        }
        if (subject.isEmpty() || to.length() < 3){
            _subjectText.setError("3-аас доошгүй тэмдэгт");
        }else{
            _subjectText.setError(null);
        }
        if (composeEmail.isEmpty() || to.length() < 0) {
            _composeEmailText.setError("3-аас доошгүй тэмдэгт");
        }else{
            _composeEmailText.setError(null);
        }


        return valid;
    }
}