package com.example.testapplication.services


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.testapplication.data.entities.SumInterface
import io.jetchart.common.animation.fadeInAnimation
import io.jetchart.pie.PieChart
import io.jetchart.pie.Pies
import io.jetchart.pie.Slice
import io.jetchart.pie.renderer.FilledSliceDrawer



@Composable
fun PieChartComposable(data:List<SumInterface>) {
    val pies:MutableList<Slice> = mutableListOf()
    val legend:MutableList<Pair<String,Color>> = mutableListOf()

    for (i in 0 until data.size) {

        pies.add(Slice(data[i].GetValue().toFloat(),data[i].GetColor()))
        legend.add(Pair<String,Color>(data[i].GetName(),data[i].GetColor()))
    }

    Column(modifier = Modifier){
            PieChart(
                pies = Pies(pies),
                modifier = Modifier
                    .padding(horizontal = 50.dp)
                    .height(300.dp)
                ,

                animation = fadeInAnimation(1000),
                sliceDrawer = FilledSliceDrawer(thickness = 60f)
            )

        legend.forEach { elem ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .horizontalScroll(ScrollState(0)),
                horizontalArrangement = Arrangement.Start
            ) {
                Canvas(
                    modifier = Modifier
                        .size(10.dp, 10.dp)
                        .padding(10.dp)
                ) {

                    drawCircle(
                        color = elem.second,
                        center = center,
                        radius = 5.dp.toPx()
                    )
                }
                Text(modifier = Modifier.padding(horizontal = 10.dp), text = elem.first)
            }
        }

    }

}
//Получение случайного цвета
fun GetRandomColor():Color{
    return Color(
        red = (0..255).random(),
        blue = (0..255).random(),
        green = (0..255).random()
    )
}