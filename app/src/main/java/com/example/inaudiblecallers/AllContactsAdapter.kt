package com.example.inaudiblecallers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ExpandableListView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.all_contact_sample.view.*
import kotlinx.android.synthetic.main.content_main.*

class AllContactsAdapter(var contacts: ArrayList<AllContactsHolder>, private val clickListener: (AllContactsHolder) -> Unit) : RecyclerView.Adapter<AllContactsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllContactsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_contact_sample, parent, false);
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return contacts.size;
    }

    override fun onBindViewHolder(holder: AllContactsAdapter.ViewHolder, position: Int) {
        holder.bind(contacts[position], clickListener);
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var view = view;
        fun bind(contact: AllContactsHolder, clickListener: (AllContactsHolder) -> Unit) {
            view.contactName.text = contact.contactName;
            view.contactNumber.text = contact.contactNumber;

            Log.d("FF", contact.contactImage.length.toString());

            if(contact.contactImage.equals("null")) {
                Log.d("FF",  "src");
                view.contactImage.setImageResource(android.R.drawable.ic_menu_report_image);
            }
            else {
                Log.d("FF",  "uri");
                view.contactImage.setImageURI(Uri.parse(contact.contactImage));
            }

            view.setOnClickListener { clickListener(contact) };

        }
    }

//    public fun filterList(filter: ArrayList<AllContactsHolder>) {
//        contacts = filter;
//        notifyDataSetChanged();
//    }

}

//class AllContactsAdapter(val contacts: ArrayList<AllContactsHolder>, val context: Context, private val clickListener: (AllContactsHolder) -> Unit) : BaseAdapter() {
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val inflater = LayoutInflater.from(context);
//
//        val view: View;
//        view = convertView ?: inflater.inflate(R.layout.all_contact_sample, parent, false);
//
//        val nameView = view.contactName;
//        val imageView = view.contactImage;
//        val numberView = view.contactNumber;
//
//        nameView.text = contacts[position].contactName;
////        imageView.setImageResource(contacts[position].contactImage);
//        numberView.text = contacts[position].contactNumber;
//
//        view.setOnClickListener { clickListener(AllContactsHolder) };
//
////        view.setOnClickListener {
////            Log.d("DRRD1", contacts[position].contactName);
//////            DBHelper(context).writableDatabase.execSQL("DELETE FROM " + ContactDetails.TABLE_NAME + "WHERE contact = " + contacts[position].contactName);
////
////            var x = AllContactsHolder("0" , contacts[position].contactName, contacts[position].contactNumber)
////
////            val sharedPreferences: SharedPreferences = context.getSharedPreferences("tode",Context.MODE_PRIVATE);
////            val editor:SharedPreferences.Editor =  sharedPreferences.edit()
////            editor.putString("name", contacts[position].contactName);
////            editor.putString("number", contacts[position].contactNumber);
////            editor.apply();
////            editor.commit();
////
//////            MainActivity().from.remove(x);
//
//////            MainActivity().recyclerView.adapter = AllContactsAdapter(MainActivity().from, this@MainActivity);
////
////            MainActivity().delete(x, context);
////
//////            ContactDetails.deleteContact(
//////                DBHelper(context).writableDatabase,
//////                contacts[position].contactName
//////            )
////
////        }
//
//        return view;
//    }
//
////    fun removeItem(x: AllContactsHolder): ArrayList<AllContactsHolder>{
////        Log.d("DRRD3",  "yyyy");
////        contacts.remove(x);
////        return contacts;
////    }
//
//    override fun getItem(p0: Int): Any {
//        return contacts[p0];
//    }
//
//    override fun getItemId(p0: Int): Long {
//        return p0.toLong();
//    }
//
//    override fun getCount(): Int {
//        return contacts.size;
//    }
//
//}