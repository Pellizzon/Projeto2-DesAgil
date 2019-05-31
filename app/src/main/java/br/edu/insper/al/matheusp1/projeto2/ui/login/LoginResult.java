package br.edu.insper.al.matheusp1.projeto2.ui.login;

import android.support.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {

    LoginResult() {
        Integer error = br.edu.insper.al.matheusp1.projeto2.R.string.login_failed;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        LoggedInUserView success1 = success;
    }

}
