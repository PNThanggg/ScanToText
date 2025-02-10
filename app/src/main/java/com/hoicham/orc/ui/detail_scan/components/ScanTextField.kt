package com.hoicham.orc.ui.detail_scan.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hoicham.orc.ui.support.theme.TextColorGray

@Composable
fun ScanDateText(
    modifier: Modifier = Modifier, text: String
) {
    Text(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .then(modifier),
        text = text,
        fontSize = 14.sp,
        color = TextColorGray
    )
}


@Composable
fun ScanTextField(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 22.sp,
    maxLines: Int = Int.MAX_VALUE,
    text: String,
    onTextChanged: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.then(modifier),
        value = text,
        onValueChange = onTextChanged,
//        colors = TextFieldDefaults.outlinedTextFieldColors(
//            cursorColor = MaterialTheme.colorScheme.primaryVariant,
//            textColor = MaterialTheme.colorScheme.primaryVariant,
//            disabledBorderColor = Color.Transparent,
//            errorBorderColor = Color.Transparent,
//            focusedBorderColor = Color.Transparent,
//            unfocusedBorderColor = Color.Transparent
//        ),
        placeholder = {
            Text(
                text = "Enter Title...", color = Color.LightGray, fontSize = fontSize
            )
        },
        textStyle = TextStyle(fontSize = fontSize),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        maxLines = maxLines,
    )
}

@Preview
@Composable
fun ScanTextPreview() {
    ScanDateText(text = "Title")
}