/*
 *
 *  * Copyright 2019 HuuThanhDTD.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.huuthanhdtd.myhome

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Relay_1")
        var preValue:Int = 0

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val value = dataSnapshot.getValue(Int::class.java)
                preValue = value!! // (value!! shl 0) and 0x1
                switch1.isChecked = ((preValue shr 0) and 0x1) == 1
                switch2.isChecked = ((preValue shr 2) and 0x1) == 1
                switch3.isChecked = ((preValue shr 1) and 0x1) == 1
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        myRef.addValueEventListener(postListener)

        //Den 1 Click
        switch1.setOnClickListener {
            var newVal = 0
            if (switch1.isChecked == false) {
                newVal = preValue and (0x1 shl 0).inv() //Clear bit
            }
            else {
                newVal = preValue or  (0x1 shl 0) //Set bit
            }
            switch1.isChecked = !switch1.isChecked
            myRef.setValue(newVal)

        }
        //Den 2 Click
        switch2.setOnClickListener {
            var newVal = 0
            if (switch2.isChecked == false) {
                newVal = preValue and (0x1 shl 2).inv() //Clear bit
            }
            else {
                newVal = preValue or  (0x1 shl 2) //Set bit
            }
            switch2.isChecked = !switch1.isChecked
            myRef.setValue(newVal)

        }
        //Den Cong Click
        switch3.setOnClickListener {
            var newVal = 0
            if (switch3.isChecked == false) {
                newVal = preValue and (0x1 shl 1).inv() //Clear bit
            }
            else {
                newVal = preValue or  (0x1 shl 1) //Set bit
            }
            switch3.isChecked = !switch1.isChecked
            myRef.setValue(newVal)

        }

    }


}
