package com.ssoaharison.mypasswordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ssoaharison.mypasswordmanager.ui.theme.MyPasswordManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPasswordManagerTheme {
                MyPasswordManagerApp()
            }
        }
    }
}

@Composable
fun MyPasswordManagerApp() {
    MyPasswordManagerTheme {
        val startDestination = MyPasswordManagerDestinations.SEARCH_ROUTE
        val navController = rememberNavController()
        val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
        val navActions: MyPasswordManagerNavigationActions = remember(navController) {
            MyPasswordManagerNavigationActions(navController)
        }
        val selectedItem by remember { mutableIntStateOf(0) }
        Scaffold(
            bottomBar = {
                MyPasswordManagerNavBar(
                    mainScreens,
                    navActions,
                    selectedItem
                )
            }
        ) { paddingValues ->
            MyPasswordManagerNavGraph(
                navController = navController,
                startDestination = startDestination,
                navActions = navActions,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun MyPasswordManagerNavBar(
    screens: List<String>,
    navigationActions: MyPasswordManagerNavigationActions,
    selectedItem: Int
) {
    val selectedItemIcons =
        listOf(painterResource(R.drawable.ic_search), painterResource(R.drawable.ic_dock_to_right))
    val unSelectedItemIcons = listOf(
        painterResource(R.drawable.ic_search_bold),
        painterResource(R.drawable.ic_dock_to_right_filled)
    )
    NavigationBar {
        screens.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == index) selectedItemIcons[index] else unSelectedItemIcons[index],
                        contentDescription = screen
                    )
                },
                label = { Text(screen) },
                selected = selectedItem == index,
                onClick = {
                    if (index == 0) navigationActions.navigateToSearch() else navigationActions.navigateToDetails()
                }
            )
        }
    }
}















