package com.example.simonsays

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimonSaysGame()
        }
    }
}

val colors = listOf(
    Color.Red,
    Color.Yellow,
    Color.Green,
    Color.Blue
)

@Composable
fun SimonSaysGame() {

    var sequence by remember { mutableStateOf(listOf<Int>()) }
    var userInput by remember { mutableStateOf(listOf<Int>()) }
    var showing by remember { mutableStateOf(false) }
    var flashIndex by remember { mutableStateOf(-1) }
    var gameOver by remember { mutableStateOf(false) }
    var started by remember { mutableStateOf(false) }
    var level by remember { mutableStateOf(1) }

    fun addStep() {
        sequence = sequence + Random.nextInt(0, 4)
        userInput = emptyList()
    }

    fun startGame() {
        sequence = emptyList()
        userInput = emptyList()
        level = 1
        gameOver = false
        started = true
        addStep()
    }

    LaunchedEffect(sequence, started) {
        if (!started) return@LaunchedEffect

        showing = true
        delay(500)

        for (i in sequence.indices) {
            flashIndex = sequence[i]
            delay(600)
            flashIndex = -1
            delay(250)
        }

        showing = false
    }

    fun handleTap(index: Int) {
        if (showing || gameOver) return

        val newInput = userInput + index
        userInput = newInput

        // check correctness
        for (i in newInput.indices) {
            if (newInput[i] != sequence[i]) {
                gameOver = true
                return
            }
        }

        // next level
        if (newInput.size == sequence.size) {
            level += 1
            addStep()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // TITLE FIXED
        Text(
            text = "SIMON SAYS",
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Level: $level",
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (!started) {
            Button(onClick = { startGame() }) {
                Text("Start")
            }
        }
//Game Over Title
        if (gameOver) {
            Text("GAME OVER",
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(10.dp)
            )
            Button(onClick = { startGame() }) {
                Text(
                    text= "Restart"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

      //when clicking on the boxes
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            ColorBox(colors[0], flashIndex == 0, { handleTap(0) })
            ColorBox(colors[1], flashIndex == 1, { handleTap(1) })
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            ColorBox(colors[2], flashIndex == 2, { handleTap(2) })
            ColorBox(colors[3], flashIndex == 3, { handleTap(3) })
        }
    }
}

@Composable
fun ColorBox(color: Color, flash: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(140.dp)
            .height(250.dp)
            .background(if (flash) Color.White else color)
            .clickable { onClick() }
    )
}