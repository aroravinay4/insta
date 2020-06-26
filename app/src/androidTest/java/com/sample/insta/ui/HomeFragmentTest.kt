package com.sample.insta.ui

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.sample.insta.R
import com.sample.insta.TestComponentRule
import com.sample.insta.data.model.User
import com.sample.insta.ui.home.HomeFragment
import com.sample.insta.ui.utils.RVMatcher.atPositionOnView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class HomeFragmentTest {

    private val component =
        TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)


    @get:Rule
    public val chain = RuleChain.outerRule(component)


    @Before
    fun setUp() {
        val userRepository = component.testComponent!!.getUserRepository()
        val user = User("id", "name", "email", "accessToken", "profilePicUrl")
        userRepository.saveUserData(user)


    }


    @Test
    fun postAvailable_shouldDisplay() {
        launchFragmentInContainer<HomeFragment>(Bundle(), R.style.AppTheme)
        onView(withId(R.id.rvPost)).check(matches(isDisplayed()))
        onView(withId(R.id.rvPost)).check(
            matches(
                atPositionOnView(
                    0,
                    withText("name1"),
                    R.id.tvName
                )
            )
        )


/*        onView(withId(R.id.rvPost))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(1))
            .check(matches(atPositionOnView(1, withText("name2"), R.id.tvName)))*/

    }
}