package com.example.sam.rapidrecall

// Responsible for sending messages from the view to the HistoryModel
class GameController(private val model: HistoryModel) {
    fun guessInt(g: String){
        model.guess( g.map { it.digitToInt() })
    }
    suspend fun startGame(length: Float){
        model.displayGame(length.toInt())
    }
}