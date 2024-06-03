package com.example.qlsv.Account.Login

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qlsv.Account.ForgetPassword.ForgetPasswordActivity
import com.example.qlsv.Account.Register.RegisterActivity
import com.example.qlsv.MainActivity
import com.example.qlsv.R
import com.example.qlsv.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), LoginInterface {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var presenter: LoginPresenter
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = LoginPresenter(this)
        clickEvents()
    }

    private fun clickEvents() {
        binding.txtForgetPassword.setOnClickListener {
            presenter.navigateToForgetPassword()
        }
        binding.txtRegister.setOnClickListener {
            presenter.navigateToRegister()
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            if (presenter.handleLogin(email, password)) {
                presenter.login(email, password)
            }

        }
        setupInputValidation()
    }

    private fun setupInputValidation() {
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtErrorLogin.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtErrorLogin.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        presenter.handleBackPressed()
    }

    override fun showProgressBar(show: Boolean) {
        binding.edtEmail.isEnabled = !show
        binding.edtPassword.isEnabled = !show
        binding.btnLogin.isEnabled = !show
        binding.txtRegister.isEnabled = !show
        binding.txtForgetPassword.isEnabled = !show
        binding.btnLogin.isEnabled = !show
        binding.layoutProgressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun navigateToRegisterActivity() {
        binding.txtRegister.setTextColor(resources.getColor(R.color.black))
        handler.postDelayed({
            binding.txtRegister.setTextColor(resources.getColor(R.color.blue))
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }, 100)
    }

    override fun navigateToForgetPasswordActivity() {
        binding.txtForgetPassword.setTextColor(resources.getColor(R.color.blue))
        handler.postDelayed({
            binding.txtForgetPassword.setTextColor(resources.getColor(R.color.gray))
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }, 100)
    }

    override fun showError(error: String) {
        binding.txtErrorLogin.text = error
        binding.txtErrorLogin.visibility = View.VISIBLE
    }

    override fun showExitDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Thoát ứng dụng")
            .setMessage("Bạn có chắc chắn muốn thoát khỏi ứng dụng không?")
            .setPositiveButton("Có") { _: DialogInterface, _: Int ->
                finish()
            }
            .setNegativeButton("Không") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
        builder.create().show()
    }
}
