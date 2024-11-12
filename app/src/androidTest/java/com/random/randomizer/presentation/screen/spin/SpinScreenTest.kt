package com.random.randomizer.presentation.screen.spin

import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SpinScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var mappers: SpinMappers

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun spinScreen_displaysWheelSegmentList_whenSegmentsPresent() {
        val getWheelSegmentStreamUseCase = mockk<GetWheelSegmentsStreamUseCase>()

        every {
            getWheelSegmentStreamUseCase()
        } returns flow {
            emit(
                listOf(
                    WheelSegment(1, "fake", "", null, null),
                    WheelSegment(2, "fake2", "", null, null)
                ),
            )
        }

        val viewModel = SpinViewModel(getWheelSegmentStreamUseCase, mappers)

        composeTestRule.setContent {
            SpinScreen(viewModel = viewModel)
        }

        composeTestRule
            .onAllNodesWithText("fake")
            .assertAny(hasText("fake"))
    }
}