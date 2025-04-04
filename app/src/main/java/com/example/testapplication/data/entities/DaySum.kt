package com.example.testapplication.data.entities

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.example.testapplication.services.GetRandomColor

class DaySum : SumInterface {
        @ColumnInfo(name = "SUBSTR(noteDate,1,2)")
        var day:String = "01"
        @ColumnInfo(name = "SUM(noteSum)")
        var sum:Int = 0
    @Ignore
    val color:Color = GetRandomColor()
    override fun GetValue(): Int {
        return sum
    }

    override fun GetName(): String {
        return day
    }
    override fun GetColor(): Color {
        return color
    }

}