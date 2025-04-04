package com.example.testapplication.view


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.testapplication.R
import com.example.testapplication.data.entities.Note
import com.example.testapplication.data.NoteViewModel
import com.example.testapplication.ui.theme.Black
import com.example.testapplication.ui.theme.GreyGreen
import com.example.testapplication.ui.theme.VeryWhiteYellowGreen
import com.example.testapplication.ui.theme.White
import com.example.testapplication.ui.theme.jost


import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale


//Добавление новой note
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(vm:NoteViewModel= viewModel(),navController: NavController) {

    Scaffold (topBar = {
        TopAppBar(colors = TopAppBarColors(containerColor = White,
            scrolledContainerColor = White,
            navigationIconContentColor = White,
            titleContentColor = GreyGreen,
            actionIconContentColor = GreyGreen), modifier = Modifier.background(White),title = {Text(modifier = Modifier.background(White), fontFamily = jost, text="My finances", fontWeight = FontWeight.Bold, color = com.example.testapplication.ui.theme.GreyGreen)},
        )
    },

        modifier = Modifier.background(White)){
        val selectedDate = remember {
            mutableStateOf(
                LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        }
        val category = remember { mutableStateOf("") }
        val description = remember { mutableStateOf("") }
        val sum = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(top = it.calculateTopPadding()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
            .padding(top = 50.dp)) {


            DatePickerFieldToModal(selectedDate = selectedDate)
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp), value = category.value,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = White,
                    focusedContainerColor = White,
                    focusedLabelColor = GreyGreen,
                    focusedPlaceholderColor = GreyGreen,
                    focusedIndicatorColor = GreyGreen,
                    focusedLeadingIconColor = GreyGreen,
                    cursorColor = GreyGreen
                ),
                maxLines = 1,
                label = { Text(stringResource(R.string.Category), fontFamily = jost) },
                onValueChange = { newText -> category.value = newText })
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp), value = description.value,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = White,
                    focusedContainerColor = White,
                    focusedLabelColor = GreyGreen,
                    focusedPlaceholderColor = GreyGreen,
                    focusedIndicatorColor = GreyGreen,
                    focusedLeadingIconColor = GreyGreen,
                    cursorColor = GreyGreen
                ),
                maxLines = 1,
                label = { Text(stringResource(R.string.Description), fontFamily = jost) },
                onValueChange = { newText -> description.value = newText })
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = White,
                    focusedContainerColor = White,
                    focusedLabelColor = GreyGreen,
                    focusedPlaceholderColor = GreyGreen,
                    focusedIndicatorColor = GreyGreen,
                    focusedLeadingIconColor = GreyGreen,
                    cursorColor = GreyGreen
                ),
                maxLines = 1,
                value = sum.value,
                label = { Text(stringResource(R.string.Sum), fontFamily = jost) },
                onValueChange = { newText -> sum.value = newText },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

            Row(horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(bottom = 30.dp, end = 20.dp, start = 20.dp)) {
                //Отмена добавления
                TextButton(
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
                    Text(fontSize = 20.sp,text=stringResource(R.string.Cancel), fontFamily = jost)

                }
                //Подтверждение добавления
                TextButton(
                    onClick = {
                        vm.addNote(
                            Note(
                                date = convertMillisToDate(selectedDate.value),
                                category = category.value,
                                description = description.value,
                                sum = sum.value
                            )
                        )
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = White,
                        disabledContainerColor = VeryWhiteYellowGreen,
                        contentColor = GreyGreen,
                        disabledContentColor = Black
                    )
                ) {
                    Text(fontSize = 20.sp,text=stringResource(R.string.Add), fontFamily = jost)

                }

            }

        }

    }
}


//DatePicker для выбора даты
@Composable
fun DatePickerFieldToModal(modifier: Modifier = Modifier,selectedDate:MutableState<Long>) {

    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = convertMillisToDate(selectedDate.value),
        onValueChange = { },
        label = { Text(stringResource(R.string.Date), fontFamily = jost) },
        placeholder = { Text("MM.DD.YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        colors = TextFieldDefaults.colors(unfocusedContainerColor = White,
            focusedContainerColor = White,
            focusedLabelColor = GreyGreen,
            focusedPlaceholderColor = GreyGreen,
            focusedIndicatorColor = GreyGreen,
            focusedLeadingIconColor = GreyGreen,
            cursorColor = GreyGreen
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .pointerInput(selectedDate)

            {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate.value = it ?: 0 },
            onDismiss = { showModal = false }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        colors = DatePickerDefaults.colors(
            containerColor = White,
            selectedDayContainerColor = GreyGreen,
            selectedYearContainerColor = GreyGreen,
            dateTextFieldColors = TextFieldDefaults.colors(unfocusedContainerColor = White,
                focusedContainerColor = White,
                focusedLabelColor = GreyGreen,
                focusedPlaceholderColor = GreyGreen,
                focusedIndicatorColor = GreyGreen,
                focusedLeadingIconColor = GreyGreen,
                cursorColor = GreyGreen
            ),
            dividerColor = GreyGreen,
            currentYearContentColor = Black,
            disabledDayContentColor = GreyGreen,
            todayDateBorderColor = GreyGreen,



        ),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    disabledContainerColor = VeryWhiteYellowGreen,
                    contentColor = GreyGreen,
                    disabledContentColor = Black
                )) {
                Text(stringResource(R.string.Ok), fontFamily = jost)
            }
        },
        dismissButton = {

            TextButton(onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    disabledContainerColor = VeryWhiteYellowGreen,
                    contentColor = GreyGreen,
                    disabledContentColor = Black
                )) {

                Text(stringResource(R.string.Cancel), fontFamily = jost)
            }
        }
    ) {
        DatePicker(state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = White,
                selectedDayContainerColor = GreyGreen,
                selectedYearContainerColor = GreyGreen,
                dateTextFieldColors = TextFieldDefaults.colors(unfocusedContainerColor = White,
                    focusedContainerColor = White,
                    focusedLabelColor = GreyGreen,
                    focusedPlaceholderColor = GreyGreen,
                    focusedIndicatorColor = GreyGreen,
                    focusedLeadingIconColor = GreyGreen,
                    cursorColor = GreyGreen
                ),
                dividerColor = GreyGreen,
                currentYearContentColor = Black,
                disabledDayContentColor = GreyGreen,
                todayDateBorderColor = GreyGreen,
                todayContentColor = GreyGreen


            )
        )
    }
}


fun convertMillisToDate(millis: Long): String {

    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}