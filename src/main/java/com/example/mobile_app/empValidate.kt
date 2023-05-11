package com.example.mf1

class empValidate(
    val id: String,
    val type: String,
    val name: String,
    val email: String,
    val phone: String,
    val reason: String
) {

    // Function to check if the name is not empty
    fun isNameValid(): Boolean {
        return name.isNotEmpty()
    }

    // Function to check if the email is not empty
    fun isEmailValid(): Boolean {
        return email.isNotEmpty()
    }

    // Function to check if the phone number is not empty
    fun isPhoneValid(): Boolean {
        return phone.isNotEmpty()
    }

    // Function to check if the reason is not empty
    fun isReasonValid(): Boolean {
        return reason.isNotEmpty()
    }

    // Function to check if the type is not empty
    fun isTypeValid(): Boolean {
        return type.isNotEmpty()
    }

    // Function to check if all input fields are valid
    fun isInputValid(): Boolean {
        return isTypeValid() && isNameValid() && isEmailValid() && isPhoneValid() && isReasonValid()
    }

    // Function to check if the ID is unique
    fun isIdUnique(empValidateList: List<empValidate>): Boolean {
        for (v in empValidateList) {
            if (v.id == id && v != this) {
                return false
            }
        }
        return true
    }

    // Function to check if the input fields and ID are valid
    fun isValid(empValidateList: List<empValidate>): Boolean {
        return isInputValid() && isIdUnique(empValidateList)
    }
}
