package com.example.qlsv.Account

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.qlsv.MainActivity
import com.example.qlsv.R
import com.example.qlsv.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        clickEvents()
    }

    private fun clickEvents() {
        binding.txtForgetPassword.setOnClickListener {
            binding.txtForgetPassword.setTextColor(resources.getColor(R.color.blue))
            handler.postDelayed({
                binding.txtForgetPassword.setTextColor(resources.getColor(R.color.gray))
                val intent = Intent(this, ForgetPasswordActivity::class.java)
                startActivity(intent)
            }, 100)
        }
        binding.txtRegister.setOnClickListener {
            binding.txtRegister.setTextColor(resources.getColor(R.color.black))
            handler.postDelayed({
                binding.txtRegister.setTextColor(resources.getColor(R.color.blue))
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }, 100)

        }
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)
            } else {
                binding.txtErrorLogin.text = "Vui lòng nhập Email và mật khẩu"
                binding.txtErrorLogin.visibility = View.VISIBLE
            }
        }
        setupInputValidation()
    }

    private fun setupInputValidation() {
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtErrorLogin.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtErrorLogin.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun login(email: String, password: String) {
        enableDisplay(false)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        val isEmailVerified = user.isEmailVerified
                        if (isEmailVerified) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()

                        } else {
                            binding.txtErrorLogin.text = "Email chưa được xác thực"
                            binding.txtErrorLogin.visibility = View.VISIBLE
                            enableDisplay(true)
                        }
                    }
                } else {
                    binding.txtErrorLogin.text = "Email hoặc mật khẩu không chính xác"
                    binding.txtErrorLogin.visibility = View.VISIBLE
                    enableDisplay(true)
                }
            }
    }

    private fun enableDisplay(status: Boolean) {
        if (status) {
            binding.edtEmail.isEnabled = true
            binding.edtPassword.isEnabled = true
            binding.btnLogin.isEnabled = true
            binding.txtForgetPassword.isEnabled = true
            binding.txtRegister.isEnabled = true
            binding.layoutProgressBar.visibility = View.GONE
            binding.imgGmail.isEnabled = true
            binding.imgFacebook.isEnabled = true
        } else {
            binding.edtEmail.isEnabled = false
            binding.edtPassword.isEnabled = false
            binding.btnLogin.isEnabled = false
            binding.txtForgetPassword.isEnabled = false
            binding.txtRegister.isEnabled = false
            binding.layoutProgressBar.visibility = View.VISIBLE
            binding.imgGmail.isEnabled = false
            binding.imgFacebook.isEnabled = false
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Thoát ứng dụng")
            .setMessage("Bạn có chắc chắn muốn thoát khỏi ứng dụng không?")
            .setPositiveButton("Có") { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton("Không") { _, _ ->
            }

        val dialog = builder.create()
        dialog.show()
    }
}