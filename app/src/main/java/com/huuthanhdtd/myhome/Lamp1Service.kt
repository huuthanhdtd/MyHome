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

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Lamp1Service: TileService(){
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("Relay_1")
    var preValue:Int = 0

    override fun onClick() {
        super.onClick()

        // Called when the user click the tile
        val tile = qsTile
        var newVal = 0
        if (tile.state == Tile.STATE_ACTIVE) {
            newVal = preValue and (0x1 shl 0).inv() //Clear bit
        }
        else {
            newVal = preValue or  (0x1 shl 0) //Set bit
        }
        myRef.setValue(newVal)

    }

    override fun onTileRemoved() {
        super.onTileRemoved()

        // Do something when the user removes the Tile
    }

    override fun onTileAdded() {
        super.onTileAdded()

        // Do something when the user add the Tile
    }

    override fun onStartListening() {
        super.onStartListening()

        // Called when the Tile becomes visible
        val tile = qsTile // this is getQsTile() method form java, used in Kotlin as a property
        //tile.label = "New Label"
        //tile.icon = Icon.createWithResource(this, R.drawable.other_icon)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val value = dataSnapshot.getValue(Int::class.java)
                preValue = value!! // (value!! shl 0) and 0x1
                tile.state = Tile.STATE_INACTIVE + ((preValue shr 0) and 0x1)
//                if (((preValue shr 0) and 0x1) == 1){
//                    tile.state = Tile.STATE_ACTIVE
//                }else{
//                    tile.state = Tile.STATE_INACTIVE
//                }
                tile.updateTile() // you need to call this method to apply changes
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        myRef.addValueEventListener(postListener)


    }

    override fun onStopListening() {
        super.onStopListening()

        // Called when the tile is no longer visible
    }


}