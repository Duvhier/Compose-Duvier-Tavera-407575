package com.example.profile

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.palette.graphics.Palette
import coil.compose.rememberAsyncImagePainter
import com.example.profile.ui.theme.ProfileTheme
import java.io.File
import java.io.InputStream

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile() {
    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showChatModal by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }

    var dominantColor by remember { mutableStateOf(Color(0xFF2196F3)) }
    var vibrantColor by remember { mutableStateOf(Color(0xFF00BCD4)) }

    val imageFile = remember {
        File(context.cacheDir, "profile_photo.jpg").apply { createNewFile() }
    }
    val imageUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            selectedImageUri = imageUri
            extractColorsFromUri(context, imageUri) { dominant, vibrant ->
                dominantColor = dominant
                vibrantColor = vibrant
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            extractColorsFromUri(context, uri) { dominant, vibrant ->
                dominantColor = dominant
                vibrantColor = vibrant
            }
        }
    }

    // Variable para controlar qué acción ejecutar después de los permisos
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }

        if (allGranted) {
            // Ejecutar la acción pendiente si todos los permisos fueron concedidos
            pendingAction?.invoke()
        } else {
            showPermissionDialog = true
        }
        pendingAction = null
    }

    fun checkPermissions(action: () -> Unit) {
        // Determinar qué permisos solicitar basado en la versión de Android
        val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        // Verificar si ya tenemos los permisos
        val hasAllPermissions = permissionsToRequest.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }

        if (hasAllPermissions) {
            action()
        } else {
            // Guardar la acción y solicitar permisos
            pendingAction = action
            permissionLauncher.launch(permissionsToRequest)
        }
    }

    var followers by remember { mutableIntStateOf(245) }
    var following by remember { mutableIntStateOf(180) }
    var posts by remember { mutableIntStateOf(32) }
    var isFollowing by remember { mutableStateOf(false) }

    val animatedFollowers by animateIntAsState(followers, tween(600))
    val animatedFollowing by animateIntAsState(following, tween(600))
    val animatedPosts by animateIntAsState(posts, tween(600))

    val gradientBrush = Brush.linearGradient(listOf(dominantColor, vibrantColor))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(130.dp)) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(120.dp)
                            .background(gradientBrush, CircleShape)
                            .padding(4.dp)
                    ) {
                        if (selectedImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(selectedImageUri),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.cat),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }

                    FloatingActionButton(
                        onClick = {
                            checkPermissions {
                                cameraLauncher.launch(imageUri)
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = "Take Photo",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    FloatingActionButton(
                        onClick = {
                            checkPermissions {
                                galleryLauncher.launch("image/*")
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Duvier Tavera",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Mobile Developer & Tech Enthusiast",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(animatedPosts, "Posts")
                    StatItem(animatedFollowers, "Followers")
                    StatItem(animatedFollowing, "Following")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            if (isFollowing) followers -= 1 else followers += 1
                            isFollowing = !isFollowing
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isFollowing) "Unfollow" else "Follow")
                    }

                    OutlinedButton(
                        onClick = { showChatModal = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Message")
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showChatModal,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            ChatModal(onClose = { showChatModal = false })
        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            confirmButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Permisos requeridos") },
            text = { Text("Por favor otorga permisos de cámara y almacenamiento para cambiar tu foto de perfil.") }
        )
    }
}

@Composable
fun ChatModal(onClose: () -> Unit) {
    var message by remember { mutableStateOf(TextFieldValue("")) }

    Surface(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .wrapContentHeight()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Chat con Duvier Tavera",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Escribe un mensaje...") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onClose) {
                    Text("Cerrar")
                }
                Button(onClick = {
                    message = TextFieldValue("")
                    onClose()
                }) {
                    Text("Enviar")
                }
            }
        }
    }
}

@Composable
fun StatItem(number: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            number.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            label,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

fun extractColorsFromUri(
    context: android.content.Context,
    uri: Uri,
    onColorsExtracted: (Color, Color) -> Unit
) {
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        Palette.from(bitmap).generate { palette ->
            val dominant = palette?.getDominantColor(0xFF2196F3.toInt()) ?: 0xFF2196F3.toInt()
            val vibrant = palette?.getVibrantColor(0xFF00BCD4.toInt()) ?: 0xFF00BCD4.toInt()
            onColorsExtracted(Color(dominant), Color(vibrant))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun PreviewProfile() {
    ProfileTheme {
        Profile()
    }
}