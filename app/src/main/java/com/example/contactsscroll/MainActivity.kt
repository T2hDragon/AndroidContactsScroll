package com.example.contactsscroll;

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsscroll.data.objects.Person
import com.example.contactsscroll.data.repositories.ContactTypeRepository
import com.example.contactsscroll.data.repositories.PersonRepository
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_person_dialog.view.*


class MainActivity : AppCompatActivity() {

    private lateinit var personRepo: PersonRepository
    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var contactTypeRepo: ContactTypeRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        personRepo = PersonRepository(this).open()
        contactTypeRepo = ContactTypeRepository(this).open()


        recyclerViewPersons.layoutManager = LinearLayoutManager(this)
        adapter = PersonDataRecyclerViewAdapter(this, personRepo)
        recyclerViewPersons.adapter = adapter

        DeleteOnSwipeLeft()


        show_add_person_dialog.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.add_person_dialog, null)
            val close = view.findViewById<ImageView>(R.id.add_contact_dialog_close)
            close.setOnClickListener {
                dialog.dismiss()
            }
            val accept = view.findViewById<ImageView>(R.id.add_contact_dialog_accept)
            accept.setOnClickListener {
                buttonAddPersonOnClick(view)
                dialog.dismiss()
                (adapter as PersonDataRecyclerViewAdapter).refreshData()

                adapter.notifyDataSetChanged()

            }

            dialog.setCancelable(true)
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
                    contactTypeRepo.removePersonContacts(viewHolder.itemView.tag as Int)
                    personRepo.removeById(viewHolder.itemView.tag as Int)
                    (adapter as PersonDataRecyclerViewAdapter).refreshData()

                    adapter.notifyDataSetChanged()
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewPersons)
    }

    fun buttonAddPersonOnClick(view: View) {
        if (view.editTextAddPersonFirstName.text.toString()
                .trim() != "" || view.editTextAddPersonLastName.text.toString().trim() != ""
        ) {
            personRepo.add(
                Person(
                    view.editTextAddPersonFirstName.text.toString(),
                    view.editTextAddPersonLastName.text.toString()
                )
            )
            (adapter as PersonDataRecyclerViewAdapter).refreshData()

            adapter.notifyDataSetChanged()
        } else {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("Forename or surname needs to have at least 1 letter")
            alertDialogBuilder.show()
        }

    }


    override fun onResume() {
        super.onResume()
        (adapter as PersonDataRecyclerViewAdapter).refreshData()

        adapter.notifyDataSetChanged()
    }


    override fun onDestroy() {
        super.onDestroy()
        personRepo.close()
    }


}