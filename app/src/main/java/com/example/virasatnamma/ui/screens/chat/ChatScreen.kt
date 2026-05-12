package com.example.virasatnamma.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasatnamma.data.model.ChatMessage
import com.example.virasatnamma.ui.theme.BrownDark
import com.example.virasatnamma.ui.theme.BrownMedium
import com.example.virasatnamma.ui.theme.GoldPrimary
import com.example.virasatnamma.viewmodel.HeritageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    siteId: String,
    viewModel: HeritageViewModel,
    onBack: () -> Unit
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isTyping by viewModel.isAiTyping.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val site = viewModel.getSiteById(siteId)

    LaunchedEffect(siteId) {
        viewModel.initChat(siteId)
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(BrownDark, BrownMedium)))
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("AI Heritage Guide", color = GoldPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(site?.name ?: "", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
        }

        // Messages
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message = message)
            }
            if (isTyping) {
                item { TypingIndicator() }
            }
        }

        // Suggestion chips
        if (messages.size <= 1) {
            SuggestionChips { suggestion ->
                viewModel.sendMessage(suggestion, siteId)
            }
        }

        // Input bar
        Surface(
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ask about history, tips…", fontSize = 14.sp) },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    maxLines = 3,
                    singleLine = false
                )
                Spacer(Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendMessage(inputText.trim(), siteId)
                            inputText = ""
                        }
                    },
                    containerColor = BrownDark,
                    contentColor = GoldPrimary,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isUser) {
            Surface(
                shape = RoundedCornerShape(50),
                color = BrownDark,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Bottom)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = if (message.isUser) 18.dp else 4.dp,
                topEnd = if (message.isUser) 4.dp else 18.dp,
                bottomStart = 18.dp,
                bottomEnd = 18.dp
            ),
            color = if (message.isUser) BrownDark else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                color = if (message.isUser) Color.White else BrownDark,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
        }
    }
}

@Composable
private fun TypingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(shape = RoundedCornerShape(50), color = BrownDark, modifier = Modifier.size(32.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(Icons.Default.AutoAwesome, null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
            }
        }
        Spacer(Modifier.width(8.dp))
        Surface(
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 18.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Text("Thinking…", modifier = Modifier.padding(12.dp, 10.dp), color = BrownMedium, fontSize = 13.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuggestionChips(onChipClick: (String) -> Unit) {
    val suggestions = listOf("Tell me the history", "Architecture style?", "Best time to visit", "Local food nearby")
    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
        Text("Quick questions:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(0.5f))
        Spacer(Modifier.height(6.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            suggestions.take(2).forEach { s ->
                SuggestionChip(
                    onClick = { onChipClick(s) },
                    label = { Text(s, fontSize = 12.sp) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = GoldPrimary.copy(alpha = 0.15f),
                        labelColor = BrownDark
                    )
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            suggestions.drop(2).forEach { s ->
                SuggestionChip(
                    onClick = { onChipClick(s) },
                    label = { Text(s, fontSize = 12.sp) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = GoldPrimary.copy(alpha = 0.15f),
                        labelColor = BrownDark
                    )
                )
            }
        }
    }
}
