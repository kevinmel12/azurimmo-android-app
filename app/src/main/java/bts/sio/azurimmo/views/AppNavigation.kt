package bts.sio.azurimmo.views

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import bts.sio.azurimmo.views.appartement.AppartementAdd
import bts.sio.azurimmo.views.appartement.AppartementList
import bts.sio.azurimmo.views.batiment.BatimentAdd
import bts.sio.azurimmo.views.batiment.BatimentList
import bts.sio.azurimmo.views.contrat.ContratAdd
import bts.sio.azurimmo.views.contrat.ContratList

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "batiment_list",
        modifier = modifier
    ) {
        // Liste des bâtiments avec bouton d'ajout (page de base)
        composable("batiment_list") {
            BatimentList(
                onBatimentClick = { batimentId ->
                    navController.navigate("batiment_appartements_list/$batimentId")
                },
                onAddBatimentClick = {
                    navController.navigate("add_batiment")
                }
            )
        }

        // Liste des appartements avec bouton d'ajout
        composable(
            route = "batiment_appartements_list/{batimentId}",
            arguments = listOf(navArgument("batimentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val batimentId = backStackEntry.arguments?.getInt("batimentId")
            if (batimentId != null) {
                AppartementList(
                    batimentId = batimentId,
                    viewModel = viewModel(),
                    onAddAppartementClick = {
                        navController.navigate("add_appartement/$batimentId")
                    },
                    onAppartementClick = { appartementId ->
                        navController.navigate("appartement_contrat/$appartementId")
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Erreur : Identifiant de bâtiment manquant")
            }
        }

        // Contrat d'un appartement
        composable(
            route = "appartement_contrat/{appartementId}",
            arguments = listOf(navArgument("appartementId") { type = NavType.IntType })
        ) { backStackEntry ->
            val appartementId = backStackEntry.arguments?.getInt("appartementId")
            if (appartementId != null) {
                ContratList(
                    appartementId = appartementId,
                    onAddContratClick = {
                        navController.navigate("add_contrat/$appartementId")
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Erreur : Identifiant d'appartement manquant")
            }
        }

        composable("add_batiment") {
            BatimentAdd(
                onBatimentAdd = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Route pour ajouter un contrat à un appartement
        composable("add_contrat/{appartementId}",
            arguments = listOf(navArgument("appartementId") { type = NavType.IntType })
        ) { backStackEntry ->
            val appartementId = backStackEntry.arguments?.getInt("appartementId")
            if (appartementId != null) {
                ContratAdd(
                    onAddContrat = {
                        navController.popBackStack()
                    },
                    appartementId = appartementId,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Erreur : Identifiant d'appartement manquant")
            }
        }

        // Route pour ajouter un appartement
        composable("add_appartement/{batimentId}",
            arguments = listOf(navArgument("batimentId") { type = NavType.IntType })
        )
        { backStackEntry ->
            val batimentId = backStackEntry.arguments?.getInt("batimentId")
            println("Ouverture de add_appartement avec batimentId = $batimentId")

            if (batimentId != null) {
                AppartementAdd(
                    onAddAppartement = { navController.popBackStack()},
                    batimentId = batimentId,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Erreur : Identifiant de bâtiment manquant")
            }
        }
    }
}