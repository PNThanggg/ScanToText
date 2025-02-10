package com.hoicham.orc.ui.detail_scan.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ScanDetailHeader(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChanged: (String) -> Unit,
    dateCreated: String,
    dateModified: String,
    isCreated: Int
) {
    var titleText by remember { mutableStateOf(title) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = isCreated) {
        if (isCreated == 1) focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.then(modifier)
    ) {
        ScanTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            text = titleText,
            onTextChanged = {
                titleText = it
                onTitleChanged(it)
            },
            maxLines = 2
        )

        ScanDateText(
            text = dateCreated
        )

        ScanDateText(
            text = dateModified
        )
    }
}

@Preview
@Composable
fun ScanDetailHeaderPreview() {
    ScanDetailHeader(
        title = "Title",
        onTitleChanged = {},
        dateCreated = "Date Created",
        dateModified = "Date Modified",
        isCreated = 1
    )
}