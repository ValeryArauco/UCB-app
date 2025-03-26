//package com.example.data.persistence.database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.data.persistence.DAOs.UserDao
//import com.example.data.persistence.entities.UserEntity
//
//
//@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
//abstract class AppDatabase : RoomDatabase() {
//
//    abstract fun userDao(): UserDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//
//        @Synchronized
//        fun getDatabase(context: Context): AppDatabase {
//
//            val applicationContext = context.applicationContext
//
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    applicationContext,
//                    AppDatabase::class.java,
//                    "app_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}