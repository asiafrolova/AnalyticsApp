package com.example.testapplication.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.testapplication.R
import com.example.testapplication.data.NoteViewModel
import com.example.testapplication.data.entities.SumInterface
import com.example.testapplication.services.BarChartComposable
import com.example.testapplication.services.PieChartComposable
import com.example.testapplication.ui.theme.Black
import com.example.testapplication.ui.theme.GreyGreen
import com.example.testapplication.ui.theme.VeryWhiteYellowGreen
import com.example.testapplication.ui.theme.White
import com.example.testapplication.ui.theme.jost
import kotlin.math.abs
import kotlin.math.max

// Экран для статистики

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatisticsScreen(vm: NoteViewModel = viewModel(),navController: NavController,pattern:String?="__.__.____"){

    //Получение данных в отдельном потоке по категориям трат за определенный период
   val filteredCategoriesSumSpend by vm.allNotesSumCategoriesSpend.collectAsState()
    LaunchedEffect(Unit) {
        vm.getSumInCategoriesSpend(pattern!!)
    }
    //Получение данных в отдельном по категориям поступлений за определенный период
    val filteredCategoriesSumIncome by vm.allNotesSumCategoriesIncome.collectAsState()
    LaunchedEffect(Unit) {
        vm.getSumInCategoriesIncome(pattern!!)
    }
    val yearSumSpend by vm.SumYearSpend.collectAsState()
    val yearSumIncome by vm.SumYearIncome.collectAsState()

    val monthSumSpend by vm.SumMonthSpend.collectAsState()
    val monthSumIncome by vm.SumMonthIncome.collectAsState()

    val daySumSpend by vm.SumDaySpend.collectAsState()
    val daySumIncome by vm.SumDayIncome.collectAsState()


    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFFFFFF))
        .padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        //Если в паттерне не указан год/месяц/день получения данных по годам/месяцам/дням
        var mode=""
        if(pattern?.subSequence(6,10)=="____"){
            LaunchedEffect(Unit) {
                vm.getSumInYearSpend(pattern)
                vm.getSumInYearIncome(pattern)
            }
            mode="year"
        }else if(pattern?.subSequence(3,5)=="__"){

            LaunchedEffect(Unit) {
                vm.getSumInMonthSpend(pattern)
                vm.getSumInMonthIncome(pattern)
            }
            mode="month"
        }else if(pattern?.subSequence(0,2)=="__"){
            LaunchedEffect(Unit) {
                vm.getSumInDaySpend(pattern)
                vm.getSumInDayIncome(pattern)
            }
            mode="day"
        }



            //Отображение графиков, если данных нет отображение Text("Данных нет")
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White
            ) {
                val scrollState = rememberScrollState()

                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)) {


                        Row(modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),horizontalArrangement = Arrangement.Center) {
                            if(filteredCategoriesSumSpend.size==0){
                                Text(text="Нет данных", fontSize = 20.sp,
                                    modifier = Modifier.padding(10.dp),
                                    fontFamily = jost)
                            }else {
                                PieChartComposable(filteredCategoriesSumSpend)

                            }
                        }
                    Row(modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),horizontalArrangement = Arrangement.Center ){
                        if(filteredCategoriesSumSpend.size>0) {
                            when (mode) {
                                "year" -> BarChartComposable(
                                    yearSumSpend,
                                    CalculateHeight(listOf(yearSumSpend, yearSumIncome))
                                )

                                "month" -> BarChartComposable(
                                    monthSumSpend,
                                    CalculateHeight(listOf(monthSumSpend, monthSumIncome))
                                )

                                "day" -> BarChartComposable(
                                    daySumSpend,
                                    CalculateHeight(listOf(daySumSpend, daySumIncome))
                                )

                            }
                        }
                    }

                        Row(modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),horizontalArrangement = Arrangement.Center) {
                            if(filteredCategoriesSumIncome.size==0){
                                Text(text="Нет данных", fontSize = 20.sp,
                                    modifier = Modifier.padding(10.dp),
                                    fontFamily = jost)
                            }else{
                                PieChartComposable(filteredCategoriesSumIncome)
                            }


                        }

                            Row(modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),horizontalArrangement = Arrangement.Center ){
                                if(filteredCategoriesSumIncome.size>0) {
                                    when (mode) {
                                        "year" -> BarChartComposable(
                                            yearSumIncome,
                                            CalculateHeight(listOf(yearSumSpend, yearSumIncome))
                                        )

                                        "month" -> BarChartComposable(
                                            monthSumIncome,
                                            CalculateHeight(listOf(monthSumSpend, monthSumIncome))
                                        )

                                        "day" -> BarChartComposable(
                                            daySumIncome,
                                            CalculateHeight(listOf(daySumSpend, daySumIncome))
                                        )

                                    }
                                }
                            }
                    //Кнопка изменения периода
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        TextButton(shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 40.dp)
                                .shadow(7.dp, RoundedCornerShape(20.dp))
                                ,
                            onClick = { navController.navigate("MainScreen/${3}") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = White,
                                disabledContainerColor = VeryWhiteYellowGreen,
                                contentColor = GreyGreen,
                                disabledContentColor = Black
                            )
                        )
                        {
                            Text(stringResource(R.string.Select_period), modifier = Modifier.padding(10.dp), fontSize = 20.sp, fontFamily = jost)
                        }
                    }



                    }
                }
        }
    }




//Вычисление масштаба отображения столбчатой диаграммы в зависимости от максимального значения
fun CalculateHeight(data:List<List<SumInterface>>):Float{
    var maxSum=1
    for (datum in data) {
        for (sumInterface in datum) {
            maxSum=max(maxSum,abs(sumInterface.GetValue()))
        }
    }
    var del=1f
    while(maxSum<100 || maxSum>1000){
        if(maxSum<100){
            del*=10
            maxSum*=10
        }else{
            maxSum/=10
            del/=10
        }
    }
    return del


}
