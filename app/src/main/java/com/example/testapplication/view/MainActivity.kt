package com.example.testapplication.view

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.testapplication.data.NoteViewModel

@Suppress("UNCHECKED_CAST")
class NoteViewModelFactory(val application: Application):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(application) as T
    }

}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            val owner = LocalViewModelStoreOwner.current
            owner?.let {
                val viewModel:NoteViewModel = viewModel (
                    it,
                    "NoteViewModel",
                    NoteViewModelFactory(LocalContext.current.applicationContext as Application)
                )
                Main(viewModel)
            }



        }
    }
}
//Навигация с помощью NavHost и NavController
@Composable
fun Main(vm:NoteViewModel = viewModel()){
    val navController = rememberNavController()
    NavHost(navController=navController, startDestination = "MainScreen"){
        composable(route = "MainScreen") {
            MainScreen(navController,vm)
        }
        composable(route = "MainScreen/{index}", arguments = listOf(
            navArgument(name = "index"){
                type=NavType.IntType
            }
        )){
                index->
            MainScreen(index=index.arguments?.getInt("index"), vm = vm, navController = navController)
        }
        composable(route = "MainScreen/{index}/{pattern}", arguments = listOf(
            navArgument(name = "index"){
                type=NavType.IntType
            },
            navArgument(name = "pattern"){
                type=NavType.StringType
            }
        )){
            obj->
            MainScreen(index=obj.arguments?.getInt("index"), vm = vm, navController = navController, pattern = obj.arguments?.getString("pattern"))
        }
        composable(route = "StatisticsScreen/{pattern}", arguments = listOf(
            navArgument(name = "pattern"){
                type=NavType.StringType
            }
        )) {
                pattern->
            StatisticsScreen(pattern=pattern.arguments?.getString("pattern"),vm=vm, navController = navController)
        }
        composable(route = "DetailScreen/{index}", arguments = listOf(
            navArgument(name = "index"){
                type=NavType.IntType
            }
        )){
                index->
            DetailScreen(id=index.arguments?.getInt("index"),vm=vm, navController = navController)
        }
        composable(route="AddNoteScreen") {
            AddNoteScreen(vm=vm, navController = navController)
        }
        composable(route="SelectPeriod") {
            SelectPeriod(vm=vm, navController = navController)
        }
    }
}


