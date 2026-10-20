package com.example.uikit.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.uikit.R
import com.example.uikit.theme.MatuleError
import com.example.uikit.theme.MatulePrimary
import com.example.uikit.theme.MatuleSurfaceMuted
import com.example.uikit.theme.MatuleTextSecondary

/**
 * Поле ввода Matule с подписью сверху и сообщением об ошибке снизу.
 * Для пароля справа показывается иконка глаза: по нажатию пароль показывается или скрывается.
 *
 * @param value текущий текст в поле
 * @param onValueChange вызывается при каждом изменении текста
 * @param label подпись над полем (например «Вход по E-mail»)
 * @param placeholder серый текст-подсказка, пока поле пустое
 * @param modifier модификатор для внешнего управления размером/отступами
 * @param errorText текст ошибки; если не null — поле подсвечивается красным и ошибка показывается под ним
 * @param isPassword если true — символы скрыты точками и появляется иконка глаза
 * @param keyboardType тип клавиатуры (обычная, email, цифры и т.д.)
 */
@Composable
fun MatuleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MatuleTextSecondary,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = MatuleTextSecondary) },
            singleLine = true,
            isError = errorText != null,
            shape = RoundedCornerShape(10.dp),
            visualTransformation = if (isPassword && !isPasswordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = if (isPassword) {
                {
                    Icon(
                        painter = painterResource(
                            if (isPasswordVisible) R.drawable.ic_eye_visible else R.drawable.ic_eye_hidden
                        ),
                        contentDescription = null,
                        tint = MatuleTextSecondary,
                        modifier = Modifier.clickable { isPasswordVisible = !isPasswordVisible }
                    )
                }
            } else {
                null
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MatuleSurfaceMuted,
                unfocusedContainerColor = MatuleSurfaceMuted,
                focusedBorderColor = MatulePrimary,
                unfocusedBorderColor = MatuleSurfaceMuted,
                errorBorderColor = MatuleError,
                errorContainerColor = MatuleSurfaceMuted
            )
        )
        if (errorText != null) {
            Text(
                text = errorText,
                color = MatuleError,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}