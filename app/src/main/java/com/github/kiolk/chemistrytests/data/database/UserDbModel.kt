package com.github.kiolk.chemistrytests.data.database

import com.github.kiolk.chemistrytests.data.database.UserDbModel.UserDB.USER_ID
import com.github.kiolk.chemistrytests.data.database.UserDbModel.UserDB.USER_JSON
import com.github.kiolk.chemistrytests.data.database.UserDbModel.UserDB.TABLE_NAME

object UserDbModel {
    object UserDB {
        val USER_ID = "id"
        val USER_JSON = "result_json"
        val TABLE_NAME = "resultInformation"
    }
    val SQL_CREATE_ENTRIES = """CREATE TABLE $TABLE_NAME (
        $USER_ID TEXT PRIMARY KEY NOT NULL,
        $USER_JSON TEXT)""".trimMargin()

    val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

    val SQL_QUERY_ALL = "SELECT * FROM $TABLE_NAME ORDER BY $USER_ID DESC"
}