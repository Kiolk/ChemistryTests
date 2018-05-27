package com.github.kiolk.chemistrytests.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBConnector private constructor(context : Context) : SQLiteOpenHelper(context, appDbName, null, dbVersion ){

    companion object {
        private val appDbName = "ChemistryTests"
        private val dbVersion = 1
        var instance : DBConnector? = null

        fun initInstance(pContext: Context) {
            if(instance == null){
               instance = DBConnector(pContext)
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(QuestionDBModel.SQL_CREATE_ENTRIES)
        db?.execSQL(TestParamsDBModel.SQL_CREATE_ENTRIES)
        db?.execSQL(UserDbModel.SQL_CREATE_ENTRIES)
        db?.execSQL(CourseDbModel.SQL_CREATE_ENTRIES)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(QuestionDBModel.SQL_DELETE_ENTRIES)
        db?.execSQL(TestParamsDBModel.SQL_DELETE_ENTRIES)
        db?.execSQL(UserDbModel.SQL_DELETE_ENTRIES)
        db?.execSQL(CourseDbModel.SQL_DELETE_ENTRIES)

    }
}

