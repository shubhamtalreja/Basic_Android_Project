package com.example.todo_app

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

const val DB_name= "todo.db"
class Taskactivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalender: Calendar
    lateinit var datesetListener : DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener


    val db by lazy {
        Room.databaseBuilder(
            this,
            Appdatabase::class.java,
            DB_name

        )


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        val datedit = findViewById<TextInputEditText>(R.id.dateedit)


        datedit.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.dateedit ->{
                setListener()
            }
            R.id.timeedit ->{
                setTime()
            }

        }
    }

    private fun setTime() {
        myCalender = Calendar.getInstance()
        timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hours_of_day, Minute ->
            myCalender.set(Calendar.HOUR_OF_DAY,hours_of_day)
            myCalender.set(Calendar.MINUTE,Minute)
//            updatetime()

        }
        val timePickerDialog = TimePickerDialog(
            this,timeSetListener, myCalender.get(Calendar.HOUR_OF_DAY),
           myCalender.get(Calendar.MINUTE),false
        )
        timePickerDialog.show()
    }

//    private fun updatetime() {
//        val timeedit = findViewById<TextInputLayout>(R.id.timeedit)
//        val format = "h:mm a"
//        val sdf=  SimpleDateFormat(format)
//    }

    private fun setListener() {
        myCalender = Calendar.getInstance()
        datesetListener = DatePickerDialog.OnDateSetListener { _, Year, Month, DayofMonth ->
            myCalender.set(Calendar.YEAR,Year)
            myCalender.set(Calendar.MONTH,Month)
            myCalender.set(Calendar.DAY_OF_MONTH,DayofMonth)
            updatefun()

        }
        val datePickerDialog = DatePickerDialog(
            this,datesetListener, myCalender.get(Calendar.YEAR),
            myCalender.get(Calendar.MONTH),myCalender.get(Calendar.DAY_OF_MONTH),
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()


    }

    private fun updatefun() {
        val datedit = findViewById<TextInputEditText>(R.id.dateedit)
        val textInp = findViewById<TextInputLayout>(R.id.textInp)

        val myformat = " EEE, d MMM yyyy"
        val sdf=  SimpleDateFormat(myformat)
        datedit.setText(sdf.format(myCalender.time))
        textInp.visibility = View.VISIBLE

    }
}