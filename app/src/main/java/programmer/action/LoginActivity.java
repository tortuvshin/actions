package programmer.action;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final String PREFER_NAME = "UserInfo";
    UserSession session;
    AlertDialogManager alert = new AlertDialogManager();
    private SharedPreferences sharedPreferences;
    EditText nameText;
    EditText passwordText;
    Button loginButton;
    TextView signUpLink;

    Animation shake;
    Animation myshake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameText = (EditText)findViewById(R.id.username);
        passwordText = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.login);
        signUpLink = (TextView)findViewById(R.id.signup);

        shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        myshake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.myshake);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_top_to_bottom, R.anim.exit_slide_down);

            }
        });
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        // TODO: Implement your own authentication logic here.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                this.finish();
            }
        }
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

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        //Toast.makeText(getBaseContext(), "Хэрэглэгчийн мэдээллээ шинэчлээд дахин оролдно уу !", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String username = nameText.getText().toString();
        String password = passwordText.getText().toString();
        session = new UserSession(getApplicationContext());
        //Toast.makeText(getApplicationContext(), "Хэрэглэгч нэвтэрсэн байдал: " + session.isUserLoggedIn(), Toast.LENGTH_LONG).show();
        sharedPreferences = getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        if (username.isEmpty() || password.length() < 3) {
            nameText.startAnimation(shake);
            nameText.setError("хэрэглэгчийн нэр 3-аас багагүй тэмдэгт байна");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 16) {
            passwordText.startAnimation(shake);
            passwordText.setError("нууц үг 4-өөс 16 тэмдэгт байна");
            valid = false;
        } else {
            passwordText.setError(null);
        }
        if(username.trim().length() > 0 && password.trim().length() > 0){
            String uName = null;
            String uPassword =null;
            if (sharedPreferences.contains("Name"))
            {
                uName = sharedPreferences.getString("Name", "");
            }
            if (sharedPreferences.contains("Password"))
            {
                uPassword = sharedPreferences.getString("Password", "");
            }
            if(username.equals(uName) && password.equals(uPassword)){
                session.createUserLoginSession(uName,
                        uPassword);
                Intent i = new  Intent(getApplicationContext(),MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                overridePendingTransition(0,R.anim.wave_scale);
            }else{
                //alert.showAlertDialog(LoginActivity.this, "  Алдаа..", "Хэрэглэгчийн нэр, нууц үг буруу байна !!!", false);
                nameText.startAnimation(myshake);
                passwordText.startAnimation(myshake);
                valid = false;
            }
        } else {
            Toast.makeText(LoginActivity.this,"Хэрэглэгчийн нэр нууц үгээ оруулна уу",Toast.LENGTH_SHORT).show();
            valid = false;
        }
    return valid;
    }
}