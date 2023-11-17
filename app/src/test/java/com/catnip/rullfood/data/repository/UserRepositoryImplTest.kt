package com.catnip.rullfood.data.repository

import app.cash.turbine.test
import com.catnip.rullfood.data.network.firebase.auth.FirebaseAuthDataSource
import com.catnip.rullfood.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    @MockK
    private lateinit var firebaseAuthDataSource: FirebaseAuthDataSource

    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userRepository = UserRepositoryImpl(firebaseAuthDataSource)
    }

    @Test
    fun `doLogin loading`() {
        val email = "email"
        val password = "password"
        coEvery { firebaseAuthDataSource.doLogin(any(), any()) } returns true
        runTest {
            userRepository.doLogin(email, password)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Loading)
                    coVerify { firebaseAuthDataSource.doLogin(any(), any()) }
                }
        }
    }

    @Test
    fun `doLogin success`() {
        val email = "email"
        val password = "password"
        coEvery { firebaseAuthDataSource.doLogin(any(), any()) } returns true
        runTest {
            userRepository.doLogin(email, password)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Success)
                    coVerify { firebaseAuthDataSource.doLogin(any(), any()) }
                }
        }
    }

    @Test
    fun `doRegister loading`() {
        val fullName = "fullname"
        val email = "email"
        val password = "password"
        coEvery { firebaseAuthDataSource.doRegister(any(), any(), any()) } returns true
        runTest {
            userRepository.doRegister(fullName, email, password)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Loading)
                    coVerify { firebaseAuthDataSource.doRegister(any(), any(), any()) }
                }
        }
    }

    @Test
    fun `doRegister success`() {
        val fullName = "fullname"
        val email = "email"
        val password = "password"
        coEvery { firebaseAuthDataSource.doRegister(any(), any(), any()) } returns true
        runTest {
            userRepository.doRegister(fullName, email, password)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Success)
                    coVerify { firebaseAuthDataSource.doRegister(any(), any(), any()) }
                }
        }
    }

    @Test
    fun doLogout() {
        every { firebaseAuthDataSource.doLogout() } returns true
        val result = userRepository.doLogout()
        assertTrue(result)
        verify { firebaseAuthDataSource.doLogout() }
    }

    @Test
    fun isLoggedIn() {
        every { firebaseAuthDataSource.isLoggedIn() } returns true
        val result = userRepository.isLoggedIn()
        assertTrue(result)
        verify { firebaseAuthDataSource.isLoggedIn() }
    }
}
