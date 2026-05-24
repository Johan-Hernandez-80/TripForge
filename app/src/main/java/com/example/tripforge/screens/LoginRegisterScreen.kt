package com.example.tripforge.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripforge.data.AuthenticationDataStore
import kotlinx.coroutines.launch

@Composable
fun LoginRegisterScreen(onAuthSuccess: () -> Unit) {
    val context = LocalContext.current
    val authStore = remember { AuthenticationDataStore(context) }
    val scope = rememberCoroutineScope()
    
    var isLoginMode by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        Text(
            text = "TripForge",
            fontSize = 32.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 30.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TabRow(
                    selectedTabIndex = if (isLoginMode) 0 else 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ) {
                    Tab(
                        selected = isLoginMode,
                        onClick = { 
                            isLoginMode = true
                            errorMessage = ""
                        },
                        text = { Text("Login") }
                    )
                    Tab(
                        selected = !isLoginMode,
                        onClick = { 
                            isLoginMode = false
                            errorMessage = ""
                        },
                        text = { Text("Register") }
                    )
                }
                
                if (errorMessage.isNotEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
                
                if (isLoginMode) {
                    LoginForm(
                        email = email,
                        password = password,
                        showPassword = showPassword,
                        onEmailChange = { email = it; errorMessage = "" },
                        onPasswordChange = { password = it; errorMessage = "" },
                        onShowPasswordChange = { showPassword = it },
                        isLoading = isLoading,
                        onLogin = {
                            isLoading = true
                            errorMessage = ""
                            scope.launch {
                                val result = authStore.login(email.trim(), password)
                                result.onSuccess {
                                    isLoading = false
                                    onAuthSuccess()
                                }
                                result.onFailure {
                                    isLoading = false
                                    errorMessage = it.message ?: "Login failed"
                                }
                            }
                        }
                    )
                } else {
                    RegisterForm(
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                        name = name,
                        showPassword = showPassword,
                        showConfirmPassword = showConfirmPassword,
                        onEmailChange = { email = it; errorMessage = "" },
                        onPasswordChange = { password = it; errorMessage = "" },
                        onConfirmPasswordChange = { confirmPassword = it; errorMessage = "" },
                        onNameChange = { name = it; errorMessage = "" },
                        onShowPasswordChange = { showPassword = it },
                        onShowConfirmPasswordChange = { showConfirmPassword = it },
                        isLoading = isLoading,
                        onRegister = {
                            isLoading = true
                            errorMessage = ""
                            scope.launch {
                                val result = authStore.register(
                                    email.trim(),
                                    password,
                                    name.trim()
                                )
                                result.onSuccess {
                                    isLoading = false
                                    onAuthSuccess()
                                }
                                result.onFailure {
                                    isLoading = false
                                    errorMessage = it.message ?: "Registration failed"
                                }
                            }
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun LoginForm(
    email: String,
    password: String,
    showPassword: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onShowPasswordChange: (Boolean) -> Unit,
    isLoading: Boolean,
    onLogin: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Welcome Back",
            fontSize = 20.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            enabled = !isLoading,
            singleLine = true
        )
        
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val icon = if (showPassword) "Hide" else "Show"
                TextButton(onClick = { onShowPasswordChange(!showPassword) }) {
                    Text(icon, fontSize = 12.sp)
                }
            },
            enabled = !isLoading,
            singleLine = true
        )
        
        Button(
            onClick = onLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = email.isNotEmpty() && password.isNotEmpty() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Login", fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun RegisterForm(
    email: String,
    password: String,
    confirmPassword: String,
    name: String,
    showPassword: Boolean,
    showConfirmPassword: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onShowPasswordChange: (Boolean) -> Unit,
    onShowConfirmPasswordChange: (Boolean) -> Unit,
    isLoading: Boolean,
    onRegister: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Create Account",
            fontSize = 20.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Full Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            enabled = !isLoading,
            singleLine = true
        )
        
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            enabled = !isLoading,
            singleLine = true
        )
        
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password (min 6 characters)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val icon = if (showPassword) "Hide" else "Show"
                TextButton(onClick = { onShowPasswordChange(!showPassword) }) {
                    Text(icon, fontSize = 12.sp)
                }
            },
            enabled = !isLoading,
            singleLine = true
        )
        
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirm Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val icon = if (showConfirmPassword) "Hide" else "Show"
                TextButton(onClick = { onShowConfirmPasswordChange(!showConfirmPassword) }) {
                    Text(icon, fontSize = 12.sp)
                }
            },
            enabled = !isLoading,
            singleLine = true
        )
        
        Button(
            onClick = onRegister,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = email.isNotEmpty() && password.isNotEmpty() && 
                    confirmPassword.isNotEmpty() && name.isNotEmpty() && 
                    password == confirmPassword && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Create Account", fontSize = 16.sp)
            }
        }
    }
}
