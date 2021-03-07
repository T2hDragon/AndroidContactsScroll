package com.example.contactsscroll;

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsscroll.data.objects.Contact
import com.example.contactsscroll.data.objects.ContactType
import com.example.contactsscroll.data.objects.Person
import com.example.contactsscroll.data.repositories.ContactRepository
import com.example.contactsscroll.data.repositories.ContactTypeRepository
import com.example.contactsscroll.data.repositories.PersonRepository
import kotlinx.android.synthetic.main.person_row_view.view.*


class ContactDataRecyclerViewAdapter(
        private val context: Context,
        private val contactTypeRepo: ContactTypeRepository,
        private val contactRepo: ContactRepository,
        private val personRepo: PersonRepository,
        private val personID: Int,
) : RecyclerView.Adapter<ContactDataRecyclerViewAdapter.ViewHolder>() {

    lateinit var contactTypeDataSet: List<ContactType>
    lateinit var contactDataSet: List<Contact>
    lateinit var personDataSet: List<Person>
    lateinit var contactTypeList: MutableList<ContactType>

    fun refreshData() {
        contactTypeDataSet = contactTypeRepo.getAll()
        contactDataSet = contactRepo.getAll()
        personDataSet = personRepo.getAll()
        contactTypeList = getContactTypes();


    }

    init {
        refreshData()
    }

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = inflater.inflate(R.layout.contact_overview_row_view, parent, false)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val contactType = contactTypeList.get(position)
        var contact: Contact? = null
        for (cont in contactRepo.getAll()) {
            if (cont.id == contactType.contactId) {
                contact = cont
            }
        }
        holder.itemView.setOnClickListener { view ->
            val context = view!!.context
            val intent = Intent(context, ContactTypeActivity::class.java)
            intent.putExtra("ContactTypeId", contactType.id.toString())
            intent.putExtra("ContactId", contact!!.id.toString())
            intent.putExtra("ContactType", contactType.contactType)
            intent.putExtra("Contact", contact.contact)
            context.startActivity(intent)

        }

        holder.itemView.tag = intArrayOf(contactType.id, contactType.contactId)
        //https://stackoverflow.com/questions/55492820/how-to-open-a-new-activity-from-clicking-an-item-from-recyclerview
        holder.itemView.textViewContactType.text = contactType.contactType
        holder.itemView.textViewContact.text = contact!!.contact

    }


    override fun getItemCount(): Int {
        return getContactTypes().count()
    }

    private fun getContactTypes(): MutableList<ContactType> {
        val contactTypeList: MutableList<ContactType> = mutableListOf()
        for (contactType in contactTypeDataSet) {
            if (contactType.personId == personID) {
                contactTypeList.add(contactType)
            }
        }
        return contactTypeList
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}