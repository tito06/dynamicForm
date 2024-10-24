package com.example.dynamicform.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.dynamicform.model.Form
import com.example.dynamicform.model.FormDataStorage
import com.example.dynamicform.model.FormField

class FormVm : ViewModel() {

    val formFields = mutableStateListOf<FormField>()
    var formTitle = mutableStateOf("User Form")


    fun addTextField(){
        formFields.add(FormField.TextField("Enter Name", label = "Text Field"))
    }


    fun addDropDown(){
        formFields.add(FormField.DropdownField(options = listOf("option1, option2"), label = "New DropDown"))
    }


    fun addCheckBox(){
        formFields.add(FormField.CheckboxField(label = "New Check Box"))
    }

    fun removeField(index:Int){
        formFields.removeAt(index)
    }


    fun saveForm(){
        FormDataStorage.dynamicForm = Form(title = formTitle.value, fields = formFields)
    }

}