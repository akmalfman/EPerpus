package com.example.eperpus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eperpus.adapter.BukuAdapter
import com.example.eperpus.adapter.BukuUserAdapter
import com.example.eperpus.databinding.ActivityAdminBinding
import com.example.eperpus.databinding.ActivityUserBinding
import com.example.eperpus.model.Buku
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener
    private lateinit var adapter: BukuUserAdapter
    private var originalBukuList = mutableListOf<Buku>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://smpn1rembang-6b6e5-default-rtdb.asia-southeast1.firebasedatabase.app/")
        ref = database.getReference("buku")

        val layoutManager = LinearLayoutManager(this)
        binding.rvBuku.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvBuku.addItemDecoration(itemDecoration)

        adapter = BukuUserAdapter(this)
        binding.rvBuku.adapter = adapter
        // Fetch and display mata pelajaran from Firebase
        fetchBuku()

        binding.btnSortByCategory.setOnClickListener {
            showCategoryDialog()
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                // User clicked Yes, perform logout
                logout()
            }
            .setNegativeButton("No") { dialog, _ ->
                // User clicked No, dismiss the dialog
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun logout() {
        // Perform logout action here, for example, sign out from Firebase Auth
        FirebaseAuth.getInstance().signOut()

        // Redirect to your login or home activity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showCategoryDialog() {
        val categories = getCategoryList()

        val categoryArray = categories.toTypedArray()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Category")
            .setItems(categoryArray) { _, which ->
                // Handle the selected category
                val selectedCategory = categoryArray[which]
                if (selectedCategory != null) {
                    filterBukuByCategory(selectedCategory)
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Reset All") { _, _ ->
                resetData()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun getCategoryList(): List<String?> {
        // Retrieve the list of unique categories from the original data
        return originalBukuList.map { it.kategori }.distinct()
    }

    private fun filterBukuByCategory(category: String) {
        val bukuList = originalBukuList.filter { it.kategori == category }

        // Update the adapter with the filtered list
        adapter.setData(bukuList)
    }

    private fun resetData() {
        adapter.setData(originalBukuList)
    }

    private fun fetchBuku() {
        // ValueEventListener to update the adapter when data changes
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("MapelActivity", "Data changed. Count: ${snapshot.childrenCount}")

                val bukuList = mutableListOf<Buku>()

                for (dataSnapshot in snapshot.children) {
                    val buku = dataSnapshot.getValue(Buku::class.java)
                    buku?.let {
                        bukuList.add(it)
                    }
                }
// Store the original data
                originalBukuList = bukuList.toMutableList()
                adapter.setData(bukuList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if necessary
            }
        }

        // Attach the listener to the database reference
        ref.addValueEventListener(valueEventListener)

        // ... (your existing code)
    }


}