
package com.eggdevs.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme

@Composable
fun SuperRunnerDialog(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onDismissDialog: () -> Unit = {},
    primaryContent: @Composable () -> Unit = {},
    secondaryContent: @Composable () -> Unit = {}
) {
    Dialog(
        onDismissRequest = { onDismissDialog() },
    ) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                secondaryContent()
                primaryContent()
            }
        }
    }
}

@Preview
@Composable
private fun SuperRunnerDialogPrev() {
    SuperRunnerTheme {
        SuperRunnerDialog(
            title = "title",
            description = "des",
            primaryContent = {
                SuperRunnerActionButton(text = "Okay", modifier = Modifier.width(100.dp))
            },
            secondaryContent = {
                SuperRunnerOutlinedActionButton(text = "Cancel", modifier = Modifier.width(100.dp))
            }
        )
    }
}