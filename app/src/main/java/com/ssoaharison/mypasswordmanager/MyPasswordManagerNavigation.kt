package com.ssoaharison.mypasswordmanager

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.ssoaharison.mypasswordmanager.MyPasswordManagerDestinationsArgs.TITLE_ARG
import com.ssoaharison.mypasswordmanager.MyPasswordManagerDestinationsArgs.DETAIL_ID_ARG
import com.ssoaharison.mypasswordmanager.MyPasswordManagerDestinationsArgs.USER_MESSAGE_ARG
import com.ssoaharison.mypasswordmanager.MyPasswordManagerScreens.DETAILS_SCREEN
import com.ssoaharison.mypasswordmanager.MyPasswordManagerScreens.DETAIL_CONTENT_SCREEN
import com.ssoaharison.mypasswordmanager.MyPasswordManagerScreens.SEARCH_SCREEN
import com.ssoaharison.mypasswordmanager.MyPasswordManagerScreens.UPSERT_DETAIL_SCREEN

private object MyPasswordManagerScreens {
    const val DETAIL_CONTENT_SCREEN = "detailContent"
    const val UPSERT_DETAIL_SCREEN = "upsertDetail"
    const val SEARCH_SCREEN = "search"
    const val DETAILS_SCREEN = "details"
}

object MyPasswordManagerDestinationsArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val DETAIL_ID_ARG = "detailId"
    const val TITLE_ARG = "appName"
}

object MyPasswordManagerDestinations {
    const val DETAILS_ROUTE = "$DETAILS_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
    const val SEARCH_ROUTE = "$SEARCH_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
    const val DETAIL_CONTENT_ROUTE = "$DETAIL_CONTENT_SCREEN/{$DETAIL_ID_ARG}"
    const val UPSERT_DETAIL_ROUTE = "$UPSERT_DETAIL_SCREEN/{$TITLE_ARG}?$DETAIL_ID_ARG={$DETAIL_ID_ARG}"
}

class MyPasswordManagerNavigationActions (private val navController: NavHostController){

    fun navigateToDetails(userMessage: Int = 0) {
        val navigatesFromDrawer = userMessage == 0
        navController.navigate(
            DETAILS_SCREEN.let {
                if (userMessage != 0) "$it?$USER_MESSAGE_ARG=$userMessage" else it
            }
        ) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = !navigatesFromDrawer
                saveState = navigatesFromDrawer
            }
            launchSingleTop = true
            restoreState = navigatesFromDrawer
        }
    }

    fun navigateToSearch(userMessage: Int = 0) {
        val navigatesFromDrawer = userMessage == 0
        navController.navigate(
            SEARCH_SCREEN.let {
                if (userMessage != 0) "$it?$USER_MESSAGE_ARG=$userMessage" else it
            }
        ) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = !navigatesFromDrawer
                saveState = navigatesFromDrawer
            }
            launchSingleTop = true
            restoreState = navigatesFromDrawer
        }
    }

    fun navigateToDetailContent(detailId: String) {
        navController.navigate("$DETAIL_CONTENT_SCREEN/$detailId")
    }

    fun navigateToUpsertDetail(title: Int, detailId: String?) {
        navController.navigate(
            "$UPSERT_DETAIL_SCREEN/$title".let {
                if (detailId != null) "$it?$DETAIL_ID_ARG=$detailId" else it
            }
        )
    }

}

val mainScreens = listOf(SEARCH_SCREEN, DETAILS_SCREEN)