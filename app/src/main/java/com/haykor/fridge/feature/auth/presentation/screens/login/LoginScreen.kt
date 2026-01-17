package com.haykor.fridge.feature.auth.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.haykor.fridge.core.components.FridgeAppButton
import com.haykor.fridge.core.components.FridgeAppTextField
import com.haykor.fridge.core.theme.FridgeAppTheme


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.LoginSuccess -> onLoginSuccess()
            }
        }
    }
    LoginScreen(
        email = state.email,
        onEmailChange = { viewModel.onEmailChange(it) },
        password = state.password,
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onLoginButtonClick = { viewModel.login() },
        onNavigateRegister = { onNavigateRegister() },
        isLoading = state.isLoading,
        error = state.error,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun LoginScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginButtonClick: () -> Unit,
    onNavigateRegister: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        contentWindowInsets = WindowInsets.statusBars,
        modifier = modifier
            .clickable(
                onClick = { focusManager.clearFocus() },
                interactionSource = interactionSource,
                indication = null
            )
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(innerPadding)
                .clip(RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            Text(
                text = "FridgeApp",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 80.dp, bottom = 32.dp)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Вход",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    LoginFields(
                        email = email,
                        onEmailChange = { onEmailChange(it) },
                        password = password,
                        onPasswordChange = { onPasswordChange(it) },
                        onLoginButtonClick = onLoginButtonClick,
                        onNavigateRegister = onNavigateRegister,
                        isLoading = isLoading,
                        error = error,
                    )
                }
            }
        }
    }
}

@Composable
fun LoginFields(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginButtonClick: () -> Unit,
    onNavigateRegister: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null
) {
    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically
        ),
        modifier = modifier
    ) {
        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        // Email
        FridgeAppTextField(
            value = email,
            onValueChange = onEmailChange,
            leadingIcon = {
                Icon(Icons.Filled.Email, null)
            },
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    defaultKeyboardAction(ImeAction.Next)
                }
            ),
            isError = error != null,
            label = "Почта",
            modifier = Modifier
                .fillMaxWidth()
        )
        FridgeAppTextField(
            value = password,
            onValueChange = onPasswordChange,
            isSecret = true,
            leadingIcon = {
                Icon(Icons.Filled.Key, null)
            },
            isError = error != null,
            label = "Пароль",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                autoCorrectEnabled = false
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (!isLoading && email.isNotBlank() && password.isNotBlank())
                        onLoginButtonClick()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            FridgeAppButton(
                onClick = onLoginButtonClick,
                enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Войти")
                }
            }
            FridgeAppButton(
                onClick = onNavigateRegister,
            ) {
                Text("Регистрация")
            }
        }
    }
}

@Preview(
    device = "spec:parent=pixel_6,navigation=buttons",
    showSystemUi = true
)
@Composable
private fun LoginFieldsPreview() {
    FridgeAppTheme {
        LoginScreen(
            email = "pussydestroyer@gmail.com",
            onEmailChange = {},
            password = "qewrty",
            onPasswordChange = {},
            onLoginButtonClick = {},
            onNavigateRegister = {},
            modifier = Modifier.fillMaxSize(),
            isLoading = false,
            error = null,
        )
    }
}