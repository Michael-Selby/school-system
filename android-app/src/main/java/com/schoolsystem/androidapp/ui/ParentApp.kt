@file:OptIn(ExperimentalMaterial3Api::class)

package com.schoolsystem.androidapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.schoolsystem.androidapp.auth.AuthUiState
import com.schoolsystem.androidapp.auth.AuthViewModel
import com.schoolsystem.androidapp.data.ParentLoginRequest
import com.schoolsystem.androidapp.data.ParentSignupRequest
import java.util.UUID

private object AuthDestinations {
    const val Login = "login"
    const val Signup = "signup"
    const val Dashboard = "dashboard"
}

@Composable
fun ParentApp(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val uiState by authViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.loginResult?.studentId) {
        if (uiState.loginResult != null) {
            navController.navigate(AuthDestinations.Dashboard) {
                popUpTo(AuthDestinations.Login) { inclusive = true }
            }
            authViewModel.clearLoginResult()
        }
    }

    NavHost(navController, startDestination = AuthDestinations.Login) {
        composable(AuthDestinations.Login) {
            LoginScreen(
                uiState = uiState,
                onLogin = authViewModel::login,
                onNavigateToSignup = { navController.navigate(AuthDestinations.Signup) }
            )
        }
        composable(AuthDestinations.Signup) {
            SignupScreen(
                uiState = uiState,
                onSignup = authViewModel::signup,
                onNavigateToLogin = {
                    navController.navigate(AuthDestinations.Login) { popUpTo(AuthDestinations.Signup) { inclusive = true } }
                }
            )
        }
        composable(AuthDestinations.Dashboard) {
            DashboardScreen(
                loginResult = uiState.loginResult,
                onLogout = {
                    navController.navigate(AuthDestinations.Login) {
                        popUpTo(AuthDestinations.Dashboard) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
private fun LoginScreen(
    uiState: AuthUiState,
    onLogin: (ParentLoginRequest) -> Unit,
    onNavigateToSignup: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    val inputError = remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .padding(24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Welcome Parent", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Log in to access your child’s dashboard", style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            singleLine = true,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors()
        )
        OutlinedTextField(
            value = studentId,
            onValueChange = { studentId = it },
            singleLine = true,
            label = { Text("Student ID") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors()
        )
        inputError.value?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(
            onClick = {
                val uuid = runCatching { UUID.fromString(studentId) }.getOrNull()
                if (uuid == null) {
                    inputError.value = "Enter a valid UUID for the Student ID"
                    return@Button
                }
                inputError.value = null
                onLogin(ParentLoginRequest(parentEmail = email.trim(), password = password, studentId = uuid))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log in")
        }
        TextButton(onClick = onNavigateToSignup) {
            Text("Don’t have an account? Sign up")
        }
        uiState.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        if (uiState.isLoading) {
            Text("Logging in...", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SignupScreen(
    uiState: AuthUiState,
    onSignup: (ParentSignupRequest) -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Create an account", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        TextFieldOutline(label = "Full Name", value = fullName) { fullName = it }
        TextFieldOutline(label = "Email", value = email) { email = it }
        TextFieldOutline(label = "Phone", value = phone) { phone = it }
        TextFieldOutline(label = "Password", value = password) { password = it }
        Button(
            onClick = {
                onSignup(ParentSignupRequest(fullName = fullName.trim(), email = email.trim(), phone = phone.trim(), password = password))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign up")
        }
        uiState.signupSuccess.let {
            if (uiState.signupSuccess) {
                Text("Signup successful. You can now log in.", color = MaterialTheme.colorScheme.primary)
            }
        }
        uiState.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        OutlinedButton(onClick = onNavigateToLogin, modifier = Modifier.fillMaxWidth()) {
            Text("Back to login")
        }
    }
}

@Composable
private fun TextFieldOutline(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors()
    )
}

@Composable
private fun DashboardScreen(
    loginResult: com.schoolsystem.androidapp.data.ParentLoginResponse?,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Dashboard", style = MaterialTheme.typography.headlineSmall)
            loginResult?.let {
                Text("Logged in as: ${it.parentEmail}")
                Text("Student: ${it.studentName}")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Text("Log out")
            }
        }
    }
}
