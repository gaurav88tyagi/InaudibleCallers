package com.example.inaudiblecallers

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class ContactDetails {

    data class Contact(
        val id: Int?,
        val contact: String,
        val number: String
    )
    companion object {
        val TABLE_NAME = "contacts";

        val CMD_CREATE_TABLE = """
            CREATE TABLE $TABLE_NAME (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            contact TEXT,
            number TEXT
            );
            """.trimIndent();

        fun insertContact(db: SQLiteDatabase, contact: Contact) {
            val contactRow = ContentValues();
            contactRow.put("contact", contact.contact);
            contactRow.put("number", contact.number);

            db.insert(TABLE_NAME, null, contactRow);
        }

        fun deleteContact(db: SQLiteDatabase, contact: String) {
            db.delete(TABLE_NAME, "contact" + "=" + contact, null)>0;
        }

        fun getAllContacts(db: SQLiteDatabase): ArrayList<AllContactsHolder> {
            val contacts = ArrayList<AllContactsHolder>();

            val cursor = db.query(
                TABLE_NAME,
                arrayOf("id", "contact", "number"),
                null, null, null, null, null
            );

            cursor.moveToFirst();

            val contactCol = cursor.getColumnIndex("contact");
            val numberCol = cursor.getColumnIndex("number");

            do {
                val contact = cursor.getString(contactCol);
                val number = cursor.getString(numberCol);
                val x = AllContactsHolder(0, contact, number);

                contacts.add(x);
            }while (cursor.moveToNext())
            cursor.close();

            return contacts;
        }
    }


}