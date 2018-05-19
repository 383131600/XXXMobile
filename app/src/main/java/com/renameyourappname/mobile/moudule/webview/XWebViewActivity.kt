package com.renameyourappname.mobile.moudule.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.*
import com.renameyourappname.mobile.R
import com.renameyourappname.mobile.common.Constant
import com.renameyourappname.mobile.moudule.base.ui.BaseActivity

import kotlinx.android.synthetic.main.activity_xweb_view.*

class XWebViewActivity : BaseActivity<XWebViewPresenter>(), XWebViewContract.View {
    private lateinit var title: String
    private lateinit var url: String
    private var isLoadHtmlText=false
    private lateinit var htmlText:String

    override fun initInjector() {
        mActivityComponent.inject(this)
    }

    override fun getBundleExtras(extras: Bundle) {
        title = extras.getString(Constant.EXTRA_TITLE,"")
        url = extras.getString(Constant.EXTRA_URL,"")
        isLoadHtmlText=extras.getBoolean(Constant.EXTRA_IS_LOAD_HTML_TEXT)
        htmlText=extras.getString(Constant.EXTRA_HTML_TEXT,"")
    }

    override fun getContentViewLayoutID(): Int {
        return R.layout.activity_xweb_view
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViews() {

        setToolbarTitle(title)
        showMenuButton()
        setMenuButtonDrawable(resources.getDrawable(R.drawable.ic_svg_refresh))


        //第三方cookies开关
        CookieManager.getInstance().setAcceptThirdPartyCookies(webview, true)

        webview.settings.javaScriptEnabled = true
        webview.settings.javaScriptCanOpenWindowsAutomatically = true
        webview.settings.setAppCacheEnabled(true)
        webview.isVerticalScrollBarEnabled = true
        webview.isHorizontalScrollBarEnabled = false
        webview.settings.allowFileAccessFromFileURLs = true
        webview.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webview.settings.loadsImagesAutomatically = true
        webview.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webview.settings.defaultTextEncodingName = "UTF-8"
        //强制等比缩放
        webview.settings.useWideViewPort=true
        webview.settings.layoutAlgorithm=WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webview.settings.loadWithOverviewMode=true

        webview.settings.setSupportZoom(false)
        //webView.settings.useWideViewPort=false

        webview.settings.builtInZoomControls = false

        if (isLoadHtmlText){
            webview.loadData(htmlText,"text/html","UTF-8")
        }else{
            webview.loadUrl(url)
        }


        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                // refreshLayout.isRefreshing = false
                super.onPageFinished(view, url)

            }


            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
            }
        }
        webview.webChromeClient=object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress==100){
                    hideLoading()
                }else{
                    showLoading()
                }
                super.onProgressChanged(view, newProgress)
            }
        }
    }


    override fun initEvents() {
        mTvMenu!!.setOnClickListener {
            webview.reload()
        }
    }


    override fun onBackPressed() {
        if (webview.canGoBack()){
            webview.goBack()
        }else{
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //不释放webview的话onProgressChanged有可能会导致内存泄漏
        webview.destroy()
    }
}
