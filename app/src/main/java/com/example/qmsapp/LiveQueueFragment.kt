package com.example.qmsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.qmsapp.databinding.FragmentHomeBinding
import com.example.qmsapp.databinding.FragmentLiveQueueBinding

class LiveQueueFragment : Fragment() {

    private var _binding: FragmentLiveQueueBinding?=null
    private val binding get()=_binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentLiveQueueBinding.inflate(inflater, container, false)
        val view = binding.root

        val parent= activity as MainActivity
        parent.setActionBarTitle("" +
                "",true)

        val webSettings: WebSettings =binding.webView.settings
        webSettings.javaScriptEnabled=true
        webSettings.domStorageEnabled=true
        webSettings.mediaPlaybackRequiresUserGesture=false

        binding.webView.webViewClient= WebViewClient()
        binding.webView.loadUrl("https://qmsdemo.infotech.rs/qmsremoteview/")

        return view
    }
}