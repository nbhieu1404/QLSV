package com.example.qlsv.Account

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.qlsv.R
import com.example.qlsv.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        clickEvents()
    }


    private fun clickEvents() {
        binding.txtLogin.setOnClickListener {
            val handler = Handler()
            binding.txtLogin.setTextColor(resources.getColor(R.color.blue))
            handler.postDelayed({
                binding.txtLogin.setTextColor(resources.getColor(R.color.black))
                finish()
            }, 100)
        }

        binding.btnConfirm.setOnClickListener {
            val inputEmail = binding.edtEmail.text.toString()
            when {
                inputEmail.isBlank() -> {
                    binding.boxEmail.error = "Vui lòng nhập Email"
                }

                !Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches() -> {
                    binding.boxEmail.error = "Vui lòng nhập Email đúng định dạng"
                }

                else -> {
                    binding.boxEmail.error = null
                    Toast.makeText(this, "Thành công", Toast.LENGTH_SHORT).show()
                    finish()
                }
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

        binding.btnConfirm.setOnClickListener {
            sendForgetPassword()
        }
    }

    private fun sendForgetPassword() {
        val email = binding.edtEmail.text.toString().trim()
        when {
            email.isBlank() -> {
                binding.txtErrorEmail.text = "Vui lòng nhập Email"
                binding.txtErrorEmail.visibility = View.VISIBLE
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.txtErrorEmail.text = "Vui lòng nhập Email đúng định dạng"
                binding.txtErrorEmail.visibility = View.VISIBLE
            }

            else -> {
                enableDisplay(false)
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Vui lòng kiểm tra email để lấy lại mật khẩu",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Gửi Email đặt lại mật khẩu thất bại. Vui lòng kiểm tra lại Email",
                                Toast.LENGTH_LONG
                            ).show()
                            enableDisplay(true)
                        }
                    }
            }
        }
    }

    private fun setupInputValidation() {
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
    }

    private fun enableDisplay(status: Boolean) {
        if (status) {
            binding.btnBack.isEnabled = true
            binding.edtEmail.isEnabled = true
            binding.btnConfirm.isEnabled = true
            binding.txtLogin.isEnabled = true
            binding.btnConfirm.isEnabled = true
            binding.layoutProgressBar.visibility = View.GONE
        } else {
            binding.btnBack.isEnabled = false
            binding.edtEmail.isEnabled = false
            binding.btnConfirm.isEnabled = false
            binding.txtLogin.isEnabled = false
            binding.btnConfirm.isEnabled = false
            binding.layoutProgressBar.visibility = View.VISIBLE
        }
    }

}