package com.example.p1

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import kotlinx.datetime.*


data class Task (val id: String,
                 var photoPath: String,
                 var name: String,
                 var description: String,
                 var rating: Int,
                 var deadline: String,
                 var dateAdded: Instant = Clock.System.now())  : Serializable, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
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



