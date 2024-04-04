package com.example.taxiiii

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.location.Location

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_activity) // Используйте правильное имя layout

        val phoneEditText: EditText = findViewById(R.id.editTextPhone)
        val nameEditText: EditText = findViewById(R.id.editTextText)
        val lastNameEditText: EditText = findViewById(R.id.editTextText2)
        val registerButton: Button = findViewById(R.id.button)

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        if (sharedPreferences.contains("NAME")) {
            val phone = sharedPreferences.getString("PHONE", "")
            val name = sharedPreferences.getString("NAME", "")
            val lastName = sharedPreferences.getString("LAST_NAME", "")

            phoneEditText.setText(phone)
            nameEditText.setText(name)
            lastNameEditText.setText(lastName)
            val registerButton: Button = findViewById(R.id.button)
            registerButton.text = "Вход"
        }

        registerButton.setOnClickListener {
            val phone = phoneEditText.text.toString()
            val name = nameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()

            if (phone.isNotBlank() && name.isNotBlank() && lastName.isNotBlank()) {
                saveUserData(phone, name, lastName) // Сохраняем данные пользователя
                Toast.makeText(this, "Регистрация прошла успешно: $name $lastName, телефон: $phone", Toast.LENGTH_LONG).show()

                val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
                intent.putExtra("name", name + " " + lastName)
                intent.putExtra("number", phone)
                startActivity(intent)
                finish() // Закройте эту Activity, чтобы пользователь не мог вернуться назад
            } else {
                Toast.makeText(this, "Пожалуйста, заполните все поля корректно.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(phone: String, name: String, lastName: String) {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("PHONE", phone)
        editor.putString("NAME", name)
        editor.putString("LAST_NAME", lastName)
        editor.apply() // Или editor.commit() для синхронного сохранения
    }
}
