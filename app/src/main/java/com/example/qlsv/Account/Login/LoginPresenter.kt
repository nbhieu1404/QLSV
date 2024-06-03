package com.example.qlsv.Account.Login

import com.google.firebase.auth.FirebaseAuth

class LoginPresenter(private val view: LoginInterface) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun handleLogin(email: String, password: String): Boolean {
        var isValid = true
        if (email.isEmpty() && password.isEmpty()) {
            view.showError("Email và mật khẩu không được để trống")
            isValid = false
        } else if (email.isEmpty()) {
            view.showError("Email không được để trống")
            isValid = false
        } else if (password.isEmpty()) {
            view.showError("Mật khẩu không được để trống")
            isValid = false
        }
        return isValid
    }

    fun login(email: String, password: String) {
        view.showProgressBar(true)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    val user = auth.currentUser
                    if (user?.isEmailVerified == true) {
                        view.navigateToMainActivity()
                    } else {
                        view.showToast("Email chưa được xác thực")
                    }
                } else {
                    view.showToast("Đăng nhập thất bại, vui lòng kiểm tra thông tin đăng nhập")
                }
                view.showProgressBar(false)
            }
    }

    fun navigateToRegister() {
        view.navigateToRegisterActivity()
    }

    fun navigateToForgetPassword() {
        view.navigateToForgetPasswordActivity()
    }

    fun handleBackPressed() {
        view.showExitDialog()
    }
}