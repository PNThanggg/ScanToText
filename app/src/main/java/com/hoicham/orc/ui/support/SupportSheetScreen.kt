package com.hoicham.orc.ui.support

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hoicham.orc.R
import com.hoicham.orc.ui.support.theme.AppTheme

@Composable
fun SupportScreen(
    modifier: Modifier = Modifier,
    state: SupportUiState,
    onVendorSelected: (Vendor) -> Unit,
    onSupportClicked: () -> Unit
) {
    AppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp, start = 4.dp, end = 4.dp, bottom = 4.dp).then(modifier)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().then(modifier)
            ) {
                SupportTopBar(modifier = Modifier.padding(horizontal = 16.dp))

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = stringResource(R.string.consider_support),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "You can do that by\nchoosing one of the options below.",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.payment_method),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.payment_options),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))

                VendorList(
                    modifier = Modifier.fillMaxWidth(),
                    vendors = state.vendors,
                    onVendorSelected = onVendorSelected
                )

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                        .padding(bottom = 12.dp).height(64.dp),
                    onClick = { onSupportClicked() },
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(text = stringResource(R.string.support_btn), fontSize = 20.sp)
                }
            }
        }
    }
}