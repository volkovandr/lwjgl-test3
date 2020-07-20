package org.sample.interface

import org.joml.Vector2d
import org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback

object MouseInput {
    private val previousPos: Vector2d = new Vector2d(-1, -1)
    private val currentPos: Vector2d = new Vector2d(0, 0)
    private val displacementVector: Vector2d = new Vector2d(0, 0)
    private var inWindow: Boolean = false
    private var leftPressed: Boolean = false
    private var rightPressed: Boolean = false

    def init(): Unit = {
        for(
            window <- Window.window
        ) {
            glfwSetCursorPosCallback(window)
        }
    }
}