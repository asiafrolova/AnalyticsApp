package com.example.testapplication.view


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column



import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.BuildCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.testapplication.data.NoteViewModel
import com.example.testapplication.ui.theme.GreyGreen
import com.example.testapplication.ui.theme.White
import com.example.testapplication.ui.theme.jost

import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, vm: NoteViewModel = viewModel(), index:Int?=0, pattern:String?="__.__.____"){

    val openDialog = remember { mutableStateOf(false) }
    val noteList by vm.noteList.observeAsState(listOf())
    val todayNoteList by vm.todayNoteList.observeAsState(listOf())

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = index!!, pageCount = { HomeTab.entries.size})

    val selectedTabIndex = remember{ derivedStateOf { pagerState.currentPage}}

    Scaffold (topBar = {TopAppBar( title = {Text(text="My finances", fontFamily = jost, fontWeight = FontWeight.Bold, color = com.example.testapplication.ui.theme.GreyGreen)},
        modifier = Modifier.background(White).clickable { openDialog.value=true},
        colors = TopAppBarColors(containerColor = White,
            scrolledContainerColor = White,
            navigationIconContentColor = White,
            titleContentColor = GreyGreen,
            actionIconContentColor = GreyGreen
        )
    )},
        floatingActionButton = { AddFloatingButton(navController) },
        modifier = Modifier.background(Color.White)){

        Column (modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding())
            .background(Color.White), verticalArrangement = Arrangement.Top){
            TabRow(containerColor = White, selectedTabIndex=selectedTabIndex.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .background(White, shape = RoundedCornerShape(50)),
                indicator = { _: List<TabPosition> ->
                    Box {}
                }) {
                HomeTab.entries.forEachIndexed { index, homeTab ->
                    val selected = selectedTabIndex.value==index
                    Tab(
                        selected = selected,
                        modifier = if (selected)
                        Modifier.padding(horizontal = 3.dp).background(com.example.testapplication.ui.theme.WhiteYellowGreen, shape = RoundedCornerShape(50))
                        else
                        Modifier.padding(horizontal = 3.dp).background(White, shape = RoundedCornerShape(50)),
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(homeTab.ordinal) }
                        },
                        text = {Text(text=homeTab.text, color =GreyGreen, fontFamily = jost, fontSize = 15.sp)}
                    )

                }
            }
            HorizontalPager(state = pagerState,

                modifier = Modifier.fillMaxWidth().weight(1f)
                    .background(White)) {
                Box(modifier = Modifier.fillMaxSize()
                    .padding(10.dp)
                    .background(White),
                    contentAlignment = Alignment.TopCenter
                ){
                    if (index==3 && selectedTabIndex.value==2){
                        SelectPeriod(vm=vm, navController = navController)
                    }else {
                        when (selectedTabIndex.value) {
                            0 -> ListItems(
                                navController,
                                todayNoteList,
                                { vm.deleteNote(it) })

                            1 -> ListItems(
                                navController,
                                noteList,
                                { vm.deleteNote(it) })

                            2 -> StatisticsScreen(vm = vm, navController = navController, pattern = pattern)

                        }
                    }


                }
            }
        }


    }
    //Диалог "о приложении"
    if (openDialog.value) {
        AlertDialog(

            containerColor = com.example.testapplication.ui.theme.VeryWhiteYellowGreen,
            onDismissRequest = { openDialog.value = false},
            title = { Text(text = "О приложении", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = jost) },
            text = { Text("Версия - ${1.0}\nmin Android - ${9.0}\nРазработчик - asetrr (github asiafrolova)", fontFamily = jost) },
            confirmButton = {
                Button({ openDialog.value = false


                }, colors = ButtonColors(containerColor = GreyGreen,
                    contentColor = White,
                    disabledContentColor =com.example.testapplication.ui.theme.GreyGreenShadow ,
                    disabledContainerColor = com.example.testapplication.ui.theme.WhiteShadow)
                ) {
                    Text("Ок", fontSize = 20.sp, fontFamily = jost)
                }
            }
        )
    }

}
//Добавление новых note
@Composable
fun AddFloatingButton(navController: NavController){
    FloatingActionButton(onClick = {
        navController.navigate("AddNoteScreen")
        }, containerColor = com.example.testapplication.ui.theme.YellowGreen, contentColor = com.example.testapplication.ui.theme.GreyGreen) {
        Icon(Icons.Filled.Add, contentDescription = "Add")
    }
}
//Меню
enum class HomeTab(

    val text:String
){
    TodayNotes(
        text = "Сегодня"
    ),
    AllNotes(
        text = "Все записи"
    ),
    Statistics(
        text = "Статистика"
    )
}