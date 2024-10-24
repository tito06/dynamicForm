package com.example.dynamicform

/*sealed class FormField(val id: String, val label: String) {
    data class TextField(val hint: String, val value: String = "") : FormField(id = "text", label = "Text Field")
    data class DropdownField(val options: List<String>, val selectedOption: String = "") : FormField(id = "dropdown", label = "Dropdown Field")
    data class CheckboxField(val checked: Boolean = false) : FormField(id = "checkbox", label = "Checkbox Field")
}*/


sealed class FormField(open var label: String) {
    data class TextField(val hint: String = "", var value: String = "", override var label: String) : FormField(label)
    data class DropdownField(var options: List<String>, var selectedOption: String = "", override var label: String) : FormField(label)
    data class CheckboxField(var checked: Boolean = false, override var label: String) : FormField(label)
}
