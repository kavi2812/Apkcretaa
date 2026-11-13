package com.example.cretaoemlauncher

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Background = Color(0xFF101317)
private val Panel = Color(0xFF1B2026)
private val Panel2 = Color(0xFF242B32)
private val Accent = Color(0xFFB7D9E8)
private val TextMain = Color(0xFFF3F5F6)
private val TextMuted = Color(0xFFAAB3BA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(16, 19, 23)
        window.navigationBarColor = android.graphics.Color.rgb(16, 19, 23)
        setContent { CretaLauncher { action -> launchAction(action) } }
    }

    private fun launchAction(action: String) {
        val intent = when (action) {
            "maps" -> Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="))
            "phone" -> Intent(Intent.ACTION_DIAL)
            "settings" -> Intent(Settings.ACTION_SETTINGS)
            "bluetooth" -> Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
            "music" -> getMusicIntent()
            "apps" -> Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_MUSIC)
            else -> null
        }
        try {
            if (intent != null) startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            try { startActivity(Intent(Settings.ACTION_SETTINGS)) } catch (_: Exception) {}
        } catch (_: Exception) {}
    }

    private fun getMusicIntent(): Intent? {
        val candidates = listOf(
            "com.spotify.music",
            "com.google.android.youtube.music",
            "com.android.music"
        )
        for (pkg in candidates) {
            val i = packageManager.getLaunchIntentForPackage(pkg)
            if (i != null) return i
        }
        return Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_MUSIC)
    }
}

@Composable
private fun CretaLauncher(onAction: (String) -> Unit) {
    var selectedTab by remember { mutableStateOf("HOME") }
    var time by remember { mutableStateOf(SimpleDateFormat("h:mm", Locale.getDefault()).format(Date())) }
    var date by remember { mutableStateOf(SimpleDateFormat("EEE, d MMM", Locale.getDefault()).format(Date())) }

    LaunchedEffect(Unit) {
        while (true) {
            time = SimpleDateFormat("h:mm", Locale.getDefault()).format(Date())
            date = SimpleDateFormat("EEE, d MMM", Locale.getDefault()).format(Date())
            kotlinx.coroutines.delay(30_000)
        }
    }

    MaterialTheme(colorScheme = darkColorScheme(
        background = Background, surface = Panel, primary = Accent,
        onBackground = TextMain, onSurface = TextMain
    )) {
        Row(
            Modifier.fillMaxSize().background(Background).padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Left-side OEM-inspired navigation rail
            Column(
                Modifier.width(74.dp).fillMaxHeight().clip(RoundedCornerShape(22.dp))
                    .background(Panel).padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("H", color = Accent, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                    RailButton(Icons.Default.Home, "HOME", selectedTab == "HOME") { selectedTab = "HOME" }
                    RailButton(Icons.Default.Map, "MAP", selectedTab == "MAP") {
                        selectedTab = "MAP"; onAction("maps")
                    }
                    RailButton(Icons.Default.MusicNote, "AUDIO", selectedTab == "AUDIO") {
                        selectedTab = "AUDIO"; onAction("music")
                    }
                    RailButton(Icons.Default.Phone, "PHONE", selectedTab == "PHONE") {
                        selectedTab = "PHONE"; onAction("phone")
                    }
                }
                RailButton(Icons.Default.Settings, "SET", false) { onAction("settings") }
            }

            Column(Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    Modifier.fillMaxWidth().height(62.dp).clip(RoundedCornerShape(18.dp))
                        .background(Panel).padding(horizontal = 22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("CRETA", color = TextMain, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 3.sp)
                        Text("Connected drive", color = TextMuted, fontSize = 11.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                        Icon(Icons.Default.Bluetooth, contentDescription = "Bluetooth", tint = Accent, modifier = Modifier.size(20.dp).clickable { onAction("bluetooth") })
                        Icon(Icons.Default.Wifi, contentDescription = "Wi-Fi", tint = TextMuted, modifier = Modifier.size(20.dp))
                        Column(horizontalAlignment = Alignment.End) {
                            Text(time, fontSize = 20.sp, fontWeight = FontWeight.Medium, color = TextMain)
                            Text(date, fontSize = 10.sp, color = TextMuted)
                        }
                    }
                }

                Row(Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1.15f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Main navigation card
                        Box(
                            Modifier.weight(1.45f).fillMaxWidth().clip(RoundedCornerShape(22.dp))
                                .background(Panel2).clickable { onAction("maps") }.padding(22.dp)
                        ) {
                            Column(Modifier.align(Alignment.TopStart)) {
                                Text("NAVIGATION", color = TextMuted, fontSize = 11.sp, letterSpacing = 2.sp)
                                Spacer(Modifier.height(8.dp))
                                Text("Where to?", color = TextMain, fontSize = 27.sp, fontWeight = FontWeight.Medium)
                                Text("Tap to open Google Maps", color = TextMuted, fontSize = 13.sp)
                            }
                            Icon(Icons.Default.NearMe, contentDescription = null, tint = Accent,
                                modifier = Modifier.align(Alignment.BottomEnd).size(64.dp))
                            Text("MAP  ↗", color = Accent, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.align(Alignment.BottomStart))
                        }
                        Row(Modifier.weight(0.78f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            MiniTile("Phone", Icons.Default.Call, Modifier.weight(1f)) { onAction("phone") }
                            MiniTile("Bluetooth", Icons.Default.Bluetooth, Modifier.weight(1f)) { onAction("bluetooth") }
                        }
                    }

                    Column(Modifier.weight(0.95f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(22.dp))
                                .background(Panel).padding(20.dp)
                        ) {
                            Column {
                                Text("NOW PLAYING", color = TextMuted, fontSize = 11.sp, letterSpacing = 2.sp)
                                Spacer(Modifier.height(15.dp))
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Box(Modifier.size(54.dp).clip(RoundedCornerShape(14.dp)).background(Panel2), contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.MusicNote, contentDescription = null, tint = Accent, modifier = Modifier.size(30.dp))
                                    }
                                    Column {
                                        Text("Your music", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                                        Text("Open your music app", color = TextMuted, fontSize = 12.sp)
                                    }
                                }
                            }
                            Row(Modifier.align(Alignment.BottomStart), horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = TextMuted, modifier = Modifier.size(27.dp))
                                Icon(Icons.Default.PlayArrow, contentDescription = "Open music", tint = Accent, modifier = Modifier.size(34.dp).clickable { onAction("music") })
                                Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = TextMuted, modifier = Modifier.size(27.dp))
                            }
                        }
                        Column(Modifier.weight(0.85f).fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Panel).padding(18.dp)) {
                            Text("QUICK ACCESS", color = TextMuted, fontSize = 11.sp, letterSpacing = 2.sp)
                            Spacer(Modifier.height(12.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                QuickApp("Apps", Icons.Default.Apps, Modifier.weight(1f)) { onAction("settings") }
                                QuickApp("Settings", Icons.Default.Settings, Modifier.weight(1f)) { onAction("settings") }
                                QuickApp("Maps", Icons.Default.Map, Modifier.weight(1f)) { onAction("maps") }
                            }
                        }
                    }
                }

                Row(
                    Modifier.fillMaxWidth().height(35.dp).padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("OEM-INSPIRED • ANDROID", color = TextMuted, fontSize = 10.sp, letterSpacing = 1.3.sp)
                    Text("Drive focused. Arrive safely.", color = TextMuted, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun RailButton(icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        Modifier.width(62.dp).clip(RoundedCornerShape(14.dp))
            .background(if (selected) Panel2 else Color.Transparent)
            .clickable { onClick() }.padding(vertical = 9.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, tint = if (selected) Accent else TextMuted, modifier = Modifier.size(22.dp))
        Spacer(Modifier.height(4.dp))
        Text(label, color = if (selected) Accent else TextMuted, fontSize = 8.sp, letterSpacing = 0.5.sp)
    }
}

@Composable
private fun MiniTile(title: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier.height(66.dp).clip(RoundedCornerShape(18.dp)).background(Panel)
            .clickable { onClick() }.padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(icon, contentDescription = null, tint = Accent, modifier = Modifier.size(23.dp))
        Text(title, fontSize = 13.sp, color = TextMain, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun QuickApp(title: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier.clip(RoundedCornerShape(14.dp)).background(Panel2).clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Icon(icon, contentDescription = title, tint = Accent, modifier = Modifier.size(24.dp))
        Text(title, fontSize = 11.sp, color = TextMain)
    }
}
