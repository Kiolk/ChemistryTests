package com.github.kiolk.chemistrytests.data.database

import com.github.kiolk.chemistrytests.data.database.CourseDbModel.CourseDB.COURSE_ID
import com.github.kiolk.chemistrytests.data.database.CourseDbModel.CourseDB.COURSE_JSON
import com.github.kiolk.chemistrytests.data.database.CourseDbModel.CourseDB.TABLE_NAME

object CourseDbModel {
    object CourseDB {
        val COURSE_ID = "id"
        val COURSE_JSON = "course_json"
        val TABLE_NAME = "courses"
    }
    val SQL_CREATE_ENTRIES = """CREATE TABLE $TABLE_NAME (
        $COURSE_ID BIGINT PRIMARY KEY NOT NULL,
        $COURSE_JSON TEXT)""".trimMargin()

    val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

    val SQL_QUERY_ALL = "SELECT * FROM $TABLE_NAME ORDER BY $COURSE_ID DESC"
}