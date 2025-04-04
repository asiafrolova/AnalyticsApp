package com.example.testapplication.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.testapplication.R
import com.example.testapplication.data.NoteViewModel
import com.example.testapplication.ui.theme.Black
import com.example.testapplication.ui.theme.GreyGreen
import com.example.testapplication.ui.theme.NegativeSum
import com.example.testapplication.ui.theme.PositiveSum
import com.example.testapplication.ui.theme.VeryWhiteYellowGreen
import com.example.testapplication.ui.theme.White
import com.example.testapplication.ui.theme.jost

//Детализированная информация о note
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(id:Int?, vm:NoteViewModel= viewModel(),navController:NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = White,
                    scrolledContainerColor = White,
                    navigationIconContentColor = White,
                    titleContentColor = GreyGreen,
                    actionIconContentColor = GreyGreen
                ),
                modifier = Modifier.background(White),
                title = {
                    Text(
                        modifier = Modifier.background(White),
                        text = "My finances",
                        fontWeight = FontWeight.Bold,
                        color = GreyGreen,
                        fontFamily = jost
                    )
                },
            )
        },
        modifier = Modifier.background(White)
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(top = it.calculateTopPadding()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //Получение информации о выбранной note в отдельном потоке
        LaunchedEffect(Unit) {
            vm.getNoteById(id!!)
        }
        var note = vm.note
        Column(modifier = Modifier.fillMaxSize().padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween) {

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.
            fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    Text(

                        note.sum.toString(),
                        fontSize = 50.sp,
                        color = if (note.sum < 0) NegativeSum else PositiveSum,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier
                            .padding(top = 30.dp),
                        fontFamily = jost

                    )
                }
                Column(horizontalAlignment = Alignment.Start, modifier = Modifier
                    ) {
                    Text(
                        stringResource(R.string.Category) + ": " + note.category, fontSize = 20.sp,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier
                            .padding(top = 70.dp),
                        fontFamily = jost
                    )
                    Text(
                        stringResource(R.string.Description) + ": " + note.description,
                        fontSize = 20.sp,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier
                            .padding(top = 10.dp),
                        fontFamily = jost
                    )
                    Text(
                        stringResource(R.string.Date) + ": " + note.dateStr, fontSize = 20.sp,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier.padding(top = 10.dp),
                        fontFamily = jost
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()) {
                TextButton(shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.shadow(7.dp, RoundedCornerShape(20.dp)),
                    onClick = {
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = White,
                        disabledContainerColor = VeryWhiteYellowGreen,
                        contentColor = GreyGreen,
                        disabledContentColor = Black
                    )
                ) {
                    Text(fontSize = 20.sp, fontFamily = jost, text = stringResource(R.string.Back), modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp))

                }
            }
        }


    }
}
}