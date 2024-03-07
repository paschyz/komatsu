package com.komatsu.data


import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.komatsu.R
import com.komatsu.data.models.LoggedInUser
import com.komatsu.databinding.ActivityAuthenticationBinding
import com.komatsu.ui.viewmodel.LoginViewModel
import java.util.Date

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of authentication status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    private lateinit var authenticationViewModel: LoginViewModel
    private lateinit var _binding: ActivityAuthenticationBinding
    private lateinit var _account: Auth0
    private var _cachedCredentials: Credentials? = null
    private var _cachedUserProfile: UserProfile? = null

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    var account: Auth0
        get() = _account
        set(value) {
            _account = value
        }

    var cachedCredentials: Credentials?
        get() = _cachedCredentials
        set(value) {
            _cachedCredentials = value
        }

    var cachedUserProfile: UserProfile?
        get() = _cachedUserProfile
        set(value) {
            _cachedUserProfile = value
        }

    val isLoggedIn: Boolean
        get() = user != null || cachedCredentials != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }



    fun authentication(username: String, password: String): com.komatsu.data.Result<LoggedInUser> {
        // handle authentication
        val result = dataSource.authentication(username, password)

        if (result is com.komatsu.data.Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }


    fun authenticateWithOAuth0(
        context: Context,
        scheme: String,
        domain: String,
        onSuccess: (Credentials) -> Unit,
        onFailure: (AuthenticationException) -> Unit,
    ) {
        WebAuthProvider.login(_account)
            .withScheme(scheme)
            .withAudience("https://$domain/api/v2/")
            .withScope("openid profile email")
            .start(
                context,
                object : Callback<Credentials, AuthenticationException> {
                    override fun onFailure(error: AuthenticationException) {
                        onFailure(error)
                    }

                    override fun onSuccess(credentials: Credentials) {
                        Log.i("LoginRepository", "credentials: $credentials")
                        onSuccess(credentials)
                    }

                }
            )
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}