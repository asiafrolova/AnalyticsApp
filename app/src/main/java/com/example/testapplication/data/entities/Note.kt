package com.example.testapplication.data.entities

import android.icu.util.TimeZone
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testapplication.R
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

//Класс для работы с Room
@Entity(tableName = "notes")
class Note {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="noteId")
    var id:Int= 0
    @ColumnInfo(name="noteDate")
    var dateStr:String =LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

    @ColumnInfo(name="noteCategory")
    var category:String = "Other"

    @ColumnInfo(name="noteDescription")
    var description:String = ""
    @ColumnInfo(name="noteSum")
    var sum:Long = 0
    @ColumnInfo(name="noteDateSeconds")
    var dateSeconds:Long = LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC)
    constructor(){}
    constructor(date:String,category: String,description:String,sum:String){
        this.dateStr = date
        this.dateSeconds=LocalDate.parse(date,DateTimeFormatter.ofPattern("dd.MM.yyyy")).atStartOfDay().toEpochSecond(ZoneOffset.UTC)

        this.category=if (category.length>0) category.trim(' ') else "Другое"
        this.description=description.trim(' ')
        this.sum=if (sum.length>0) sum.toLong() else 0
    }


}
