package com.myrealtrip.newsreader.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.myrealtrip.newsreader.R
import com.myrealtrip.newsreader.adapter.RecyclerViewAdapter
import com.myrealtrip.newsreader.model.Item
import com.myrealtrip.newsreader.utils.StringUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        private val TAG= MainActivity::class.java.simpleName
        private const val USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36"
        private const val NEWS_RSS_URL = "https://news.google.com/rss?hl=ko&gl=KR&ceid=KR:ko"
        private const val REFERRER_URL = "https://www.google.com"
        private const val CSS_TITLE = "title"
        private const val CSS_LINK = "link"
        private const val META_IMAGE = "meta[property=og:image]"
        private const val META_DESCRIPTION = "meta[property=og:description]"
    }

    private lateinit var adapter : RecyclerViewAdapter

    override fun onRefresh() {
        adapter.clear()
        loadData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    // 화면 초기화
    private fun initView() {
        loadSplash()
        setActionBar()
        swipe_refresh_layout.setOnRefreshListener(this)
        setRVLayoutManager()
        setRVAdapter()
        loadData()
    }

    private fun setActionBar() {
        supportActionBar!!.title = "구글 뉴스"
    }

    // 스플래시 화면 로드
    private fun loadSplash() {
        startActivity(Intent(this, SplashActivity::class.java))
    }

    // 리사이클뷰 어댑터 설정
    private fun setRVAdapter() {
        adapter = RecyclerViewAdapter()
        adapter.setHasStableIds(true)
        adapter.setItemClickListener(object : RecyclerViewAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(applicationContext, DetailActivity::class.java)
                intent.putExtra("item", adapter.getItem(position))
                startActivity(intent)
            }
        })
        recycler_view.adapter = adapter
    }

    // 리사이클뷰 레이아웃 매니저 설정
    private fun setRVLayoutManager() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_view.layoutManager = layoutManager
        recycler_view.setHasFixedSize(true)
        recycler_view.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.Main).launch {
            val doc = withContext(Dispatchers.Default) { getDocument(NEWS_RSS_URL) }
            doc?.let { document ->
                document.select("item").forEach { item ->
                    val news = Item()
                    val title = getElementText(item, CSS_TITLE)
                    val link = getElementText(item, CSS_LINK)
                    if (title.isNotEmpty()) news.title = title
                    if (link.isNotEmpty()) {
                        news.link = link
                        val doc2 = withContext(Dispatchers.Default) { getDocument(link) }
                        doc2?.let { it2 ->
                            val url = getDocumentAttribute(it2, META_IMAGE)
                            val content = getDocumentAttribute(it2, META_DESCRIPTION)
                            if (url.isNotEmpty()) news.url = url
                            if (content.isNotEmpty()) news.content = StringUtils.instance.replaceSpace(content)
                        }
                    }
                    // 키워드 입력
                    val array = StringUtils.instance.getKeywords(news.content)
                    when (array.size) {
                        1 -> {
                            news.firstKeyword = array[0]
                        }
                        2 -> {
                            news.firstKeyword = array[0]
                            news.secondKeyword = array[1]
                        }
                        3 -> {
                            news.firstKeyword = array[0]
                            news.secondKeyword = array[1]
                            news.thirdKeyword = array[2]
                        }
                    }
                    Log.i(TAG, news.toString())
                    adapter.addItem(news)
                }
            }
            swipe_refresh_layout.isRefreshing = false
        }
    }

    // Null 체크, Text 값 가져오기
    private fun getElementText(element: Element, cssQuery: String) : String {
        var result = ""
        val elements = element.select(cssQuery)
        if (!elements.isNullOrEmpty()) {
            result = elements.first().text()
            Log.i(TAG, "$cssQuery : $result")
        }
        return result
    }

    // Null 체크, 속성 값 가져오기
    private fun getDocumentAttribute(document: Document, cssQuery: String) : String {
        var result = ""
        val elements = document.select(cssQuery)
        if (!elements.isNullOrEmpty()) {
            result = elements.first().attr("content")
            Log.i(TAG, "$cssQuery : $result")
        }
        return result
    }

    // 문서 로드
    private fun getDocument(url: String) : Document? {
        var doc: Document? = null
        try {
            doc = Jsoup.connect(url).userAgent(USER_AGENT).referrer(REFERRER_URL).get()
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }
        return doc
    }

}
