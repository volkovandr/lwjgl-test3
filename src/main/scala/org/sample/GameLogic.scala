package org.sample

import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import org.lwjgl.glfw.GLFW.GLFW_REPEAT
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_KEY_R
import org.lwjgl.glfw.GLFW.GLFW_KEY_G
import org.lwjgl.glfw.GLFW.GLFW_KEY_B

object GameLogic {

    private val step= 0.01f

    var color = (0.5f, 0.5f, 0.5f)
    private var delta = (0.0f, 0.0f, 0.0f)

    def init(): Unit = {
        println("Welcome to the Game!")
        println("Press R to increase the Red color, and Ctrl+R to decrease it")
        println("Press G to increase the Green color, and Ctrl+G to decrease it")
        println("Press B to increase the Blue color, and Ctrl+B to decrease it")
    }

    def input(key: Int, action: Int, mods: Int): Unit = {
        delta = if((action == GLFW_PRESS || action == GLFW_REPEAT)) {
            key match {
                case GLFW_KEY_R if (mods & Settings.CTRL_BIT) == 0 => (step, 0.0f, 0.0f)
                case GLFW_KEY_R =>                                    (-step, 0.0f, 0.0f)
                case GLFW_KEY_G if (mods & Settings.CTRL_BIT) == 0 => (0.0f, step, 0.0f)
                case GLFW_KEY_G =>                                    (0.01f, -step, 0.0f)
                case GLFW_KEY_B if (mods & Settings.CTRL_BIT) == 0 => (0.0f, 0.0f, step)
                case GLFW_KEY_B =>                                    (0.0f, 0.0f, -step)
                case _ => (0.0f, 0.0f, 0.0f)
            }
        } else (0.0f, 0.0f, 0.0f)
    }

    def update(): Unit = {
        color = (
            withinBounds(color._1, delta._1),
            withinBounds(color._2, delta._2),
            withinBounds(color._3, delta._3)
        )
    }

    private def withinBounds(input: Float, delta: Float): Float = (input + delta) match {
        case i if i < 0.0f => 0.01f
        case i if i > 1.0f => 1.0f
        case i => i
    }
}