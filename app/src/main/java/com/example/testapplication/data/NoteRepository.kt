package com.example.testapplication.data

import androidx.lifecycle.LiveData
import com.example.testapplication.data.entities.CategorySum
import com.example.testapplication.data.entities.DaySum
import com.example.testapplication.data.entities.MonthSum
import com.example.testapplication.data.entities.Note
import com.example.testapplication.data.entities.YearSum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class NoteRepository(private val noteDao:NoteDao){
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    //Список всех note
    val noteList: LiveData<List<Note>> = noteDao.getNotes()
    //Список note относящихся к сегодняшней дате
    val todayNoteList: LiveData<List<Note>> = noteDao.getNoteByDate(LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("dd.MM.yyyy")))

    //val todayNoteList: LiveData<List<Note>> = noteDao.getNoteByDate(String.format("Date: ${SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().time)}"))
    //Список уникальных годов из базы данных
    val yearsInDatabase:LiveData<MutableList<String>> = noteDao.getYearsInDatabase()


    fun getSumInCategoriesSpending(datePattern:String):List<CategorySum>{
        return noteDao.getSumInCategoriesSpending(datePattern)

    }
    fun getSumInCategoriesIncome(datePattern:String):List<CategorySum>{
        return noteDao.getSumInCategoriesIncome(datePattern)

    }
    fun getDaySumSpend(datePattern:String):List<DaySum>{
        return  noteDao.getSumDaySpend(datePattern)
    }
    fun getMonthSumSpend(datePattern:String):List<MonthSum>{
        return  noteDao.getSumMonthSpend(datePattern)
    }
    fun getYearSumSpend(datePattern:String):List<YearSum>{
        return  noteDao.getSumYearSpend(datePattern)
    }

    fun getDaySumIncome(datePattern:String):List<DaySum>{
        return  noteDao.getSumDayIncome(datePattern)
    }
    fun getMonthSumIncome(datePattern:String):List<MonthSum>{
        return  noteDao.getSumMonthIncome(datePattern)
    }

    fun getYearIncome(datePattern:String):List<YearSum>{
        return  noteDao.getSumYearIncome(datePattern)
    }

    fun addNote(note: Note){
        coroutineScope.launch(Dispatchers.IO) {
            noteDao.addNote(note)
        }
    }
    fun getNoteById(id:Int): Note {

        return noteDao.getNoteById(id)


    }
    fun deleteNote(id:Int){
        coroutineScope.launch (Dispatchers.IO){
            noteDao.deleteNote(id)
        }
    }
}