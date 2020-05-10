package com.sample.insta.utils.common

import com.sample.insta.R
import java.lang.reflect.Field
import java.util.regex.Pattern

object Validator {
    private const val MIN_PASSWORD_LENGTH = 6

    private val EMAIL_ADDRESS = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun validateLoginFields(
        email: String?,
        password: String?,
        name: String?,
        check: Boolean
    ): List<Validation> =
        ArrayList<Validation>().apply {
            when {
                email.isNullOrBlank() ->
                    add(
                        Validation(
                            Validation.Field.EMAIL,
                            Resource.error(R.string.email_field_empty)
                        )
                    )
                !EMAIL_ADDRESS.matcher(email).matches() -> add(
                    Validation(
                        Validation.Field.EMAIL,
                        Resource.error(R.string.email_field_invaild)
                    )
                )
                else -> add(Validation(Validation.Field.EMAIL, Resource.success()))
            }

            when {
                password.isNullOrBlank() ->
                    add(
                        Validation(
                            Validation.Field.PASSWORD,
                            Resource.error(R.string.password_field_empty)
                        )
                    )

                password.length < MIN_PASSWORD_LENGTH -> add(
                    Validation(
                        Validation.Field.PASSWORD,
                        Resource.error(R.string.password_field_small_length)
                    )
                )
                else -> add(Validation(Validation.Field.PASSWORD, Resource.success()))
            }
            when (check) {
                true -> {
                    if (name.isNullOrBlank()) {
                        add(
                            Validation(
                                Validation.Field.NAME,
                                Resource.error(R.string.name_field_empty)
                            )
                        )
                    } else
                        add((Validation(Validation.Field.NAME, Resource.success())))
                }
                false -> add((Validation(Validation.Field.NAME, Resource.success())))

            }
        }
}

data class Validation(val field: Field, val resource: Resource<Int>) {

    enum class Field {
        EMAIL,
        PASSWORD,
        NAME
    }
}