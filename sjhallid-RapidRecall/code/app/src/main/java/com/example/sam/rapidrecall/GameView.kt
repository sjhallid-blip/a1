package com.example.sam.rapidrecall

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


// This class is responsible for displaying everything, and implements
// ViewObserver for the update() function in order to subscribe to the HistoryModel.
// In order to send messages to the model, it takes in the gameController
class GameView(
    val gameController: GameController
): ViewObserver<HistoryModel> {
    private var pastGames: List<PastGame> = emptyList()
    private var numPastGames by mutableIntStateOf(0)
    private var currentNum:Int? by mutableStateOf(null)
    private var totalAccuracy by mutableDoubleStateOf(0.0)
    private var correct by mutableStateOf(true)
    private var correctSequence by mutableStateOf("")
    private var theGuess by mutableStateOf("")
    private var correctguesses by mutableIntStateOf(0)

    override fun update(model: HistoryModel) {
        pastGames = model.getPastGames()
        numPastGames = model.getNumPastGames()
        currentNum = model.getCurrentNum()
        totalAccuracy = model.getTotalAccuracy()
        correct = model.getWasCorrect()
        correctSequence = model.getTheCorrectSequence()
        theGuess = model.getTheGuess()
        correctguesses = model.getCorrectguesses()
    }

    @Composable
    fun Content(modifier: Modifier = Modifier){
        val scope = rememberCoroutineScope() // Allows to call functions that pause without freezing UI
        var sequenceLength by remember { mutableFloatStateOf(1f) }
        var guess by remember { mutableStateOf("") }
        var currentScreen by remember { mutableStateOf("Home") }
        Column(modifier = modifier.padding(20.dp),horizontalAlignment = Alignment.CenterHorizontally ) {
            if (currentScreen == "Home") {
                Text(
                    text = "RAPID RECALL",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Slider(
                    value = sequenceLength,
                    onValueChange = { sequenceLength = it },
                    valueRange = 1f..10f,
                    steps = 8,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { // Launching using scope to prevent freezing UI
                        scope.launch { gameController.startGame(sequenceLength) }
                        currentScreen = "Display Sequence"
                    },
                ) {
                    Text("Start (${sequenceLength.toInt()} numbers)")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { // Launching using scope to prevent freezing UI
                        currentScreen = "attempt summary"
                    },
                ) {
                    Text("attempt summary")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { // Launching using scope to prevent freezing UI
                        currentScreen = "log"
                    },
                ) {
                    Text("log")
                }
            }
            if (currentScreen == "attempt summary"){
                Column() {
                    Button(
                        onClick = {
                            currentScreen = "Home"
                        }
                    ) {
                        Text("Back")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Total games:$numPastGames",)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Correct games:$correctguesses",)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Accuracy:${totalAccuracy*100}%")
                }
            }
            if(currentScreen == "log"){
                Button(
                    onClick = {
                        currentScreen = "Home"
                    }
                ) {
                    Text("Back")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Past games:")
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(pastGames) { game ->
                        Text(
                            text = "${if (game.wasCorrect) "Correct" else "Incorrect"} " +
                                    "Length: ${game.sequenceLength} | "+
                                    "Target: ${game.targetSequence} | " +
                                    "Input: ${game.userInput} | " +
                                    "At: ${game.timestamp}"
                        )
                    }
                }
            }
            if (currentScreen == "Display Sequence") {

                if (currentNum != null) { // Just display nothing if null
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentNum.toString(),
                            fontSize = 120.sp, // Fills the screen
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (currentNum == -1) currentScreen =
                    "Guess Sequence" // Signaled that sequence is done
            }
            if (currentScreen == "Guess Sequence") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    TextField(
                        value = guess,
                        onValueChange = { guess = it },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Can only type numbers
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (guess != "") {
                    Button(
                        onClick = {
                            gameController.guessInt(guess)
                            guess = "" // Resetting
                            currentScreen = "Post Guess Sequence"
                        }
                    ) {
                        Text("SUBMIT")
                    }
                }
            }
            if (currentScreen == "Post Guess Sequence") {
                if (correct) Text("Correct!",fontSize = 50.sp,fontWeight = FontWeight.Bold)
                else Text("Incorrect!",fontSize = 50.sp,fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Correct sequence:$correctSequence",)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Your guess:$theGuess",)
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        currentScreen = "Home"
                    }
                ) {
                    Text("Home")
                }
            }

        }
    }
}