package com.example.komatsu.ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.komatsu.data.Result

import com.example.komatsu.R
import com.example.komatsu.data.LoginRepository
import com.example.komatsu.ui.view.LoggedInUserView
import com.example.komatsu.ui.view.LoginFormState
import com.example.komatsu.ui.view.LoginResult

class LoginViewModel(private val authenticationRepository: LoginRepository) : ViewModel() {

    private val _authenticationForm = MutableLiveData<LoginFormState>()
    val authenticationFormState: LiveData<LoginFormState> = _authenticationForm

    private val _authenticationResult = MutableLiveData<LoginResult>()
    val authenticationResult: LiveData<LoginResult> = _authenticationResult

    fun authentication(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = authenticationRepository.authentication(username, password)

        if (result is Result.Success) {
            _authenticationResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _authenticationResult.value = LoginResult(error = R.string.authentication_failed)
        }
    }

    fun authenticationDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _authenticationForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _authenticationForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _authenticationForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}