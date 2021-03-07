package com.example.contactsscroll.data.repositories

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.contactsscroll.DbHelper
import com.example.contactsscroll.data.objects.ContactType

class ContactTypeRepository(val context: Context) {

    private lateinit var dbHelper: DbHelper
    private lateinit var db: SQLiteDatabase

    fun open(): ContactTypeRepository {
        dbHelper = DbHelper(context)
        db = dbHelper.writableDatabase

        return this
    }

    fun close() {
        dbHelper.close()
    }


    fun add(contactType: ContactType) {
        val contentValues = ContentValues()
        contentValues.put(DbHelper.CONTACT_TYPE, contactType.contactType)
        contentValues.put(DbHelper.PERSON_ID, contactType.personId)
        contentValues.put(DbHelper.CONTACT_ID, contactType.contactId)
        db.insert(DbHelper.CONTACT_TYPE_TABLE_NAME, null, contentValues)
    }


    fun getAll(): List<ContactType> {
        val contactTypes = ArrayList<ContactType>()

        val columns =
            arrayOf(
                DbHelper.CONTACT_TYPE_ID,
                DbHelper.CONTACT_TYPE,
                DbHelper.CONTACT_ID,
                DbHelper.PERSON_ID
            )


        val cursor =
            db.query(DbHelper.CONTACT_TYPE_TABLE_NAME, columns, null, null, null, null, null)

        while (cursor.moveToNext()) {
            contactTypes.add(
                ContactType(
                    cursor.getInt(cursor.getColumnIndex(DbHelper.CONTACT_TYPE_ID)),
                    cursor.getString(cursor.getColumnIndex(DbHelper.CONTACT_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(DbHelper.CONTACT_ID)),
                    cursor.getInt(cursor.getColumnIndex(DbHelper.PERSON_ID))
                )
            )
        }

        cursor.close()

        return contactTypes
    }

    fun removeContactById(contact_type_id: Int, contact_id: Int) {
        db.execSQL("DELETE FROM ${DbHelper.CONTACT_TABLE_NAME} WHERE ${DbHelper.CONTACT_TABLE_NAME}.${DbHelper.CONTACT_ID} = $contact_id;")
        db.execSQL("DELETE FROM ${DbHelper.CONTACT_TYPE_TABLE_NAME} WHERE ${DbHelper.CONTACT_TYPE_TABLE_NAME}.${DbHelper.CONTACT_TYPE_ID} = $contact_type_id;")

    }

    fun removePersonContacts(person_id: Int) {
        for (contactType in getAll()) {
            if (contactType.personId == person_id) {
                db.execSQL("DELETE FROM ${DbHelper.CONTACT_TABLE_NAME} WHERE ${DbHelper.CONTACT_TABLE_NAME}.${DbHelper.CONTACT_ID} = ${contactType.contactId};")
                db.execSQL("DELETE FROM ${DbHelper.CONTACT_TYPE_TABLE_NAME} WHERE ${DbHelper.CONTACT_TYPE_TABLE_NAME}.${DbHelper.CONTACT_TYPE_ID} = ${contactType.id};")
            }
        }
    }

    fun editById(id: Int, contact_type: String) {
        val values = ContentValues();
        values.put(DbHelper.CONTACT_TYPE, contact_type)
        db.update(
            DbHelper.CONTACT_TYPE_TABLE_NAME,
            values,
            "${DbHelper.CONTACT_TYPE_ID}=$id",
            null
        );
    }

}