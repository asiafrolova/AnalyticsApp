package com.example.testapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testapplication.data.entities.Note

//База данных для работы с Room
@Database(entities = [(Note::class)], version = 1)
abstract class NoteRoomDatabase: RoomDatabase(){
    abstract fun noteDao(): NoteDao

    companion object{
        private var INSTANCE :NoteRoomDatabase? = null
        fun getInstance(context:Context): NoteRoomDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteRoomDatabase::class.java,
                        "notesdb"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE=instance

                }
                return instance
            }
        }
    }

}