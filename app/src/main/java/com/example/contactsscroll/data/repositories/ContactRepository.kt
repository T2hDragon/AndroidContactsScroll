package com.example.contactsscroll.data.repositories

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.contactsscroll.DbHelper
import com.example.contactsscroll.data.objects.Contact

class ContactRepository(val context: Context) {

    private lateinit var dbHelper: DbHelper
    private lateinit var db: SQLiteDatabase

    fun open(): ContactRepository {
        dbHelper = DbHelper(context)
        db = dbHelper.writableDatabase
        return this
    }

    fun close() {
        dbHelper.close()
    }

    fun add(contact: Contact) {
        val contentValues = ContentValues()
        contentValues.put(DbHelper.CONTACT, contact.contact)
        db.insert(DbHelper.CONTACT_TABLE_NAME, null, contentValues)
    }


    fun getAll(): List<Contact> {
        val contacts = ArrayList<Contact>()

        val columns =
            arrayOf(DbHelper.CONTACT_ID, DbHelper.CONTACT)


        val cursor = db.query(DbHelper.CONTACT_TABLE_NAME, columns, null, null, null, null, null)

        while (cursor.moveToNext()) {
            contacts.add(
                Contact(
                    cursor.getInt(0),
                    cursor.getString(1)
                )
            )
        }

        cursor.close()

        return contacts
    }


    fun editById(id: Int, contact: String) {
        val values = ContentValues();
        values.put(DbHelper.CONTACT, contact)
        db.update(DbHelper.CONTACT_TABLE_NAME, values, "${DbHelper.CONTACT_ID}=$id", null);
    }


}