@file:OptIn(ExperimentalMaterial3Api::class)

package com.eggdevs.core.presentation.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eggdevs.core.presentation.designsystem.AnalyticsIcon
import com.eggdevs.core.presentation.designsystem.ArrowLeftIcon
import com.eggdevs.core.presentation.designsystem.LogoIcon
import com.eggdevs.core.presentation.designsystem.Poppins
import com.eggdevs.core.presentation.designsystem.R
import com.eggdevs.core.presentation.designsystem.SuperRunnerGreen
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme
import com.eggdevs.core.presentation.designsystem.models.DropDownItem

@Composable
fun SuperRunnerToolbar(
    modifier: Modifier = Modifier,
    title: String = "",
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    dropDownItemList: List<DropDownItem> = emptyList(),
    onMenuItemClick: (Int) -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    startContent: (@Composable () -> Unit)? = null,
) {
    var isDropDownMenuOpen by rememberSaveable {
        mutableStateOf(false)
    }
    TopAppBar(
        modifier = modifier,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                startContent?.invoke()
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = Poppins
                )
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = ArrowLeftIcon,
                        contentDescription = stringResource(id = R.string.go_back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
        colors = colors,
        actions = {
            if (dropDownItemList.isNotEmpty()) {
                Box {
                    DropdownMenu(
                        expanded = isDropDownMenuOpen,
                        onDismissRequest = {
                            isDropDownMenuOpen = false
                        }
                    ) {
                        dropDownItemList.forEachIndexed { index, menuItem ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clickable { onMenuItemClick(index) },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = menuItem.icon,
                                    contentDescription = menuItem.title
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = menuItem.title)
                            }
                        }
                    }

                    IconButton(onClick = { isDropDownMenuOpen = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(id = R.string.open_menu),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun SuperRunnerToolbarPreview() {
    SuperRunnerTheme {
        SuperRunnerToolbar(
            title = "SuperRunner",
            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = Color.Transparent
            ),
            showBackButton = false,
            startContent = {
                Icon(
                    imageVector = LogoIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp),
                    tint = SuperRunnerGreen
                )
            },
            dropDownItemList = listOf(
                DropDownItem(
                    title = "Analytics",
                    icon = AnalyticsIcon
                )
            )
        )
    }
}