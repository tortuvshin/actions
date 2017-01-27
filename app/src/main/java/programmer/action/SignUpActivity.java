package programmer.action;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    AlertDialogManager alert = new AlertDialogManager();
    public SharedPreferences sharedPreferences;
    Editor editor;

    UserSession session;
    EditText nameEditText, emailEditText, passwordEditText;
    Button signUpButton;
    TextView loginLink;

    Animation waveAnimation,shakeAnimation,myShakeAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameEditText = (EditText)findViewById(R.id.sName);
        emailEditText = (EditText)findViewById(R.id.sEmail);
        passwordEditText = (EditText)findViewById(R.id.sPassword);
        signUpButton = (Button)findViewById(R.id.btnSignUp);
        loginLink = (TextView)findViewById(R.id.linkLogin);

        waveAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.wave_scale);
        shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        myShakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.myshake);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();

            }
        });
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }
        signUpButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Бүртгэл үүсгэж байна...");
        progressDialog.show();

        // TODO: Implement your own signup logic here.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onSignupSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
        }

    public void onSignupSuccess() {
        signUpButton.setEnabled(true);
        setResult(RESULT_OK, null);
        alert.showAlertDialog(SignUpActivity.this, "    Амжилттай бүртгэгдлээ.", "Таны бүртгэл амжилттай боллоо...", true);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 3000);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Бүртгүүлэхэд алдаа гарлаа", Toast.LENGTH_LONG).show();
        signUpButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", 0);
        editor = sharedPreferences.edit();

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameEditText.startAnimation(shakeAnimation);
            nameEditText.setError("хамгийн багадаа 3 тэмдэгт");
            valid = false;
        } else {
            nameEditText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
           emailEditText.startAnimation(shakeAnimation);
            emailEditText.setError("И-мэйл хаягаа зөв оруулна уу");
            valid = false;
        } else {
            emailEditText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 17) {
            passwordEditText.startAnimation(shakeAnimation);
            passwordEditText.setError("нууц үг 4-өөс 16 тэмдэгт");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }
        editor.putString("Name", name);
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.commit();
        return valid;
    }
}