package org.sample.interface

import org.joml.Vector2d
import org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback
import org.lwjgl.glfw.GLFW.glfwSetInputMode
import org.lwjgl.glfw.GLFW.glfwSetCursorEnterCallback
import org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import org.lwjgl.glfw.GLFW.GLFW_CURSOR
import org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED
import org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL
import org.joml.Vector2f

object MouseInput {
    private val previousPos: Vector2d = new Vector2d(-1, -1)
    private val currentPos: Vector2d = new Vector2d(0, 0)
    private val _displacementVector: Vector2f = new Vector2f(0, 0)
    private var _inWindow: Boolean = false
    private var _leftPressed: Boolean = false
    private var _rightPressed: Boolean = false
    var locked: Boolean = false
    private var _leftClick: Boolean = false

    def init(): Unit = {
        for(
            window <- Window.window
        ) {
            glfwSetCursorPosCallback(window, (_, x, y) => {
                currentPos.x = x
                currentPos.y = y
            })
            glfwSetCursorEnterCallback(window, (_, entered) => {
                _inWindow = entered
            })
            glfwSetMouseButtonCallback(window, (_, button, action, _) => {
                _leftPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS
                _rightPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS
                _leftClick = _leftPressed
            })
        }
    }

    def displacementVector: Vector2f = _displacementVector
    def inWindow: Boolean = _inWindow
    def leftPressed: Boolean = _leftPressed
    def rightPressed: Boolean = _rightPressed
    
    def leftClick: Boolean = {
        val click = _leftClick
        _leftClick = false
        click
    }

    def input(): Unit = {
        _displacementVector.x = 0
        _displacementVector.y = 0
        if(inWindow) {
            val dx = currentPos.x - previousPos.x
            val dy = currentPos.y - previousPos.y
            if(dx != 0) {
                displacementVector.x = dx.toFloat
            }
            if(dy != 0) {
                displacementVector.y = dy.toFloat
            }
        }
        previousPos.x = currentPos.x
        previousPos.y = currentPos.y
    }

    def lockMouse(): Unit = {
        for(
            window <- Window.window
        ) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
            locked = true
            println("Mouse locked. Press ESC to unlock")
        }
    }

    def unlockMouse(): Unit = {
        for(
            window <- Window.window
        ) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
            locked = false
            println("Mouse unlocked")
        }
    }
}