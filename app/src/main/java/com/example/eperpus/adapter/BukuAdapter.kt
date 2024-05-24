package com.example.eperpus.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eperpus.DetailBukuActivity
import com.example.eperpus.databinding.ItemRowBukuBinding
import com.example.eperpus.model.Buku
import com.google.firebase.database.FirebaseDatabase

class BukuAdapter(private val context: Context) : RecyclerView.Adapter<BukuAdapter.BukuViewHolder>() {

    private var bukuList: MutableList<Buku> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        val binding = ItemRowBukuBinding.inflate(LayoutInflater.from(context), parent, false)
        return BukuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        val buku = bukuList[position]
        holder.binding.tvBuku.text = buku.judul
        Glide.with(holder.itemView.context)
            .load(buku.photoUrl) // URL Gambar
            .into(holder.binding.imgItemBuku)

//         Delete button click
        holder.binding.btnHapus.setOnClickListener {
            // Remove data from Firebase
            Log.d("FirebaseDelete", "UID to delete: ${buku.uid}")
            buku.uid?.let { uid ->
                val database = FirebaseDatabase.getInstance("https://eperpus-b5004-default-rtdb.asia-southeast1.firebasedatabase.app/")
                val ref = database.getReference("buku").child(uid)
                ref.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FirebaseDelete", "Deletion successful")
                    } else {
                        Log.e("FirebaseDelete", "Error deleting item", task.exception)
                    }
                }
            }
        }

        // Detail button click (if you haven't implemented it yet)
        holder.binding.btnEdit.setOnClickListener {
            val intent = Intent(context, DetailBukuActivity::class.java)
            intent.putExtra("buku", buku)
            context.startActivity(intent)
        }
    }


    fun setData(updatedList: List<Buku>) {
        bukuList.clear()
        bukuList.addAll(updatedList)
        notifyDataSetChanged()
    }

    fun getData(): List<Buku> {
        return bukuList
    }


    override fun getItemCount(): Int {
        return bukuList.size
    }

    inner class BukuViewHolder(val binding: ItemRowBukuBinding) : RecyclerView.ViewHolder(binding.root)
}
