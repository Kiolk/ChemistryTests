package com.github.kiolk.chemistrytests.data.database

import com.github.kiolk.chemistrytests.data.database.QuestionDBModel.QuestionDB.QUESTION_ID
import com.github.kiolk.chemistrytests.data.database.QuestionDBModel.QuestionDB.QUESTION_JSON
import com.github.kiolk.chemistrytests.data.database.QuestionDBModel.QuestionDB.TABLE_NAME

object QuestionDBModel{
    object QuestionDB {
        val QUESTION_ID = "id"
        val QUESTION_JSON = "question_json"
        val TABLE_NAME = "questions"
    }
    val SQL_CREATE_ENTRIES = """CREATE TABLE $TABLE_NAME (
        $QUESTION_ID BIGINT PRIMARY KEY NOT NULL,
        $QUESTION_JSON TEXT)""".trimMargin()

    val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

    val SQL_QUERY_ALL = "SELECT * FROM $TABLE_NAME ORDER BY $QUESTION_ID DESC"
}