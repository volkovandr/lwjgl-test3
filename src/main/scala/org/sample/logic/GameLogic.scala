package org.sample.logic

import org.sample.helpers.Settings

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

object GameLogic {

    private val step= 0.01f
    private val house = new House
    val gameObjects: List[GameObject] = List(house)
    var color = (0.0f, 0.0f, 0.0f)
    private var delta = (0.0f, 0.0f, 0.0f)

    def init(): Unit = {
        house.moveTo(0, 0, -1.5f)
        println("Welcome to the Game!")
        println("Press R to increase the Red color, and Ctrl+R to decrease it")
        println("Press G to increase the Green color, and Ctrl+G to decrease it")
        println("Press B to increase the Blue color, and Ctrl+B to decrease it")
        println("Use cursor keys to move the house, numpad's + and - to move it farther or closer,")
        println("Q and E, W and S, A and D to rotate it and SPACE to bring everything back.")
    }

    def input(key: Int, action: Int, mods: Int): Unit =  key match {
        case GLFW_KEY_UP =>   house.moveBy(0, 0.01f, 0)
        case GLFW_KEY_DOWN => house.moveBy(0, -0.01f, 0)
        case GLFW_KEY_RIGHT => house.moveBy(0.01f, 0, 0)
        case GLFW_KEY_LEFT =>  house.moveBy(-0.01f, 0, 0)
        case GLFW_KEY_KP_ADD =>      house.moveBy(0.0f, 0, 0.01f)
        case GLFW_KEY_KP_SUBTRACT => house.moveBy(0.0f, 0, -0.01f)
        case GLFW_KEY_Q => house.rotateBy(0, 0, 1.0f)
        case GLFW_KEY_E => house.rotateBy(0, 0, -1.0f)
        case GLFW_KEY_W => house.rotateBy(1.0f, 0, 0)
        case GLFW_KEY_S => house.rotateBy(-1.0f, 0, 0)
        case GLFW_KEY_A => house.rotateBy(0, 1.0f, 0)
        case GLFW_KEY_D => house.rotateBy(0, -1.0f, 0)
        case GLFW_KEY_SPACE => 
            house.moveTo(0, 0, -1.5f)
            house.rotateTo(0, 0, 0)
        case _ => delta = if((action == GLFW_PRESS || action == GLFW_REPEAT)) {
            key match {
                case GLFW_KEY_R if (mods & Settings.CTRL_BIT) == 0 => (step, 0.0f, 0.0f)
                case GLFW_KEY_R =>                                    (-step, 0.0f, 0.0f)
                case GLFW_KEY_G if (mods & Settings.CTRL_BIT) == 0 => (0.0f, step, 0.0f)
                case GLFW_KEY_G =>                                    (0.0f, -step, 0.0f)
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