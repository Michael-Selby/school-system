@file:OptIn(ExperimentalMaterial3Api::class)

package com.schoolsystem.androidapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.net.Uri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.schoolsystem.androidapp.auth.AuthUiState
import com.schoolsystem.androidapp.auth.AuthViewModel
import com.schoolsystem.androidapp.data.ParentLoginRequest
import com.schoolsystem.androidapp.data.ParentSignupRequest
import com.schoolsystem.androidapp.data.StudentProfileResponse
import kotlinx.coroutines.launch

private object AuthDestinations {
    const val Login = "login"
    const val Signup = "signup"
    const val Dashboard = "dashboard"
    const val PurchaseForm = "purchase_form"
}

@Composable
fun ParentApp(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val uiState by authViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.loginResult?.parentId) {
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
            DashboardWithDrawer(
                uiState = uiState,
                onLookupChild = authViewModel::lookupChildByIndex,
                onClearChild = authViewModel::clearChildProfile,
                onRefreshChildren = authViewModel::fetchChildrenForParent,
                onLogout = {
                    navController.navigate(AuthDestinations.Login) {
                        popUpTo(AuthDestinations.Dashboard) { inclusive = true }
                    }
                    authViewModel.logout()
                },
                onNavigateToPurchase = { navController.navigate(AuthDestinations.PurchaseForm) }
            )
        }
        composable(AuthDestinations.PurchaseForm) {
            PurchaseFormScreen(
                uiState = uiState,
                onBack = { navController.popBackStack() },
                onInitiatePurchase = authViewModel::initiateFormPurchase,
                onVerifyPayment = authViewModel::verifyPayment,
                onClearPurchase = authViewModel::clearPurchaseFlow
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
        Button(
            onClick = {
                onLogin(ParentLoginRequest(parentEmail = email.trim(), password = password))
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
    uiState: AuthUiState,
    onLookupChild: (String) -> Unit,
    onClearChild: () -> Unit,
    onOpenDrawer: () -> Unit,
    onRefreshChildren: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLookupDialog by remember { mutableStateOf(false) }
    var indexInput by remember { mutableStateOf("") }

    LaunchedEffect(uiState.session?.parentId) {
        onRefreshChildren()
    }

    Card(
        modifier = modifier
            .padding(24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TextButton(onClick = onOpenDrawer) { Text("Menu") }
            Text("Dashboard", style = MaterialTheme.typography.headlineSmall)
            uiState.session?.let {
                Text("Logged in as: ${it.parentName}")
                Text(it.parentEmail)
            }
            if (uiState.isChildrenLoading) {
                Text("Loading children...", style = MaterialTheme.typography.bodySmall)
            }
            uiState.childrenError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            if (uiState.children.isNotEmpty()) {
                Text("Children", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                uiState.children.forEach {
                    ChildProfileCard(it)
                }
            }
            OutlinedButton(onClick = onRefreshChildren, modifier = Modifier.fillMaxWidth()) {
                Text("Refresh children")
            }
            uiState.childProfile?.let {
                ChildProfileCard(it)
            }
            uiState.childLookupError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            if (uiState.isChildLookupLoading) {
                Text("Fetching child profile...", style = MaterialTheme.typography.bodySmall)
            }
            Button(onClick = {
                showLookupDialog = true
                indexInput = ""
                onClearChild()
            }, modifier = Modifier.fillMaxWidth()) {
                Text("View child profile")
            }
            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Text("Log out")
            }
        }
    }

    if (showLookupDialog) {
        AlertDialog(
            onDismissRequest = { showLookupDialog = false },
            title = { Text("Child index number") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Enter the customized index number assigned to your child.")
                    OutlinedTextField(
                        value = indexInput,
                        onValueChange = { indexInput = it },
                        singleLine = true,
                        label = { Text("Index number") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    onLookupChild(indexInput.trim())
                    showLookupDialog = false
                }, enabled = indexInput.isNotBlank()) {
                    Text("Lookup")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLookupDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ChildProfileCard(profile: StudentProfileResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(profile.fullName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("Grade: ${profile.grade}")
            Text("Status: ${profile.status}")
            Text("Enrolled: ${profile.enrollmentDate}")
        }
    }
}

@Composable
private fun DashboardWithDrawer(
    uiState: AuthUiState,
    onLookupChild: (String) -> Unit,
    onClearChild: () -> Unit,
    onRefreshChildren: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToPurchase: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerItem(
                    label = { Text("Dashboard") },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Purchase Form") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToPurchase()
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Log out") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    }
                )
            }
        }
    ) {
        DashboardScreen(
            uiState = uiState,
            onLookupChild = onLookupChild,
            onClearChild = onClearChild,
            onOpenDrawer = { scope.launch { drawerState.open() } },
            onRefreshChildren = onRefreshChildren,
            onLogout = onLogout
        )
    }
}

@Composable
private fun PurchaseFormScreen(
    uiState: AuthUiState,
    onBack: () -> Unit,
    onInitiatePurchase: (String, String, String?) -> Unit,
    onVerifyPayment: (String) -> Unit,
    onClearPurchase: () -> Unit
) {
    val context = LocalContext.current
    var childIndex by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(uiState.session?.parentEmail ?: "") }
    var phone by remember { mutableStateOf("") }

    LaunchedEffect(uiState.purchaseAuthUrl) {
        val url = uiState.purchaseAuthUrl
        if (url != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    }

    Card(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Purchase Admission Form", style = MaterialTheme.typography.headlineSmall)
            Text("Price: GHS 1.00 per form")
            OutlinedTextField(
                value = childIndex,
                onValueChange = { childIndex = it },
                singleLine = true,
                label = { Text("Child Index (optional)") }
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                label = { Text("Email") }
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                singleLine = true,
                label = { Text("Phone") }
            )
            if (uiState.isPurchaseLoading) {
                Text("Starting payment...", style = MaterialTheme.typography.bodySmall)
            }
            uiState.purchaseError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            Button(
                onClick = {
                    onClearPurchase()
                    val idx = childIndex.trim().ifBlank { null }
                    onInitiatePurchase(email.trim(), phone.trim(), idx)
                },
                enabled = email.isNotBlank() && phone.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Proceed to payment")
            }
            if (uiState.purchaseReference != null) {
                if (uiState.isVerifyLoading) {
                    Text("Verifying payment...", style = MaterialTheme.typography.bodySmall)
                }
                uiState.verifyError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
                uiState.verifyStatus?.let { Text("Payment status: $it") }
                OutlinedButton(onClick = { onVerifyPayment(uiState.purchaseReference) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Verify payment")
                }
            }
            TextButton(onClick = onBack) { Text("Back") }
        }
    }
}
