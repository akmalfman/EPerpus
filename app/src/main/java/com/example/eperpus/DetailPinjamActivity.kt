package com.example.eperpus

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.eperpus.databinding.ActivityDetailPinjamBinding
import com.example.eperpus.model.Buku
import com.example.eperpus.model.Peminjaman
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailPinjamActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPinjamBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPinjamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val bukuId = intent.getStringExtra("bukuId")
        val bukuJudul = intent.getStringExtra("bukuJudul")
        val bukuphotoUrl = intent.getStringExtra("bukuPhotoUrl")
        val bukuKategori = intent.getStringExtra("bukuKategori")
        val bukuHalaman = intent.getStringExtra("bukuHalaman")

        // Initialize Firebase Database reference
        databaseReference =
            FirebaseDatabase.getInstance("https://smpn1rembang-6b6e5-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("buku")

        // Display the details in your UI components
        binding.tvJudul.text = bukuJudul
        binding.tvKategori.text = bukuKategori
        binding.tvHalaman.text = bukuHalaman

        // Load and display the image using Glide
        if (!bukuphotoUrl.isNullOrEmpty()) {
            Glide.with(this@DetailPinjamActivity)
                .load(bukuphotoUrl)
                .into(binding.imgBuku)
        }

        binding.btnPinjam.setOnClickListener {
            val durasi = binding.edtDurasi.text.toString()
            if (durasi.isEmpty()) {
                // Durasi is empty, display a toast
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                uploadPinjam(bukuId, bukuJudul, bukuphotoUrl, bukuKategori, bukuHalaman, durasi)

            }

//            // Check if the Buku object is not null
//            if (bukuId != null) {
//                // Retrieve the data from Firebase using UID
//                bukuId.let { uid ->
//                    databaseReference.child(uid).addListenerForSingleValueEvent(object :
//                        ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            if (snapshot.exists()) {
//                                // Get the Buku object from the snapshot
//                                val updatedBuku = snapshot.getValue(Buku::class.java)
//
//                                // Display the details in your UI components
//                                if (updatedBuku != null) {
//                                    binding.tvJudul.text = updatedBuku.judul
//                                    binding.tvKategori.text = updatedBuku.kategori
//                                    binding.tvHalaman.text = updatedBuku.halaman
//                                    binding.tvDesc.text = updatedBuku.deskripsi
//
//                                    // Load and display the image from Firebase Storage using Glide
//                                    if (!updatedBuku.photoUrl.isNullOrEmpty()) {
//                                        Glide.with(this@DetailPinjamActivity)
//                                            .load(updatedBuku.photoUrl)
//                                            .into(binding.imgBuku)
//                                    }
//                                    // Add more code to display other details as needed
//                                }
//                            } else {
//                                // Handle the case where the data does not exist
//                                Toast.makeText(
//                                    this@DetailPinjamActivity,
//                                    "Error: No data found",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                finish()
//                            }
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            Log.e("FirebaseError", "Error reading data", error.toException())
//                            Toast.makeText(
//                                this@DetailPinjamActivity,
//                                "Error reading data",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            finish()
//                        }
//                    })
//                }
//            } else {
//                // Handle the case where the Buku object is null
//                Toast.makeText(this, "Error: No Buku data found", Toast.LENGTH_SHORT).show()
//                finish() // Close the activity if no valid data is found
//            }
        }
    }

    private fun uploadPinjam(
        bukuId: String?,
        bukuJudul: String?,
        bukuphotoUrl: String?,
        bukuKategori: String?,
        bukuHalaman: String?,
        durasi: String
    ) {
        // Get the bukuId from the uid of the selected book
        // Get the userId from the current authenticated user
        val email = auth.currentUser?.email ?: ""

        // Get a reference to the "peminjaman" table in Firebase
        val peminjamanRef =
            FirebaseDatabase.getInstance("https://smpn1rembang-6b6e5-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("peminjaman")

        // Generate a unique ID for the peminjaman record
        val peminjamanId = peminjamanRef.push().key

        // Create a Peminjaman object
        val peminjaman = Peminjaman(
            null,
            bukuId,
            bukuJudul,
            email,
            bukuphotoUrl,
            bukuKategori,
            bukuHalaman,
            durasi
        )

        // Store the Peminjaman object in the "peminjaman" table with the generated ID
        peminjamanId?.let {
            peminjamanRef.child(it).setValue(peminjaman)
            Toast.makeText(this, "Peminjaman successful!", Toast.LENGTH_SHORT).show()
        }

        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }
}
