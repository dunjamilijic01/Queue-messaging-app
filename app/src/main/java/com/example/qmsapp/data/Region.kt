package com.example.qmsapp.data

import android.os.Parcel
import android.os.Parcelable

data class Region(val regionId: Int,
                  val regionName: String? ="",
                  val iconPath: String? ="") :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(regionId)
        parcel.writeString(regionName)
        parcel.writeString(iconPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Region> {
        override fun createFromParcel(parcel: Parcel): Region {
            return Region(parcel)
        }

        override fun newArray(size: Int): Array<Region?> {
            return arrayOfNulls(size)
        }
    }

}
