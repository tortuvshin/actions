package programmer.action;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class Web extends AppCompatActivity {
    private static final String TAG = "Web";
    Context mContext;
    AlertDialogManager alert = new AlertDialogManager();
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    String rline = "";
    EditText _urlText;
    Button _webButton;
    WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        _urlText = (EditText)findViewById(R.id.web_input_url);
        _webButton = (Button)findViewById(R.id.web_btn_connect);
        webView = (WebView)findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();

        CookieSyncManager.createInstance(Web.this);
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(webView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollBarStyle(webView.SCROLLBARS_OUTSIDE_OVERLAY);

        //final ProgressDialog progress = ProgressDialog.show(Web.this, "","Уншиж байна");

        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        cd = new ConnectionDetector(getApplicationContext());

        _webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String connectUrl = _urlText.getText().toString();
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    webView.setWebViewClient(new WebViewClient() {

                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            Log.i("WebContent", "Navigating to " + url);
                            view.loadUrl(url);
                            return true;
                        }

                        public void onPageFinished(WebView view, String url) {
                            Log.i("WebContent", "Finished loading of " + url);
                        }

                        public void onReceivedError(WebView view, int errorCode, String errorDescription, String errorUrl) {
                            Log.e("Programmer", "ERR AT   -> " + errorUrl);
                            Log.e("WebContent", "ERR CODE -> " + errorCode);
                            Log.e("WebContent", "ERR MSG  -> " + errorDescription);
                            //new Spawner().spawnView(WebContent.this, Offline.class); //TODO: Replace the offline-activity with an server-offline-activity
                        }
                    });
                        webView.loadUrl("http://"+connectUrl);
                        alert.showAlertDialog(Web.this, "Холболт","Холбогдсон вэбийн холбоос "+connectUrl,false);

                } else {
                    alert.showAlertDialog(Web.this, "Интернэт холболтоо шалгана уу !!!",
                            "Интернэт холболтоо байхгүй байна !!!", false);
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    public void onPause(){
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

}
