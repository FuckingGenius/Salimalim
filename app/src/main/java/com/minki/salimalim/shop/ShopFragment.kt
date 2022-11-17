package com.minki.salimalim.shop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.minki.salimalim.MainActivity
import com.minki.salimalim.R
import kotlinx.android.synthetic.main.shop_housekeeping.*

class ShopFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shop_housekeeping,null)
    }

    val link = "https://m.shopping.naver.com/home/m/index.naver"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ShopWebView.apply{
            this.settings.setSupportMultipleWindows(false)
            this.settings.setSupportZoom(true)
            this.settings.javaScriptCanOpenWindowsAutomatically = true
            this.settings.javaScriptEnabled = true
            this.settings.useWideViewPort = true
            this.settings.domStorageEnabled = true
            this.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            this.webViewClient = MyWebViewClient()
            this.loadUrl(link)
        }
        (activity as MainActivity).searchView = ShopWebView
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if((activity as MainActivity).isSearch) run {
            val query = (activity as MainActivity).surfingQuery
            ShopWebView.loadUrl("https://msearch.shopping.naver.com/search/all?query=${query}&frm=NVSHSRC&vertical=home")
            (activity as MainActivity).isSearch = false
        }
    }
}