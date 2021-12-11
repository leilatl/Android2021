package com.example.androidproject

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androidproject.firestore.FirestoreClass
import com.example.androidproject.ui.dashboard.DashboardFragment

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val id = FirestoreClass().getUserID()
        val testid = "NalMRadSrnMtYJABQVQX5dcFxMy2"
        assertEquals(testid, id)
    }
}