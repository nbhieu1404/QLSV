package com.example.qlsv.Account

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.qlsv.R
import com.example.qlsv.databinding.ActivityForgetPasswordBinding

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickEvents()
    }

    private fun clickEvents() {
        binding.txtLogin.setOnClickListener {
            finish()
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
    }
}