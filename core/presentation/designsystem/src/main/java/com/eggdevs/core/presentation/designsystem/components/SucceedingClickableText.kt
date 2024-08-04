package com.eggdevs.core.presentation.designsystem.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.eggdevs.core.presentation.designsystem.Poppins

@Composable
fun SucceedingClickableText(
    modifier: Modifier = Modifier,
    precedingText: String = "",
    succeedingClickableText: String = "",
    onClick: () -> Unit = {}
) {
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontFamily = Poppins,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            append(precedingText)
            append(" ")
            pushStringAnnotation(
                tag = "clickable_text",
                annotation = "babu" // useless
            )
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = Poppins
                )
            ) {
                append(succeedingClickableText)
            }
        }
    }
    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(
                tag = "clickable_text",
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                onClick()
            }
        }
    )

}