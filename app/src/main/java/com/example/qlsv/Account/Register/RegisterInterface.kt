package com.example.qlsv.Account.Register

interface RegisterInterface {
    fun showErrorUsername(message: String)
    fun showErrorEmail(message: String)
    fun showErrorPassword(message: String)
    fun showErrorPasswordConfirm(message: String)
    fun showPolicyError(isError: Boolean)
    fun enableDisplay(status: Boolean)
    fun showToast(message: String)
    fun finishActivity(code: Int)
}