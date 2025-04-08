package com.example.testapplication.data

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplication.BuildConfig
import com.example.testapplication.data.entities.CategorySum
import com.example.testapplication.data.entities.DaySum
import com.example.testapplication.data.entities.MonthSum
import com.example.testapplication.data.entities.Note
import com.example.testapplication.data.entities.YearSum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFFont
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Field


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

    var baseApplication:Application = application

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

    //Функция экспорта в Exel
    suspend fun exportToExcel() {
        try {

            val workbook = XSSFWorkbook()
            val folder = File(baseApplication.filesDir, "exported_files")
            //проверка существует ли папка
            if (!folder.exists()) {
                folder.mkdirs()
            }
            exportNotes(workbook)
            val file = File(folder, "notes.xlsx")
            try {
                //запись workbook в файл
                withContext(Dispatchers.IO) {
                    FileOutputStream(file).use { outputStream ->
                        workbook.write(outputStream)
                    }

                    //Получение uri файла
                    val fileUri = FileProvider.getUriForFile(
                        baseApplication.applicationContext,
                        BuildConfig.APPLICATION_ID + ".fileprovider",
                        file
                    )

                    //Открытик файла
                    val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(fileUri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }


                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        putExtra(Intent.EXTRA_STREAM, fileUri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }

                    val chooserIntent =
                        Intent.createChooser(
                            shareIntent,
                            "Open or Share File"
                        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)



                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(viewIntent))

                    baseApplication.startActivity(chooserIntent)

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun exportNotes(workbook: XSSFWorkbook) {
        val sheet = workbook.createSheet("Notes")

        //Имена полей для экспорта
        val preferredNoteFields = arrayOf(
            "dateStr",
            "category",
            "description",
            "sum"
        )

        //Все поля класса Note
        val allNoteFields = Note::class.java.declaredFields
        //Фильтрация полей для экспорта
        val noteFieldsToExport = arrayOfNulls<Field>(preferredNoteFields.size)

        for (i in preferredNoteFields.indices) {
            for (field in allNoteFields) {
                if (field.name == preferredNoteFields[i]) {
                    field.isAccessible = true
                    noteFieldsToExport[i] = field
                    break
                }
            }
        }

        // добавления заголовков
        val headerRow = sheet.createRow(0)
        val headerStyle = createHeaderStyle(workbook)
        for (i in noteFieldsToExport.indices) {
            val cell = headerRow.createCell(i)
            cell.setCellValue(noteFieldsToExport[i]?.name)
            cell.cellStyle = headerStyle
        }


        val notes:List<Note> = repository.noteList.value?: listOf()
        var rowNum = 1
        for (note in notes) {
            val row = sheet.createRow(rowNum++)
            for (i in noteFieldsToExport.indices) {
                try {

                    val value = noteFieldsToExport[i]?.get(note)
                    row.createCell(i).setCellValue(value.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }
    //Загрузка резервной копии из excel
    fun importFromExcel(uri:Uri):Boolean{

        try {
            val inputStream = baseApplication.contentResolver.openInputStream(uri)
            val workbook = WorkbookFactory.create(inputStream)
            for (sheet in workbook) {
                if (sheet.sheetName == "Notes") {
                    for (row in sheet) {
                        if (row.rowNum == 0) {
                            continue
                        }
                        if (row.getCell(0) == null || row.getCell(0).cellType == CellType.BLANK) {
                            break
                        }
                        val dateStr = row.getCell(0).stringCellValue
                        val category = row.getCell(1).stringCellValue
                        val description = row.getCell(2).stringCellValue
                        val sum = row.getCell(3).stringCellValue
                        repository.addNote(
                            Note(
                                date = dateStr,
                                category = category,
                                description = description,
                                sum = sum
                            )
                        )
                    }
                }
            }
            return true
        }catch (e:Exception){
            return false
        }
    }
    fun createHeaderStyle(workbook: XSSFWorkbook): XSSFCellStyle {
        // Создание заголовка
        val headerStyle: XSSFCellStyle = workbook.createCellStyle()

        // Установка цвета фона
        headerStyle.fillForegroundColor = IndexedColors.LIGHT_BLUE.index
        headerStyle.fillPattern = FillPatternType.SOLID_FOREGROUND

        // Установка цвета шрифта
        val font: XSSFFont = workbook.createFont()
        font.bold = true
        font.color = IndexedColors.WHITE.index
        headerStyle.setFont(font)

        // Уставнока ориентации
        headerStyle.alignment = HorizontalAlignment.CENTER
        headerStyle.verticalAlignment = VerticalAlignment.CENTER

        return headerStyle
    }

}