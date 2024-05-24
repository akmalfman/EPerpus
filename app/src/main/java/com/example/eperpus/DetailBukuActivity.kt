package com.example.eperpus

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.eperpus.databinding.ActivityDetailBukuBinding
import com.example.eperpus.model.Buku
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailBukuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBukuBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive the Buku object from the intent
//        val buku = intent.getParcelableExtra<Buku>("buku")
        // Receiving MataPelajaran object in another activity
        val buku = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<Buku>("buku", Buku::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Buku>("buku")
        }

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://smpn1rembang-6b6e5-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("buku")

        // Check if the Buku object is not null
        if (buku != null) {
            // Retrieve the data from Firebase using UID
            buku.uid?.let { uid ->
                databaseReference.child(uid).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Get the Buku object from the snapshot
                            val updatedBuku = snapshot.getValue(Buku::class.java)

                            // Display the details in your UI components
                            if (updatedBuku != null) {
                                binding.edtJudul.setText(updatedBuku.judul)
                                binding.edtKategori.setText(updatedBuku.kategori)
                                binding.edtHalaman.setText(updatedBuku.halaman)
                                binding.edtDesc.setText(updatedBuku.deskripsi)

                                // Load and display the image from Firebase Storage using Glide
                                if (!updatedBuku.photoUrl.isNullOrEmpty()) {
                                    Glide.with(this@DetailBukuActivity)
                                        .load(updatedBuku.photoUrl)
                                        .into(binding.previewImageView)
                                }
                                // Add more code to display other details as needed
                            }
                        } else {
                            // Handle the case where the data does not exist
                            Toast.makeText(this@DetailBukuActivity, "Error: No data found", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("FirebaseError", "Error reading data", error.toException())
                        Toast.makeText(this@DetailBukuActivity, "Error reading data", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                })
            }
        } else {
            // Handle the case where the Buku object is null
            Toast.makeText(this, "Error: No Buku data found", Toast.LENGTH_SHORT).show()
            finish() // Close the activity if no valid data is found
        }

        binding.btnSimpan.setOnClickListener {
            // Implement the logic to update values in the database
            if (buku != null) {
                buku.judul = binding.edtJudul.text.toString()
                buku.kategori = binding.edtKategori.text.toString()
                buku.halaman = binding.edtHalaman.text.toString()
                buku.deskripsi = binding.edtDesc.text.toString()

                // Update the values in the database excluding photoUrl
                val updates = HashMap<String, Any>()
                updates["judul"] = buku.judul!!
                updates["kategori"] = buku.kategori!!
                updates["halaman"] = buku.halaman!!
                updates["deskripsi"] = buku.deskripsi!!

                databaseReference.child(buku.uid ?: "").updateChildren(updates)
                    .addOnSuccessListener {
                        Toast.makeText(this@DetailBukuActivity, "Data updated successfully", Toast.LENGTH_SHORT).show()

                        // Move to AdminActivity on success
                        val intent = Intent(this@DetailBukuActivity, AdminActivity::class.java)
                        startActivity(intent)
                        finish() // Optional: Close the current activity if needed
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@DetailBukuActivity, "Error updating data", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}