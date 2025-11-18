package com.haykor.fridge.auth.presentation.screens.registration

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.haykor.fridge.core.theme.FridgeAppTheme

@Composable
fun RegistrationScreen(
    onRegistrationSuccess: () -> Unit,
    onNavigateLogin: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is RegistrationEvent.RegistrationSuccess -> onRegistrationSuccess()
            }
        }
    }

    RegistrationScreen(
        username = state.username,
        onUsernameChange = { viewModel.onUsernameChange(it) },
        email = state.email,
        onEmailChange = { viewModel.onEmailChange(it) },
        password = state.password,
        onPasswordChange = { viewModel.onPasswordChange(it) },
        passwordRepeat = state.passwordRepeat,
        onPasswordRepeatChange = { viewModel.onPasswordRepeatChange(it) },
        onRegisterButtonClick = { viewModel.register() },
        onNavigateLogin = { onNavigateLogin() },
        isLoading = state.isLoading,
        error = state.error,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun RegistrationScreen(
    username: String,
    onUsernameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordRepeat: String,
    onPasswordRepeatChange: (String) -> Unit,
    onRegisterButtonClick: () -> Unit,
    onNavigateLogin: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null
) {
    Scaffold(
        contentWindowInsets = WindowInsets.statusBars,
        modifier = modifier
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
                        text = "Регистрация",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    RegistrationFields(
                        username = username,
                        onUsernameChange = { onUsernameChange(it) },
                        email = email,
                        onEmailChange = { onEmailChange(it) },
                        password = password,
                        onPasswordChange = { onPasswordChange(it) },
                        passwordRepeat = passwordRepeat,
                        onPasswordRepeatChange = { onPasswordRepeatChange(it) },
                        onRegisterButtonClick = onRegisterButtonClick,
                        onNavigateLogin = onNavigateLogin,
                        isLoading = isLoading,
                        error = error,
                    )
                }
            }
        }
    }
}

@Composable
fun RegistrationFields(
    username: String,
    onUsernameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordRepeat: String,
    onPasswordRepeatChange: (String) -> Unit,
    onRegisterButtonClick: () -> Unit,
    onNavigateLogin: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
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
        // Username
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            leadingIcon = {
                Icon(Icons.Filled.Person, null)
            },
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    defaultKeyboardAction(ImeAction.Next)
                }
            ),
            singleLine = true,
            maxLines = 1,
            isError = error != null,
            label = {
                Text("Имя пользователя")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        // Email
        OutlinedTextField(
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
            singleLine = true,
            maxLines = 1,
            isError = error != null,
            label = {
                Text("Почта")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        // Password
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            leadingIcon = {
                Icon(Icons.Filled.Key, null)
            },
            trailingIcon = {
                IconButton(
                    onClick = { isPasswordVisible = !isPasswordVisible }
                ) {
                    Icon(
                        imageVector = if (isPasswordVisible) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        },
                        contentDescription = null
                    )
                }
            },
            isError = (error != null) || (password.isNotBlank() && password != passwordRepeat),
            label = {
                Text("Пароль")
            },
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation(mask = '*')
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                autoCorrectEnabled = false
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    defaultKeyboardAction(ImeAction.Next)
                }
            ),
            singleLine = true,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        // Password repeat
        OutlinedTextField(
            value = passwordRepeat,
            onValueChange = onPasswordRepeatChange,
            leadingIcon = {
                Icon(Icons.Filled.Key, null)
            },
            trailingIcon = {
                IconButton(
                    onClick = { isPasswordVisible = !isPasswordVisible }
                ) {
                    Icon(
                        imageVector = if (isPasswordVisible) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        },
                        contentDescription = null
                    )
                }
            },
            isError = (error != null) || (password.isNotBlank() && password != passwordRepeat),
            label = {
                Text("Повторите пароль")
            },
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation(mask = '*')
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                autoCorrectEnabled = false
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (!isLoading && email.isNotBlank() && password.isNotBlank())
                        onRegisterButtonClick()
                }
            ),
            singleLine = true,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onNavigateLogin,
            ) {
                Text("Вход")
            }
            Button(
                onClick = onRegisterButtonClick,
                enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Регистрация")
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun RegistrationFieldsPreview() {
    FridgeAppTheme {
        RegistrationScreen(
            email = "pussydestroyer@gmail.com",
            username = "HayKor",
            onUsernameChange = {},
            onEmailChange = {},
            password = "qwerty",
            onPasswordChange = {},
            passwordRepeat = "qwert",
            onPasswordRepeatChange = {},
            onRegisterButtonClick = {},
            onNavigateLogin = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}