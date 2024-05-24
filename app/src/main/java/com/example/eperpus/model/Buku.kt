package com.example.eperpus.model
import android.os.Parcel
import android.os.Parcelable

data class Buku(
    val uid: String? = null,
    var judul: String? = null,
    var kategori: String? = null,
    var halaman: String? = null,
    var deskripsi: String? = null,
    val photoUrl: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(judul)
        parcel.writeString(kategori)
        parcel.writeString(halaman)
        parcel.writeString(deskripsi)
        parcel.writeString(photoUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Buku> {
        override fun createFromParcel(parcel: Parcel): Buku {
            return Buku(parcel)
        }

        override fun newArray(size: Int): Array<Buku?> {
            return arrayOfNulls(size)
        }
    }
}
