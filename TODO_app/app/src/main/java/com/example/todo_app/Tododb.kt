package com.example.todo_app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tododb(
   var title:String,
   var description:String,
   var category:String,
   var date:Long,
   var time:Long,
   var isFinished: Int=-1,
   @PrimaryKey
   var id:Long=0

)
