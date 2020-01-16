package org.baylasan.sudanmap.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.ui.auth.AuthActivity
import org.baylasan.sudanmap.ui.intro.IntroActivity
import org.baylasan.sudanmap.ui.main.MainActivity
import org.koin.android.ext.android.inject


class SplashActivity : AppCompatActivity() {

    private val sessionManager: SessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler()
            .postDelayed({
                val isFirstTime = sessionManager.isFirstTime()
                val isLoggedIn = sessionManager.isLoggedIn()
                if (isFirstTime) {
                    val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    if (isLoggedIn) {
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@SplashActivity, AuthActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }


            }, 3000)


    }
}
