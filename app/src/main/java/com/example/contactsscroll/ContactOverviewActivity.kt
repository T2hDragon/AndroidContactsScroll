package com.example.contactsscroll

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsscroll.data.objects.Contact
import com.example.contactsscroll.data.objects.ContactType
import com.example.contactsscroll.data.objects.Person
import com.example.contactsscroll.data.repositories.ContactRepository
import com.example.contactsscroll.data.repositories.ContactTypeRepository
import com.example.contactsscroll.data.repositories.PersonRepository
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.add_contact_dialog.view.*
import kotlinx.android.synthetic.main.contact_overview_activity.*
import kotlinx.android.synthetic.main.edit_person_dialog.view.*


class ContactOverviewActivity : AppCompatActivity() {

    private lateinit var contactTypeRepo: ContactTypeRepository
    private lateinit var contactRepo: ContactRepository
    private lateinit var personRepo: PersonRepository
    private lateinit var adapter: RecyclerView.Adapter<*>

    var personId: Int? = null
    var person: Person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_overview_activity)
        contactTypeRepo = ContactTypeRepository(this).open()
        contactRepo = ContactRepository(this).open()
        personRepo = PersonRepository(this).open()
        personId = intent.getStringExtra("PersonId")!!.toInt()
        recyclerViewContactTypes.layoutManager = LinearLayoutManager(this)
        adapter = ContactDataRecyclerViewAdapter(
            this,
            contactTypeRepo,
            contactRepo,
            personRepo,
            personId!!
        )
        recyclerViewContactTypes.adapter = adapter
        person = personRepo.getById(personId!!)
        contact_name_text_view.setText(person!!.firstName + " " + person!!.lastName)

        DeleteOnSwipeLeft()

        show_add_contact_dialog.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.add_contact_dialog, null)
            val close = view.findViewById<ImageView>(R.id.add_contact_dialog_close)
            close.setOnClickListener {
                dialog.dismiss()
            }
            val accept = view.findViewById<ImageView>(R.id.add_contact_dialog_accept)
            accept.setOnClickListener {
                buttonAddContactOnClick(view)
                dialog.dismiss()
                (adapter as ContactDataRecyclerViewAdapter).refreshData()
                adapter.notifyDataSetChanged()

            }

            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()

        }


        show_edit_person_dialog.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.edit_person_dialog, null)
            view.editTextEditPersonFirstName.setText(person!!.firstName)
            view.editTextEditPersonLastName.setText(person!!.lastName)

            val close = view.findViewById<ImageView>(R.id.edit_person_dialog_close)
            close.setOnClickListener {
                dialog.dismiss()
            }
            val accept = view.findViewById<ImageView>(R.id.edit_person_dialog_accept)
            accept.setOnClickListener {
                buttonPersonEditOnClick(view)
                dialog.dismiss()
                contact_name_text_view.setText(view.editTextEditPersonFirstName.text.toString() + " " + view.editTextEditPersonLastName.text.toString())
                (adapter as ContactDataRecyclerViewAdapter).refreshData()
                adapter.notifyDataSetChanged()

            }

            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()


        }

    }

    private fun DeleteOnSwipeLeft() {
        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    var idList: IntArray = viewHolder.itemView.tag as IntArray
                    contactTypeRepo.removeContactById(idList.get(0), idList.get(1))
                    (adapter as ContactDataRecyclerViewAdapter).refreshData()

                    adapter.notifyDataSetChanged()
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewContactTypes)
    }

    fun buttonAddContactOnClick(view: View) {
        if (view.editTextContactType.text.toString().trim() != "") {

            contactRepo.add(
                Contact(
                    view.editTextContact.text.toString(),
                )
            )
            contactTypeRepo.add(
                ContactType(
                    view.editTextContactType.text.toString(),
                    contactRepo.getAll().last().id,
                    personId!!
                )
            )
            view.editTextContact.setText("")
            view.editTextContactType.setText("")
            (adapter as ContactDataRecyclerViewAdapter).refreshData()

            adapter.notifyDataSetChanged()
        } else {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("Contact type needs to have at least 1 letter")
            alertDialogBuilder.show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        contactTypeRepo.close()
        contactRepo.close()
    }

    fun buttonPersonEditOnClick(view: View) {
        if (view.editTextEditPersonFirstName.text.toString()
                .trim() != "" || view.editTextEditPersonLastName.text.toString()
                .trim() != ""
        ) {
            personRepo.editById(
                personId!!,
                view.editTextEditPersonFirstName.text.toString(),
                view.editTextEditPersonLastName.text.toString()
            )

        } else {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("Forename or surname needs to have at least 1 letter")
            alertDialogBuilder.show()
        }
    }


    override fun onResume() {
        super.onResume()
        (adapter as ContactDataRecyclerViewAdapter).refreshData()
        adapter.notifyDataSetChanged()
    }

}