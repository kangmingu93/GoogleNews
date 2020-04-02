package com.myrealtrip.newsreader.activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.myrealtrip.newsreader.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val FINISH_DELAY : Long = 1300 // 1.3초
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()
    }

    // 화면 초기화
    private fun initView() {
        getAppVersion()
        setFinishDelay()
    }

    // 일정 시간 후 종료
    private fun setFinishDelay() {
        Handler().postDelayed({
            finish()
        },
            FINISH_DELAY
        )
    }

    // 앱 버전 로드
    private fun getAppVersion() {
        val pi = applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)
        text_view_app_version.text = pi.versionName
    }

}
