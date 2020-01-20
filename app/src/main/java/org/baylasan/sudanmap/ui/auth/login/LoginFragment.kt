package org.baylasan.sudanmap.ui.auth.login


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_login.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.auth.signup.SignUpActivity
import org.baylasan.sudanmap.ui.main.MainActivity
import org.baylasan.sudanmap.utils.asString
import org.baylasan.sudanmap.utils.gone
import org.baylasan.sudanmap.utils.show
import org.baylasan.sudanmap.utils.toast
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

            loginViewModel.login(email, password)
        }


        observeEvent()
    }

    private fun observeEvent() {
        loginViewModel.event.observe(this, Observer { event ->
            when (event) {
                is DataEvent -> {
                    hideProgress()
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                }
                is LoadingEvent -> {
                    loginProgress.show()
                }
                is ErrorEvent -> {
                    hideProgress()

                    activity?.toast(event.errorMessage ?: "error")
                }
                is ValidationErrorEvent -> {
                    hideProgress()
                    if (event.message != 0) {
                        activity?.toast(event.message)
                    }
                }
                else -> hideProgress()
            }
        })
    }

    private fun hideProgress() {
        loginProgress.gone()
    }


}
