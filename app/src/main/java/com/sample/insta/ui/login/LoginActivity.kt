package com.sample.insta.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.sample.insta.R
import com.sample.insta.di.components.ActivityComponent
import com.sample.insta.ui.base.BaseActivity
import com.sample.insta.ui.home.HomeActivity
import com.sample.insta.ui.signup.SignUpActivity
import com.sample.insta.utils.common.Status
import kotlinx.android.synthetic.main.activity_login_screen.*

class LoginActivity : BaseActivity<LoginViewModel>() {
    override fun provideLayoutId(): Int = R.layout.activity_login_screen

    override fun injectDependency(activity: ActivityComponent) =
        activity.inject(this)

    override fun setUpView(savedInstanceState: Bundle?) {

        email_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onEmailChange(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        password_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onPasswordChange(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        login_btn.setOnClickListener { viewModel.login() }

        sign_up_email.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
            finish()
        }

    }


    override fun setupObservers() {
        super.setupObservers()

        viewModel.emailField.observe(this, Observer {
            if (email_et.text.toString() != it) email_et.setText(it)
        })

        viewModel.emailValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> email_et.error = it.data?.run { getString(this) }
                else -> email_et.error = null
            }
        })

        viewModel.passwordField.observe(this, Observer {
            if (password_et.text.toString() != it) password_et.setText(it)
        })

        viewModel.passwordValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> password_et.error = it.data?.run { getString(this) }
                else -> password_et.error = null
            }
        })

        viewModel.logIn.observe(this, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.launchHomeActivity.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            }
        })

    }


}
