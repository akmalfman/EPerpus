package com.example.eperpus.model
import android.os.Parcel
import android.os.Parcelable

data class Peminjaman(
    val uid: String? = null,
    val bukuId: String? = null,
    val judul: String? = null,
    val userName: String? = null,
    val photoUrl: String? = null,
    val kategori: String? = null,
    val halaman: String? = null,
    val durasi: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(bukuId)
        parcel.writeString(judul)
        parcel.writeString(userName)
        parcel.writeString(photoUrl)
        parcel.writeString(kategori)
        parcel.writeString(halaman)
        parcel.writeString(durasi)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Peminjaman> {
        override fun createFromParcel(parcel: Parcel): Peminjaman {
            return Peminjaman(parcel)
        }

        override fun newArray(size: Int): Array<Peminjaman?> {
            return arrayOfNulls(size)
        }
    }
}
