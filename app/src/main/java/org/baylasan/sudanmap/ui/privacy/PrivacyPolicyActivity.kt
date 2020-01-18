package org.baylasan.sudanmap.ui.privacy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import org.baylasan.sudanmap.R

class PrivacyPolicyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        backButton.setOnClickListener { finish() }

    }
}
