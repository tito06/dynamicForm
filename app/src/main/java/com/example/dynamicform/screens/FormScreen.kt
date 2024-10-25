package com.example.dynamicform.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.dynamicform.model.Form
import com.example.dynamicform.model.FormDataStorage
import com.example.dynamicform.model.FormField
import com.example.dynamicform.viewModel.FormVm

@SuppressLint("RememberReturnType")
@Composable
fun FormScreen(navController: NavHostController, formViewModel: FormVm= viewModel()) {

    val formFields = formViewModel.formFields

    Column(modifier = Modifier.padding(16.dp,50.dp,16.dp,16.dp)) {
        Text("Admin: Configure Your Form", style = MaterialTheme.typography.headlineSmall)

        // Add text field button
        Button(onClick = {
            formViewModel.addTextField()
        }) {
            Text("Add Text Field")
        }

        // Add dropdown button
        Button(onClick = {
            formViewModel.addDropDown()
        }) {
            Text("Add Dropdown Field")
        }

        // Add checkbox button
        Button(onClick = {
            formViewModel.addCheckBox()
        }) {
            Text("Add Checkbox Field")
        }

        // Render the form fields as the admin adds them
        formFields.forEachIndexed { index, field ->
            when (field) {
                is FormField.TextField -> {
                    var textValue by remember { mutableStateOf(field.label) }
                    OutlinedTextField(
                        value = textValue,
                        onValueChange = {
                            textValue = it
                            field.label = it // Allow admin to change label
                        },
                        label = { Text("Text Field Label") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is FormField.DropdownField -> {
                    var options by remember { mutableStateOf(field.options.joinToString()) }
                    OutlinedTextField(
                        value = options,
                        onValueChange = {
                            options = it
                            field.options = it.split(",").map { option -> option.trim() }
                        },
                        label = { Text("Dropdown Options (comma-separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is FormField.CheckboxField -> {
                    var checkboxLabel by remember { mutableStateOf(field.label) }
                    OutlinedTextField(
                        value = checkboxLabel,
                        onValueChange = {
                            checkboxLabel = it
                            field.label = it // Allow admin to change label
                        },
                        label = { Text("Checkbox Label") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }




            }

            IconButton(
                onClick = {
                    formViewModel.removeField(index)
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {

                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Field")

            }
        }



        Spacer(modifier = Modifier.padding(16.dp))

        // Save form and navigate to user form
        Button(onClick = {
            formViewModel.saveForm()
            navController.navigate("user")
        }) {
            Text("Save and Navigate to User Form")
        }
    }
}








@Composable
fun UserFormScreen() {
    val form = FormDataStorage.dynamicForm ?: return

    val fieldValues = remember { mutableStateMapOf<String, Any>() }

    var expanded by remember { mutableStateOf(false) }


    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = form.title, style = MaterialTheme.typography.headlineMedium)

        // Dynamically render fields
        form.fields.forEach { field ->
            when (field) {
                is FormField.TextField -> {
                    var value by remember { mutableStateOf(field.value) }
                    OutlinedTextField(
                        value = value,
                        onValueChange = {
                            value = it
                            fieldValues[field.label] = it
                        },
                        label = { Text(field.label) },
                        placeholder = { Text(field.hint) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                }

                is FormField.DropdownField -> {
                    var selectedOption by remember { mutableStateOf(field.selectedOption) }
                    Box {
                        Button(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(selectedOption)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {expanded = false},
                        ) {
                            field.options.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = Color.White) },
                                    onClick = {
                                        selectedOption = option
                                        fieldValues[field.label] = option
                                    }
                                )
                            }
                        }
                    }
                }

                is FormField.CheckboxField -> {
                    var checked by remember { mutableStateOf(field.checked) }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = checked, onCheckedChange = {
                            checked = it
                            fieldValues[field.label] = it
                        })
                        Text(field.label)
                    }
                }
            }
        }

        Button(onClick = { println("Submitted data: $fieldValues") }) {
            Text("Submit")
        }
    }
}