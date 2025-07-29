package com.example.tictactoeapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var boardButtons: Array<Array<Button>>
    private var currentPlayer = "X"
    private lateinit var turnTextView: TextView
    private lateinit var playerXScore: TextView
    private lateinit var playerOScore: TextView
    private lateinit var resetScoreButton: Button

    private var xScore = 0
    private var oScore = 0

    private var playerXName = "Player X"
    private var playerOName = "Player O"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        turnTextView = findViewById(R.id.turnTextView)
        playerXScore = findViewById(R.id.playerXScoreText)
        playerOScore = findViewById(R.id.playerOScoreText)
        resetScoreButton = findViewById(R.id.resetScoreButton)

        val resetButton = findViewById<Button>(R.id.resetButton)

        boardButtons = Array(3) { row ->
            Array(3) { col ->
                val buttonId = resources.getIdentifier("button$row$col", "id", packageName)
                findViewById<Button>(buttonId).apply {
                    setOnClickListener { onButtonClick(this, row, col) }
                }
            }
        }

        askPlayerNames()

        resetButton.setOnClickListener {
            resetBoard()
        }

        resetScoreButton.setOnClickListener {
            xScore = 0
            oScore = 0
            updateScore()
            Toast.makeText(this, "Scoreboard reset!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun askPlayerNames() {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val inputX = EditText(this)
        inputX.hint = "Enter name for Player X"
        layout.addView(inputX)

        val inputO = EditText(this)
        inputO.hint = "Enter name for Player O"
        layout.addView(inputO)

        AlertDialog.Builder(this)
            .setTitle("Player Names")
            .setView(layout)
            .setCancelable(false)
            .setPositiveButton("Start") { _, _ ->
                playerXName = inputX.text.toString().ifBlank { "Player X" }
                playerOName = inputO.text.toString().ifBlank { "Player O" }
                turnTextView.text = "$playerXName's Turn (X)"
            }
            .show()
    }

    private fun onButtonClick(button: Button, row: Int, col: Int) {
        if (button.text.isNotEmpty()) return

        button.text = currentPlayer
        if (checkWin()) {
            val winnerName = if (currentPlayer == "X") playerXName else playerOName
            if (currentPlayer == "X") xScore++ else oScore++
            updateScore()
            showWinnerDialog("$winnerName wins!")
        } else if (isBoardFull()) {
            showWinnerDialog("It's a draw!")
        } else {
            currentPlayer = if (currentPlayer == "X") "O" else "X"
            val currentName = if (currentPlayer == "X") playerXName else playerOName
            turnTextView.text = "$currentName's Turn ($currentPlayer)"
        }
    }

    private fun checkWin(): Boolean {
        for (i in 0..2) {
            if (boardButtons[i][0].text == currentPlayer &&
                boardButtons[i][1].text == currentPlayer &&
                boardButtons[i][2].text == currentPlayer) return true

            if (boardButtons[0][i].text == currentPlayer &&
                boardButtons[1][i].text == currentPlayer &&
                boardButtons[2][i].text == currentPlayer) return true
        }

        if (boardButtons[0][0].text == currentPlayer &&
            boardButtons[1][1].text == currentPlayer &&
            boardButtons[2][2].text == currentPlayer) return true

        if (boardButtons[0][2].text == currentPlayer &&
            boardButtons[1][1].text == currentPlayer &&
            boardButtons[2][0].text == currentPlayer) return true

        return false
    }

    private fun isBoardFull(): Boolean {
        return boardButtons.all { row -> row.all { it.text.isNotEmpty() } }
    }

    private fun showWinnerDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle(message)
            .setMessage("Do you want to play again?")
            .setPositiveButton("Yes") { _, _ -> resetBoard() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun resetBoard() {
        for (row in boardButtons) {
            for (button in row) {
                button.text = ""
            }
        }
        currentPlayer = "X"
        turnTextView.text = "$playerXName's Turn (X)"
    }

    private fun updateScore() {
        playerXScore.text = "X: $xScore"
        playerOScore.text = "O: $oScore"
    }
}
