package org.baylasan.sudanmap.ui.auth.signup


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.util.Patterns
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.asString
import org.baylasan.sudanmap.common.isMatch
import org.baylasan.sudanmap.common.toClickableSpan
import org.baylasan.sudanmap.common.toast
import org.baylasan.sudanmap.data.user.model.RegisterRequest
import org.baylasan.sudanmap.ui.main.MainActivity
import org.baylasan.sudanmap.ui.privacy.PrivacyPolicyActivity
import org.baylasan.sudanmap.ui.terms.TOSActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 */
class SignUpActivity : AppCompatActivity() {


    private var selectedType = "user"

    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        bindProgressButton(signUpBtn)
        setAdjustScreen()

        signUpBtn.setOnClickListener {
            performValidation()
        }

        observeViewModel()

        val privacyPolicyText = getString(R.string.privacy_policy)
        val termsOfServicesText = getString(R.string.terms_of_use)
        val signUpNote = getString(R.string.by_signing_up)
        val and = getString(R.string.and)
        val space = " "

        signUpNoteText.movementMethod=LinkMovementMethod.getInstance()
        signUpNoteText.apply {
            append(space)
            append(signUpNote)
            append(space)
            append(privacyPolicyText.toClickableSpan {
             startActivity(Intent(this@SignUpActivity,PrivacyPolicyActivity::class.java))
            })
            append(space)
            append(and)
            append(space)
            append(termsOfServicesText.toClickableSpan {
                startActivity(Intent(this@SignUpActivity,TOSActivity::class.java))
            })
        }
    }

    private fun observeViewModel() {
        viewModel.events.observe(this, Observer { event ->
            when (event) {
                is DataEvent -> {
                    signUpBtn.hideProgress(R.string.done)
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
                is LoadingEvent -> {
                    signUpBtn.showProgress {
                        buttonText = getString(R.string.sign_up)
                        progressColor = Color.WHITE
                    }

                }
                is ErrorEvent -> {
                    signUpBtn.hideProgress(R.string.failed)
                    toast(event.errorMessage)
                }
                is NetworkErrorEvent, TimeoutEvent -> {
                    signUpBtn.hideProgress(getString(R.string.connection_faile))
                }

            }

        })
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
            RegisterRequest(email, name, password, passwordConfirmation, type = selectedType)
        viewModel.register(registerRequest)
    }

    private fun setAdjustScreen() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        /*android:windowSoftInputMode="adjustPan|adjustResize"*/
    }

}
