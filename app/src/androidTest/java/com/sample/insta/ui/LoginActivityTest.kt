package com.sample.insta.ui

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.sample.insta.TestComponentRule
import com.sample.insta.di.component.TestComponent
import com.sample.insta.ui.login.LoginActivity
import dagger.Component
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class LoginActivityTest {

    private val component =
        TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    //    val main = IntentsTestRule(LoginActivity::class.java, false, false)

    @get:Rule
    //  val chain = RuleChain.outerRule(component).around(main)
    val chain = RuleChain.outerRule(component)

    @Before
    fun setUp() {

    }

    @Test
    fun testCheckViewsDisplay() {
        // main.launchActivity(Intent(component.getContext(), LoginActivity::class.java))
        launch(LoginActivity::class.java)
    }

    @Test
    fun givenValidEmailAndPwd_whenLogin_shouldOpenMainActivity() {

    }
}