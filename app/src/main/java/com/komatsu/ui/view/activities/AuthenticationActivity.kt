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
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.google.android.material.snackbar.Snackbar
import com.komatsu.R
import com.komatsu.data.LoginRepository
import com.komatsu.databinding.ActivityAuthenticationBinding
import com.komatsu.ui.view.LoggedInUserView
import com.komatsu.ui.viewmodel.LoginViewModel
import com.komatsu.ui.viewmodel.LoginViewModelFactory
import org.koin.android.ext.android.inject
import java.util.Date

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var authenticationViewModel: LoginViewModel
    private lateinit var _binding: ActivityAuthenticationBinding
    private lateinit var account: Auth0
    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null

    private val loginRepository: LoginRepository by inject()
    override fun onStart() {
        super.onStart()
        if (cachedCredentials != null) {
            showSnackbar("Welcome back, ${cachedUserProfile?.name ?: "we missed you"}")
            val intent = Intent(this@AuthenticationActivity, ExploreActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        loginRepository.account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        loadCredentials()

        _binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val loginAsGuest = _binding.loginAsGuest
        val loginWithOAuth = _binding.loginWithOauth

        authenticationViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginWithOAuth?.setOnClickListener {
            loginRepository.authenticateWithOAuth0(this,
                getString(R.string.com_auth0_scheme),
                getString(R.string.com_auth0_domain),
                { credentials ->
                    cachedCredentials = credentials
                    fetchUserProfile()
                    persistCredentials(credentials)
                    updateUiWithUser(LoggedInUserView(displayName = cachedUserProfile?.name ?: ""))
                },
                { error ->
                    showSnackbar("Login failed: ${error.message}")
                }
            )
        }

        loginAsGuest?.setOnClickListener {
            showSnackbar("Logged in as guest, you will have to login again next time")
            val intent = Intent(this@AuthenticationActivity, ExploreActivity::class.java)
            startActivity(intent)
            finish()
        }

        val decorView = window.decorView
        decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
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

    private fun fetchUserProfile() {
        val client = AuthenticationAPIClient(loginRepository.account)

        client.userInfo(loginRepository.cachedCredentials?.accessToken!!)
            .start(
                object : Callback<UserProfile, AuthenticationException> {
                    override fun onFailure(error: AuthenticationException) {
                        showSnackbar("Failed to fetch user profile: ${error.message}")
                    }

                    override fun onSuccess(profile: UserProfile) {
                        loginRepository.cachedUserProfile = profile
                    }
                }
            )
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(_binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun persistCredentials(credentials: Credentials) {
        val sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("accessToken", credentials.accessToken)
            putString("idToken", credentials.idToken)
            putString("refreshToken", credentials.refreshToken)
            putString("tokenType", credentials.type)
            putString("scope", credentials.scope)
            putLong("expiresAtTime", credentials.expiresAt.time)
            apply()
        }
    }


    private fun loadCredentials() {
        val sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)
        val idToken = sharedPreferences.getString("idToken", null)
        val refreshToken = sharedPreferences.getString("refreshToken", null)
        val tokenType = sharedPreferences.getString("tokenType", null)
        val scope = sharedPreferences.getString("scope", null)
        val expiresAtTime = sharedPreferences.getLong("expiresAt", 0)

        val expiresAt = Date(expiresAtTime)
        if (accessToken != null && idToken != null && tokenType != null) {
            loginRepository.cachedCredentials =
                Credentials(idToken, accessToken, tokenType, refreshToken, expiresAt, scope)
            fetchUserProfile()
        }
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