package com.github.kiolk.chemistrytests.data.database

import android.content.ContentValues
import android.database.Cursor
import com.github.kiolk.chemistrytests.data.database.QuestionDBModel.QuestionDB.QUESTION_ID
import com.github.kiolk.chemistrytests.data.database.QuestionDBModel.QuestionDB.QUESTION_JSON
import com.github.kiolk.chemistrytests.data.database.QuestionDBModel.QuestionDB.TABLE_NAME
import com.github.kiolk.chemistrytests.data.database.TestParamsDBModel.TestParamsDB.TEST_PARAMS_ID
import com.github.kiolk.chemistrytests.data.database.TestParamsDBModel.TestParamsDB.TEST_PARAMS_JSON
import com.github.kiolk.chemistrytests.data.models.CloseQuestion
import com.github.kiolk.chemistrytests.data.models.TestParams
import com.google.gson.Gson

class DBOperations {
    private val helper: DBConnector = DBConnector.instance!!

    fun getAllQuestions(): MutableList<CloseQuestion> {
        return helper.readableDatabase.query(QuestionDBModel.QuestionDB.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null).use { cursor -> this.allQuestionFromCursor(cursor) }
    }


    private fun allQuestionFromCursor(cursor: Cursor): MutableList<CloseQuestion> {
        val list = mutableListOf<CloseQuestion>()
        while (cursor.moveToNext()) {
            list.add(questionFromCursor(cursor))
        }
        cursor.close()
        return list
    }

    private fun questionFromCursor(cursor: Cursor): CloseQuestion {
        val json = cursor.getString(cursor.getColumnIndex(QuestionDBModel.QuestionDB.QUESTION_JSON))
        val ob: CloseQuestion = Gson().fromJson(json, CloseQuestion::class.java)
        return ob

    }
//
//    fun insertArray(notes: Array<DayNoteModel>) {
//        fromNotes(notes).forEach {
//            val readableDatabase = helper.readableDatabase
//            try {
//                readableDatabase.beginTransaction()
//                readableDatabase.insert(TABLE_NAME, null, it)
//                readableDatabase.setTransactionSuccessful()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            } finally {
//                readableDatabase.endTransaction()
//            }
//        }
//    }

    fun insertQuestion(question: CloseQuestion) {
        val contentValue = fromQuestion(question)
        val readableDatabase = helper.readableDatabase
        try {
            readableDatabase.beginTransaction()
            readableDatabase.replace(TABLE_NAME, null, contentValue)
            readableDatabase.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            readableDatabase.endTransaction()
        }
    }

    fun getAllTestsParams(): MutableList<TestParams> {
        return helper.readableDatabase.query(TestParamsDBModel.TestParamsDB.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null).use { cursor -> this.allTestParamsFromCursor(cursor) }
    }


    private fun allTestParamsFromCursor(cursor: Cursor): MutableList<TestParams> {
        val list = mutableListOf<TestParams>()
        while (cursor.moveToNext()) {
            list.add(testParamsFromCursor(cursor))
        }
        cursor.close()
        return list
    }


    private fun testParamsFromCursor(cursor: Cursor): TestParams {
        val json = cursor.getString(cursor.getColumnIndex(TestParamsDBModel.TestParamsDB.TEST_PARAMS_JSON))
        val ob : TestParams = Gson().fromJson(json, TestParams::class.java)
        return ob
    }


    fun insertTest(testParams: TestParams) {
        val contentValue = fromTestParams(testParams)
        val readableDatabase = helper.readableDatabase
        try {
            readableDatabase.beginTransaction()
            readableDatabase.replace(TestParamsDBModel.TestParamsDB.TABLE_NAME, null, contentValue)
            readableDatabase.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            readableDatabase.endTransaction()
        }
    }

    private fun fromTestParams(testParams: TestParams): ContentValues {
        return ContentValues().apply {
            put(TEST_PARAMS_ID, testParams.testId)
            put(TEST_PARAMS_JSON, Gson().toJson(testParams))
        }
    }


//    private fun fromNotes(notes: Array<out DayNoteModel>): List<ContentValues> {
//        return notes.map(this::fromDayNote)
//    }


    private fun fromQuestion(question: CloseQuestion): ContentValues {
        return ContentValues().apply {
            put(QUESTION_ID, question.questionId)
            put(QUESTION_JSON, Gson().toJson(question))
        }
    }
}