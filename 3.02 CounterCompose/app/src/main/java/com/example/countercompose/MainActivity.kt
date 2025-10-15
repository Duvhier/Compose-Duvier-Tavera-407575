package com.example.countercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countercompose.ui.theme.CounterComposeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CounterComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var counter by remember { mutableStateOf(0) }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF002B5B),
            Color(0xFF007ACC),
            Color(0xFF00B4D8)
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.countercompose),
            contentDescription = "App logo",
            modifier = Modifier
                .size(160.dp)
                .graphicsLayer {
                    shadowElevation = 12f
                    shape = RoundedCornerShape(20.dp)
                    clip = true
                    alpha = 0.95f
                }
                .padding(bottom = 12.dp)
        )

        Text(
            text = stringResource(id = R.string.counter_text),
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(
            text = counter.toString(),
            fontSize = 64.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedButton(
                text = stringResource(id = R.string.plus),
                baseColor = Color(0xFF007ACC),
                pressedColor = Color(0xFF0099FF)
            ) {
                counter++
            }

            AnimatedButton(
                text = stringResource(id = R.string.minus),
                baseColor = Color(0xFF00B4D8),
                pressedColor = Color(0xFF48CAE4)
            ) {
                if (counter > 0) counter--
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CounterComposeTheme {
        MainScreen(modifier = Modifier.padding(20.dp))
    }
}

/**
 * 🔘 Composable reutilizable con animación de escala y color
 */
@Composable
fun AnimatedButton(
    text: String,
    baseColor: Color,
    pressedColor: Color,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val scale by animateFloatAsState(targetValue = if (isPressed) 1.1f else 1f)
    val color by animateColorAsState(targetValue = if (isPressed) pressedColor else baseColor)

    Button(
        onClick = {
            isPressed = true
            onClick()

            scope.launch {
                kotlinx.coroutines.delay(150)
                isPressed = false
            }
        },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier
            .padding(8.dp)
            .height(80.dp)
            .width(100.dp)
            .scale(scale)
    ) {
        Text(
            text = text,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}