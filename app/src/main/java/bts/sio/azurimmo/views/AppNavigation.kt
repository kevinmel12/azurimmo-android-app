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
import bts.sio.azurimmo.views.appartement.AppartementEdit
import bts.sio.azurimmo.views.appartement.AppartementList
import bts.sio.azurimmo.views.batiment.BatimentAdd
import bts.sio.azurimmo.views.batiment.BatimentEdit
import bts.sio.azurimmo.views.batiment.BatimentList
import bts.sio.azurimmo.views.contrat.ContratAdd
import bts.sio.azurimmo.views.contrat.ContratEdit
import bts.sio.azurimmo.views.contrat.ContratList
import bts.sio.azurimmo.views.locataire.LocataireAdd
import bts.sio.azurimmo.views.locataire.LocataireEdit
import bts.sio.azurimmo.views.locataire.LocataireList

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "batiment_list",
        modifier = modifier
    ) {
        // Liste des bâtiments avec boutons d'ajout/edit/delete
        composable("batiment_list") {
            BatimentList(
                onBatimentClick = { batimentId ->
                    navController.navigate("batiment_appartements_list/$batimentId")
                },
                onAddBatimentClick = {
                    navController.navigate("add_batiment")
                },
                onEditBatiment = { batiment ->
                    navController.navigate("edit_batiment/${batiment.id}")
                },
                onDeleteBatiment = { batiment ->
                    // La suppression est gérée dans le ViewModel via le callback
                }
            )
        }

        // Ajouter un bâtiment
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

        // Modifier un bâtiment
        composable(
            route = "edit_batiment/{batimentId}",
            arguments = listOf(navArgument("batimentId") { type = NavType.LongType }) // ✅ CORRIGÉ: LongType
        ) { backStackEntry ->
            val batimentId = backStackEntry.arguments?.getLong("batimentId") // ✅ CORRIGÉ: getLong
            if (batimentId != null) {
                BatimentEdit(
                    batimentId = batimentId,
                    onBatimentUpdate = {
                        navController.popBackStack()
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Erreur : Identifiant de bâtiment manquant")
            }
        }

        // Liste des appartements avec boutons d'ajout/edit/delete
        composable(
            route = "batiment_appartements_list/{batimentId}",
            arguments = listOf(navArgument("batimentId") { type = NavType.LongType }) // ✅ CORRIGÉ: LongType
        ) { backStackEntry ->
            val batimentId = backStackEntry.arguments?.getLong("batimentId") // ✅ CORRIGÉ: getLong
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
                    onEditAppartement = { appartement ->
                        navController.navigate("edit_appartement/${appartement.id}")
                    },
                    onDeleteAppartement = { appartement ->
                        // La suppression est gérée dans le ViewModel via le callback
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Erreur : Identifiant de bâtiment manquant")
            }
        }

        // Ajouter un appartement
        composable("add_appartement/{batimentId}",
            arguments = listOf(navArgument("batimentId") { type = NavType.LongType }) // ✅ CORRIGÉ: LongType
        ) { backStackEntry ->
            val batimentId = backStackEntry.arguments?.getLong("batimentId") // ✅ CORRIGÉ: getLong
            if (batimentId != null) {
                AppartementAdd(
                    onAddAppartement = { navController.popBackStack() },
                    batimentId = batimentId,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Erreur : Identifiant de bâtiment manquant")
            }
        }

        // Modifier un appartement
        composable(
            route = "edit_appartement/{appartementId}",
            arguments = listOf(navArgument("appartementId") { type = NavType.LongType }) // ✅ CORRIGÉ: LongType
        ) { backStackEntry ->
            val appartementId = backStackEntry.arguments?.getLong("appartementId") // ✅ CORRIGÉ: getLong
            if (appartementId != null) {
                AppartementEdit(
                    appartementId = appartementId,
                    onAppartementUpdate = {
                        navController.popBackStack()
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Erreur : Identifiant d'appartement manquant")
            }
        }

        // Liste des contrats d'un appartement avec boutons d'ajout/edit/delete
        composable(
            route = "appartement_contrat/{appartementId}",
            arguments = listOf(navArgument("appartementId") { type = NavType.LongType }) // ✅ CORRIGÉ: LongType
        ) { backStackEntry ->
            val appartementId = backStackEntry.arguments?.getLong("appartementId") // ✅ CORRIGÉ: getLong
            if (appartementId != null) {
                ContratList(
                    appartementId = appartementId,
                    onAddContratClick = {
                        navController.navigate("add_contrat/$appartementId")
                    },
                    onContratClick = { contratId ->
                        navController.navigate("contrat_locataires/$contratId")
                    },
                    onEditContrat = { contrat ->
                        navController.navigate("edit_contrat/${contrat.id}")
                    },
                    onDeleteContrat = { contrat ->
                        // La suppression est gérée dans le ViewModel via le callback
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Erreur : Identifiant d'appartement manquant")
            }
        }

        // Ajouter un contrat à un appartement
        composable("add_contrat/{appartementId}",
            arguments = listOf(navArgument("appartementId") { type = NavType.LongType }) // ✅ CORRIGÉ: LongType
        ) { backStackEntry ->
            val appartementId = backStackEntry.arguments?.getLong("appartementId") // ✅ CORRIGÉ: getLong
            if (appartementId != null) {
                ContratAdd(
                    onAddContrat = {
                        navController.popBackStack()
                    },
                    appartementId = appartementId.toInt(), // ✅ Conversion pour ContratAdd qui attend Int
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Erreur : Identifiant d'appartement manquant")
            }
        }

        // Modifier un contrat
        composable(
            route = "edit_contrat/{contratId}",
            arguments = listOf(navArgument("contratId") { type = NavType.LongType }) // ✅ CORRIGÉ: LongType
        ) { backStackEntry ->
            val contratId = backStackEntry.arguments?.getLong("contratId") // ✅ CORRIGÉ: getLong
            if (contratId != null) {
                ContratEdit(
                    contratId = contratId,
                    onContratUpdate = {
                        navController.popBackStack()
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Erreur : Identifiant de contrat manquant")
            }
        }

        // Liste des locataires d'un contrat avec boutons d'ajout/edit/delete
        composable(
            route = "contrat_locataires/{contratId}",
            arguments = listOf(navArgument("contratId") { type = NavType.LongType }) // ✅ CORRIGÉ: LongType
        ) { backStackEntry ->
            val contratId = backStackEntry.arguments?.getLong("contratId") // ✅ CORRIGÉ: getLong
            if (contratId != null) {
                LocataireList(
                    contratId = contratId,
                    onAddLocataireClick = {
                        navController.navigate("add_locataire")
                    },
                    onLocataireClick = { locataireId ->
                        // Navigation vers détail locataire si nécessaire
                    },
                    onEditLocataire = { locataire ->
                        navController.navigate("edit_locataire/${locataire.id}")
                    },
                    onDeleteLocataire = { locataire ->
                        // La suppression est gérée dans le ViewModel via le callback
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Erreur : Identifiant de contrat manquant")
            }
        }

        // Ajouter un locataire
        composable("add_locataire") {
            LocataireAdd(
                onAddLocataire = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Modifier un locataire
        composable(
            route = "edit_locataire/{locataireId}",
            arguments = listOf(navArgument("locataireId") { type = NavType.LongType }) // ✅ CORRIGÉ: LongType
        ) { backStackEntry ->
            val locataireId = backStackEntry.arguments?.getLong("locataireId") // ✅ CORRIGÉ: getLong
            if (locataireId != null) {
                LocataireEdit(
                    locataireId = locataireId,
                    onLocataireUpdate = {
                        navController.popBackStack()
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Erreur : Identifiant de locataire manquant")
            }
        }
    }
}