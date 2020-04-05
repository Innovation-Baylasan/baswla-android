package org.baylasan.sudanmap.ui.privacy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.common.gone
import org.baylasan.sudanmap.common.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class PrivacyPolicyActivity : AppCompatActivity() {
    private val viewModel by viewModel<PolicyViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        backButton.setOnClickListener { finish() }
        viewModel.loadPolicy()
        viewModel.uiState.observe(this, Observer {
            if (it is UiState.Loading) {
                progressBar.visible()
                errorView.gone()
                webView.gone()
            }
            if (it is UiState.Success) {
                progressBar.gone()
                errorView.gone()
                webView.visible()
                webView.loadData(it.data.data.value, "text/html", "utf-8")

            }
            if (it is UiState.Error) {
                progressBar.gone()
                errorView.visible()
                webView.gone()
                retryButton.setOnClickListener {
                    viewModel.loadPolicy()
                }
            }
        })


    }
}
