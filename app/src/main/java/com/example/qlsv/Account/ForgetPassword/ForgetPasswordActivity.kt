package com.example.qlsv.Account.ForgetPassword

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qlsv.R
import com.example.qlsv.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity(), ForgetPasswordInterface {
    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var presenter: ForgetPasswordPresenter
    private val BACK_LOGIN_TEXT = 1
    private val BACK_LOGIN_ICON = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = ForgetPasswordPresenter(this)

        clickEvents()
    }

    private fun clickEvents() {
        binding.txtLogin.setOnClickListener {
            presenter.backLoginActivity(BACK_LOGIN_TEXT)
        }

        binding.btnConfirm.setOnClickListener {
            val inputEmail = binding.edtEmail.text.toString().trim()
            if (presenter.handleForgetPassword(inputEmail)) {
                presenter.resetPassword(inputEmail)

            }
        }

        binding.btnBack.setOnClickListener {
            presenter.backLoginActivity(BACK_LOGIN_ICON)
        }

        setupInputValidation()
    }

    private fun setupInputValidation() {
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtErrorEmail.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                presenter.handleForgetPassword(s.toString().trim())
            }
        })
    }

    override fun showEmailError(error: String) {
        binding.txtErrorEmail.text = error
        binding.txtErrorEmail.visibility = View.VISIBLE
    }


    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun finishActivity(code: Int) {
        when (code) {
            BACK_LOGIN_TEXT -> {
                val handler = Handler()
                binding.txtLogin.setTextColor(resources.getColor(R.color.blue))
                handler.postDelayed({
                    binding.txtLogin.setTextColor(resources.getColor(R.color.black))
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

    override fun enableDisplay(status: Boolean) {
        binding.btnBack.isEnabled = status
        binding.edtEmail.isEnabled = status
        binding.btnConfirm.isEnabled = status
        binding.txtLogin.isEnabled = status
        binding.btnConfirm.isEnabled = status
        binding.layoutProgressBar.visibility = if (status) View.GONE else View.VISIBLE
    }
}
