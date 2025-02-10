package com.hoicham.orc.ui.detail_scan.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hoicham.orc.database.entity.ExtractionModel
import com.hoicham.orc.database.entity.ExtractionModelType
import com.hoicham.orc.ui.support.theme.ChipBlue
import com.hoicham.orc.ui.support.theme.ChipGreen
import com.hoicham.orc.ui.support.theme.ChipOrange
import com.hoicham.orc.ui.support.theme.ChipYellow
import compose.icons.TablerIcons
import compose.icons.tablericons.At
import compose.icons.tablericons.LetterCase
import compose.icons.tablericons.Link
import compose.icons.tablericons.Phone

@Composable
fun TextEntityChip(
    modifier: Modifier = Modifier,
    entity: ExtractionModel,
    onClick: () -> Unit = {},
) {
    val chipColor = when (entity.type) {
        ExtractionModelType.EMAIL -> ChipYellow
        ExtractionModelType.PHONE -> ChipGreen
        ExtractionModelType.URL -> ChipOrange
        ExtractionModelType.OTHER -> ChipBlue
    }

    val icon = when (entity.type) {
        ExtractionModelType.EMAIL -> TablerIcons.At
        ExtractionModelType.PHONE -> TablerIcons.Phone
        ExtractionModelType.URL -> TablerIcons.Link
        ExtractionModelType.OTHER -> TablerIcons.LetterCase
    }

    Surface(
        modifier = Modifier.requiredHeight(36.dp).then(modifier),
        shape = CircleShape,
        onClick = onClick,
        color = chipColor
    ) {
        Row(
            modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(
                space = 6.dp, alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = entity.content,
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Icon(
                modifier = Modifier.drawBehind {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.6f), radius = size.minDimension / 1.65f
                    )
                }, imageVector = icon, contentDescription = "", tint = Color.Black
            )
        }
    }
}
