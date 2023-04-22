package com.example.p1

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.time.LocalDate


data class Task (val photoPath: String,
                 val name: String,
                 val description: String,
                 val rating: Int,
                 val deadline: String)  : Serializable, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(photoPath)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(rating)
        parcel.writeString(deadline)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }

}



