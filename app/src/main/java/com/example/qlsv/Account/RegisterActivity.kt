package com.example.qlsv.Account

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.qlsv.R
import com.example.qlsv.databinding.ActivityRegisterBinding
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var STATUS_REGISTER_SUCCESS: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickEvents()
    }

    private fun clickEvents() {
        binding.txtLogin.setOnClickListener {
            finish()
        }
        binding.btnSignUp.setOnClickListener {
            if (validateInputs()) {
                signUp()
            } else {
                Toast.makeText(this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show()
            }
        }
        setupInputValidation()

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
    }

    private fun validateInputs(): Boolean {
        val isUsernameValid = binding.txtErrorUsername.visibility == View.INVISIBLE
        val isEmailValid = binding.txtErrorEmail.visibility == View.INVISIBLE
        val isPasswordValid = binding.txtErrorPassword.visibility == View.INVISIBLE
        val isPasswordConfirmValid = binding.txtErrorPasswordConfirm.visibility == View.INVISIBLE
        val isPolicyAccepted = binding.cbPolicy.isChecked

        binding.cbPolicy.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.txtPolicy.setTextColor(ContextCompat.getColor(this, R.color.blue_status))
                binding.txtAccept.setTextColor(Color.BLACK)
            } else {
                binding.txtPolicy.setTextColor(Color.RED)
                binding.txtAccept.setTextColor(Color.RED)
            }
        }
        binding.txtAccept.setTextColor(if (isPolicyAccepted) Color.BLACK else Color.RED)
        binding.txtPolicy.setTextColor(
            if (isPolicyAccepted) ContextCompat.getColor(
                this,
                R.color.blue_status
            ) else Color.RED
        )

        return isUsernameValid && isEmailValid && isPasswordValid && isPasswordConfirmValid && isPolicyAccepted
    }


    private fun signUp() {
        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
    }

    fun hasSpecialCharacters(string: String): Boolean {
        val regex = Regex("[^\\w\\s]")
        return regex.containsMatchIn(string)
    }
}