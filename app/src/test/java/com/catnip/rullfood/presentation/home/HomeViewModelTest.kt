package com.catnip.rullfood.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catnip.rullfood.data.repository.MenuRepository
import com.catnip.rullfood.data.repository.UserRepository
import com.catnip.rullfood.model.User
import com.catnip.rullfood.tools.MainCoroutineRule
import com.catnip.rullfood.tools.getOrAwaitValue
import com.catnip.rullfood.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class HomeViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var menuRepo: MenuRepository

    @MockK
    lateinit var userRepo: UserRepository

    @MockK
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { menuRepo.getMenus(any()) } returns flow {
            emit(
                ResultWrapper.Success(
                    listOf(
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true)
                    )
                )
            )
        }

        coEvery { menuRepo.getCategory() } returns flow {
            emit(
                ResultWrapper.Success(
                    listOf(
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true)
                    )
                )
            )
        }

        coEvery { userRepo.getCurrentUser() } returns User("fullName", "url", "email")

        viewModel = spyk(
            HomeViewModel(
                menuRepository = menuRepo,
                userRepository = userRepo
            )
        )
    }

    @Test
    fun getMenus() {
        viewModel.getMenus()
        val result = viewModel.menu.getOrAwaitValue()
        assertEquals(result.payload?.size, 3)
        coVerify { menuRepo.getMenus() }
    }

//    @Test
//    fun getCategories() {
//        viewModel.getCategory()
//        val result = viewModel.category.getOrAwaitValue()
//        assertEquals(result.payload?.size, 4)
//        coVerify { menuRepo.getCategory() }
//    }
//
//    @Test
//    fun `get current user`() {
//        val result = viewModel.getCurrentUser()
//        assertTrue(result is User)
//        assertEquals(result?.email, "email")
//        coVerify { userRepo.getCurrentUser() }
//    }
}
