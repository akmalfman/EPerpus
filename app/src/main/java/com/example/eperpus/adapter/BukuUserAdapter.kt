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
import com.example.eperpus.DetailPinjamActivity
import com.example.eperpus.databinding.ItemRowBukuBinding
import com.example.eperpus.databinding.ItemRowBukuUserBinding
import com.example.eperpus.model.Buku
import com.google.firebase.database.FirebaseDatabase

class BukuUserAdapter(private val context: Context) : RecyclerView.Adapter<BukuUserAdapter.BukuViewHolder>() {

    private var bukuList: MutableList<Buku> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        val binding = ItemRowBukuUserBinding.inflate(LayoutInflater.from(context), parent, false)
        return BukuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        val buku = bukuList[position]
        holder.binding.tvBuku.text = buku.judul
        Glide.with(holder.itemView.context)
            .load(buku.photoUrl) // URL Gambar
            .into(holder.binding.imgItemBuku)

        // Detail button click (if you haven't implemented it yet)
        holder.binding.btnPinjam.setOnClickListener {
            val intent = Intent(context, DetailPinjamActivity::class.java)
            intent.putExtra("bukuId", buku.uid)
            intent.putExtra("bukuJudul", buku.judul)
            intent.putExtra("bukuPhotoUrl", buku.photoUrl)
            intent.putExtra("bukuKategori", buku.kategori)
            intent.putExtra("bukuHalaman", buku.halaman)
            context.startActivity(intent)
        }
    }


    fun setData(updatedList: List<Buku>) {
        bukuList.clear()
        bukuList.addAll(updatedList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return bukuList.size
    }

    inner class BukuViewHolder(val binding: ItemRowBukuUserBinding) : RecyclerView.ViewHolder(binding.root)
}
