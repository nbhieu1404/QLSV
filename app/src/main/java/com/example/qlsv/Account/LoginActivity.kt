package com.example.qlsv.Account

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        clickEvents()
    }

    private fun clickEvents() {
        binding.txtForgetPassword.setOnClickListener {
            val handler = Handler()
            binding.txtForgetPassword.setTextColor(resources.getColor(R.color.blue))
            handler.postDelayed({
                binding.txtForgetPassword.setTextColor(resources.getColor(R.color.gray))
                val intent = Intent(this, ForgetPasswordActivity::class.java)
                startActivity(intent)
            }, 100)
        }
        binding.txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)
            }
        }
    }

    private fun login(email: String, password: String) {
        enableDisplay(true)
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
                            Toast.makeText(
                                this, "Email chưa được xác thực",
                                Toast.LENGTH_SHORT
                            ).show()
                            enableDisplay(false)
                        }
                    }
                } else {
                    Toast.makeText(
                        this, "Email hoặc mật khẩu chính xác",
                        Toast.LENGTH_SHORT
                    ).show()
                    enableDisplay(false)
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