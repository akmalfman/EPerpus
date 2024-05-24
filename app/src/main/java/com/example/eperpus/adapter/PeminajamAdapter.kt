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
import com.example.eperpus.databinding.ItemRowPeminjamanBinding
import com.example.eperpus.model.Buku
import com.example.eperpus.model.Peminjaman
import com.google.firebase.database.FirebaseDatabase

class PeminajamAdapter(private val context: Context) : RecyclerView.Adapter<PeminajamAdapter.BukuViewHolder>() {

    private var peminjamanList: MutableList<Peminjaman> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        val binding = ItemRowPeminjamanBinding.inflate(LayoutInflater.from(context), parent, false)
        return BukuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        val peminjaman = peminjamanList[position]
        holder.binding.tvBuku.text = peminjaman.judul
        holder.binding.tvDurasi.text = peminjaman.durasi
        holder.binding.tvUsername.text = peminjaman.userName
        Glide.with(holder.itemView.context)
            .load(peminjaman.photoUrl) // URL Gambar
            .into(holder.binding.imgItemBuku)

        Log.e("peminajamList", "onBindViewHolder: $peminjaman", )


    }


    fun setData(updatedList: List<Peminjaman>) {
        peminjamanList.clear()
        peminjamanList.addAll(updatedList)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return peminjamanList.size
    }

    inner class BukuViewHolder(val binding: ItemRowPeminjamanBinding) : RecyclerView.ViewHolder(binding.root)
}
