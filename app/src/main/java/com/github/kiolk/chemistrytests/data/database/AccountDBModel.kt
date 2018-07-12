package com.github.kiolk.chemistrytests.data.database

import com.github.kiolk.chemistrytests.data.database.AccountDBModel.AccountDB.ACCOUNT_ID
import com.github.kiolk.chemistrytests.data.database.AccountDBModel.AccountDB.ACCOUNT_JSON
import com.github.kiolk.chemistrytests.data.database.AccountDBModel.AccountDB.TABLE_NAME

object AccountDBModel {
    object AccountDB {
        val ACCOUNT_ID = "id"
        val ACCOUNT_JSON = "account_json"
        val TABLE_NAME = "accounts"
    }
    val SQL_CREATE_ENTRIES = """CREATE TABLE $TABLE_NAME (
        $ACCOUNT_ID BIGINT PRIMARY KEY NOT NULL,
        $ACCOUNT_JSON TEXT)""".trimMargin()

    val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

    val SQL_QUERY_ALL = "SELECT * FROM $TABLE_NAME ORDER BY $ACCOUNT_ID DESC"
}