package com.sample.insta.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.sample.insta.R

import com.sample.insta.di.components.ActivityComponent
import com.sample.insta.ui.base.BaseActivity
import com.sample.insta.ui.main.HomeActivity
import com.sample.insta.ui.login.LoginActivity
import com.sample.insta.ui.login.LoginViewModel
import com.sample.insta.utils.common.Status
import kotlinx.android.synthetic.main.activity_login_screen.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity<LoginViewModel>() {
    override fun provideLayoutId(): Int = R.layout.activity_sign_up

    override fun injectDependency(activity: ActivityComponent) = activity.inject(this)

    override fun setUpView(savedInstanceState: Bundle?) {
        et_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onEmailChange(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        et_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onPasswordChange(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        et_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onNameChange(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        bt_sign_up.setOnClickListener { viewModel.signUp() }

        login_with_email.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }


    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.emailField.observe(this, Observer {
            if (et_email.text.toString() != it) email_et.setText(it)
        })

        viewModel.emailValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> layout_email.error = it.data?.run { getString(this) }
                else -> layout_email.isEnabled = true
            }
        })

        viewModel.passwordField.observe(this, Observer {
            if (et_password.text.toString() != it) et_password.setText(it)
        })

        viewModel.passwordValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> layout_password.error = it.data?.run { getString(this) }
                else -> layout_password.isEnabled = true
            }
        })

        viewModel.nameField.observe(this, Observer {
            if (et_name.text.toString() != it) et_name.setText(it)
        })

        viewModel.nameValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> layout_name.error = it.data?.run { getString(this) }
                else -> layout_name.isEnabled = true
            }
        })

        viewModel.logIn.observe(this, Observer {    
            pb_loading.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.launchHomeActivity.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            }
        })

    }


}
