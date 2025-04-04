package com.example.testapplication.data

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplication.data.entities.CategorySum
import com.example.testapplication.data.entities.DaySum
import com.example.testapplication.data.entities.MonthSum
import com.example.testapplication.data.entities.Note
import com.example.testapplication.data.entities.YearSum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteViewModel(application:Application):ViewModel() {
    //Список всех note
    val noteList:LiveData<List<Note>>
    //Список note относящихся к сегодняшней дате
    val todayNoteList:LiveData<List<Note>>

    //Список уникальных годов из базы данных
    val yearsInDatabase:LiveData<MutableList<String>>

    private val repository:NoteRepository
    //note для обработки нажатия на отдельную note и получения информации о ней
    var note by mutableStateOf(Note())

    //Данные о тратах и поступлениях по категориям за определнный период
    private  val _allNotesSumCategoriesSpend =MutableStateFlow<List<CategorySum>>(arrayListOf())
    val allNotesSumCategoriesSpend get() = _allNotesSumCategoriesSpend.asStateFlow()
    private  val _allNotesSumCategoriesIncome =MutableStateFlow<List<CategorySum>>(arrayListOf())
    val allNotesSumCategoriesIncome get() = _allNotesSumCategoriesIncome.asStateFlow()

    //Данные о тратах и поступлениях по месяцам за определнный период
    private  val _SumMonthSpend =MutableStateFlow<List<MonthSum>>(arrayListOf())
    val SumMonthSpend get() = _SumMonthSpend.asStateFlow()
    private  val _SumMonthIncome =MutableStateFlow<List<MonthSum>>(arrayListOf())
    val SumMonthIncome get() = _SumMonthIncome.asStateFlow()

    //Данные о тратах и поступлениях по дням за определнный период
    private  val _SumDaySpend =MutableStateFlow<List<DaySum>>(arrayListOf())
    val SumDaySpend get() = _SumDaySpend.asStateFlow()
    private  val _SumDayIncome =MutableStateFlow<List<DaySum>>(arrayListOf())
    val SumDayIncome get() = _SumDayIncome.asStateFlow()

    //Данные о тратах и поступлениях по годам за определнный период
    private  val _SumYearSpend =MutableStateFlow<List<YearSum>>(arrayListOf())
    val SumYearSpend get() = _SumYearSpend.asStateFlow()
    private  val _SumYearIncome =MutableStateFlow<List<YearSum>>(arrayListOf())
    val SumYearIncome get() = _SumYearIncome.asStateFlow()



    init{
        val noteDb = NoteRoomDatabase.getInstance(application)
        val noteDao = noteDb.noteDao()
        repository = NoteRepository(noteDao)
        noteList = repository.noteList
        todayNoteList = repository.todayNoteList
        yearsInDatabase = repository.yearsInDatabase
    }
    fun getNoteById(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            note=repository.getNoteById(id)
        }
    }
    fun addNote(note: Note){
        repository.addNote(note)
    }
    fun deleteNote(id:Int){
        repository.deleteNote(id)
    }
    fun getSumInCategoriesSpend(datePattern:String){
        viewModelScope.launch (Dispatchers.IO){
            _allNotesSumCategoriesSpend.value=repository.getSumInCategoriesSpending(datePattern)
        }
    }
    fun getSumInCategoriesIncome(datePattern:String){
        viewModelScope.launch (Dispatchers.IO){
            _allNotesSumCategoriesIncome.value=repository.getSumInCategoriesIncome(datePattern)
        }
    }

    fun getSumInYearSpend(datePattern:String){
        viewModelScope.launch (Dispatchers.IO){
            _SumYearSpend.value=repository.getYearSumSpend(datePattern)
        }
    }
    fun getSumInYearIncome(datePattern:String){
        viewModelScope.launch (Dispatchers.IO){
            _SumYearIncome.value=repository.getYearIncome(datePattern)
        }
    }

    fun getSumInMonthSpend(datePattern:String){
        viewModelScope.launch (Dispatchers.IO){
            _SumMonthSpend.value=repository.getMonthSumSpend(datePattern)
        }
    }
    fun getSumInMonthIncome(datePattern:String){
        viewModelScope.launch (Dispatchers.IO){
            _SumMonthIncome.value=repository.getMonthSumIncome(datePattern)
        }
    }

    fun getSumInDaySpend(datePattern:String){
        viewModelScope.launch (Dispatchers.IO){
            _SumDaySpend.value=repository.getDaySumSpend(datePattern)
        }
    }
    fun getSumInDayIncome(datePattern:String){
        viewModelScope.launch (Dispatchers.IO){
            _SumDayIncome.value=repository.getDaySumIncome(datePattern)
        }
    }
}