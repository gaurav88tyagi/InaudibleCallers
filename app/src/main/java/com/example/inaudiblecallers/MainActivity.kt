package com.example.inaudiblecallers

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.DatabaseUtils
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
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

        setupPermissions();

        et.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                var filter = ArrayList<AllContactsHolder>();

                var str = s.toString();

                for (s in from) {

                    if (s.contactName.toLowerCase().contains(str.toLowerCase())) {
                        filter.add(s);
                    }
                }

//                AllContactsAdapter(from , {from -> itemClicked(from)}).filterList(filter);

                recyclerView.layoutManager = LinearLayoutManager(applicationContext);
                recyclerView.adapter = AllContactsAdapter(filter , {filter -> itemClicked(filter)});

            }

        })

        if(DatabaseUtils.queryNumEntries(contactDb, TABLE_NAME).toInt() != 0) {
            from = ContactDetails.getAllContacts(contactDb);
            recyclerView.layoutManager = LinearLayoutManager(this);
            recyclerView.adapter = AllContactsAdapter(from , {from -> itemClicked(from)});
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
                var image = crPhones?.getString(crPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

//                x = phoneName.toString();
                val y = AllContactsHolder(image.toString(), phoneName, phoneNumber);

                val contain = from.contains(y);

                if(!contain)
                {
                    from.add(y);

                    recyclerView.layoutManager = LinearLayoutManager(this);
                    recyclerView.adapter = AllContactsAdapter(from, { from -> itemClicked(from) });

                    ContactDetails.insertContact(
                        DBHelper(this).writableDatabase,
                        ContactDetails.Contact(
                            null,
                            phoneName,
                            phoneNumber,
                            image.toString()
                        )
                    )
                }
                crPhones?.close()
            }
            crContacts.close()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            0 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d("DRRD2",  "DENIED");
                    finish();
                }
                else {
                    Log.d("DRRD2",  "GRANTED");
                }
            }
        }
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

            recyclerView.layoutManager = LinearLayoutManager(this);
            recyclerView.adapter = AllContactsAdapter(from , {from -> itemClicked(from)});
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

    private fun itemClicked(x: AllContactsHolder) {
        Log.d("DRRD2",  "Ooo");

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Remove")
        builder.setMessage("Are you sure you want to remove this contact?")
        builder.setIcon(android.R.drawable.ic_menu_delete)

        builder.setPositiveButton("Yes"){dialogInterface, which ->
            from.remove(x);
            DBHelper(this).writableDatabase.execSQL("DELETE FROM " + TABLE_NAME + " WHERE contact = '" + x.contactName + "'");

            et.setText("");

            recyclerView.layoutManager = LinearLayoutManager(this);
            recyclerView.adapter = AllContactsAdapter(from , {from -> itemClicked(from)});
        }
        builder.setNeutralButton("Cancel"){dialogInterface , which ->
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d("DRRD2",  "!GRANTED");
            makeRequest();
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS), 0);
    }

}
