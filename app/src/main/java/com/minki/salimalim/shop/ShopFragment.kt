package com.minki.salimalim.shop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.minki.salimalim.MainActivity
import com.minki.salimalim.R
import kotlinx.android.synthetic.main.shop_housekeeping.*

class ShopFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shop_housekeeping,null)
    }

    lateinit var callback : OnBackPressedCallback

    val link = "https://m.shopping.naver.com/home/m/index.naver"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ShopWebView.settings.apply{
            this.setSupportMultipleWindows(false)
            this.setSupportZoom(true)
            this.javaScriptCanOpenWindowsAutomatically = true
            this.javaScriptEnabled = true
            this.useWideViewPort = true
            this.domStorageEnabled = true
            this.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        }
        ShopWebView.webViewClient = MyWebViewClient()
        ShopWebView.loadUrl(link)



        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(ShopWebView.canGoBack())
                    ShopWebView.goBack()
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden){
            callback.remove()
        }
        else {
            if((activity as MainActivity).isSearch) run {
                val query = (activity as MainActivity).surfingQuery
                ShopWebView.loadUrl("https://msearch.shopping.naver.com/search/all?query=${query}&frm=NVSHSRC&vertical=home")
                (activity as MainActivity).isSearch = false
            }
            (activity as MainActivity).onBackPressedDispatcher.addCallback(this, callback)
        }
    }
}