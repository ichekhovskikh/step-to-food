package com.sugar.steptofood

import com.sugar.steptofood.rest.ApiService
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.mockito.Mock

class UserPresenterTest {

    @Mock
    lateinit var api: ApiService

    @After
    fun init() {

    }

    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, 2 + 2)
    }
}