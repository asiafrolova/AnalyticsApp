package com.example.testapplication.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.testapplication.R
import com.example.testapplication.data.NoteViewModel
import com.example.testapplication.ui.theme.Black
import com.example.testapplication.ui.theme.GreyGreen
import com.example.testapplication.ui.theme.VeryWhiteYellowGreen
import com.example.testapplication.ui.theme.White
import com.example.testapplication.ui.theme.jost
//Все возможные месяца и дни (__  означет любой день/месяц)
val months =(12 downTo 1).toList().map{i->i.toString() }.plus("__").reversed()
val days =(31 downTo 1).toList().map { i->i.toString() }.plus("__").reversed()
//Экран для выбора периода для статистики
@Composable
fun SelectPeriod(vm:NoteViewModel= viewModel(),navController: NavController){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFFFFFF))
        .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Все возможные года из базы данных
        val years by vm.yearsInDatabase.observeAsState(mutableListOf())
        if (!("____" in years)){
            years.add(0,"____")
        }
        var selectedOptionTextYear by remember { mutableStateOf("____") }
        var expandedYear by remember { mutableStateOf(false) }
        var textfieldSizeYear by remember { mutableStateOf(Size.Zero) }
        val iconYear = if (expandedYear)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        var selectedOptionTextMonth by remember { mutableStateOf("__") }
        var expandedMonth by remember { mutableStateOf(false) }
        var textfieldSizeMonth by remember { mutableStateOf(Size.Zero) }
        val iconMonth = if (expandedMonth)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        var selectedOptionTextDay by remember { mutableStateOf("__") }
        var expandedDay by remember { mutableStateOf(false) }
        var textfieldSizeDay by remember { mutableStateOf(Size.Zero) }
        val iconDay = if (expandedDay)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {


                Row(modifier = Modifier.fillMaxWidth(0.7f)) {
                    OutlinedTextField(
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = White,
                            focusedContainerColor = White,
                            focusedLabelColor = GreyGreen,
                            focusedPlaceholderColor = GreyGreen,
                            focusedIndicatorColor = GreyGreen,
                            focusedLeadingIconColor = GreyGreen,
                            cursorColor = GreyGreen
                        ),
                        value = selectedOptionTextYear,
                        onValueChange = { selectedOptionTextYear = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                textfieldSizeYear = coordinates.size.toSize()
                            },
                        label = { Text("Year", fontFamily = jost) },
                        trailingIcon = {
                            Icon(iconYear, "contentDescription",
                                Modifier.clickable { expandedYear = !expandedYear })
                        }
                    )
                    if (expandedYear) {
                        DropdownMenu(
                            containerColor = White,
                            expanded = expandedYear,
                            onDismissRequest = { expandedYear = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { textfieldSizeYear.width.toDp() })
                        ) {
                            years.forEach { label ->
                                DropdownMenuItem(

                                    text = {
                                        Text(text = label, fontFamily = jost)
                                    }, onClick = {
                                        expandedYear = !expandedYear
                                        selectedOptionTextYear = label
                                    })
                            }
                        }
                    }

                }

                Row(modifier = Modifier.fillMaxWidth(0.7f)) {
                    OutlinedTextField(
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = White,
                            focusedContainerColor = White,
                            focusedLabelColor = GreyGreen,
                            focusedPlaceholderColor = GreyGreen,
                            focusedIndicatorColor = GreyGreen,
                            focusedLeadingIconColor = GreyGreen,
                            cursorColor = GreyGreen
                        ),
                        value = selectedOptionTextMonth,
                        onValueChange = { selectedOptionTextMonth = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                textfieldSizeMonth = coordinates.size.toSize()
                            },
                        label = { Text("Month", fontFamily = jost) },
                        trailingIcon = {
                            Icon(iconMonth, "contentDescription",
                                Modifier.clickable { expandedMonth = !expandedMonth })
                        }
                    )
                    if (expandedMonth) {
                        DropdownMenu(
                            containerColor = White,
                            expanded = expandedMonth,
                            onDismissRequest = { expandedMonth = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { textfieldSizeYear.width.toDp() })
                        ) {
                            months.forEach { label ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = label, fontFamily = jost)
                                    }, onClick = {
                                        expandedMonth = !expandedMonth
                                        selectedOptionTextMonth = label
                                    })
                            }
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(0.7f)) {
                    OutlinedTextField(
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = White,
                            focusedContainerColor = White,
                            focusedLabelColor = GreyGreen,
                            focusedPlaceholderColor = GreyGreen,
                            focusedIndicatorColor = GreyGreen,
                            focusedLeadingIconColor = GreyGreen,
                            cursorColor = GreyGreen
                        ),
                        value = selectedOptionTextDay,
                        onValueChange = { selectedOptionTextDay = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                textfieldSizeDay = coordinates.size.toSize()
                            },
                        label = { Text("Day", fontFamily = jost) },
                        trailingIcon = {
                            Icon(iconDay, "contentDescription",
                                Modifier.clickable { expandedDay = !expandedDay })
                        }
                    )
                    if (expandedDay) {
                        DropdownMenu(
                            containerColor = White,
                            expanded = expandedDay,
                            onDismissRequest = { expandedDay = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { textfieldSizeDay.width.toDp() })
                        ) {
                            days.forEach { label ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = label, fontFamily = jost)
                                    }, onClick = {
                                        expandedDay = !expandedDay
                                        selectedOptionTextDay = label
                                    })
                            }
                        }
                    }
                }
            }
            //Подверждение выбора нового периода
            TextButton(
                onClick = {
                    selectedOptionTextDay =
                        if (selectedOptionTextDay.length == 1) "0" + selectedOptionTextDay else selectedOptionTextDay
                    selectedOptionTextMonth =
                        if (selectedOptionTextMonth.length == 1) "0" + selectedOptionTextMonth else selectedOptionTextMonth
                    val pattern =
                        selectedOptionTextDay + "." + selectedOptionTextMonth + "." + selectedOptionTextYear
                    navController.navigate("MainScreen/${2}/${pattern}")

                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(vertical = 20.dp).shadow(7.dp, RoundedCornerShape(20.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    disabledContainerColor = VeryWhiteYellowGreen,
                    contentColor = GreyGreen,
                    disabledContentColor = Black
                )
            ) {
                Text(stringResource(R.string.Select_period), modifier = Modifier.padding(10.dp), fontSize = 20.sp, fontFamily = jost)
            }
        }
    }
}