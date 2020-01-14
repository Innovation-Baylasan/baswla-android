package org.baylasan.sudanmap.ui.auth.signup


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.user.model.RegisterRequest
import org.baylasan.sudanmap.ui.main.MainActivity
import org.baylasan.sudanmap.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 */
class SignUpActivity : AppCompatActivity() {


    private var selectedType = 1

    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        individualBtn.setOnClickListener {
            selectedType = 2
            individualBtn.strokeColor =
                ContextCompat.getColorStateList(applicationContext, R.color.colorAccent)
            individualBtn.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorAccent
                )
            )
            individualBtn.iconTint =
                ContextCompat.getColorStateList(applicationContext, R.color.colorAccent)

            companyBtn.strokeColor =
                ContextCompat.getColorStateList(applicationContext, R.color.black_overlay)
            companyBtn.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.black_overlay
                )
            )
            companyBtn.iconTint =
                ContextCompat.getColorStateList(applicationContext, R.color.black_overlay)
        }

        companyBtn.setOnClickListener {
            selectedType = 1
            individualBtn.strokeColor =
                ContextCompat.getColorStateList(applicationContext, R.color.black_overlay)
            individualBtn.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.black_overlay
                )
            )
            individualBtn.iconTint =
                ContextCompat.getColorStateList(applicationContext, R.color.black_overlay)

            companyBtn.strokeColor =
                ContextCompat.getColorStateList(applicationContext, R.color.colorAccent)
            companyBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
            companyBtn.iconTint =
                ContextCompat.getColorStateList(applicationContext, R.color.colorAccent)
        }


        signUpBtn.setOnClickListener {
            performValidation()
        }

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.events.observe(this, Observer { event ->
            when (event) {
                is DataEvent -> {
                    hideProgress()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is LoadingEvent -> {
                    signUpProgressBar.show()

                }
                is ErrorEvent -> {
                    hideProgress()
                    try {
                        Log.d("KLD", event.errorMessage!!)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    toast(event.errorMessage ?: "error")
                }
                else -> hideProgress()
            }

        })
    }

    private fun hideProgress() {
        signUpProgressBar.gone()
    }

    private fun performValidation() {
        val name = singUpNameEditText.asString()
        val email = signUpEmalEditText.asString()
        val password = signUpPasswordEditText.asString()
        val passwordConfirmation = signUpPasswordConfirmationEditText.asString()
        if (name.isEmpty()) {
            toast(getString(R.string.name_required))
            return
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            toast(R.string.email_not_valid)
            return
        }
        if (password.isEmpty()) {
            toast(getString(R.string.password_required))
            return
        }
        if (password.length < 8) {
            toast(getString(R.string.password_should_be_8_chars))
            return
        }

        if (passwordConfirmation.isEmpty()) {
            toast(getString(R.string.please_confirm_password))
            return
        }

        if (!passwordConfirmation.isMatch(password)) {
            toast(getString(R.string.password_didnt_match))
            return
        }

        val registerRequest =
            RegisterRequest(email, name, password, passwordConfirmation, roleId = selectedType)

        viewModel.register(registerRequest)
    }

}
