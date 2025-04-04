package com.example.testapplication.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.testapplication.data.entities.CategorySum
import com.example.testapplication.data.entities.DaySum
import com.example.testapplication.data.entities.MonthSum
import com.example.testapplication.data.entities.Note
import com.example.testapplication.data.entities.YearSum

@Dao
interface NoteDao{
    //Все Note отсортированные по дате
    @Query("SELECT * FROM notes  WHERE noteDate LIKE '__.__.____' ORDER BY noteDateSeconds DESC")
    fun getNotes(): LiveData<List<Note>>

    //Добавление Note
    @Insert
    fun addNote(note: Note)

    //Удаление Note
    @Query("DELETE FROM notes WHERE noteId = :id")
    fun deleteNote(id:Int)

    //Фильтрация Note по дате
    @Query("SELECT * FROM notes WHERE noteDate= :date")
    fun getNoteByDate(date:String): LiveData<List<Note>>

    //Запрос Note по id
    @Query("SELECT * FROM notes WHERE noteId=:id")
    fun getNoteById(id:Int): Note


    //Получение сум трат в категориях за определенный период
    @Query("SELECT noteCategory,SUM(noteSum) FROM notes WHERE noteDate LIKE :datePattern AND noteSum<0 GROUP BY noteCategory")
    fun getSumInCategoriesSpending(datePattern:String): List<CategorySum>

    //Получение сум поступлений в категориях за определенный период
    @Query("SELECT noteCategory,SUM(noteSum) FROM notes WHERE noteDate LIKE :datePattern AND noteSum>0 GROUP BY noteCategory")
    fun getSumInCategoriesIncome(datePattern:String): List<CategorySum>

    //Получение списка уникальных годов записей в базе данных
    @Query("SELECT SUBSTR(noteDate,7,4) FROM notes GROUP BY SUBSTR(noteDate,7,4)")
    fun getYearsInDatabase():LiveData<MutableList<String>>

    //Получение сум трат по месяцам за определенный период
    @Query("SELECT SUBSTR(noteDate,4,2),SUM(noteSum) FROM notes WHERE noteDate LIKE :datePattern AND noteSum<0 GROUP BY SUBSTR(noteDate,4,2)")
    fun getSumMonthSpend(datePattern:String): List<MonthSum>

    //Получение сум трат по дням за определенный период
    @Query("SELECT SUBSTR(noteDate,1,2),SUM(noteSum) FROM notes WHERE noteDate LIKE :datePattern AND noteSum<0 GROUP BY SUBSTR(noteDate,1,2)")
    fun getSumDaySpend(datePattern:String): List<DaySum>

    //Получение сум трат по годам за определенный период
    @Query("SELECT SUBSTR(noteDate,7,4),SUM(noteSum) FROM notes WHERE noteDate LIKE :datePattern AND noteSum<0 GROUP BY SUBSTR(noteDate,7,4)")
    fun getSumYearSpend(datePattern:String): List<YearSum>

    //Получение сум поступлений по месяцам за определенный период
    @Query("SELECT SUBSTR(noteDate,4,2),SUM(noteSum) FROM notes WHERE noteDate LIKE :datePattern AND noteSum>0 GROUP BY SUBSTR(noteDate,4,2)")
    fun getSumMonthIncome(datePattern:String): List<MonthSum>

    //Получение сум поступлений по дням за определенный период
    @Query("SELECT SUBSTR(noteDate,1,2),SUM(noteSum) FROM notes WHERE noteDate LIKE :datePattern AND noteSum>0 GROUP BY SUBSTR(noteDate,1,2)")
    fun getSumDayIncome(datePattern:String): List<DaySum>

    //Получение сум поступлений по годам за определенный период
    @Query("SELECT SUBSTR(noteDate,7,4),SUM(noteSum) FROM notes WHERE noteDate LIKE :datePattern AND noteSum>0 GROUP BY SUBSTR(noteDate,7,4)")
    fun getSumYearIncome(datePattern:String): List<YearSum>
}
