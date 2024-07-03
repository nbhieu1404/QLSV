package com.example.qlsv.Account.Register

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.qlsv.R
import com.example.qlsv.databinding.ActivityRegisterBinding
import android.util.Patterns
import android.widget.Toast

class RegisterActivity : AppCompatActivity(), RegisterInterface {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var presenter: RegisterPresenter
    private val BACK_LOGIN_TEXT = 1
    private val BACK_LOGIN_ICON = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = RegisterPresenter(this)

        clickEvents()
    }

    private fun clickEvents() {
        binding.txtLogin.setOnClickListener {
            presenter.backLogin(BACK_LOGIN_TEXT)
        }
        binding.btnSignUp.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val username = binding.edtUsername.text.toString().trim()
            val passwordConfirm = binding.edtPasswordConfirm.text.toString().trim()
            val policy = binding.cbPolicy.isChecked

            if (presenter.validateInputs(username, email, password, passwordConfirm, policy)) {
                presenter.signUp(email, password, username)
            }
        }
        binding.btnBack.setOnClickListener {
            presenter.backLogin(BACK_LOGIN_ICON)

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

    fun hasSpecialCharacters(string: String): Boolean {
        val regex = Regex("[^\\w\\s]")
        return regex.containsMatchIn(string)
    }

    override fun showErrorUsername(message: String) {
        binding.txtErrorUsername.text = message
        binding.txtErrorUsername.visibility = View.VISIBLE
    }

    override fun showErrorEmail(message: String) {
        binding.txtErrorEmail.text = message
        binding.txtErrorEmail.visibility = View.VISIBLE
    }

    override fun showErrorPassword(message: String) {
        binding.txtErrorPassword.text = message
        binding.txtErrorPassword.visibility = View.VISIBLE
    }

    override fun showErrorPasswordConfirm(message: String) {
        binding.txtErrorPasswordConfirm.text = message
        binding.txtErrorPasswordConfirm.visibility = View.VISIBLE
    }

    override fun showPolicyError(isError: Boolean) {
        val color = if (isError) Color.RED else ContextCompat.getColor(this, R.color.blue_status)
        binding.txtPolicy.setTextColor(color)
        binding.txtAccept.setTextColor(color)
    }

    override fun enableDisplay(status: Boolean) {
        binding.edtUsername.isEnabled = status
        binding.edtEmail.isEnabled = status
        binding.edtPassword.isEnabled = status
        binding.edtPasswordConfirm.isEnabled = status
        binding.cbPolicy.isEnabled = status
        binding.btnSignUp.isEnabled = status
        binding.txtLogin.isEnabled = status
        binding.layoutProgressBar.visibility = if (status) View.GONE else View.VISIBLE
    }


    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun finishActivity(code: Int) {
        when (code) {
            BACK_LOGIN_TEXT -> {
                binding.txtLogin.setTextColor(resources.getColor(R.color.blue))
                Handler().postDelayed({
                    binding.txtLogin.setTextColor(resources.getColor(R.color.blue_status))
                    finish()
                }, 100)
            }

            BACK_LOGIN_ICON -> {
                val handler = Handler()
                binding.btnBack.setImageResource(R.drawable.icon_back_gray)
                handler.postDelayed({
                    binding.btnBack.setImageResource(R.drawable.icon_back_white)
                    finish()
                }, 100)
            }

            else -> {
                finish()
            }

        }
    }

}