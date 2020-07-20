package org.sample.logic

import org.sample.helpers.Settings

import org.sample.graph.Camera
import org.sample.interface.KeyboardInput
import java.awt.RenderingHints.Key
import org.sample.interface.MouseInput

object GameLogic {

    private val step= 0.01f
    private val mouseSensitivity = 0.1f
    private val mainHouse = new House
    private val house1 = new House
    val gameObjects: List[GameObject] = List(mainHouse, house1)
    var camera: Camera = Camera()
    var color = (0.0f, 0.0f, 0.0f)
    private var delta = (0.0f, 0.0f, 0.0f)

    def init(): Unit = {
        mainHouse.moveTo(0, 0, -2.5f)
        house1.moveTo(2.5f, 0, -2.5f)
        println("Welcome to the Game!")
        println("Press R to increase the Red color, and Ctrl+R to decrease it")
        println("Press G to increase the Green color, and Ctrl+G to decrease it")
        println("Press B to increase the Blue color, and Ctrl+B to decrease it")
        println("Use cursor keys to move the house, numpad's + and - to move it farther or closer,")
        println("Q and E, W and S, A and D to rotate it and SPACE to bring everything back.")
    }

    def input(): Unit =  {
        processKeyInput()
        processMouseInput()
    }

    private def processMouseInput(): Unit = {
        MouseInput.input()
        val mouseMovedBy = MouseInput.displacementVector
        camera.rotateBy(mouseMovedBy.y * mouseSensitivity, mouseMovedBy.x * mouseSensitivity, 0)
    }

    private def processKeyInput(): Unit = {
        if(KeyboardInput.KEY_UP) mainHouse.moveBy(0, 0.01f, 0)
        if(KeyboardInput.KEY_DOWN) mainHouse.moveBy(0, -0.01f, 0)
        if(KeyboardInput.KEY_RIGHT) mainHouse.moveBy(0.01f, 0, 0)
        if(KeyboardInput.KEY_LEFT) mainHouse.moveBy(-0.01f, 0, 0)
        if(KeyboardInput.KEY_PLUS) mainHouse.moveBy(0.0f, 0, 0.01f)
        if(KeyboardInput.KEY_MINUS) mainHouse.moveBy(0.0f, 0, -0.01f)
        if(KeyboardInput.KEY_Q) mainHouse.rotateBy(0, 0, 1.0f)
        if(KeyboardInput.KEY_E) mainHouse.rotateBy(0, 0, -1.0f)
        if(KeyboardInput.KEY_W) mainHouse.rotateBy(1.0f, 0, 0)
        if(KeyboardInput.KEY_S) mainHouse.rotateBy(-1.0f, 0, 0)
        if(KeyboardInput.KEY_A) mainHouse.rotateBy(0, 1.0f, 0)
        if(KeyboardInput.KEY_D) mainHouse.rotateBy(0, -1.0f, 0)
        if(KeyboardInput.KEY_SPACE) {
            mainHouse.moveTo(0, 0, -2.5f)
            mainHouse.rotateTo(0, 0, 0)
        }
        delta = if(KeyboardInput.KEY_R || KeyboardInput.KEY_G || KeyboardInput.KEY_B) {
            val sign = if(KeyboardInput.KEY_CTRL) -1 else 1
            (
                if(KeyboardInput.KEY_R) sign * step else 0.0f,
                if(KeyboardInput.KEY_G) sign * step else 0.0f,
                if(KeyboardInput.KEY_B) sign * step else 0.0f,
            )
        } else {
            (0.0f, 0.0f, 0.0f)
        }
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