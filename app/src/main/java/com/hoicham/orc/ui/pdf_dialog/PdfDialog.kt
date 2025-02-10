package com.hoicham.orc.ui.pdf_dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hoicham.orc.R
import com.hoicham.orc.ui.pdf_dialog.components.PdfDropdownMenu

@Composable
fun PdfDialog(
    modifier: Modifier = Modifier,
    colorList: List<String>,
    fontSizeList: List<String>,
    onCancelClick: () -> Unit,
    onExportClick: (String, String) -> Unit
) {
    val titleTextStyle = TextStyle(
        color = MaterialTheme.colorScheme.primaryContainer,
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium
    )

    var selectedFont by remember {
        mutableStateOf(fontSizeList.first())
    }

    var selectedColor by remember {
        mutableStateOf(colorList.first())
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.then(modifier)
    ) {
        Column(
            modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.customize_your_export),
                style = titleTextStyle,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            PdfDropdownMenu(items = colorList,
                label = stringResource(id = R.string.color),
                onSelected = { selectedColor = it })

            PdfDropdownMenu(items = fontSizeList,
                label = stringResource(id = R.string.font_size),
                onSelected = { selectedFont = it })

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    12.dp, alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onCancelClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }

                Button(onClick = { onExportClick(selectedColor, selectedFont) }) {
                    Text(text = stringResource(id = R.string.export))
                }
            }
        }
    }
}