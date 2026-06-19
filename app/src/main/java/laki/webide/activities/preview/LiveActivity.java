package laki.webide.activities.preview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.besome.sketch.lib.base.BaseAppCompatActivity;
import a.a.a.mB;
import laki.webide.databinding.ActivityLivePreviewBinding;
import laki.webide.managers.LivePreviewServer;

public class LiveActivity extends BaseAppCompatActivity {

    private WebView webView;
    private String sc_id;
    private String pageName;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        enableEdgeToEdgeNoContrast();
        super.onCreate(savedInstanceState);
        
        ActivityLivePreviewBinding binding = ActivityLivePreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        var toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Live Preview");
        
        pageName = getIntent().getStringExtra("title");
        getSupportActionBar().setSubtitle(pageName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        
        toolbar.setNavigationOnClickListener(v -> {
            if (!mB.a()) {
                onBackPressed();
            }
        });
        
        sc_id = getIntent().getStringExtra("sc_id");
        webView = binding.webview;
        
        // Configure WebView
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        
        webView.setWebViewClient(new WebViewClient());
        
        // Start live preview server if not already running
        LivePreviewServer.startServer(this, sc_id);
        
        // Load the local preview URL
        String url = LivePreviewServer.getUrl(pageName);
        webView.loadUrl(url);
    }
}
