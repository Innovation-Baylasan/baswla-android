package org.baylasan.sudanmap.ui.auth.login


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

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

        observeError()

        observeEvent()
    }

    private fun observeEvent() {
        loginViewModel.event.observe(this, Observer { event ->
            when (event) {
                is DataEvent -> {
                    hideProgress()
                }
                is LoadingEvent -> {
                    loginProgress.show()

                }
                is ErrorEvent -> {
                    hideProgress()
                    try {
                        Log.d("KLD", event.errorMessage)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    activity?.toast(event.errorMessage ?: "error")
                }
                else -> hideProgress()
            }
        })
    }

    private fun hideProgress() {
        loginProgress.gone()
    }

    private fun observeError() {
        loginViewModel.errorMessage.observe(this, Observer { messageId ->
            messageId?.let {
                activity?.toast(it)
            }
        })
    }


}
