package com.elfennani.aniwatch.ui.screens.status

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elfennani.aniwatch.domain.models.ShowStatus
import com.elfennani.aniwatch.domain.models.StatusDate
import com.elfennani.aniwatch.domain.models.StatusDetails
import com.elfennani.aniwatch.domain.models.formatText
import com.elfennani.aniwatch.domain.models.toCalendar
import com.elfennani.aniwatch.ui.composables.Button
import com.elfennani.aniwatch.ui.composables.TextSkeleton
import com.elfennani.aniwatch.ui.composables.dummyShow
import com.elfennani.aniwatch.ui.screens.status.composables.ProgressBottomSheet
import com.elfennani.aniwatch.ui.screens.status.composables.ScoreBottomSheet
import com.elfennani.aniwatch.ui.screens.status.composables.Setting
import com.elfennani.aniwatch.ui.screens.status.composables.StatusBottomSheet
import com.elfennani.aniwatch.ui.screens.status.composables.StatusEditorDatePicker
import com.elfennani.aniwatch.ui.screens.status.composables.StatusEditorScaffold
import com.elfennani.aniwatch.ui.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusEditorScreen(
    state: StatusEditorUiState,
    onUiEvent: (StatusEditorUiEvent) -> Unit,
    onBack: () -> Unit,
    onErrorDismiss: (Int) -> Unit,
) {
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(key1 = state.dateModal) {
        when (state.dateModal) {
            StatusDateModal.START ->
                datePickerState.selectedDateMillis = state.status?.startedAt
                    ?.toCalendar()
                    ?.toInstant()
                    ?.toEpochMilli()

            StatusDateModal.FINISH ->
                datePickerState.selectedDateMillis = state.status?.completedAt
                    ?.toCalendar()
                    ?.toInstant()
                    ?.toEpochMilli()

            else -> {}
        }
    }

    StatusEditorScaffold(
        errors = state.errors,
        onErrorDismiss = onErrorDismiss,
        onBack = onBack,
        onDelete = { /*TODO*/ }
    ) { paddingValues ->
        if (state.dateModal != null) {
            StatusEditorDatePicker(
                datePickerState = datePickerState,
                onConfirm = { onUiEvent(StatusEditorUiEvent.SetModalDate(state.dateModal, it)) },
                onDismiss = { onUiEvent(StatusEditorUiEvent.CloseModal) }
            )
        }

        when (state.bottomModal) {
            EditorSheetModal.SCORE -> ScoreBottomSheet(
                initialValue = state.status?.score ?: 0,
                onDismiss = { onUiEvent(StatusEditorUiEvent.CloseBottomSheet) },
                onConfirm = { onUiEvent(StatusEditorUiEvent.SetScore(it)) }
            )

            EditorSheetModal.PROGRESS -> ProgressBottomSheet(
                initialValue = state.status?.progress ?: 0,
                total = state.show?.episodesCount ?: 0,
                onDismiss = { onUiEvent(StatusEditorUiEvent.CloseBottomSheet) },
                onConfirm = { onUiEvent(StatusEditorUiEvent.SetProgress(it)) }
            )

            EditorSheetModal.STATUS -> StatusBottomSheet(
                selected = state.status?.status,
                onDismiss = { onUiEvent(StatusEditorUiEvent.CloseBottomSheet) },
                onConfirm = {
                    onUiEvent(StatusEditorUiEvent.SetStatus(it))
                }
            )

            else -> {}
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (state.isPending) {
                Column(
                    modifier = Modifier.padding(AppTheme.sizes.large),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.large)
                ) {
                    TextSkeleton(
                        style = AppTheme.typography.titleNormal,
                        modifier = Modifier.width(256.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.medium)
                    ) {
                        (0..3).forEach { _ ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(AppTheme.sizes.normal)
                            ) {
                                TextSkeleton(
                                    style = AppTheme.typography.labelSmall,
                                    modifier = Modifier.weight(1f)
                                )
                                TextSkeleton(
                                    style = AppTheme.typography.labelSmall,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            } else {
                val show = state.show!!
                val status = state.status!!

                Column(
                    modifier = Modifier.padding(AppTheme.sizes.large),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.sizes.large)
                ) {
                    Text(text = show.name, style = AppTheme.typography.titleNormal)
                    Column {
                        Setting(label = "Status", value = status.status?.formatText()) {
                            onUiEvent(StatusEditorUiEvent.OpenBottomSheet(EditorSheetModal.STATUS))
                        }
                        Setting(label = "Score", value = status.score.toString()) {
                            onUiEvent(StatusEditorUiEvent.OpenBottomSheet(EditorSheetModal.SCORE))
                        }
                        Setting(label = "Episode", value = status.progress.toString()) {
                            onUiEvent(StatusEditorUiEvent.OpenBottomSheet(EditorSheetModal.PROGRESS))
                        }
                        Setting(
                            label = "Start Date",
                            value = status.startedAt?.format(),
                            onClear = {
                                onUiEvent(
                                    StatusEditorUiEvent.SetModalDate(
                                        StatusDateModal.START,
                                        null
                                    )
                                )
                            }
                        ) {
                            onUiEvent(StatusEditorUiEvent.OpenDateModal(StatusDateModal.START))
                        }
                        Setting(
                            label = "Finish Date",
                            value = status.completedAt?.format(),
                            onClear = {
                                onUiEvent(
                                    StatusEditorUiEvent.SetModalDate(
                                        StatusDateModal.FINISH,
                                        null
                                    )
                                )
                            }
                        ) {
                            onUiEvent(StatusEditorUiEvent.OpenDateModal(StatusDateModal.FINISH))
                        }
                    }
                }
                Box(modifier = Modifier.padding(AppTheme.sizes.large)) {
                    Button(
                        onClick = { onUiEvent(StatusEditorUiEvent.Save(onBack)) },
                        enabled = !state.isEditingPending
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ShowEditorScreenPreview() {
    AppTheme {
        StatusEditorScreen(
            state = StatusEditorUiState(
                isPending = false,
                show = dummyShow,
                status = StatusDetails(
                    status = ShowStatus.WATCHING,
                    score = 100,
                    progress = 12,
                    favorite = false,
                    startedAt = StatusDate(year = 2024, month = 6, day = 29),
                    completedAt = StatusDate(year = 2024, month = 7, day = 1)
                )
            ),
            onBack = {},
            onErrorDismiss = {},
            onUiEvent = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ShowEditorScreenPreviewDark() {
    AppTheme {
        StatusEditorScreen(
            state = StatusEditorUiState(
                isPending = false,
                show = dummyShow,
                bottomModal = EditorSheetModal.STATUS,
                status = StatusDetails(
                    status = ShowStatus.WATCHING,
                    score = 100,
                    progress = 12,
                    favorite = false,
                    startedAt = StatusDate(year = 2024, month = 6, day = 29),
                    completedAt = StatusDate(year = 2024, month = 7, day = 1)
                )
            ),
            onBack = {},
            onErrorDismiss = {},
            onUiEvent = {}
        )
    }
}

const val STATUS_EDITOR_SCREEN_PATTERN = "status/editor/{id}"
fun NavGraphBuilder.statusEditorScreen(navController: NavController) {
    composable(
        route = STATUS_EDITOR_SCREEN_PATTERN,
        arguments = listOf(navArgument("id") { type = NavType.IntType }),
    ) {
        val viewModel: StatusEditorViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()

        StatusEditorScreen(
            state = state,
            onBack = { navController.popBackStack() },
            onErrorDismiss = viewModel::dismissError,
            onUiEvent = viewModel::onEvent
        )
    }
}

fun NavController.navigateToStatusEditorScreen(id: Int, popUpToTop: Boolean = false) {
    this.navigate(
        route = STATUS_EDITOR_SCREEN_PATTERN
            .replace("{id}", id.toString())
    ) {
        if (popUpToTop) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}