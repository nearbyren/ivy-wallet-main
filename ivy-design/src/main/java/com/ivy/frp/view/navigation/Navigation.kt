package com.ivy.frp.view.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.*

class Navigation {
    var currentScreen: Screen? by mutableStateOf(null)
        private set

    private val backStack: Stack<Screen> = Stack()
    private var lastScreen: Screen? = null

    var modalBackHandling: Stack<ModalBackHandler> = Stack()

    data class ModalBackHandler(
        val id: UUID,
        val onBackPressed: () -> Boolean
    )

    fun lastModalBackHandlerId(): UUID? {
        println("我来了 Navigation lastModalBackHandlerId ${modalBackHandling.isEmpty()}")
        return if (modalBackHandling.isEmpty()) {
            null
        } else {
            //获取栈顶
            modalBackHandling.peek().id
        }
    }

    var onBackPressed: MutableMap<Screen, () -> Boolean> = mutableMapOf()

    fun navigateTo(screen: Screen, allowBackStackStore: Boolean = true) {
        println("我来了 Navigation navigateTo screen = $screen allowBackStackStore = $allowBackStackStore")
        if (lastScreen != null && allowBackStackStore) {
            backStack.push(lastScreen)
        }
        switchScreen(screen)
    }

    fun resetBackStack() {
        println("我来了 Navigation resetBackStack ")
        while (!backStackEmpty()) {
            popBackStack()
        }
        lastScreen = null
    }

    fun backStackEmpty() = backStack.empty()

    fun popBackStackSafe() {
        println("我来了 Navigation popBackStackSafe")
        if (!backStackEmpty()) {
            popBackStack()
        }
    }

    private fun popBackStack() {
        println("我来了 Navigation popBackStack ${backStack.peek()}")
        //弹出栈顶
        backStack.pop()
    }

    fun onBackPressed(): Boolean {
        println("我来了 Navigation onBackPressed size = ${modalBackHandling.size} - currentScreen = $currentScreen")
        if (modalBackHandling.isNotEmpty()) {
            println("我来了 Navigation onBackPressed isNotEmpty()")
            //获取栈顶
            return modalBackHandling.peek().onBackPressed()
        }
        val specialHandling = onBackPressed.getOrDefault(currentScreen) { false }.invoke()
        println("我来了 Navigation onBackPressed specialHandling = $specialHandling")
        return specialHandling || back()
    }

    fun back(): Boolean {
        //获取栈顶
        println("我来了 Navigation back pop size ${backStack}")
        if (!backStack.empty()) {
            //弹出栈顶
            switchScreen(backStack.pop())
            return true
        }
        println("我来了 Navigation back $backStack")
        return false
    }

    fun lastBackstackScreen(): Screen? {
        println("我来了 Navigation lastBackstackScreen")
        return if (!backStackEmpty()) {
            ///获取栈顶
            backStack.peek()
        } else {
            null
        }
    }

    private fun switchScreen(screen: Screen) {
        println("我来了 Navigation screen $screen")
        this.currentScreen = screen
        lastScreen = screen
    }

    fun reset() {
        println("我来了 Navigation reset")
        currentScreen = null
        resetBackStack()
    }
}