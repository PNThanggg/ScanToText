package com.hoicham.orc.ui.pdf_dialog.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfDropdownMenu(
    modifier: Modifier = Modifier,
    items: List<String>,
    label: String,
    onSelected: (String) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    var item by remember {
        mutableStateOf(items[0])
    }

    ExposedDropdownMenuBox(
        modifier = Modifier.then(modifier),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            value = item,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                focusedLabelColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                focusedTrailingIconColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            items.forEach { selectedItem ->
                DropdownMenuItem(
                    text = {
                        Text(text = selectedItem)
                    },
                    onClick = {
                        item = selectedItem
                        expanded = false
                        onSelected(selectedItem)
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun ReviewPdfDropdownMenu() {
    PdfDropdownMenu(label = "Label", items = listOf("Item 1", "Item 2", "Item 3"), onSelected = {})
}