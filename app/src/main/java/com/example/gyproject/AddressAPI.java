package com.example.gyproject;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class AddressAPI extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addressapi);

        //타이틀 변경
        getSupportActionBar().setTitle("주소 등록하기");

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new BridgeInterface(), "Android");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //안드로이드에서 -> 자바스크립트 함수 호출
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });
        //최초 웹뷰 로드
        webView.loadUrl("https://goyoo-3be0b.web.app");
    }

    private class BridgeInterface {
        @JavascriptInterface
        public void processDATA(String data) {
            //주소검색API의 결과값이 브릿지를 통해 전달받음
            Intent intent = new Intent();
            intent.putExtra("data",data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
