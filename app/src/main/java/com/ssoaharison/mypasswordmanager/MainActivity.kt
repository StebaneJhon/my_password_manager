package com.ssoaharison.mypasswordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.ssoaharison.mypasswordmanager.commonUiElements.DetailsTopAppBar
import com.ssoaharison.mypasswordmanager.ui.theme.MyPasswordManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPasswordManagerApp()
        }
    }
}

@Composable
fun MyPasswordManagerApp() {
    MyPasswordManagerTheme {
        val startDestination = MyPasswordManagerDestinations.SEARCH_ROUTE
        val navController = rememberNavController()
        val navActions: MyPasswordManagerNavigationActions = remember(navController) {
            MyPasswordManagerNavigationActions(navController)
        }
        var selectedItem by remember { mutableIntStateOf(0) }

        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize()) {
                MyPasswordManagerNavGraph(
                    navController = navController,
                    startDestination = startDestination,
                    navActions = navActions,
                    modifier = Modifier.weight(1F)
                )
                MyPasswordManagerNavBar(
                    screens = navBarItems,
                    selectedItem = selectedItem
                ) { index ->
                    selectedItem = index
                    if (selectedItem == 0) navActions.navigateToSearch() else navActions.navigateToDetails()
                }
            }
        }

    }
}

@Composable
fun MyPasswordManagerNavBar(
    modifier: Modifier = Modifier,
    screens: List<MyPasswordManagerNavBarItem>,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
) {
    NavigationBar {
        screens.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == index) painterResource(screen.onSelectedIcon) else painterResource(screen.onUnSelectedIcon),
                        contentDescription = null
                    )
                },
                label = { Text(screen.screen) },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}














