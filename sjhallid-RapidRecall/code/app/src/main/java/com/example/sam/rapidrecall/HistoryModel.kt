package com.example.sam.rapidrecall

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

// This is the main model for driving the game and storing logs.
// It implements ObservableModel in order to have views be updated.
// It uses GameModel to run specific games, by creating, inputting the guess and destroying it
class HistoryModel: ObservableModel<HistoryModel>() {
    private var pastGames = mutableListOf<PastGame>()
    private var numPastGames: Int = 0
    private var currentGame: GameModel? = null
    private var currentNum: Int? = null
    private var totalAccuracy = 0.0
    private var wasCorrect = false
    private var guess = ""
    private var theCorrectSequence = ""
    private var correctguesses = 0

    // Getters

    fun getPastGames(): List<PastGame> = pastGames
    fun getNumPastGames(): Int = numPastGames
    fun getCurrentNum(): Int? = currentNum
    fun getTotalAccuracy(): Double = totalAccuracy
    fun getWasCorrect(): Boolean = wasCorrect
    fun getTheGuess(): String = guess
    fun getTheCorrectSequence(): String = theCorrectSequence
    fun getCorrectguesses(): Int = correctguesses

    // Gameplay functions

    suspend fun displayGame(length: Int){ // shows the sequence of numbers
        currentGame = GameModel(length)
        currentNum = currentGame?.getNextNum()
        while (currentNum!=null) {
            this.notifyObservers(this)
            delay(1000.milliseconds)
            currentNum = null
            this.notifyObservers(this)
            delay(1000.milliseconds)
            currentNum = currentGame?.getNextNum()
        }
        currentNum = -1 // Signals end of sequence
        this.notifyObservers(this)
    }
    fun guess(theGuess: List<Int>){
        numPastGames++
        wasCorrect = currentGame!!.guessSequence(theGuess)
        guess = currentGame!!.getGuessedSequence().joinToString(separator = "")
        theCorrectSequence = currentGame!!.getCorrectSequence().joinToString(separator = "")
        pastGames.add(PastGame(
            currentGame!!.getSequenceLength(),
            guess,
            theCorrectSequence,
            wasCorrect,
            System.currentTimeMillis()
        ))
        currentGame = null
        computeAccuracy()
        this.notifyObservers(this)
    }

    private fun computeAccuracy(){
        var totalWins = 0
        var totalLosses = 0
        for(i in pastGames){
            if(i.wasCorrect)totalWins++
            else totalLosses++
        }
        correctguesses = totalWins
        totalAccuracy = totalWins.toDouble()/(totalWins.toDouble()+totalLosses.toDouble())
    }

}