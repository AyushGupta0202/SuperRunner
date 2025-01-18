package com.eggdevs.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme

@Composable
fun SuperRunnerLoader(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit = { CircularProgressIndicator() }
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        content()
    }
}

@Preview
@Composable
private fun SuperRunnerLoaderPreview() {
    SuperRunnerTheme {
        SuperRunnerLoader(
            modifier = Modifier
                .size(100.dp)
        )
    }
}