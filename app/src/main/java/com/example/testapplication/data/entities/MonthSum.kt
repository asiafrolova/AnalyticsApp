package com.example.testapplication.data.entities

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.example.testapplication.services.GetRandomColor

class MonthSum: SumInterface {
    @ColumnInfo(name = "SUBSTR(noteDate,4,2)")
    var month:String = "01"
    @ColumnInfo(name = "SUM(noteSum)")
    var sum:Int = 0
    @Ignore
    val color:Color = GetRandomColor()
    override fun GetValue(): Int {
        return sum
    }

    override fun GetName(): String {
        return month
    }
    override fun GetColor(): Color {
        return color
    }
}