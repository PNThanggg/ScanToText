package com.hoicham.orc.ui.detail_scan.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Copy
import compose.icons.tablericons.Ear
import compose.icons.tablericons.Language
import compose.icons.tablericons.Share


@Composable
fun ScanDetailBottomBar(
    modifier: Modifier = Modifier,
    iconButtonSize: Dp = 52.dp,
    iconSize: Dp = 28.dp,
    shape: Shape = RoundedCornerShape(18.dp),
    onCopyClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onTosClick: () -> Unit = {},
    onTranslateClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .then(modifier),
        shape = shape,
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onCopyClick, modifier = Modifier.size(iconButtonSize)) {
                Icon(
                    imageVector = TablerIcons.Copy,
                    contentDescription = "",
                    modifier = Modifier.size(iconSize)
                )
            }
            IconButton(onClick = onShareClick, modifier = Modifier.size(iconButtonSize)) {
                Icon(
                    imageVector = TablerIcons.Share,
                    contentDescription = "",
                    modifier = Modifier.size(iconSize)
                )
            }
            IconButton(onClick = onTosClick, modifier = Modifier.size(iconButtonSize)) {
                Icon(
                    imageVector = TablerIcons.Ear,
                    contentDescription = "",
                    modifier = Modifier.size(iconSize)
                )
            }
            IconButton(onClick = onTranslateClick, modifier = Modifier.size(iconButtonSize)) {
                Icon(
                    imageVector = TablerIcons.Language,
                    contentDescription = "",
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}

@Preview
@Composable
fun ScanDetailBottomBarPreview() {
    ScanDetailBottomBar()
}