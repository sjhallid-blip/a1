package com.example.sam.rapidrecall

// Each completed attempt records the sequence length,
// the user's input,
// the target sequence,
// whether the attempt was correct,
// and the current timestamp.
data class PastGame(
    val sequenceLength: Int,
    val userInput: String,
    val targetSequence: String,
    val wasCorrect: Boolean,
    val timestamp: Long
)
