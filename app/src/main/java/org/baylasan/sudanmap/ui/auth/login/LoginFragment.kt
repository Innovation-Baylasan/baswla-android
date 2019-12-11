package org.baylasan.sudanmap.ui.auth.login


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.auth.signup.SignUpActivity
import org.baylasan.sudanmap.ui.main.MainActivity


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

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
    }


}
