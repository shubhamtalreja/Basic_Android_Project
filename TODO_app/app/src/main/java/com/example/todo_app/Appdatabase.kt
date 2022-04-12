package com.example.todo_app

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [Tododb::class], version = 1)
abstract class Appdatabase: RoomDatabase(){
    abstract fun tododao() :TodoDao
}