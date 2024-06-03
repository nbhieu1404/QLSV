package com.example.qlsv.Account.ForgetPassword

interface ForgetPasswordInterface {
    fun showEmailError(error: String)
    fun showToast(message: String)
    fun finishActivity()
    fun enableDisplay(status: Boolean)
}