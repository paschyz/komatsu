package com.komatsu.data

import com.komatsu.data.models.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ authentication credentials and retrieves user information.
 */
class LoginDataSource {

    fun authentication(username: String, password: String): com.komatsu.data.Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            return com.komatsu.data.Result.Success(fakeUser)
        } catch (e: Throwable) {
            return com.komatsu.data.Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
    }
}