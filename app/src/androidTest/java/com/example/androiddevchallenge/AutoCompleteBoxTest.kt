/*
 * Copyright 2021 Paulo Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.domain.models.Person
import com.example.androiddevchallenge.presentation.MainActivity
import com.example.androiddevchallenge.presentation.components.autocomplete.AutoCompleteBoxTag
import com.example.androiddevchallenge.presentation.components.autocomplete.utils.AutoCompleteSearchBarTag
import com.example.androiddevchallenge.presentation.sample.AutoCompleteObjectSample
import com.example.androiddevchallenge.presentation.theme.MyTheme
import org.junit.Rule
import org.junit.Test

class AutoCompleteBoxTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @OptIn(ExperimentalAnimationApi::class)
    private fun launchAutoCompleteWith(persons: List<Person>) {
        composeTestRule.setContent {
            MyTheme {
                AutoCompleteObjectSample(persons = persons)
            }
        }
    }

    @Test
    fun auto_complete_box_not_visible_without_items() {
        // Given
        val persons = listOf<Person>()

        // When
        launchAutoCompleteWith(persons)

        // Then
        composeTestRule.onNodeWithTag(AutoCompleteSearchBarTag).performClick()
        composeTestRule.onNodeWithTag(AutoCompleteBoxTag).assertHeightIsEqualTo(0.dp)
    }

    @Test
    fun auto_complete_box_visible_with_items() {
        // Given
        val persons = listOf(
            Person(
                name = "Paulo Pereira",
                age = 23
            )
        )

        // When
        launchAutoCompleteWith(persons)

        // Then
        composeTestRule.onNodeWithTag(AutoCompleteSearchBarTag).performClick()
        composeTestRule.onNodeWithTag(AutoCompleteBoxTag).assertHeightIsAtLeast(1.dp)
    }

    @Test
    fun auto_complete_box_visible_with_one_item() {
        // Given
        val persons = listOf(
            Person(
                name = "Paulo Pereira",
                age = 23
            )
        )
        val expectedChildrenCount = 1

        // When
        launchAutoCompleteWith(persons)

        // Then
        composeTestRule.onNodeWithTag(AutoCompleteSearchBarTag).performClick()
        composeTestRule.onNodeWithTag(AutoCompleteBoxTag).onChildren().assertCountEquals(expectedChildrenCount)
    }

    @Test
    fun auto_complete_box_filter_has_one_result() {
        // Given
        val persons = listOf(
            Person(
                name = "Paulo Pereira",
                age = 23
            ),
            Person(
                name = "Daenerys Targaryen",
                age = 24
            ),
            Person(
                name = "Jon Snow",
                age = 24
            ),
            Person(
                name = "Sansa Stark",
                age = 20
            ),
        )
        val expectedChildrenCount = 1
        val textInput = "Paul"

        // When
        launchAutoCompleteWith(persons)

        // Then
        composeTestRule.onNodeWithTag(AutoCompleteSearchBarTag).performClick()
        composeTestRule.onNodeWithTag(AutoCompleteBoxTag).assertHeightIsAtLeast(1.dp)
        composeTestRule.onNodeWithTag(AutoCompleteSearchBarTag).performTextInput(textInput)
        composeTestRule.onNodeWithTag(AutoCompleteBoxTag).onChildren().assertCountEquals(expectedChildrenCount)
    }

    @Test
    fun auto_complete_box_filter_empty_results() {
        // Given
        val persons = listOf(
            Person(
                name = "Paulo Pereira",
                age = 23
            )
        )
        val expectedChildrenCount = 0
        val textInput = "random query"

        // When
        launchAutoCompleteWith(persons)

        // Then
        composeTestRule.onNodeWithTag(AutoCompleteSearchBarTag).performClick()
        composeTestRule.onNodeWithTag(AutoCompleteBoxTag).assertHeightIsAtLeast(1.dp)
        composeTestRule.onNodeWithTag(AutoCompleteSearchBarTag).performTextInput(textInput)
        composeTestRule.onNodeWithTag(AutoCompleteBoxTag).onChildren().assertCountEquals(expectedChildrenCount)
    }
}
