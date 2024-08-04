@file:OptIn(ExperimentalFoundationApi::class)

package com.eggdevs.core.presentation.designsystem.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.BasicSecureTextField
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.TextObfuscationMode
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eggdevs.core.presentation.designsystem.EyeClosedIcon
import com.eggdevs.core.presentation.designsystem.EyeOpenedIcon
import com.eggdevs.core.presentation.designsystem.LockIcon
import com.eggdevs.core.presentation.designsystem.R
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme

@Composable
fun SuperRunnerPasswordTextField(
    modifier: Modifier = Modifier,
    isPasswordVisible: Boolean = false,
    onTogglePasswordVisibility: () -> Unit = {},
    state: TextFieldState = TextFieldState(),
    hint: String = "",
    title: String? = null,
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (title != null) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        BasicSecureTextField(
            state = state,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            textObfuscationMode = if (isPasswordVisible) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.Hidden
            },
            keyboardType = KeyboardType.Password,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (isFocused) {
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.05f
                        )
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
                .border(
                    width = 1.dp,
                    color = if (isFocused) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp)
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            decorator = { innerBox ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = LockIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        if (state.text.isBlank() && !isFocused) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = hint,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                    alpha = 0.4f
                                )
                            )
                        }
                        innerBox()
                    }
                    IconButton(
                        onClick = {
                            onTogglePasswordVisibility()
                        }
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible) {
                                EyeOpenedIcon
                            } else {
                                EyeClosedIcon
                            },
                            contentDescription = if (isPasswordVisible) {
                                stringResource(id = R.string.show_password)
                            } else {
                                stringResource(id = R.string.hide_password)
                            },
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            
        }
    }
}

@Preview
@Composable
private fun SuperRunnerTextFieldPreview() {
    SuperRunnerTheme {
        SuperRunnerPasswordTextField(
            state = rememberTextFieldState(),
            hint = "Password",
            title = "Password",
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}