package screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class, ExperimentalComposeUiApi::class)
@Composable
fun LocalHostSetUpScreen(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    onCheck: (Boolean) -> Unit
) {
    var ipAddress by remember { mutableStateOf("192.168.0.130") }
    var port by remember { mutableStateOf("8080") }
    var isChecked by remember { mutableStateOf(false) }
    var isIpWrong by rememberSaveable { mutableStateOf(false) }
    var isPortWrong by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxWidth().padding(4.dp)) {
        Image(
            painterResource("compose-multiplatform.xml"),
            contentDescription = "Compose Multiplatform icon",
            modifier = Modifier.align(Alignment.CenterHorizontally).width(250.dp).height(250.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(modifier = modifier.fillMaxWidth().padding(8.dp)) {
            Column(modifier = modifier.padding(8.dp)) {
                Text(
                    text = "Set up your local host",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.h6
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = ipAddress,
                    onValueChange = {
                        ipAddress = it
                    },
                    trailingIcon = {
                        if (isIpWrong)
                            Icon(Icons.Filled.Warning, "error", tint = MaterialTheme.colors.error)
                    },
                    label = { Text("IpAddress") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                    isError = isIpWrong,
                    maxLines = 1
                )
                if (isIpWrong) {
                    Text(
                        text = "Invalid ip address",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                OutlinedTextField(
                    value = port,
                    onValueChange = {
                        port = it
                    },
                    trailingIcon = {
                        if (isPortWrong)
                            Icon(Icons.Filled.Warning, "error", tint = MaterialTheme.colors.error)
                    },
                    label = { Text("Port") },
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    isError = isPortWrong,
                    maxLines = 1
                )

                if (isPortWrong) {
                    Text(
                        text = "Invalid port address",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = it
                            onCheck(it)
                        },
                        enabled = true
                    )

                    Text(
                        text = "Remember me",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Spacer(Modifier.weight(1f).background(MaterialTheme.colors.background))

                    Button(
                        onClick = {
                            if (ipAddress.isNotEmpty() && port.isNotEmpty()) {
                                val localHostAddress = "http://$ipAddress:$port"
                                isPortWrong = !isValidPort(port.toInt())
                                isIpWrong = !isValidIpAddress(ipAddress)
                                onClick(localHostAddress)
                            }
                        }
                    ) {
                        Text("Open Web")
                    }
                }
            }
        }
    }
}


fun isValidIpAddress(ip: String): Boolean {
    // Regular expression for IPv4
    val ipv4Pattern = Regex(
        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$",
    )

    // Regular expression for IPv6
    val ipv6Pattern = Regex(
        "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|" +
                "^(?:[0-9a-fA-F]{1,4}:){6}:[0-9a-fA-F]{1,4}$|" +
                "^(?:[0-9a-fA-F]{1,4}:){5}(?::[0-9a-fA-F]{1,4}){1,2}$|" +
                "^(?:[0-9a-fA-F]{1,4}:){4}(?::[0-9a-fA-F]{1,4}){1,3}$|" +
                "^(?:[0-9a-fA-F]{1,4}:){3}(?::[0-9a-fA-F]{1,4}){1,4}$|" +
                "^(?:[0-9a-fA-F]{1,4}:){2}(?::[0-9a-fA-F]{1,4}){1,5}$|" +
                "^[0-9a-fA-F]{1,4}:(?::[0-9a-fA-F]{1,4}){1,6}$|" +
                "^:(?::[0-9a-fA-F]{1,4}){1,7}$|" +
                "^::[0-9a-fA-F]{1,4}$"
    )

    return ipv4Pattern.matches(ip) || ipv6Pattern.matches(ip)
}

fun isValidPort(port: Int): Boolean {
    return port in 1..65535
}

