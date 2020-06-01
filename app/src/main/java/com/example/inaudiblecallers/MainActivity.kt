package com.example.inaudiblecallers

import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inaudiblecallers.ContactDetails.Companion.TABLE_NAME
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    var from = ArrayList<AllContactsHolder>();
//    var x = String();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val dbHelper = DBHelper(this);
        val contactDb = dbHelper.writableDatabase;

        if(DatabaseUtils.queryNumEntries(contactDb, TABLE_NAME).toInt() != 0) {
            from = ContactDetails.getAllContacts(contactDb);
            recyclerView.adapter = AllContactsAdapter(from , this);
        }


        fab.setOnClickListener { view ->
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(intent, 201)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 201 && data?.data != null) {
            val contactUri = data.data
            val crContacts = contactUri?.let { contentResolver.query(it, null, null, null, null) }
            crContacts?.moveToFirst()
            val id = crContacts?.getString(crContacts.getColumnIndex(ContactsContract.Contacts._ID))
            if (crContacts?.getString(crContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))?.let {
                    Integer.parseInt(
                        it
                    )
                }!! > 0) {
                val crPhones = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    arrayOf(id),
                    null
                )
                crPhones?.moveToFirst()
                var phoneName = crPhones?.getString(crPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).toString();
                var phoneNumber = crPhones?.getString(crPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).toString();
//                var phoneImage = crPhones?.getString(crPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STATUS_ICON));

//                x = phoneName.toString();
                val y = AllContactsHolder(0, phoneName, phoneNumber);
                from.add(y);

                recyclerView.adapter = AllContactsAdapter(from, this);

                ContactDetails.insertContact(
                    DBHelper(this).writableDatabase,
                    ContactDetails.Contact(
                        null,
                        phoneName,
                        phoneNumber
                    )
                )

                crPhones?.close()

            }
            crContacts.close()
        }
    }

    fun delete(name: String) {

//        DBHelper(this).writableDatabase.execSQL("DELETE FROM " + TABLE_NAME + "WHERE contact = " + name);
        Log.d("DRRD", name + "Ooo");

//        ContactDetails.deleteContact(
//            DBHelper(this).writableDatabase,
//            name
//        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Log.d("DRRD", "CLICKED");

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Remove")
        builder.setMessage("Are you sure you want to remove all the contacts?")
        builder.setIcon(android.R.drawable.ic_menu_delete)

        builder.setPositiveButton("Yes"){dialogInterface, which ->
            DBHelper(this).writableDatabase.execSQL("DELETE FROM " + TABLE_NAME);
            from.clear();
            recyclerView.adapter = AllContactsAdapter(from , this);
        }
        builder.setNeutralButton("Cancel"){dialogInterface , which ->
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
