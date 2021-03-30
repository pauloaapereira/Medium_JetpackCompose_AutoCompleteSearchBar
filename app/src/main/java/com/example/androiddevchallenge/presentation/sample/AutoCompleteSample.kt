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
package com.example.androiddevchallenge.presentation.sample

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.domain.models.Person
import com.example.androiddevchallenge.presentation.components.TextSearchBar
import com.example.androiddevchallenge.presentation.components.autocomplete.AutoCompleteBox

const val AutoCompleteSearchBarTag = "AutoCompleteSearchBar"

@ExperimentalAnimationApi
@Composable
fun AutoCompleteSample(persons: List<Person>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        AutoCompleteBox(
            items = persons,
            itemContent = { person ->
                PersonAutoCompleteItem(person)
            }
        ) {
            var value by remember { mutableStateOf("") }
            val view = LocalView.current

            onItemSelected { person ->
                value = person.name
                view.clearFocus()
            }

            TextSearchBar(
                modifier = Modifier.semantics { testTag = AutoCompleteSearchBarTag },
                value = value,
                label = "Search",
                onDoneActionClick = {
                    view.clearFocus()
                },
                onFocusChanged = { focusState ->
                    isSearching = focusState == FocusState.Active
                },
                onValueChange = { query ->
                    if (value.isBlank() && query.isBlank())
                        view.clearFocus()

                    value = query
                    filter(value)
                }
            )
        }
    }
}

@Composable
fun PersonAutoCompleteItem(person: Person) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = person.name, style = MaterialTheme.typography.subtitle2)
        Text(text = person.age.toString(), style = MaterialTheme.typography.subtitle2)
    }
}
