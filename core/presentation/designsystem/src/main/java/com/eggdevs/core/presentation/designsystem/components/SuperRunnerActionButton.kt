package com.eggdevs.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eggdevs.core.presentation.designsystem.SuperRunnerBlack
import com.eggdevs.core.presentation.designsystem.SuperRunnerGray
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme

@Composable
fun SuperRunnerActionButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = SuperRunnerGray,
            disabledContentColor = SuperRunnerBlack,
        ),
        shape = RoundedCornerShape(100f),
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            SuperRunnerLoader(
                content = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(15.dp)
                            .alpha(if (isLoading) 1f else 0f),
                        strokeWidth = 1.5.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            )
            Text(
                text = text,
                modifier = Modifier
                    .alpha(if (isLoading) 0f else 1f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
private fun SuperRunnerActionButtonPreview() {
    SuperRunnerTheme {
        SuperRunnerActionButton(
            text = "Sign Up"
        )
    }
}