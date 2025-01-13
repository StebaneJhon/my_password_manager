package com.ssoaharison.mypasswordmanager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.autofill.AutofillManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ssoaharison.mypasswordmanager.MyPasswordManagerScreens.DETAILS_SCREEN
import com.ssoaharison.mypasswordmanager.MyPasswordManagerScreens.SEARCH_SCREEN
import com.ssoaharison.mypasswordmanager.MyPasswordManagerScreens.SETTINGS_SCREEN
import com.ssoaharison.mypasswordmanager.ui.theme.MyPasswordManagerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestAutofillServicePermission = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { isGranted ->
        if (isGranted.resultCode != RESULT_OK) {
            Toast.makeText(this, getString(R.string.message_must_set_the_app_as_current_autofill_service), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val autofillManager = getSystemService(AutofillManager::class.java)
            if (!autofillManager.hasEnabledAutofillServices()) {
                val intent = Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE)
                intent.setData(Uri.parse("package:com.ssoaharison.mypasswordmanager"))
                requestAutofillServicePermission.launch(intent)
            }

            MyPasswordManagerApp(this)
        }
    }
}

@Composable
fun MyPasswordManagerApp(context: Context) {
    MyPasswordManagerTheme {
        val startDestination = MyPasswordManagerDestinations.SEARCH_ROUTE
        val navController = rememberNavController()
        val navActions: MyPasswordManagerNavigationActions = remember(navController) {
            MyPasswordManagerNavigationActions(navController)
        }

        val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination
        val currentScreen = currentRoute.split("?").first()

        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize()) {
                MyPasswordManagerNavGraph(
                    context = context,
                    navController = navController,
                    startDestination = startDestination,
                    navActions = navActions,
                    modifier = Modifier.weight(1F)
                )
                MyPasswordManagerNavBar(
                    screens = navBarItems,
                    selectedItem = currentScreen
                ) { currentScreen ->
                    when (currentScreen) {
                        SEARCH_SCREEN -> navActions.navigateToSearch()
                        DETAILS_SCREEN -> navActions.navigateToDetails()
                        SETTINGS_SCREEN -> navActions.navigateToSettings()
                    }
                }
            }
        }
    }
}

@Composable
fun MyPasswordManagerNavBar(
    modifier: Modifier = Modifier,
    screens: List<MyPasswordManagerNavBarItem>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
) {
    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == screen.screen) painterResource(screen.onSelectedIcon) else painterResource(screen.onUnSelectedIcon),
                        contentDescription = null
                    )
                },
                label = { Text(screen.screen) },
                selected = selectedItem == screen.screen,
                onClick = { onItemSelected(screen.screen) }
            )
        }
    }
}














