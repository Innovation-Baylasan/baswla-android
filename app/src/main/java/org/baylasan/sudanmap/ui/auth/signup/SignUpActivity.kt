package org.baylasan.sudanmap.ui.auth.signup


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.baylasan.sudanmap.R


/**
 * A simple [Fragment] subclass.
 */
class SignUpActivity : AppCompatActivity() {


private var selectedType = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sign_up)


        individualBtn.setOnClickListener {
            selectedType = 1
            individualBtn.strokeColor = ContextCompat.getColorStateList(applicationContext, R.color.colorAccent)
            individualBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
            individualBtn.iconTint = ContextCompat.getColorStateList(applicationContext, R.color.colorAccent)

            companyBtn.strokeColor = ContextCompat.getColorStateList(applicationContext, R.color.black_overlay)
            companyBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.black_overlay))
            companyBtn.iconTint = ContextCompat.getColorStateList(applicationContext, R.color.black_overlay)
        }

        companyBtn.setOnClickListener {
            selectedType = 0
            individualBtn.strokeColor = ContextCompat.getColorStateList(applicationContext, R.color.black_overlay)
            individualBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.black_overlay))
            individualBtn.iconTint = ContextCompat.getColorStateList(applicationContext, R.color.black_overlay)

            companyBtn.strokeColor = ContextCompat.getColorStateList(applicationContext, R.color.colorAccent)
            companyBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
            companyBtn.iconTint = ContextCompat.getColorStateList(applicationContext, R.color.colorAccent)
        }

    }

}
