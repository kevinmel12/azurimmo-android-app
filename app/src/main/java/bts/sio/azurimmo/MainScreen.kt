package bts.sio.azurimmo

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import bts.sio.azurimmo.views.AppHeader
import bts.sio.azurimmo.views.AppNavigation
import bts.sio.azurimmo.views.appartement.AppartementList
import bts.sio.azurimmo.views.batiment.BatimentList

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { AppHeader() } // TopBar commune à tous les écrans
    ) { innerPadding ->
        AppNavigation(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}