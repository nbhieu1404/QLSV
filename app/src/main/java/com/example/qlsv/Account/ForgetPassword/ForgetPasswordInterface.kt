package com.example.qlsv.Account.ForgetPassword

interface ForgetPasswordInterface {
    fun showEmailError(error: String)
    fun showToast(message: String)
    fun finishActivity(code: Int)
    fun enableDisplay(status: Boolean)
}