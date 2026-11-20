package com.example.zikirmatik // Kendi proje paket adınla değiştir

import android.content.Context
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val prefs by lazy {
        getSharedPreferences("zikirmatik_prefs", Context.MODE_PRIVATE)
    }

    private var count by mutableStateOf(0)
    private var showSplash by mutableStateOf(true)
    private var showResetDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        count = prefs.getInt("zikir_count", 0)

        setContent {
            LaunchedEffect(Unit) {
                delay(1500)
                showSplash = false
            }

            Crossfade(targetState = showSplash, label = "splash") { isSplash ->
                if (isSplash) {
                    SplashScreen()
                } else {
                    ZikirmatikScreen(
                        count = count,
                        showResetDialog = showResetDialog,
                        onIncrement = { incrementCount() },
                        onResetClick = { showResetDialog = true },
                        onResetConfirm = {
                            resetCount()
                            showResetDialog = false
                        },
                        onResetDismiss = { showResetDialog = false }
                    )
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (!showSplash && !showResetDialog && event?.repeatCount == 0) {
                incrementCount()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun incrementCount() {
        count++
        prefs.edit().putInt("zikir_count", count).apply()
        window.decorView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }

    private fun resetCount() {
        count = 0
        prefs.edit().putInt("zikir_count", 0).apply()
        window.decorView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ZikirmatikIcon(modifier = Modifier.size(140.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Zikirmatik",
                color = Color(0xFFECFDF5),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ZikirmatikIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawRoundRect(
            color = Color(0xFF10B981),
            topLeft = Offset(w * 0.22f, h * 0.12f),
            size = Size(w * 0.56f, h * 0.76f),
            cornerRadius = CornerRadius(w * 0.22f, w * 0.22f)
        )
        drawRoundRect(
            color = Color(0xFF064E3B),
            topLeft = Offset(w * 0.30f, h * 0.22f),
            size = Size(w * 0.40f, h * 0.18f),
            cornerRadius = CornerRadius(w * 0.05f, w * 0.05f)
        )
        drawCircle(
            color = Color(0xFFA7F3D0),
            radius = w * 0.045f,
            center = Offset(w * 0.66f, h * 0.47f)
        )
        drawCircle(
            color = Color(0xFFECFDF5),
            radius = w * 0.14f,
            center = Offset(w * 0.50f, h * 0.66f)
        )
    }
}

@Composable
fun ZikirmatikScreen(
    count: Int,
    showResetDialog: Boolean,
    onIncrement: () -> Unit,
    onResetClick: () -> Unit,
    onResetConfirm: () -> Unit,
    onResetDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "ZİKİRMATİK",
            color = Color(0xFF94A3B8),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(top = 24.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E293B), RoundedCornerShape(24.dp))
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                color = Color(0xFF34D399),
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onIncrement,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
            modifier = Modifier.size(200.dp)
        ) {
            Text(
                text = "ARTIR",
                color = Color(0xFF022C22),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            OutlinedButton(
                onClick = onResetClick,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF87171)),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(48.dp)
            ) {
                Text(text = "Sıfırla", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Ses açma / kısma tuşları sayacı artırır",
                color = Color(0xFF64748B),
                fontSize = 13.sp
            )
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = onResetDismiss,
            containerColor = Color(0xFF1E293B),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCBD5E1),
            title = { Text("Sıfırlama Onayı") },
            text = { Text("Zikir sayacını sıfırlamak istediğine emin misin?") },
            confirmButton = {
                TextButton(onClick = onResetConfirm) {
                    Text("Evet, Sıfırla", color = Color(0xFFF87171))
                }
            },
            dismissButton = {
                TextButton(onClick = onResetDismiss) {
                    Text("İptal", color = Color(0xFF94A3B8))
                }
            }
        )
    }
}
