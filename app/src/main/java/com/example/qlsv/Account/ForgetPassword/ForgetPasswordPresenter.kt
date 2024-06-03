package com.example.qlsv.Account.ForgetPassword

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordPresenter(private val view: ForgetPasswordInterface) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun handleForgetPassword(email: String): Boolean {
        var isValid = true
        if (email.isEmpty()) {
            view.showEmailError("Vui lòng nhập Email")
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showEmailError("Vui lòng nhập Email đúng định dạng")
            isValid = false
        }
        return isValid
    }

    fun resetPassword(email: String) {
        view.enableDisplay(false)
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    view.showToast("Kiểm tra Email để đặt lại mật khẩu")
                    view.finishActivity()
                } else {
                    view.showToast(result.exception.toString())
                }
            }
    }

    fun backLoginActivity() {
        view.finishActivity()
    }
}
