package com.example.contactsscroll;

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsscroll.data.objects.Person
import com.example.contactsscroll.data.repositories.PersonRepository
import kotlinx.android.synthetic.main.person_row_view.view.*


class PersonDataRecyclerViewAdapter(
    private val context: Context,
    private val personRepo: PersonRepository
) : RecyclerView.Adapter<PersonDataRecyclerViewAdapter.ViewHolder>() {

    lateinit var personDataSet: List<Person>

    fun refreshData() {
        personDataSet = personRepo.getAll()
    }

    init {
        refreshData()
    }

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = inflater.inflate(R.layout.person_row_view, parent, false)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = personDataSet.get(position)
        //https://stackoverflow.com/questions/55492820/how-to-open-a-new-activity-from-clicking-an-item-from-recyclerview
        holder.itemView.setOnClickListener { view ->

            val context = view!!.context
            val intent = Intent(context, ContactOverviewActivity::class.java)
            intent.putExtra("PersonId", person.id.toString())
            context.startActivity(intent)

        }

        holder.itemView.tag = person.id
        holder.itemView.textViewContactType.text = person.firstName
        holder.itemView.textViewContact.text = person.lastName
    }

    override fun getItemCount(): Int {
        return personDataSet.count()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}