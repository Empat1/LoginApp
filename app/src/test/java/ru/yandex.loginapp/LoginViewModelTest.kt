package ru.yandex.loginapp

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private var viewModel: LoginViewModel? = null
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel()
    }

    @After
    fun tearDown() {
        viewModel = null
        Dispatchers.resetMain()
    }

    @Test
    fun input_empty_login() = runTest {
        viewModel?.login("", "")

        assertEquals(LoginScreenState.EmptyFieldsError, viewModel?.state?.value)
    }

    @Test
    fun input_invalidation_login() = runTest {
        viewModel?.login("qwerty", "qwerty")

        assertEquals(LoginScreenState.EmailValidationError, viewModel?.state?.value)
    }

    @Test
    fun input_login() = runTest {
        viewModel?.login(LOGIN, PASSWORD)
        testDispatcher.scheduler.runCurrent()
        assertEquals(LoginScreenState.Loading, viewModel?.state?.value)
    }

    @Test
    fun input_login_and_wait_success() = runTest {
        viewModel?.login(LOGIN, PASSWORD)
        testDispatcher.scheduler.runCurrent()
        testDispatcher.scheduler.advanceTimeBy(3500)
        assertEquals(LoginScreenState.Success, viewModel?.state?.value)
    }

    companion object {
        private const val LOGIN = "login@yandex.ru"
        private const val PASSWORD = "password"
    }
}