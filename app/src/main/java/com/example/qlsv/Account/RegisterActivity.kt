package com.example.qlsv.Account

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.nfc.Tag
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.qlsv.R
import com.example.qlsv.databinding.ActivityRegisterBinding
import android.util.Patterns
import android.widget.Toast
import com.example.qlsv.Class.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val USER = "users"
    private val DEFAULT_AVATAR =
        "https://firebasestorage.googleapis.com/v0/b/qlsv-4e0cf.appspot.com/o/ImageDefault%2FAvatar.png?alt=media&token=f597b625-e043-4140-befd-db594e024472"
    private val DEFAULT_COVER_IMAGE =
        "https://firebasestorage.googleapis.com/v0/b/qlsv-4e0cf.appspot.com/o/ImageDefault%2FCoverImage.jpg?alt=media&token=1745252f-f1f1-481e-9702-d5ab6ca2b7fe"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        clickEvents()
    }

    private fun clickEvents() {
        binding.txtLogin.setOnClickListener {
            binding.txtLogin.setTextColor(resources.getColor(R.color.blue))
            Handler().postDelayed({
                binding.txtLogin.setTextColor(resources.getColor(R.color.blue_status))
                finish()
            }, 100)

        }
        binding.btnSignUp.setOnClickListener {
            if (validateInputs()) {
                Log.d("click", "click")
                enableDisplay(false)
                val email = binding.edtEmail.text.toString().trim()
                val password = binding.edtPassword.text.toString().trim()
                val username = binding.edtUsername.text.toString().trim()
                signUp(email, password, username)
            } else {

                errorsRegister()
            }
        }
        binding.btnBack.setOnClickListener {
            val handler = Handler()
            binding.btnBack.setImageResource(R.drawable.icon_back_gray)
            handler.postDelayed({
                binding.btnBack.setImageResource(R.drawable.icon_back_white)
                finish()
            }, 100)

        }
        setupInputValidation()
    }

    private fun errorsRegister() {
        val username = binding.edtUsername.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val passwordConfirm = binding.edtPasswordConfirm.text.toString().trim()
        val policy = binding.cbPolicy.isChecked
        when {
            username.isBlank() -> {
                binding.txtErrorUsername.text = "Tên người dùng không được bỏ trống"
                binding.txtErrorUsername.visibility = View.VISIBLE
            }

            email.isBlank() -> {
                binding.txtErrorEmail.text = "Email không được bỏ trống"
                binding.txtErrorEmail.visibility = View.VISIBLE
            }

            password.isBlank() -> {
                binding.txtErrorPassword.text = "Mật khẩu không được bỏ trống"
                binding.txtErrorPassword.visibility = View.VISIBLE
            }

            passwordConfirm.isBlank() -> {
                binding.txtErrorPasswordConfirm.text = "Mật khẩu không được bỏ trống"
                binding.txtErrorPasswordConfirm.visibility = View.VISIBLE
            }

            !policy -> {
                binding.txtPolicy.setTextColor(Color.RED)
                binding.txtAccept.setTextColor(Color.RED)
            }
        }
    }

    private fun setupInputValidation() {
        binding.edtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtErrorUsername.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                val inputUsername = s.toString().trim()
                when {
                    inputUsername.isBlank() -> {
                        binding.txtErrorUsername.text = "Tên người dùng không được bỏ trống"
                        binding.txtErrorUsername.visibility = View.VISIBLE
                    }

                    inputUsername.length <= 1 || hasSpecialCharacters(inputUsername) -> {
                        binding.txtErrorUsername.text = "Vui lòng nhập tên có nghĩa"
                        binding.txtErrorUsername.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.txtErrorUsername.visibility = View.INVISIBLE
                    }
                }
            }
        })

        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtErrorEmail.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                val inputEmail = s.toString().trim()
                when {
                    inputEmail.isBlank() -> {
                        binding.txtErrorEmail.text = "Email không được bỏ trống"
                        binding.txtErrorEmail.visibility = View.VISIBLE
                    }

                    !Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches() -> {
                        binding.txtErrorEmail.text = "Vui lòng nhập Email đúng định dạng"
                        binding.txtErrorEmail.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.txtErrorEmail.visibility = View.INVISIBLE
                    }
                }
            }
        })

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtErrorPassword.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                val inputPassword = s.toString().trim()
                when {
                    inputPassword.isBlank() -> {
                        binding.txtErrorPassword.text = "Mật khẩu không được bỏ trống"
                        binding.txtErrorPassword.visibility = View.VISIBLE
                    }

                    inputPassword.length < 6 -> {
                        binding.txtErrorPassword.text = "Mật khẩu phải có ít nhất 6 ký tự"
                        binding.txtErrorPassword.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.txtErrorPassword.visibility = View.INVISIBLE
                    }
                }
            }
        })

        binding.edtPasswordConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtErrorPasswordConfirm.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                val inputPasswordConfirm = s.toString().trim()
                when {
                    inputPasswordConfirm.isBlank() -> {
                        binding.txtErrorPasswordConfirm.text = "Mật khẩu không được bỏ trống"
                        binding.txtErrorPasswordConfirm.visibility = View.VISIBLE
                    }

                    inputPasswordConfirm != binding.edtPassword.text.toString().trim() -> {
                        binding.txtErrorPasswordConfirm.text = "Mật khẩu không trung khớp"
                        binding.txtErrorPasswordConfirm.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.txtErrorPasswordConfirm.visibility = View.INVISIBLE
                    }
                }
            }
        })

        binding.cbPolicy.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.txtPolicy.setTextColor(ContextCompat.getColor(this, R.color.blue_status))
                binding.txtAccept.setTextColor(Color.BLACK)
            } else {
                binding.txtPolicy.setTextColor(Color.RED)
                binding.txtAccept.setTextColor(Color.RED)
            }
        }
    }

    private fun validateInputs(): Boolean {
        val isUsernameValid = binding.txtErrorUsername.visibility == View.INVISIBLE
        val isEmailValid = binding.txtErrorEmail.visibility == View.INVISIBLE
        val isPasswordValid = binding.txtErrorPassword.visibility == View.INVISIBLE
        val isPasswordConfirmValid = binding.txtErrorPasswordConfirm.visibility == View.INVISIBLE
        val isPolicyAccepted = binding.cbPolicy.isChecked

        val inputUsername = binding.edtUsername.text.toString().trim()
        val inputEmail = binding.edtEmail.text.toString().trim()
        val inputPassword = binding.edtPassword.text.toString().trim()
        val inputPasswordConfirm = binding.edtPasswordConfirm.text.toString().trim()
        if (inputUsername.isNotEmpty() && inputEmail.isNotEmpty() && inputPassword.isNotEmpty() && inputPasswordConfirm.isNotEmpty()) {
            return isUsernameValid && isEmailValid && isPasswordValid && isPasswordConfirmValid && isPolicyAccepted
        }
        return false
    }

    private fun signUp(email: String, password: String, username: String) {
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
                                Log.d("Register", "verify")
                                verifyEmail()
                            }
                            .addOnFailureListener { exception ->
                                Log.d("Register", "Error Failure: ${exception.message}")
                                Toast.makeText(this, "Đăng ký không thành công", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                    enableDisplay(true)
                } else {
                    emailAlreadyExists(result.exception)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Register", "Error Failure: ${exception.message}")
                Toast.makeText(this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show()
            }

    }

    private fun verifyEmail() {
        auth.currentUser
            ?.sendEmailVerification()
            ?.addOnSuccessListener {
                Toast.makeText(this, "Vui lòng kiểm tra email để xác thực", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
            ?.addOnFailureListener {
                Toast.makeText(this, "Gửi xác thực email thất bại", Toast.LENGTH_SHORT).show()
            }
    }

    fun hasSpecialCharacters(string: String): Boolean {
        val regex = Regex("[^\\w\\s]")
        return regex.containsMatchIn(string)
    }

    private fun emailAlreadyExists(e: Exception?) {
        if (e is FirebaseAuthUserCollisionException) {
            Toast.makeText(this, "Email đã tồn tại", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
        }
        enableDisplay(true)
    }

    private fun enableDisplay(status: Boolean) {
        if (status) {
            binding.edtUsername.isEnabled = true
            binding.edtEmail.isEnabled = true
            binding.edtPassword.isEnabled = true
            binding.edtPasswordConfirm.isEnabled = true
            binding.cbPolicy.isEnabled = true
            binding.btnSignUp.isEnabled = true
            binding.layoutProgressBar.visibility = View.GONE
            binding.txtLogin.isEnabled = true
        } else {
            binding.edtUsername.isEnabled = false
            binding.edtEmail.isEnabled = false
            binding.edtPassword.isEnabled = false
            binding.edtPasswordConfirm.isEnabled = false
            binding.cbPolicy.isEnabled = false
            binding.btnSignUp.isEnabled = false
            binding.layoutProgressBar.visibility = View.VISIBLE
            binding.txtLogin.isEnabled = false
        }
    }
}
