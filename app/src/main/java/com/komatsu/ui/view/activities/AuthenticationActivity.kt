package com.komatsu.ui.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.komatsu.MainActivity
import com.komatsu.R
import com.komatsu.databinding.ActivityAuthenticationBinding
import com.komatsu.ui.view.LoggedInUserView
import com.komatsu.ui.view.activities.ExploreActivity
import com.komatsu.ui.viewmodel.LoginViewModel
import com.komatsu.ui.viewmodel.LoginViewModelFactory

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var authenticationViewModel: LoginViewModel
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        @SuppressLint("ObsoleteSdkInt")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.let {
//                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
//                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            }
//        } else {
//            @Suppress("DEPRECATION")
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or
//                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
//                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        }


        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.emailEditText
        val password = binding.passwordEditText
        val login = binding.login
        val register = binding.register
        val loading = binding.loading

        authenticationViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        authenticationViewModel.authenticationFormState.observe(
            this@AuthenticationActivity,
            Observer {
                val authenticationState = it ?: return@Observer

                // disable authentication button unless both username / password is valid
                login?.isEnabled = authenticationState.isDataValid

                if (authenticationState.usernameError != null) {
                    username?.error = getString(authenticationState.usernameError)
                }
                if (authenticationState.passwordError != null) {
                    password?.error = getString(authenticationState.passwordError)
                }
            })

        authenticationViewModel.authenticationResult.observe(this@AuthenticationActivity, Observer {
            val authenticationResult = it ?: return@Observer

            loading?.visibility = View.GONE
            if (authenticationResult.error != null) {
                showAuthenticationFailed(authenticationResult.error)
            }
            if (authenticationResult.success != null) {
                updateUiWithUser(authenticationResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy authentication activity once successful
            finish()
        })

        username?.afterTextChanged {
            authenticationViewModel.authenticationDataChanged(
                username.text.toString(),
                password?.text.toString()
            )
        }

        password?.apply {
            afterTextChanged {
                authenticationViewModel.authenticationDataChanged(
                    username?.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        authenticationViewModel.authentication(
                            username?.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login?.setOnClickListener {
                loading?.visibility = View.VISIBLE
                authenticationViewModel.authentication(
                    username?.text.toString(),
                    password.text.toString()
                )
            }
        }


        val decorView = window.decorView
        decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        // the above code is deprecated, adjust to the following code
        // to support the latest Android version
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            decorView.windowInsetsController?.hide(android.view.WindowInsets.Type.statusBars())
        } else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName

        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()

        val intent = Intent(this@AuthenticationActivity, ExploreActivity::class.java)
        startActivity(intent)
        finish()

    }

    private fun showAuthenticationFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}