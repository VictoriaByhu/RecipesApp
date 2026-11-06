package com.victoria.recipes.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.victoria.recipes.domain.model.PasswordError
import com.victoria.recipes.domain.model.UsernameError
import com.victoria.recipes.domain.usecase.ValidatePasswordUseCase
import com.victoria.recipes.domain.usecase.ValidateUsernameUseCase
import com.victoria.recipes.mvi.ObserveEffects
import com.victoria.recipes.ui.AppSnackbarHost
import com.victoria.recipes.ui.BackgroundStyle
import com.victoria.recipes.ui.PrimaryButton
import com.victoria.recipes.ui.ScreenBackground
import com.victoria.recipes.ui.SoftCard
import com.victoria.recipes.ui.rememberErrorMessages
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import recipesapp.shared.generated.resources.Res
import recipesapp.shared.generated.resources.auth_hide_password
import recipesapp.shared.generated.resources.auth_password
import recipesapp.shared.generated.resources.auth_password_error_empty
import recipesapp.shared.generated.resources.auth_password_error_spaces
import recipesapp.shared.generated.resources.auth_password_error_too_short
import recipesapp.shared.generated.resources.auth_password_placeholder
import recipesapp.shared.generated.resources.auth_show_password
import recipesapp.shared.generated.resources.auth_sign_in
import recipesapp.shared.generated.resources.auth_subtitle
import recipesapp.shared.generated.resources.auth_title
import recipesapp.shared.generated.resources.auth_username
import recipesapp.shared.generated.resources.auth_username_error_empty
import recipesapp.shared.generated.resources.auth_username_error_invalid
import recipesapp.shared.generated.resources.auth_username_error_too_long
import recipesapp.shared.generated.resources.auth_username_error_too_short
import recipesapp.shared.generated.resources.auth_username_placeholder
import recipesapp.shared.generated.resources.ic_lock
import recipesapp.shared.generated.resources.ic_person
import recipesapp.shared.generated.resources.ic_visibility
import recipesapp.shared.generated.resources.ic_visibility_off
import recipesapp.shared.generated.resources.img_cat

@Composable
fun AuthScreen(
    onSignedIn: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessages = rememberErrorMessages()
    val scope = rememberCoroutineScope()

    ObserveEffects(viewModel.effects) { effect ->
        when (effect) {
            AuthEffect.NavigateToHome -> onSignedIn()
            is AuthEffect.ShowErrorSnackbar -> scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(errorMessages.of(effect.error))
            }
        }
    }

    AuthContent(state = state, snackbarHostState = snackbarHostState, onIntent = viewModel::onIntent)
}

@Composable
private fun AuthContent(
    state: AuthState,
    snackbarHostState: SnackbarHostState,
    onIntent: (AuthIntent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    val signIn = {
        keyboard?.hide()
        focusManager.clearFocus()
        onIntent(AuthIntent.SignInClicked)
    }

    ScreenBackground(BackgroundStyle.WAVES) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .imePadding(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = maxHeight)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(Res.drawable.img_cat),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(130.dp).padding(top = 6.dp),
                    )
                }

                Spacer(Modifier.height(24.dp))
                Text(
                    text = stringResource(Res.string.auth_title),
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 30.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.auth_subtitle),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, lineHeight = 22.sp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 300.dp),
                )
                Spacer(Modifier.height(28.dp))

                SoftCard(
                    modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth(),
                    contentPadding = 22.dp,
                    spacing = 18.dp,
                ) {
                    AuthField(
                        label = stringResource(Res.string.auth_username),
                        value = state.username,
                        onValueChange = { onIntent(AuthIntent.UsernameChanged(it)) },
                        placeholder = stringResource(Res.string.auth_username_placeholder),
                        leadingIcon = Res.drawable.ic_person,
                        enabled = !state.isLoading,
                        error = state.usernameError?.let { usernameErrorText(it) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.None,
                            autoCorrectEnabled = false,
                            imeAction = ImeAction.Next,
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) },
                        ),
                    )

                    AuthField(
                        label = stringResource(Res.string.auth_password),
                        value = state.password,
                        onValueChange = { onIntent(AuthIntent.PasswordChanged(it)) },
                        placeholder = stringResource(Res.string.auth_password_placeholder, ValidatePasswordUseCase.MIN_LENGTH),
                        leadingIcon = Res.drawable.ic_lock,
                        enabled = !state.isLoading,
                        error = state.passwordError?.let { passwordErrorText(it) },
                        visualTransformation = if (state.isPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailing = {
                            IconButton(onClick = { onIntent(AuthIntent.TogglePasswordVisibility) }) {
                                Icon(
                                    painter = painterResource(
                                        if (state.isPasswordVisible) Res.drawable.ic_visibility_off else Res.drawable.ic_visibility
                                    ),
                                    contentDescription = stringResource(
                                        if (state.isPasswordVisible) Res.string.auth_hide_password else Res.string.auth_show_password
                                    ),
                                    modifier = Modifier.size(22.dp),
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            autoCorrectEnabled = false,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { signIn() },
                        ),
                    )

                    PrimaryButton(
                        text = stringResource(Res.string.auth_sign_in),
                        onClick = signIn,
                        loading = state.isLoading,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
        AppSnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .safeDrawingPadding()
                .imePadding()
                .padding(16.dp),
        )
    }
}

@Composable
private fun AuthField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: DrawableResource,
    error: String?,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    enabled: Boolean,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: (@Composable () -> Unit)? = null,
) {
    val colors = MaterialTheme.colorScheme
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (error != null) colors.error else colors.onSurfaceVariant,
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            placeholder = {
                Text(placeholder, color = colors.onSurfaceVariant.copy(alpha = 0.7f), fontSize = 16.sp)
            },
            leadingIcon = {
                Icon(painterResource(leadingIcon), contentDescription = null, modifier = Modifier.size(22.dp))
            },
            trailingIcon = trailing,
            isError = error != null,
            supportingText = error?.let { { Text(it, fontWeight = FontWeight.Medium) } },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            shape = MaterialTheme.shapes.small,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.surface,
                unfocusedContainerColor = colors.background,
                errorContainerColor = colors.errorContainer,
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.outline,
                errorBorderColor = colors.error,
                focusedLeadingIconColor = colors.primary,
                unfocusedLeadingIconColor = colors.onSurfaceVariant,
                errorLeadingIconColor = colors.error,
                focusedTrailingIconColor = colors.onSurfaceVariant,
                unfocusedTrailingIconColor = colors.onSurfaceVariant,
                cursorColor = colors.primary,
            ),
        )
    }
}

@Composable
private fun usernameErrorText(error: UsernameError): String = when (error) {
    UsernameError.EMPTY -> stringResource(Res.string.auth_username_error_empty)
    UsernameError.TOO_SHORT -> stringResource(Res.string.auth_username_error_too_short, ValidateUsernameUseCase.MIN_LENGTH)
    UsernameError.TOO_LONG -> stringResource(Res.string.auth_username_error_too_long, ValidateUsernameUseCase.MAX_LENGTH)
    UsernameError.INVALID_CHARACTERS -> stringResource(Res.string.auth_username_error_invalid)
}

@Composable
private fun passwordErrorText(error: PasswordError): String = when (error) {
    PasswordError.EMPTY -> stringResource(Res.string.auth_password_error_empty)
    PasswordError.TOO_SHORT -> stringResource(Res.string.auth_password_error_too_short, ValidatePasswordUseCase.MIN_LENGTH)
    PasswordError.CONTAINS_SPACES -> stringResource(Res.string.auth_password_error_spaces)
}
