package com.github.kiolk.chemistrytests.data.database

import com.github.kiolk.chemistrytests.data.database.ChemTheoryDBModel.ChemTheoryDB.CHEM_THEORY_ID
import com.github.kiolk.chemistrytests.data.database.ChemTheoryDBModel.ChemTheoryDB.CHEM_THEORY_JSON
import com.github.kiolk.chemistrytests.data.database.ChemTheoryDBModel.ChemTheoryDB.TABLE_NAME

object ChemTheoryDBModel {
    object ChemTheoryDB {
        val CHEM_THEORY_ID = "id"
        val CHEM_THEORY_JSON = "chem_theory_json"
        val TABLE_NAME = "theory"
    }
    val SQL_CREATE_ENTRIES = """CREATE TABLE $TABLE_NAME (
        $CHEM_THEORY_ID BIGINT PRIMARY KEY NOT NULL,
        $CHEM_THEORY_JSON TEXT)""".trimMargin()

    val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

    val SQL_QUERY_ALL = "SELECT * FROM $TABLE_NAME ORDER BY $CHEM_THEORY_ID DESC"
}