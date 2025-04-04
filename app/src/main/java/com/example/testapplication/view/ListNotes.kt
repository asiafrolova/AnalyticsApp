package com.example.testapplication.view

import android.annotation.SuppressLint
import android.util.Log


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.testapplication.data.entities.Note
import com.example.testapplication.ui.theme.GreyMain
import com.example.testapplication.ui.theme.jost
import androidx.compose.material3.AlertDialog as AlertDialog

//Лист из Note
@SuppressLint("UnrememberedMutableState")
@Composable
fun ListItems( navController: NavController, itemList: List<Note>, delete:(Int)->Unit){

    LazyColumn (modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(10.dp)){
        items(items = itemList, key={it.id}){ NoteItem(note=it,navController=navController,modifier=Modifier.animateItem(),{delete(it)}/*itemList=itemList*/) }

    }
}
//Одна Note
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(note: Note, navController: NavController, modifier: Modifier, delete:(Int)->Unit/*itemList:SnapshotStateList<Note>*/){
    Card(modifier= modifier.then(Modifier
        .padding(bottom = 10.dp)),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(7.dp),
        colors = CardDefaults.cardColors(com.example.testapplication.ui.theme.White),

    ){
        val openDialog = remember { mutableStateOf(false) }
        Row ( modifier = Modifier
            .fillMaxSize()
            //Обратботка долгого и короткого нажатия на note
            .combinedClickable(onClick = {navController.navigate(route="DetailScreen/${note.id}")
                Log.d("TAG","Click on "+note.id)},
                onLongClick = {openDialog.value=true

                },),
            verticalAlignment = Alignment.CenterVertically){
            Text(note.sum.toString(), fontSize = 20.sp, color = if (note.sum<0) com.example.testapplication.ui.theme.NegativeSum else com.example.testapplication.ui.theme.PositiveSum,
                modifier = Modifier
                    .widthIn(max = 100.dp)
                    .padding(start = 10.dp, top = 5.dp, bottom = 5.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = jost)
            //Категория Note
            Text(note.category,fontSize = 20.sp,
                modifier = Modifier
                    .widthIn(max = 100.dp)
                    .padding(start = 7.dp, top = 5.dp, bottom = 5.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = jost)
            //Дата Note
            Text(note.dateStr,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = 7.dp, top = 5.dp, bottom = 5.dp, end = 7.dp),
                fontFamily = jost
            )
        }

        //Подтверждение удаления Note
        if (openDialog.value) {
            AlertDialog(

                containerColor = com.example.testapplication.ui.theme.VeryWhiteYellowGreen,
                onDismissRequest = { openDialog.value = false},
                title = { Text(text = "Подтверждение действия", fontFamily = jost) },
                text = { Text("Вы действительно хотите удалить выбранный элемент?", fontFamily = jost) },
                dismissButton = {
                    Button({ openDialog.value = false

                    }, colors = ButtonColors(containerColor = com.example.testapplication.ui.theme.VeryWhiteYellowGreen,
                        contentColor = com.example.testapplication.ui.theme.DarkGrey,
                        disabledContentColor =com.example.testapplication.ui.theme.WhiteYellowGreen ,
                        disabledContainerColor = com.example.testapplication.ui.theme.GreyMain)
                    ) {
                        Text("Отмена", fontSize = 20.sp, fontFamily = jost)
                    }
                },
                confirmButton = {
                    Button({ openDialog.value = false
                        //Удаление Note из LazyColumn и из Repository
                        delete(note.id)

                    }, colors = ButtonColors(containerColor = com.example.testapplication.ui.theme.GreyGreen,
                        contentColor = com.example.testapplication.ui.theme.White,
                        disabledContentColor =com.example.testapplication.ui.theme.GreyGreenShadow ,
                        disabledContainerColor = com.example.testapplication.ui.theme.WhiteShadow)
                        ) {
                        Text("Ок", fontSize = 20.sp, fontFamily = jost)
                    }
                }
            )
        }

    }
}



