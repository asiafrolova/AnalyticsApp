package com.example.testapplication.services


import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testapplication.data.entities.SumInterface
import io.jetchart.bar.Bar
import io.jetchart.bar.BarChart
import io.jetchart.bar.Bars
import io.jetchart.bar.renderer.label.SimpleBarLabelDrawer
import io.jetchart.bar.renderer.xaxis.BarXAxisDrawer
import io.jetchart.bar.renderer.yaxis.BarEmptyYAxisDrawer
import io.jetchart.common.animation.fadeInAnimation
import kotlin.math.abs

@Composable
fun BarChartComposable(data:List<SumInterface>, del:Float) {
    val numberOfBars = data.size
    val width = numberOfBars * 80
    //Получение максимального значения для регулирования высоты диаграммы
    var maxSum=1f
    for (datum in data) {
        if(abs(datum.GetValue())>maxSum){
            maxSum=abs(datum.GetValue()).toFloat()
        }

    }


    BarChart(bars = Bars(
        bars = data.map {elem->
            Bar(label = elem.GetName(), value =abs(elem.GetValue())*150f/maxSum, color = com.example.testapplication.ui.theme.PurpleGrey40)
        }),
        modifier = Modifier.horizontalScroll(rememberScrollState()).width(width.dp).height(110.dp),
        animation = fadeInAnimation(500),
        xAxisDrawer = BarXAxisDrawer(),

        yAxisDrawer = BarEmptyYAxisDrawer(),
        labelDrawer = SimpleBarLabelDrawer(),
        valueDrawer = MyBarValueDrawer(del=maxSum),
        barHorizontalMargin = 5.dp
    )
}