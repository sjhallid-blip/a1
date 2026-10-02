package com.example.sam.rapidrecall

import kotlin.random.Random

// Model for running a game
// Responsible for creating and supplying HistoryModel the random sequence
// Responsible for checking if the guess was correct
class GameModel (
    private val sequenceLength: Int
){
    private val correctSequence = List(sequenceLength) { Random.nextInt(0, 9) }
    private var guessedSequence = List<Int>(sequenceLength, init = {0})
    private var curIndex = -1

    // GETTERS
    fun getSequenceLength(): Int = sequenceLength
    fun getCorrectSequence(): List<Int> = correctSequence
    fun getGuessedSequence(): List<Int> = guessedSequence
    // Gets the current number in correct sequence to display, if done returns NULL
    fun getNextNum(): Int?{
        curIndex++
        if(curIndex >= sequenceLength)return null
        return correctSequence[curIndex]
    }
    // PLAYING GAME
    fun guessSequence(guess: List<Int>): Boolean{
        guessedSequence = guess.toList() // Saving guess
        if(guess.size != sequenceLength)return false // mismatch of length of guess
        for (i in correctSequence.indices) {
            if(guess[i] != correctSequence[i])return false
        }
        return true
    }
}