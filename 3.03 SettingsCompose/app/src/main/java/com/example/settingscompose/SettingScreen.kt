package com.example.settingscompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.settingscompose.ui.theme.SettingsComposeTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle

val HeaderTextStyleLocal = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)

@Composable
fun SettingsContainer(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SettingsHeader()
        Spacer(modifier = Modifier.height(8.dp))
        SettingsImage()
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        SettingsCheckbox()
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        SettingsSwitch()
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        SettingsSlider()
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        SettingsRadioButtons()
        Spacer(modifier = Modifier.height(16.dp))
        SettingsAlertDialog()
    }
}

@Composable
fun SettingsHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = HeaderTextStyleLocal,
            modifier = Modifier.padding(end = 10.dp)
        )
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = stringResource(id = R.string.settings_icon_description)
        )
    }
}

@Composable
fun SettingsImage() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(start = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.settings_profile_image),
            fontSize = 18.sp
        )

        Image(
            painter = painterResource(id = R.drawable.sunflower),
            contentDescription = stringResource(id = R.string.settings_profile_image),
            modifier = Modifier
                .padding(10.dp)
                .height(64.dp)
                .clickable { /* Cambiar imagen */ }
        )

    }
}

@Composable
fun SettingsCheckbox() {
    var isChecked by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(id = R.string.settings_consent), fontSize = 18.sp)
        Checkbox(
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
    }
}

@Composable
fun SettingsSwitch() {
    var isChecked by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(id = R.string.settings_mobile_data), fontSize = 18.sp)
        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
            modifier = Modifier.padding(end = 10.dp)
        )
    }
}

@Composable
fun SettingsSlider() {
    var sliderValue by remember { mutableStateOf(0f) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(id = R.string.settings_text_size), fontSize = 18.sp, modifier = Modifier.padding(end = 16.dp))
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = 0f..1f,
            steps = 2,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SettingsRadioButtons() {
    var selectedPaymentMethod by remember { mutableStateOf("PayPal") }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = stringResource(id = R.string.payment_method), modifier = Modifier.padding(bottom = 8.dp))
        val options = listOf("PayPal", "Credit Card", "Bank Transfer")
        options.forEach { paymentMethod ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                RadioButton(
                    selected = (selectedPaymentMethod == paymentMethod),
                    onClick = { selectedPaymentMethod = paymentMethod }
                )
                Text(text = paymentMethod, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
fun SettingsAlertDialog() {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f)
            ),
            shape = MaterialTheme.shapes.large,
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
            contentPadding = PaddingValues()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.error,
                                MaterialTheme.colorScheme.errorContainer
                            )
                        ),
                        shape = MaterialTheme.shapes.large
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.sign_out),
                    color = MaterialTheme.colorScheme.onError,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    fontSize = 18.sp
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(id = R.string.alert_title)) },
            text = { Text(stringResource(id = R.string.alert_message)) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(
                        text = stringResource(id = R.string.ok),
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsPreview() {
    SettingsComposeTheme {
        SettingsContainer()
    }
}
