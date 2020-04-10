package com.subhajitkar.commercial.projet_tulip.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

public class WebFragment extends Fragment {
	private static final String TAG = "WebFragment";

	private WebView webView;
	private String webUrl;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return getLayoutInflater().inflate(R.layout.fragment_webview,container,false);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments()!=null){
			webUrl = getArguments().getString(StaticFields.KEY_INTENT_WEBVIEW);
		}
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.d(TAG, "onViewCreated: view created");

		webView = view.findViewById(R.id.webView);
		webView.getSettings().setBuiltInZoomControls(true);   //setting up webView properties
		webView.getSettings().setDisplayZoomControls(false);
		webView.getSettings().setJavaScriptEnabled(true);  //in case of any error
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl(webUrl);
	}
}