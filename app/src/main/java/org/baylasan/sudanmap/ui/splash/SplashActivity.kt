package org.baylasan.sudanmap.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.auth.AuthActivity
import org.baylasan.sudanmap.ui.intro.IntroActivity
import org.baylasan.sudanmap.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashActivity : AppCompatActivity() {

    private val sessionViewModel: SessionViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sessionViewModel.checkSession()
        sessionViewModel.sessionLiveData.observe(this, Observer {
            when (it) {
                is AuthenticatedUser -> {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is UnauthenticatedUser -> {
                    val intent = Intent(this@SplashActivity, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is FirstTimeLaunch -> {
                    val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }
        })

    }
}
