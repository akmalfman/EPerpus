package com.example.eperpus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.eperpus.databinding.ActivityRegisterAdminBinding
import com.example.eperpus.databinding.ActivityRegisterBinding
import com.example.eperpus.model.Admin
import com.example.eperpus.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterAdminBinding

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterAdminBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmailRegister.text.toString()
            val password = binding.edtPasswordRegister.text.toString()

            //Validasi email
            if (email.isEmpty()) {
                binding.edtEmailRegister.error = "Email Harus Diisi"
                binding.edtEmailRegister.requestFocus()
                return@setOnClickListener
            }

            //Validasi email tidak sesuai
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmailRegister.error = "Email Tidak Valid"
                binding.edtEmailRegister.requestFocus()
                return@setOnClickListener
            }

            //Validasi password
            if (password.isEmpty()) {
                binding.edtPasswordRegister.error = "Password Harus Diisi"
                binding.edtPasswordRegister.requestFocus()
                return@setOnClickListener
            }

            //Validasi panjang password
            if (password.length < 6) {
                binding.edtPasswordRegister.error = "Password Minimal 6 Karakter"
                binding.edtPasswordRegister.requestFocus()
                return@setOnClickListener
            }
            registerFirebase(email, password)
        }
    }

    private fun registerFirebase(email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //register student in database
                val admin = Admin(
                    null,
                    binding.edtEmailRegister.text.toString(),
                    binding.edtPasswordRegister.text.toString()
                )
                database = FirebaseDatabase.getInstance("https://smpn1rembang-6b6e5-default-rtdb.asia-southeast1.firebasedatabase.app/")
                val ref = database.getReference("admin")
                val id = FirebaseAuth.getInstance().currentUser!!.uid
                ref.child(id).setValue(admin).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}