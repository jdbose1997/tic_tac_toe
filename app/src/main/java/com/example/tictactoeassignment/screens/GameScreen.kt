package com.example.tictactoeassignment.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.BoardCellValue
import com.example.domain.GameState
import com.example.domain.PlayerAction
import com.example.domain.VictoryType
import com.example.tictactoeassignment.ui.theme.*
import com.example.tictactoeassignment.viewmodels.GameViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    sessionId: String
) {

    val state = viewModel.state
    viewModel.gameSessionId = sessionId
    viewModel.observeGameBoardMovements()
    viewModel.checkForRematch()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(state.hasWon || state.hasGameDrawn){
                Text(
                    text = state.hintText,
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Italic
                )
            }
            if(state.isRematchAsking){
                ShowReMatchAlertDialog(viewModel = viewModel)
            }
        }
        Text(
            text = "Tic Tac Toe",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive,
            color = BlueCustom
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))
                .background(GrayBackground),
            contentAlignment = Alignment.Center
        ) {
            BoardBase()
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1f),
                cells = GridCells.Fixed(3)
            ) {
                state.userInputs.forEach { (cellNo, boardCellValue) ->
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                ) {
                                    if (state.isCurrentPlayerMove) {
                                        viewModel.onAction(
                                            PlayerAction.BoardTapped(cellNo)
                                        )
                                    }
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (boardCellValue == BoardCellValue.CIRCLE.name) {
                                CircleAnimation()
                            } else if (boardCellValue == BoardCellValue.CROSS.name) {
                                CrossAnimation()
                            }

                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DrawVictoryLine(state = state)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if(!state.hasWon){
                Text(text = if(state.isCurrentPlayerMove) "Your Turn" else "Opponent's Turn", fontSize = 16.sp)
            }
            if(state.hasWon || state.hasGameDrawn){
                Button(
                    onClick = {
                        viewModel.onAction(
                            PlayerAction.AskRematch
                        )
                    },
                    shape = RoundedCornerShape(5.dp),
                    elevation = ButtonDefaults.elevation(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = BlueCustom,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Play Again",
                        fontSize = 16.sp
                    )
                }
            }

        }
    }
}

@Composable
fun DrawVictoryLine(
    state: GameState
) {
    when (state.victoryType) {
        VictoryType.HORIZONTAL1 -> WinHorizontalLine1()
        VictoryType.HORIZONTAL2 -> WinHorizontalLine2()
        VictoryType.HORIZONTAL3 -> WinHorizontalLine3()
        VictoryType.VERTICAL1 -> WinVerticalLine1()
        VictoryType.VERTICAL2 -> WinVerticalLine2()
        VictoryType.VERTICAL3 -> WinVerticalLine3()
        VictoryType.DIAGONAL1 -> WinDiagonalLine1()
        VictoryType.DIAGONAL2 -> WinDiagonalLine2()
        VictoryType.NONE -> {}
    }
}

@Composable
fun ShowReMatchAlertDialog(viewModel: GameViewModel){
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Lets Play Again!")
            },
            text = {
                Column {
                    Text("Opponent is asking for a remtach!")
                    Spacer(modifier = Modifier.height(10.dp))
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement =  Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = Modifier.wrapContentWidth().weight(1f),
                        onClick = {
                        viewModel.onAction(PlayerAction.RematchAccept)}
                    ) {
                        Text("Play")
                    }

                    Button(
                        modifier = Modifier.wrapContentWidth().weight(1f),
                        onClick = {

                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        )
    }
}

