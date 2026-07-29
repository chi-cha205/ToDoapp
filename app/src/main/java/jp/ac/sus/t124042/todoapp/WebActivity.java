package jp.ac.sus.t124042.todoapp;

//通信を利用してWebページを取得・表示する


import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    private TextView textViewUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = findViewById(R.id.webView);
        textViewUrl = findViewById(R.id.textViewUrl);

        String url =
                getIntent().getStringExtra("url");

        if (url == null || url.isEmpty()) {
            textViewUrl.setText(
                    "URLが設定されていません"
            );
            return;
        }

        if (!url.startsWith("http://")
                && !url.startsWith("https://")) {

            url = "https://" + url;
        }

        textViewUrl.setText(url);

        webView.setWebViewClient(
                new WebViewClient()
        );

        webView.getSettings()
                .setJavaScriptEnabled(true);

        webView.loadUrl(url);   //インターネット通信を利用してURLのWebページを表示する
    }

    public void backToMain(View view) {
        finish();
    }
}