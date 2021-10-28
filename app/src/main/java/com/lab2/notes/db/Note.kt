package com.lab2.notes.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
@Entity(tableName = "Notes")
class Note(
    var title: String,
    var date_time: String,
    var text: String? = ""
): Parcelable {
    @PrimaryKey(autoGenerate = true) var id: Int? = null

//    override fun toString(): String {
//        return "$title: $dateTime"
//    }
}
