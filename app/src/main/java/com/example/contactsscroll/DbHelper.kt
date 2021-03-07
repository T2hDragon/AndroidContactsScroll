package com.example.contactsscroll;

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "app.db"
        const val DATABASE_VERSION = 1

        const val PERSONS_TABLE_NAME = "PERSONS"
        const val CONTACT_TABLE_NAME = "CONTACT"
        const val CONTACT_TYPE_TABLE_NAME = "CONTACT_TYPE"


        const val PERSON_ID = "person_Id"
        const val PERSON_FIRSTNAME = "firstName"
        const val PERSON_LASTNAME = "lastName"
        const val CONTACT_TYPE_ID = "contact_type_id"
        const val CONTACT_TYPE = "contactType"
        const val CONTACT_ID = "contact_id"
        const val CONTACT = "contact"


        const val SQL_PERSON_CREATE_TABLE =
            "create table $PERSONS_TABLE_NAME (" +
                    "$PERSON_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$PERSON_FIRSTNAME TEXT NOT NULL, " +
                    "$PERSON_LASTNAME TEXT NOT NULL);"

        const val SQL_CONTACT_TYPE_CREATE_TABLE =
            "create table $CONTACT_TYPE_TABLE_NAME (" +
                    "$CONTACT_TYPE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$PERSON_ID INTEGER, " +
                    "$CONTACT_TYPE TEXT NOT NULL," +
                    "$CONTACT_ID INTEGER," +
                    "FOREIGN KEY($CONTACT_ID)  REFERENCES $CONTACT_TABLE_NAME($CONTACT_ID));" +
                    "FOREIGN KEY($PERSON_ID)  REFERENCES $PERSONS_TABLE_NAME($PERSON_ID));"


        const val SQL_CONTACT_CREATE_TABLE =
            "create table $CONTACT_TABLE_NAME (" +
                    "$CONTACT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$CONTACT TEXT NOT NULL);"


        const val SQL_DELETE_PERSON_TABLES = "DROP TABLE IF EXISTS $PERSONS_TABLE_NAME;"
        const val SQL_DELETE_CONTACT_TYPE_TABLES = "DROP TABLE IF EXISTS $CONTACT_TYPE_TABLE_NAME;"
        const val SQL_DELETE_CONTACT_TABLES = "DROP TABLE IF EXISTS $CONTACT_TABLE_NAME;"
    }

    fun onCreatTe(db: SQLiteDatabase?) {
        db?.execSQL(SQL_DELETE_PERSON_TABLES)
        db?.execSQL(SQL_DELETE_CONTACT_TYPE_TABLES)
        db?.execSQL(SQL_DELETE_CONTACT_TABLES)
        db?.execSQL(SQL_DELETE_CONTACT_TABLES)
        db?.execSQL(SQL_PERSON_CREATE_TABLE)
        db?.execSQL(SQL_CONTACT_TYPE_CREATE_TABLE)
        db?.execSQL(SQL_CONTACT_CREATE_TABLE)
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_PERSON_CREATE_TABLE)
        db?.execSQL(SQL_CONTACT_TYPE_CREATE_TABLE)
        db?.execSQL(SQL_CONTACT_CREATE_TABLE)

    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_PERSON_TABLES)
        db?.execSQL(SQL_DELETE_CONTACT_TYPE_TABLES)
        db?.execSQL(SQL_DELETE_CONTACT_TABLES)
        onCreate(db)
    }


}