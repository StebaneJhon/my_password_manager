package com.ssoaharison.mypasswordmanager.service

import android.R
import android.app.assist.AssistStructure
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.CancellationSignal
import android.service.autofill.AutofillService
import android.service.autofill.Dataset
import android.service.autofill.FillCallback
import android.service.autofill.FillContext
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.SaveCallback
import android.service.autofill.SaveInfo
import android.service.autofill.SaveRequest
import android.view.View
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import com.ssoaharison.mypasswordmanager.data.DetailsRepository
import com.ssoaharison.mypasswordmanager.data.ExternalCredential
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MyAutofillService : AutofillService() {

    @Inject
    lateinit var repository: DetailsRepository

    override fun onFillRequest(
        request: FillRequest,
        cancellationSignal: CancellationSignal,
        callback: FillCallback
    ) {
        val context: List<FillContext> = request.fillContexts
        val structure: AssistStructure = context[context.size - 1].structure

        val parsedStructure: ParsedStructure? = parseStructure(structure, applicationContext)

        if (parsedStructure == null) {
            callback.onFailure("Error")
            return
        }

        val usernamePresentation = RemoteViews(packageName, R.layout.simple_list_item_1)
        usernamePresentation.setTextViewText(R.id.text1, "my_username")
        val passwordPresentation = RemoteViews(packageName, R.layout.simple_list_item_1)
        passwordPresentation.setTextViewText(R.id.text1, "Password for my_username")

        val saveInfo = SaveInfo.Builder(
            SaveInfo.SAVE_DATA_TYPE_USERNAME or SaveInfo.SAVE_DATA_TYPE_PASSWORD,
            arrayOf(parsedStructure.usernameId, parsedStructure.passwordId)
        ).build()

        fetchUserData(parsedStructure) { userData ->
            val fillResponse = if (userData == null) {
                FillResponse.Builder()
                    .setSaveInfo(saveInfo)
                    .build()
            } else {
                FillResponse.Builder()
                    .addDataset(
                        Dataset.Builder()
                            .setValue(
                                parsedStructure.usernameId!!,
                                AutofillValue.forText(userData.username),
                                usernamePresentation
                            )
                            .setValue(
                                parsedStructure.passwordId!!,
                                AutofillValue.forText(userData.password),
                                passwordPresentation
                            )
                            .build()
                    )
                    .setSaveInfo(saveInfo)
                    .build()
            }
            callback.onSuccess(fillResponse)
        }
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        val context: List<FillContext> = request.fillContexts
        val structure: AssistStructure = context[context.size - 1].structure

        val data = traverseStructure(structure, applicationContext)

        if (data != null) {
            createNewDetail(data.appName!!, "", data.username!!, data.password!!) {
                callback.onSuccess()
            }
        } else {
            callback.onFailure("Data registration failed.")
            return
        }

    }

    fun traverseStructure(structure: AssistStructure, context: Context): FetchedData? {
        val fetchedData = FetchedData()
        val windowNodes: List<AssistStructure.WindowNode> =
            (0 until structure.windowNodeCount).map { structure.getWindowNodeAt(it) }

        fetchedData.appName = getAppNameFromAssistStructure(structure, context)

        windowNodes.forEach { windowNode ->
            val viewNode: AssistStructure.ViewNode? = windowNode.rootViewNode
            fetchData(viewNode, fetchedData)
        }

        return if (fetchedData.username == null && fetchedData.password == null && fetchedData.appName == null) null else fetchedData
    }

    private fun fetchData(viewNode: AssistStructure.ViewNode?, fetchedData: FetchedData) {
        if (viewNode == null) return
        viewNode.autofillHints?.let { hints ->
            when {
                hints.contains(View.AUTOFILL_HINT_USERNAME) -> {
                    fetchedData.username = viewNode.text.toString()
                }
                hints.contains(View.AUTOFILL_HINT_PASSWORD) -> {
                    fetchedData.password = viewNode.text.toString()
                }
            }
        }

        val children: List<AssistStructure.ViewNode>? =
            (0 until viewNode.childCount).map { viewNode.getChildAt(it) }
        children?.forEach { childNode ->
            fetchData(childNode, fetchedData)
        }
    }

    fun getAppNameFromAssistStructure(assistStructure: AssistStructure, context: Context): String? {
        val componentName = assistStructure.activityComponent
        val packageName = componentName.packageName
//        val packageManager: PackageManager = applicationContext.packageManager
        val packageManager: PackageManager = context.packageManager
        return try {
            val applicationInfo: ApplicationInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    fun parseStructure(structure: AssistStructure, context: Context): ParsedStructure? {
        val parsedStructure = ParsedStructure()
        val windowNodes: List<AssistStructure.WindowNode> =
            (0 until structure.windowNodeCount).map { structure.getWindowNodeAt(it) }
        val appName = getAppNameFromAssistStructure(structure, context)
        parsedStructure.appName = appName
        windowNodes.forEach { windowNode ->
            val viewNode: AssistStructure.ViewNode? = windowNode.rootViewNode
            traverseNode(viewNode, parsedStructure)
        }
        return if (parsedStructure.usernameId == null && parsedStructure.passwordId == null && parsedStructure.appName == null) null else parsedStructure
    }

    private fun traverseNode(
        viewNode: AssistStructure.ViewNode?,
        parsedStructure: ParsedStructure
    ) {
        if (viewNode == null) return
        viewNode.autofillHints?.let { hints ->
            when {
                hints.contains(View.AUTOFILL_HINT_USERNAME) -> parsedStructure.usernameId =
                    viewNode.autofillId

                hints.contains(View.AUTOFILL_HINT_PASSWORD) -> parsedStructure.passwordId =
                    viewNode.autofillId
            }
        }
        val children: List<AssistStructure.ViewNode> =
            (0 until viewNode.childCount).map { viewNode.getChildAt(it) }
        children.forEach { childNode ->
            traverseNode(childNode, parsedStructure)
        }
    }

    fun createNewDetail(
        appName: String,
        link: String,
        username: String,
        password: String,
        onDone: () -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        repository.createCredential(appName, link, username, password)
        onDone()
    }

    private fun searchUserData(appName: String, formatUserNata: (ExternalCredential?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getCredentialByAppName(appName)
            formatUserNata(data)
        }
    }

    fun fetchUserData(structure: ParsedStructure, userData: (UserData?) -> Unit) {
        searchUserData(structure.appName!!) { credential ->
            if (credential == null) {
                userData(null)
            } else {
                userData(
                    UserData(
                        credential.username,
                        credential.password
                    )
                )
            }
        }
    }

}

data class ParsedStructure(
    var appName: String? = null,
    var usernameId: AutofillId? = null,
    var passwordId: AutofillId? = null
)

data class UserData(
    var username: String,
    var password: String
)

data class FetchedData(
    var appName: String? = null,
    var username: String? = null,
    var password: String? = null
)