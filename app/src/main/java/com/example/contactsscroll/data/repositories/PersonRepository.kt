package com.example.contactsscroll.data.repositories;


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.contactsscroll.DbHelper
import com.example.contactsscroll.data.objects.Person

class PersonRepository(val context: Context) {

    private lateinit var dbHelper: DbHelper
    private lateinit var db: SQLiteDatabase

    fun open(): PersonRepository {
        dbHelper = DbHelper(context)
        db = dbHelper.writableDatabase
/*
        dbHelper.onCreatTe(db)
*/
        return this
    }

    fun close() {
        dbHelper.close()
    }

    fun add(person: Person) {
        val contentValues = ContentValues()
        contentValues.put(DbHelper.PERSON_FIRSTNAME, person.firstName)
        contentValues.put(DbHelper.PERSON_LASTNAME, person.lastName)
        db.insert(DbHelper.PERSONS_TABLE_NAME, null, contentValues)
    }

    fun getAll(): List<Person> {
        val persons = ArrayList<Person>()

        val columns =
            arrayOf(DbHelper.PERSON_ID, DbHelper.PERSON_FIRSTNAME, DbHelper.PERSON_LASTNAME)


        val cursor = db.query(DbHelper.PERSONS_TABLE_NAME, columns, null, null, null, null, null)

        while (cursor.moveToNext()) {
            persons.add(
                Person(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(cursor.getColumnIndex(DbHelper.PERSON_LASTNAME))
                )
            )
        }

        cursor.close()

        return persons
    }

    fun getById(id: Int): Person {

        for (person in getAll()) {
            if (person.id == id) {
                return person
            }
        }
        return Person("", "")
    }

    fun removeById(id: Int) {
        db.execSQL("DELETE FROM ${DbHelper.PERSONS_TABLE_NAME} WHERE ${DbHelper.PERSONS_TABLE_NAME}.${DbHelper.PERSON_ID}=$id;")

    }

    fun editById(id: Int, firstName: String, lastName: String) {
        val values = ContentValues();
        values.put(DbHelper.PERSON_FIRSTNAME, firstName)
        values.put(DbHelper.PERSON_LASTNAME, lastName)
        db.update(DbHelper.PERSONS_TABLE_NAME, values, "${DbHelper.PERSON_ID}=$id", null);
    }

}