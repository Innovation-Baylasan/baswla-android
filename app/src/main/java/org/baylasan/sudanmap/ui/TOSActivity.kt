package org.baylasan.sudanmap.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_tos.*
import org.baylasan.sudanmap.R

class TOSActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tos)
        backButton.setOnClickListener { finish() }

    }
}
