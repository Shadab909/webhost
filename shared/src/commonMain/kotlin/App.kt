import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import screen.LocalHostSetUpScreen
import kotlinx.coroutines.launch
import screen.webViewScreen

@Composable
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        var localhostAddress by remember { mutableStateOf("") }
        var isButtonClicked by remember { mutableStateOf(false) }


        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { contentPadding ->
            // Screen content
            Box(Modifier.fillMaxWidth().padding(contentPadding)) {
                //ScreenOne
                Column(
                    Modifier.fillMaxWidth().alpha(if (isButtonClicked) 0f else 1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LocalHostSetUpScreen(
                        modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        onClick = {
                            localhostAddress = it
                            isButtonClicked = true
                        },
                        onCheck = {

                        }
                    )

//                    if (isButtonClicked) {
//                        scope.launch {
//                            val result = snackbarHostState
//                                .showSnackbar(
//                                    message = localhostAddress,
//                                    actionLabel = "Close",
//                                    // Defaults to SnackbarDuration.Short
//                                    duration = SnackbarDuration.Indefinite
//                                )
//                            isButtonClicked = when (result) {
//                                SnackbarResult.ActionPerformed -> {
//                                    false
//                                }
//
//                                SnackbarResult.Dismissed -> {
//                                    false
//                                }
//                            }
//                        }
//                    }
                }

                //ScreenTwo
                Column(
                    Modifier.fillMaxWidth().alpha(if (isButtonClicked) 1f else 0f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    webViewScreen(
                        localhost = "http://192.168.0.130:8080/",
                        close = {
                            isButtonClicked = false
                        }
                    )
                }

            }
        }
    }
}



//expect fun getPlatformName(): String