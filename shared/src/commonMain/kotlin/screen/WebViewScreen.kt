package screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState

@Composable
fun webViewScreen(
    localhost: String,
    close : () -> Unit
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    val state = rememberWebViewState(localhost)

    state.webSettings.apply {
        isJavaScriptEnabled = true
        customUserAgentString =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_1) AppleWebKit/625.20 (KHTML, like Gecko) Version/14.3.43 Safari/625.20"
        androidWebSettings.apply {
            isAlgorithmicDarkeningAllowed = true
            safeBrowsingEnabled = true
        }
    }

    val navigator = rememberWebViewNavigator()
    var textFieldValue by remember(state.lastLoadedUrl) {
        mutableStateOf(state.lastLoadedUrl)
    }

    if (isDialogVisible) {
        DialogWithTextField(
            onDismissRequest = { isDialogVisible = false },
            onConfirmation = {
                textFieldValue = "$textFieldValue$it"
                navigator.loadUrl(textFieldValue!!)
                isDialogVisible = false
            },
            modifier = Modifier.fillMaxWidth().wrapContentHeight(Alignment.CenterVertically)
        )
    }

    Box(modifier = Modifier.fillMaxWidth(1f).fillMaxHeight(1f)) {
        if (state.errorsForCurrentRequest.isNotEmpty()) {
            Image(
                imageVector = Icons.Default.Close,
                contentDescription = "Error",
                colorFilter = ColorFilter.tint(Color.Red),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(8.dp)
            )
        }
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.primary)
                    .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .align(
                            Alignment.CenterVertically
                        ).clickable {
                            if (navigator.canGoBack) {
                                navigator.navigateBack()
                            }else{
                                close()
                            }
                        }
                )
                Text(
                    text = textFieldValue?:localhost,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 8.dp, end = 8.dp)
                        .align(
                            Alignment.CenterVertically
                        ).clickable {
                            isDialogVisible = true
                        }
                )
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.wrapContentWidth().padding(start=8.dp)
                        .align(
                            Alignment.CenterVertically
                        ).clickable {
                            navigator.reload()
                        }
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .align(
                            Alignment.CenterVertically
                        ).clickable {
                            close()
                        }
                )
            }

            val loadingState = state.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    progress = loadingState.progress,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            WebView(
                state = state,
                navigator = navigator
            )
        }
    }
}


@Composable
fun DialogWithTextField(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val focusRequester = remember { FocusRequester() }
                OutlinedTextField(
                    value = text,
                    onValueChange = {text=it},
                    label = { Text("Add Routes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp).weight(1f),
                    ) {
                        Text("Dismiss")
                    }
                    Spacer(Modifier.weight(1f).background(MaterialTheme.colors.background))
                    Button(
                        onClick = { onConfirmation(text) },
                        modifier = Modifier.padding(8.dp).weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary
                        )
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}