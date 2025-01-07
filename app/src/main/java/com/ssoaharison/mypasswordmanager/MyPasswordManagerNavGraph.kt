package com.ssoaharison.mypasswordmanager

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ssoaharison.mypasswordmanager.MyPasswordManagerDestinationsArgs.DETAIL_ID_ARG
import com.ssoaharison.mypasswordmanager.MyPasswordManagerDestinationsArgs.TITLE_ARG
import com.ssoaharison.mypasswordmanager.MyPasswordManagerDestinationsArgs.USER_MESSAGE_ARG
import com.ssoaharison.mypasswordmanager.detailContent.DetailContentScreen
import com.ssoaharison.mypasswordmanager.details.DetailsScreen
import com.ssoaharison.mypasswordmanager.search.SearchScreen
import com.ssoaharison.mypasswordmanager.upsertDetail.UpsertDetailScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun MyPasswordManagerNavGraph(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = MyPasswordManagerDestinations.SEARCH_ROUTE,
    navActions: MyPasswordManagerNavigationActions = remember(navController) {
        MyPasswordManagerNavigationActions(navController)
    },
    modifier: Modifier = Modifier
) {

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            MyPasswordManagerDestinations.DETAILS_ROUTE,
            arguments = listOf(
                navArgument(USER_MESSAGE_ARG) {type = NavType.IntType; defaultValue = 0}
            )
        ) { entry ->
            DetailsScreen(
                userMessage = entry.arguments?.getInt(USER_MESSAGE_ARG)!!,
                onUserMessageDisplayed = {entry.arguments?.putInt(USER_MESSAGE_ARG, 0)},
                onAddNewDetail = {navActions.navigateToUpsertDetail(R.string.add_new_detail, null)},
                onDetailClicked = {detail -> navActions.navigateToDetailContent(detail.id)},
                onToSettings = {}, // TODO: To be changed
                onRefresh = {}, // TODO: To be changed
            )
        }
        composable(
            MyPasswordManagerDestinations.SEARCH_ROUTE,
            arguments = listOf(
                navArgument(USER_MESSAGE_ARG) {type = NavType.IntType; defaultValue = 0}
            )
        ) { entry ->
            SearchScreen(
                userMessage = entry.arguments?.getInt(USER_MESSAGE_ARG)!!,
                onDetailClicked = {detail -> navActions.navigateToDetailContent(detail.id)},
                onAddNewDetail = {navActions.navigateToUpsertDetail(R.string.add_new_detail, null)},
                onRefresh = {},
                onToSettings = {}
            )
        }
        composable(
            MyPasswordManagerDestinations.UPSERT_DETAIL_ROUTE,
            arguments = listOf(
                navArgument(TITLE_ARG) {type = NavType.IntType},
                navArgument(DETAIL_ID_ARG) {type = NavType.StringType; nullable = true}
            )
        ) { entry ->
            val detailId = entry.arguments?.getString(DETAIL_ID_ARG)
            UpsertDetailScreen(
                topBarTitle = entry.arguments?.getInt(TITLE_ARG)!!,
                onDetailUpdate = {
                    navActions.navigateToDetails(
                        if (detailId == null) UPSERT_RESULT_OK else EDIT_RESULT_OK
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            MyPasswordManagerDestinations.DETAIL_CONTENT_ROUTE
        ) {
            DetailContentScreen(
                topBarTitle = "",
                onEditDetail = {detailId ->
                    navActions.navigateToUpsertDetail(R.string.edit_detail, detailId)
                },
                onDeleteDetail = { navActions.navigateToDetails(DELETE_RESULT_OK) },
                onBack = {navController.popBackStack()}
            )
        }
    }

}

const val UPSERT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3