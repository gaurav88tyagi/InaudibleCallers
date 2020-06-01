package com.example.inaudiblecallers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.all_contact_sample.view.*

class AllContactsAdapter(val contacts: ArrayList<AllContactsHolder>, val context: Context) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context);

        val view: View;
        view = convertView ?: inflater.inflate(R.layout.all_contact_sample, parent, false);

        val nameView = view.contactName;
        val imageView = view.contactImage;
        val numberView = view.contactNumber;

        nameView.text = contacts[position].contactName;
        imageView.setImageResource(contacts[position].contactImage);
        numberView.text = contacts[position].contactNumber;

        view.setOnClickListener {
            Log.d("DRRD", contacts[position].contactName);
//            DBHelper(context).writableDatabase.execSQL("DELETE FROM " + ContactDetails.TABLE_NAME + "WHERE contact = " + contacts[position].contactName);

            MainActivity().delete(contacts[position].contactName);

//            ContactDetails.deleteContact(
//                DBHelper(context).writableDatabase,
//                contacts[position].contactName
//            )

        }

        return view;
    }

    override fun getItem(p0: Int): Any {
        return contacts[p0];
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong();
    }

    override fun getCount(): Int {
        return contacts.size;
    }

}