package org.baylasan.sudanmap.ui.auth.login


import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_login.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.*
import org.baylasan.sudanmap.ui.auth.signup.SignUpActivity
import org.baylasan.sudanmap.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val loginViewModel: LoginViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInAsGuest.setOnClickListener {
            startActivity(Intent(activity!!, MainActivity::class.java))

        }

        signInBtn.setOnClickListener {
            startActivity(Intent(activity!!, MainActivity::class.java))

        }

        signUpAsOrg.setOnClickListener {
            startActivity(Intent(activity!!, SignUpActivity::class.java))
        }

        signInBtn.setOnClickListener {
            val email = loginEmailEt.asString()
            val password = loginPasswordEt.asString()
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loginEmailEt.error = getString(R.string.email_not_valid)
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                loginPasswordEt.error = getString(R.string.password_required)
                return@setOnClickListener
            }

            loginViewModel.login(email, password)
        }


        observeEvent()
    }

    private fun observeEvent() {
        loginViewModel.event.observe(this, Observer { event ->
            when (event) {
                is UiState.Loading -> {
                    loginProgress.visible()
                }
                is UiState.Complete -> {
                    hideProgress()
                    startActivity(Intent(activity, MainActivity::class.java))
                }
                is UiState.Error -> {
                    activity?.toast("Unable to login at the moment, try again")
                    hideProgress()
                }
            }
        })
    }

    private fun hideProgress() {
        loginProgress.gone()
    }


}
