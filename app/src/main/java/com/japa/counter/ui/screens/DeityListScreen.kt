package com.japa.counter.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.japa.counter.data.entity.DeityCounterEntity
import com.japa.counter.ui.viewmodel.CounterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeityListScreen(
    viewModel: CounterViewModel,
    onNavigateBack: () -> Unit
) {
    val deities by viewModel.allDeities.collectAsState()
    val activeDeity by viewModel.activeDeity.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val showAddEditDialog by viewModel.showAddEditDialog.collectAsState()
    val showEditCountDialog by viewModel.showEditCountDialog.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Deities") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.setShowAddEditDialog(DeityCounterEntity(name = "")) },
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Deity"
                )
            }
        }
    ) { paddingValues ->
        if (deities.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No deities yet",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap + to add your first chant",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(deities, key = { it.id }) { deity ->
                    DeityListItem(
                        deity = deity,
                        isActive = activeDeity?.id == deity.id,
                        onSelect = {
                            viewModel.setActiveDeity(deity.id)
                            onNavigateBack()
                        },
                        onEditName = { viewModel.setShowAddEditDialog(deity) },
                        onEditCount = { viewModel.setShowEditCountDialog(deity) },
                        onDelete = { viewModel.setShowDeleteDialog(deity) }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }

    // Delete confirmation dialog
    showDeleteDialog?.let { deityToDelete ->
        AlertDialog(
            onDismissRequest = { viewModel.setShowDeleteDialog(null) },
            title = { Text("Delete Deity?") },
            text = { Text("Are you sure you want to delete \"${deityToDelete.name}\"? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = { viewModel.deleteDeity(deityToDelete) }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setShowDeleteDialog(null) }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add/Edit dialog
    showAddEditDialog?.let { deity ->
        AddEditDeityDialog(
            deity = deity,
            isEdit = deity.id != 0L,
            onDismiss = { viewModel.setShowAddEditDialog(null) },
            onConfirm = { name ->
                if (deity.id == 0L) {
                    viewModel.addDeity(name)
                } else {
                    viewModel.updateDeityName(deity.id, name)
                }
            }
        )
    }

    // Edit count dialog
    showEditCountDialog?.let { deity ->
        EditCountDialog(
            deity = deity,
            onDismiss = { viewModel.setShowEditCountDialog(null) },
            onConfirm = { newCount ->
                viewModel.updateDeityCount(deity.id, newCount)
            }
        )
    }
}

@Composable
private fun DeityListItem(
    deity: DeityCounterEntity,
    isActive: Boolean,
    onSelect: () -> Unit,
    onEditName: () -> Unit,
    onEditCount: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = onSelect
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = deity.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Mala: ${deity.completedMalas}  |  Count: ${deity.currentCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isActive) {
                Text(
                    text = "Active",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit Name") },
                        leadingIcon = { Icon(Icons.Default.Edit, null) },
                        onClick = {
                            expanded = false
                            onEditName()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Edit Count") },
                        leadingIcon = { Icon(Icons.Default.Edit, null) },
                        onClick = {
                            expanded = false
                            onEditCount()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        leadingIcon = { Icon(Icons.Default.Delete, null) },
                        onClick = {
                            expanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}
