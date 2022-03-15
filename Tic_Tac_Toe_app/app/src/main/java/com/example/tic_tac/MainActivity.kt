package com.example.tic_tac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
//import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var  board : Array<Array<Button>>
    var Player = true
    var Turncount=0

    var boardstatus= Array(3){IntArray(3)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        val button7 = findViewById<Button>(R.id.button7)
        val button8 = findViewById<Button>(R.id.button8)
        val reset= findViewById<Button>(R.id.button9)


        board = arrayOf(
            arrayOf(button,button1,button2),
            arrayOf(button3,button4,button5),
            arrayOf(button6,button7,button8)
        )
        for (i in board){
            for (Button in i){
                Button.setOnClickListener(this)
            }
        }
        Initializevalue()
        reset.setOnClickListener {
            Player= true
            Turncount=0
            Initializevalue()

        }

    }

    private fun Initializevalue() {
        for ( i in 0..2){
            for (j in 0..2){
                boardstatus[i][j] = -1
                board[i][j].isEnabled= true
                board[i][j].text=""


            }
        }
    }

    override fun onClick(view: View ) {
        when(view.id){
            R.id.button ->{
                updatevalue(row=0 , col=0, player= Player)

            }
            R.id.button1 ->{
                updatevalue(row=0 , col=1, player= Player)

            }
            R.id.button2 ->{
                updatevalue(row=0 , col=2, player= Player)

            }
            R.id.button3 ->{
                updatevalue(row=1 , col=0, player= Player)

            }
            R.id.button4 ->{
                updatevalue(row=1 , col=1, player= Player)

        }
            R.id.button5 ->{
                updatevalue(row=1 , col=2, player= Player)

            }
            R.id.button6 ->{
                updatevalue(row=2 , col=0, player= Player)

            }
            R.id.button7 ->{
                updatevalue(row=2 , col=1, player= Player)

            }
            R.id.button8 ->{
                updatevalue(row=2 , col=2, player= Player)


            }

        }
        Turncount++
        Player = !Player
        if (Player){
          updatedisplay("Player X turn")
        }
        else{
        updatedisplay("Player O turn")

        }
        if (Turncount==9){
            updatedisplay("Game Draw")

        }
        checkwinner()



    }

    private fun checkwinner() {
        for (i in 0..2) {
            if (boardstatus[i][0] == boardstatus[i][1] && boardstatus[i][0] == boardstatus[i][2]) {
                if (boardstatus[i][0] == 1) {
                    updatedisplay("Player X is Winner")
                    break
                } else if (boardstatus[i][0] == 0) {
                    updatedisplay("Player O is Winner")
                    break

                }
            }
        }

        for (i in 0..2) {
            if (boardstatus[0][i] == boardstatus[1][i] && boardstatus[0][i] == boardstatus[2][i]) {
                if (boardstatus[0][i] == 1) {
                    updatedisplay("Player X is Winner")
                    break
                } else if (boardstatus[0][i] == 0) {
                    updatedisplay("Player O is Winner")
                    break

                }
            }
        }
        if (boardstatus[0][0] == boardstatus[1][1] && boardstatus[0][0] == boardstatus[2][2]) {
            if (boardstatus[0][0] == 1) {
                updatedisplay("Player X is Winner")
            } else if (boardstatus[0][0] == 0) {
                updatedisplay("Player O is Winner")


            }
        }
        if (boardstatus[0][2] == boardstatus[1][1] && boardstatus[0][2] == boardstatus[2][0]) {
            if (boardstatus[0][2] == 1) {
                updatedisplay("Player X is Winner")
            } else if (boardstatus[0][2] == 0) {
                updatedisplay("Player O is Winner")


            }
        }
    }


    private fun updatedisplay( text : String) {
                val playerturn = findViewById<TextView>(R.id.playerturn)

        playerturn.text= text
        if (text.contains("Winner")){
            disablebutton()
        }


    }

    private  fun disablebutton(){
        for (i in board){
            for (Button in i){
                Button.isEnabled = false
            }
        }

    }


    private fun updatevalue(row: Int, col: Int, player: Boolean) {
        var text = if (player) "X" else "0"
        val value = if (player) 1 else 0
        board[row][col].apply {
            isEnabled= false
            setText(text)
        }
        boardstatus[row][col]= value



    }
}