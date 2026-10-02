package com.example.sam.rapidrecall

class GameController(val model: HistoryModel) {
    fun guessInt(g: String){
        model.guess( g.map { it.digitToInt() })
    }
    suspend fun startGame(length: Float){
        model.displayGame(length.toInt())
    }
}