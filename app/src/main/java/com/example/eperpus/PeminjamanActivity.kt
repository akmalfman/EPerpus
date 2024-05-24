package com.example.eperpus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eperpus.adapter.BukuAdapter
import com.example.eperpus.adapter.PeminajamAdapter
import com.example.eperpus.databinding.ActivityAdminBinding
import com.example.eperpus.databinding.ActivityPeminjamanBinding
import com.example.eperpus.model.Buku
import com.example.eperpus.model.Peminjaman
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PeminjamanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPeminjamanBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener
    private lateinit var adapter: PeminajamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPeminjamanBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://smpn1rembang-6b6e5-default-rtdb.asia-southeast1.firebasedatabase.app/")
        ref = database.getReference("peminjaman")

        val layoutManager = LinearLayoutManager(this)
        binding.rvPeminjaman.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvPeminjaman.addItemDecoration(itemDecoration)

        adapter = PeminajamAdapter(this)
        binding.rvPeminjaman.adapter = adapter

        // Fetch and display mata pelajaran from Firebase
        fetchPeminjaman()

    }

    private fun fetchPeminjaman() {
        // ValueEventListener to update the adapter when data changes
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("MapelActivity", "Data changed. Count: ${snapshot.childrenCount}")

                val peminjamanList = mutableListOf<Peminjaman>()

                for (dataSnapshot in snapshot.children) {
                    val peminjaman = dataSnapshot.getValue(Peminjaman::class.java)
                    peminjaman?.let {
                        peminjamanList.add(it)
                    }
                }
                adapter.setData(peminjamanList)
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