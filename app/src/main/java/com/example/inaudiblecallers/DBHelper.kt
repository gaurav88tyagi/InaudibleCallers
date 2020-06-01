package com.example.inaudiblecallers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.inaudiblecallers.ContactDetails.Companion.TABLE_NAME

const val DB_NAME = "MyContacts.db";
const val DB_VER = 1;

class DBHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(ContactDetails.CMD_CREATE_TABLE);
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(p0);
    }

}













































