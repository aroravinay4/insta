package com.sample.insta.utils.common

import com.sample.insta.R
import com.sample.insta.utils.common.Resource
import com.sample.insta.utils.common.Validation
import com.sample.insta.utils.common.Validator
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.Test

class ValidatorTest {

    @Test
    fun givenValidEmailAndValidPwd_whenValidate_shouldReturnSuccess() {
        val email = "test@gmail.com"
        val password = "password"

        val validations = Validator.validateLoginFields(email, password, "", false)
        assertThat(validations, hasSize(3))
        assertThat(
            validations,
            contains(
                Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(Validation.Field.PASSWORD, Resource.success()),
                Validation(Validation.Field.NAME, Resource.success())
            )
        )

    }

    @Test
    fun givenInvalidEmailAndValidPwd_whenValidate_shouldReturnEmailError() {
        val email = "test"
        val password = "password"

        val validations = Validator.validateLoginFields(email, password, "", false)
        assertThat(validations, hasSize(3))
        assertThat(
            validations,
            contains(
                Validation(
                    Validation.Field.EMAIL, Resource.error(R.string.email_field_invaild)
                ),
                Validation(Validation.Field.PASSWORD, Resource.success()),
                Validation(Validation.Field.NAME, Resource.success())
            )
        )

    }

    @Test
    fun givenEmptyEmailAndValidPwd_whenValidate_shouldReturnEmailError() {
        val email = ""
        val password = "password"

        val validations = Validator.validateLoginFields(email, password, "", false)
        assertThat(validations, hasSize(3))
        assertThat(
            validations,
            contains(
                Validation(
                    Validation.Field.EMAIL, Resource.error(R.string.email_field_empty)
                ),
                Validation(Validation.Field.PASSWORD, Resource.success()),
                Validation(Validation.Field.NAME, Resource.success())
            )
        )

    }

    @Test
    fun givenValidEmailAndInvalidPwd_whenValidate_shouldReturnPasswordError() {
        val email = "test@gmail.com"
        val password = "pass"

        val validations = Validator.validateLoginFields(email, password, "", false)
        assertThat(validations, hasSize(3))
        assertThat(
            validations,
            contains(
                Validation(
                    Validation.Field.EMAIL, Resource.success()
                ),
                Validation(
                    Validation.Field.PASSWORD,
                    Resource.error(R.string.password_field_small_length)
                ),
                Validation(Validation.Field.NAME, Resource.success())
            )
        )

    }

    @Test
    fun givenValidEmailAndEmptyPwd_whenValidate_shouldReturnPasswordError() {
        val email = "test@gmail.com"
        val password = ""

        val validations = Validator.validateLoginFields(email, password, "", false)
        assertThat(validations, hasSize(3))
        assertThat(
            validations,
            contains(
                Validation(
                    Validation.Field.EMAIL, Resource.success()
                ),
                Validation(
                    Validation.Field.PASSWORD,
                    Resource.error(R.string.password_field_empty)
                ),
                Validation(Validation.Field.NAME, Resource.success())
            )
        )

    }

    @Test
    fun givenValidEmailAndValidPwdAndValidName_whenValidate_shouldReturnSuccess() {
        val email = "test@gmail.com"
        val password = "password"
        val name = "name"

        val validations = Validator.validateLoginFields(email, password, name, true)
        assertThat(validations, hasSize(3))
        assertThat(
            validations,
            contains(
                Validation(
                    Validation.Field.EMAIL, Resource.success()
                ),
                Validation(
                    Validation.Field.PASSWORD, Resource.success()
                ),
                Validation(Validation.Field.NAME, Resource.success())
            )
        )

    }

    @Test
    fun givenValidEmailAndValidPwdAndEmptyName_whenValidate_shouldReturnNameError() {
        val email = "test@gmail.com"
        val password = "password"
        val name = ""

        val validations = Validator.validateLoginFields(email, password, name, true)
        assertThat(validations, hasSize(3))
        assertThat(
            validations,
            contains(
                Validation(
                    Validation.Field.EMAIL, Resource.success()
                ),
                Validation(
                    Validation.Field.PASSWORD, Resource.success()
                ),
                Validation(Validation.Field.NAME, Resource.error(R.string.name_field_empty))
            )
        )

    }
}