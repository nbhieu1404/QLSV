package com.example.qlsv.Account.Login

interface LoginInterface {
    fun showProgressBar(show: Boolean)
    fun showToast(message: String)
    fun navigateToMainActivity()
    fun navigateToRegisterActivity()
    fun navigateToForgetPasswordActivity()
    fun showError(error: String)
    fun showExitDialog()
}