package com.minki.salimalim.shop

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.minki.salimalim.MainActivity
import com.minki.salimalim.R
import kotlinx.android.synthetic.main.shop_housekeeping.*

class ShopFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shop_housekeeping,null)
    }

    lateinit var thisContext : Context
    val siteList = ArrayList<CharSequence>()
    val sites : HashMap<String, String> = mutableMapOf(
        "네이버" to "https://search.shopping.naver.com/",
        "G마켓" to "https://m.gmarket.co.kr",
        "옥션" to "https://m.auction.co.kr",
        "11번가" to "https://m.11st.co.kr/page/main/home",
        "쿠팡" to "https://m.coupang.com",
    ) as HashMap<String, String>

    var link = "https://m.shopping.naver.com/home/m/index.naver"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for(i in sites.keys)
            siteList.add(i)
        thisContext = (activity as MainActivity)
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

        SelectSite.setOnClickListener {
            val dialog = AlertDialog.Builder(thisContext)
            dialog.setTitle("사이트를 선택하세요.").setItems(siteList.toTypedArray()){
                dialog, position ->
                link = sites[siteList[position]].toString()
                Log.v("왔냐",link)
                ShopWebView.loadUrl(link)
                SelectSite.text = "▼ ${siteList[position]}"
            }.show()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if((activity as MainActivity).isSearch) run {
            val query = (activity as MainActivity).surfingQuery
            ShopWebView.loadUrl("https://msearch.shopping.naver.com/search/all?query=${query}")
            SelectSite.text = "▼ 네이버"
            (activity as MainActivity).isSearch = false
        }
    }
}