package com.catnip.rullfood.presentation.splashscreen

import com.catnip.rullfood.data.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SplashViewModelTest {

    @MockK
    private lateinit var repo: UserRepository

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = SplashViewModel(repo)
    }

    @Test
    fun `user logged in`() {
        every { repo.isLoggedIn() } returns true
        val result = viewModel.isUserLoggedIn()
        verify { repo.isLoggedIn() }
        assertTrue(result)
    }
}
