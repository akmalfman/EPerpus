package com.example.eperpus

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.eperpus.databinding.ActivityAddBukuBinding
import com.example.eperpus.databinding.ActivityAdminBinding
import com.example.eperpus.model.Buku
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class AddBukuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBukuBinding
    private lateinit var auth : FirebaseAuth
    private var currentImageUri: Uri? = null
    private lateinit var storageReference: StorageReference
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBukuBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        databaseReference = FirebaseDatabase.getInstance("https://smpn1rembang-6b6e5-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("buku")


        binding.btnAddImg.setOnClickListener {
            startGallery()
        }

        binding.btnSimpan.setOnClickListener {
            val judul = binding.edtJudul.text.toString()
            val kategori = binding.edtKategori.text.toString()
            val halaman = binding.edtHalaman.text.toString()
            val desc = binding.edtDesc.text.toString()

            if (currentImageUri == null || judul.isEmpty() || kategori.isEmpty() || halaman.isEmpty() || desc.isEmpty()) {
                // Salah satu EditText kosong, tampilkan toast
                Toast.makeText(this, "Harap lengkapi semua kolom", Toast.LENGTH_SHORT).show()
            } else {
                uploadImageToStorage(judul, kategori, halaman, desc)
            }
        }
    }
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImageToStorage(judul: String, kategori: String, halaman: String, desc: String) {
        currentImageUri?.let { uri ->
            val imageRef = storageReference.child("images/$uri.jpg")

            imageRef.putFile(uri)
                .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                    // Upload berhasil, dapatkan URL gambar dari Firebase Storage
                    imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                        // Simpan data buku beserta URL gambar ke Realtime Database
                        val bukuId = databaseReference.push().key
                        val buku = Buku(bukuId, judul, kategori, halaman, desc, imageUrl.toString())
                        bukuId?.let {
                            databaseReference.child(it).setValue(buku)
                        }

                        Toast.makeText(this, "Data buku berhasil disimpan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .addOnFailureListener { exception: Exception ->
                    Log.e("Upload Image", "Gagal upload gambar: ${exception.message}")
                    Toast.makeText(this, "Gagal upload gambar", Toast.LENGTH_SHORT).show()
                }
        }
    }
}