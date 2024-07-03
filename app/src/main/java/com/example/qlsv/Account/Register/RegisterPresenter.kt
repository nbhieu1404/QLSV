package com.example.qlsv.Account.Register

import android.util.Log
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

class RegisterPresenter(private val view: RegisterInterface) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val USER = "users"
    private val DEFAULT_AVATAR =
        "https://firebasestorage.googleapis.com/v0/b/qlsv-4e0cf.appspot.com/o/ImageDefault%2FAvatar.png?alt=media&token=f597b625-e043-4140-befd-db594e024472"
    private val DEFAULT_COVER_IMAGE =
        "https://firebasestorage.googleapis.com/v0/b/qlsv-4e0cf.appspot.com/o/ImageDefault%2FCoverImage.jpg?alt=media&token=1745252f-f1f1-481e-9702-d5ab6ca2b7fe"

    fun validateInputs(
        username: String,
        email: String,
        password: String,
        passwordConfirm: String,
        policy: Boolean
    ): Boolean {
        var isValid = true

        if (username.isBlank()) {
            view.showErrorUsername("Tên người dùng không được bỏ trống")
            isValid = false
        }

        if (email.isBlank()) {
            view.showErrorEmail("Email không được bỏ trống")
            isValid = false
        }

        if (password.isBlank()) {
            view.showErrorPassword("Mật khẩu không được bỏ trống")
            isValid = false
        }

        if (passwordConfirm.isBlank()) {
            view.showErrorPasswordConfirm("Mật khẩu không được bỏ trống")
            isValid = false
        }

        if (!policy) {
            view.showPolicyError(true)
            isValid = false
        }

        return isValid
    }

    fun signUp(email: String, password: String, username: String) {
        view.enableDisplay(false)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val userID = it.uid
                        val newUser = User(
                            userID,
                            email,
                            password,
                            username,
                            DEFAULT_AVATAR,
                            DEFAULT_COVER_IMAGE
                        )
                        firestore.collection(USER).document(userID).set(newUser)
                            .addOnSuccessListener {
                                verifyEmail()
                            }
//                            .addOnFailureListener { exception ->
//                                view.updateProgressBar(false)
//                                view.enableDisplay(true)
//                                view.showToast("Đăng ký không thành công")
//                            }
                    }
                    view.enableDisplay(true)
                } else {
                    emailAlreadyExists(result.exception)
                }
            }
//            .addOnFailureListener { exception ->
//                view.updateProgressBar(false)
//                view.enableDisplay(true)
//                view.showToast("Đăng ký không thành công")
//            }
    }

    private fun verifyEmail() {
        auth.currentUser
            ?.sendEmailVerification()
            ?.addOnSuccessListener {
                view.showToast("Vui lòng kiểm tra email để xác thực")
                view.finishActivity(3)
            }
            ?.addOnFailureListener {
                view.showToast("Gửi xác thực email thất bại")
                view.enableDisplay(true)
            }
    }

    private fun emailAlreadyExists(e: Exception?) {
        if (e is FirebaseAuthUserCollisionException) {
            view.showToast("Email đã tồn tại")
        } else {
            view.showToast("Đăng ký thất bại. Vui lòng thử lại.")
        }
        view.enableDisplay(true)
    }
    fun backLogin(code: Int) {
        view.finishActivity(code)
    }
}