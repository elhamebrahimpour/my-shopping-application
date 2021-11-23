package com.elham.shoppingproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elham.shoppingproject.model.Product

@Database(entities = [Product::class], exportSchema = false, version = 1)
abstract class Database: RoomDatabase() {
    abstract fun productDao():ProductDao
    companion object{
        private const val DB_NAME:String="Database.db"
        var instance:com.elham.shoppingproject.database.Database?=null
        fun getInstance(context: Context?):com.elham.shoppingproject.database.Database{
            if (instance!=null) return instance!!
            synchronized(this){
                instance= Room
                    .databaseBuilder(context!!.applicationContext,
                        com.elham.shoppingproject.database.Database::class.java , DB_NAME)
                    .fallbackToDestructiveMigration().build()
                return instance!!
            }
        }
    }
}