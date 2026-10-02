package com.example.sam.rapidrecall

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class HistoryModel: ObservableModel<HistoryModel>() {
    private var pastGames = mutableListOf<PastGame>()
    private var numPastGames: Int = 0
    private var currentGame: GameModel? = null
    private var currentNum: Int? = null
    private var totalAccuracy = 0
    private var wasCorrect = false
    private var guess = ""
    private var theCorrectSequence = ""
    private var correctguesses = 0

    // Getters

    fun getPastGames(): List<PastGame> = pastGames
    fun getNumPastGames(): Int = numPastGames
    fun getCurrentNum(): Int? = currentNum
    fun getTotalAccuracy(): Int = totalAccuracy
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

    fun computeAccuracy(){
        var totalWins = 0
        var totalLosses = 0
        for(i in pastGames){
            if(i.wasCorrect)totalWins++
            else totalLosses++
        }
        correctguesses = totalWins
        totalAccuracy = if(totalWins==0 && totalLosses==0) 0
        else totalWins/(totalWins+totalLosses)
    }

}