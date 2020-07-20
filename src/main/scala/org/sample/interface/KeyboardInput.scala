package org.sample.interface

import org.lwjgl.glfw.GLFW.glfwSetKeyCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose

import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import org.lwjgl.glfw.GLFW.GLFW_REPEAT
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_KEY_R
import org.lwjgl.glfw.GLFW.GLFW_KEY_G
import org.lwjgl.glfw.GLFW.GLFW_KEY_B
import org.lwjgl.glfw.GLFW.GLFW_KEY_UP
import org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN
import org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT
import org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT
import org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE
import org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ADD
import org.lwjgl.glfw.GLFW.GLFW_KEY_KP_SUBTRACT
import org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE
import org.lwjgl.glfw.GLFW.GLFW_KEY_Q
import org.lwjgl.glfw.GLFW.GLFW_KEY_E
import org.lwjgl.glfw.GLFW.GLFW_KEY_W
import org.lwjgl.glfw.GLFW.GLFW_KEY_S
import org.lwjgl.glfw.GLFW.GLFW_KEY_A
import org.lwjgl.glfw.GLFW.GLFW_KEY_D
import org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL
import org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_CONTROL

object KeyboardInput {

    private var keyCallback: Option[GLFWKeyCallback] = None

    def init(): Unit = {
        for(
            window <- Window.window
        ) {
            glfwSetKeyCallback(window, {
                val callback = new GLFWKeyCallback() {
                    override def invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int): Unit = {
                        if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                            glfwSetWindowShouldClose(window, true)
                        } else {
                            keyInput(key, action, mods)
                        }
                    }
                }
                keyCallback = Some(callback)
                callback
            })
        }
    }

    def cleanup(): Unit = {
        keyCallback.foreach { callback =>
            callback.close()
            keyCallback = None
        }
    }

    var KEY_UP: Boolean = false
    var KEY_DOWN: Boolean = false
    var KEY_RIGHT: Boolean = false
    var KEY_LEFT: Boolean = false
    var KEY_PLUS: Boolean = false
    var KEY_MINUS: Boolean = false
    var KEY_Q: Boolean = false
    var KEY_W: Boolean = false
    var KEY_E: Boolean = false
    var KEY_A: Boolean = false
    var KEY_S: Boolean = false
    var KEY_D: Boolean = false
    var KEY_R: Boolean = false
    var KEY_G: Boolean = false
    var KEY_B: Boolean = false
    var KEY_CTRL: Boolean = false
    var KEY_SPACE: Boolean = false

    private def keyInput(key: Int, action: Int, mods: Int): Unit =  key match {
        case GLFW_KEY_UP => KEY_UP = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_DOWN => KEY_DOWN = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_RIGHT => KEY_RIGHT = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_LEFT =>  KEY_LEFT = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_KP_ADD => KEY_PLUS = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_KP_SUBTRACT => KEY_MINUS = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_Q => KEY_Q = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_E => KEY_E = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_W => KEY_W = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_S => KEY_S = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_A => KEY_A = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_D => KEY_D = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_R => KEY_R = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_G => KEY_G = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_B => KEY_B = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_SPACE => KEY_SPACE = action == GLFW_PRESS || action == GLFW_REPEAT
        case GLFW_KEY_LEFT_CONTROL | GLFW_KEY_RIGHT_CONTROL => KEY_CTRL = action == GLFW_PRESS || action == GLFW_REPEAT
        case _ =>
            println(s"Unknown key: [$key] action: [$action] mods: [$mods]")
    }    
}