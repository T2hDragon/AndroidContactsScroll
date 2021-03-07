package com.example.contactsscroll

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.contactsscroll.data.repositories.ContactRepository
import com.example.contactsscroll.data.repositories.ContactTypeRepository
import kotlinx.android.synthetic.main.add_contact_dialog.*

class ContactTypeActivity : AppCompatActivity() {

    private lateinit var contactTypeRepo: ContactTypeRepository
    private lateinit var contactRepo: ContactRepository


    var contactTypeId: Int? = null
    var contactId: Int? = null
    var contactType: String? = null
    var contact: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_type_activity)

        contactTypeRepo = ContactTypeRepository(this).open()
        contactRepo = ContactRepository(this).open()
        contactTypeId = intent.getStringExtra("ContactTypeId")!!.toInt()
        contactId = intent.getStringExtra("ContactId")!!.toInt()
        contactType = intent.getStringExtra("ContactType")
        contact = intent.getStringExtra("Contact")
        editTextContact.setText(contact)
        editTextContactType.setText(contactType)

    }


    override fun onDestroy() {
        super.onDestroy()
        contactTypeRepo.close()
        contactRepo.close()
    }

    fun buttonChangeContactTypeOnClick(view: View) {
        if (editTextContactType.text.toString().trim() != "") {
            contactRepo.editById(contactId!!, editTextContact.text.toString())
            contactTypeRepo.editById(contactTypeId!!, editTextContactType.text.toString())
            finish()
        } else {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("Contact type needs to be at least 1 letter")
            alertDialogBuilder.show()
        }
    }

    fun buttonDeleteContactTypeOnClick(view: View) {

        contactTypeRepo.removeContactById(contactTypeId!!, contactTypeId!!)
        finish()
    }

}