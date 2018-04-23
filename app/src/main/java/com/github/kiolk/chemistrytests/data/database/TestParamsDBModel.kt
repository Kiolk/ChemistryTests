package com.github.kiolk.chemistrytests.data.database

import com.github.kiolk.chemistrytests.data.database.TestParamsDBModel.TestParamsDB.TABLE_NAME
import com.github.kiolk.chemistrytests.data.database.TestParamsDBModel.TestParamsDB.TEST_PARAMS_ID
import com.github.kiolk.chemistrytests.data.database.TestParamsDBModel.TestParamsDB.TEST_PARAMS_JSON

object TestParamsDBModel {
    object TestParamsDB {
        val TEST_PARAMS_ID = "id"
        val TEST_PARAMS_JSON = "params_json"
        val TABLE_NAME = "params"
    }
    val SQL_CREATE_ENTRIES = """CREATE TABLE $TABLE_NAME (
        $TEST_PARAMS_ID BIGINT PRIMARY KEY NOT NULL,
        $TEST_PARAMS_JSON TEXT)""".trimMargin()

    val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

    val SQL_QUERY_ALL = "SELECT * FROM $TABLE_NAME ORDER BY $TEST_PARAMS_ID DESC"
}