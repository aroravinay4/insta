package com.sample.insta

import android.content.Context
import com.sample.insta.application.InstaApplication
import com.sample.insta.di.component.TestComponent
import com.sample.insta.di.module.ApplicationTestModule
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestComponentRule(private val context: Context) : TestRule {

    private var testComponent: TestComponent? = null
    fun getContext() = context

    private fun setUpDaggerTestComponentInApplication() {
        val application = context.applicationContext as InstaApplication

   /*     testComponent = DaggerTestComponent.builder()
            .applicationTestModule(ApplicationTestModule(application))
            .build()
        application.setComponent(testComponent!!)*/
    }

    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                try {
                    setUpDaggerTestComponentInApplication()
                    base.evaluate()
                } finally {
                    testComponent = null
                }
            }

        }
    }
}