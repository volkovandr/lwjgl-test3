package org.sample.logic

import org.sample.helpers.Settings

import org.sample.graph.Camera
import org.sample.interface.KeyboardInput
import java.awt.RenderingHints.Key
import org.sample.interface.MouseInput

object GameLogic {

    private val colorStep = 0.01f
    private val cameraStep = 0.05f
    private val mouseSensitivity = 0.1f
    private val invertMouse = true

    private val houseFront = new House
    private val houseFrontRight = new House
    private val houseFrontLeft = new House
    private val houseBack = new House
    private val houseBackRight = new House
    private val houseBackLeft = new House
    val gameObjects: List[GameObject] = List(houseFront, houseFrontRight, houseFrontLeft, houseBack, houseBackLeft, houseBackRight)
    var camera: Camera = Camera()
    var color = (0.0f, 0.0f, 0.0f)
    private var delta = (0.0f, 0.0f, 0.0f)

    def init(): Unit = {
        houseFront.moveTo(0, 0, -2.5f)
        houseFrontRight.moveTo(2.5f, 0, -2.5f)
        houseFrontLeft.moveTo(-2.5f, 0, -2.5f)
        houseBack.moveTo(0, 0, 2.5f)
        houseBackRight.moveTo(2.5f, 0, 2.5f)
        houseBackLeft.moveTo(-2.5f, 0, 2.5f)
        List(houseBack, houseBackLeft, houseBackRight).foreach(_.rotateBy(0, 180.0f, 0))
        println("Welcome to the Game!")
        println("Press R to increase the Red color, and Ctrl+R to decrease it")
        println("Press G to increase the Green color, and Ctrl+G to decrease it")
        println("Press B to increase the Blue color, and Ctrl+B to decrease it")
        println("Use cursor keys to move the house in front, numpad's + and - to move it farther or closer,")
        println("Q and E to rotate the house and WSAD to move around. SPACE brings everything back.")
    }

    def input(): Unit =  {
        processKeyInput()
        processMouseInput()
    }

    private def processMouseInput(): Unit = {
        MouseInput.input()
        if(MouseInput.leftClick && !MouseInput.locked) MouseInput.lockMouse()
        if(MouseInput.locked) {
            val mouseMovedBy = MouseInput.displacementVector
            val xSign = if(invertMouse) -1 else 1
            camera.rotateBy(xSign * mouseMovedBy.y * mouseSensitivity, mouseMovedBy.x * mouseSensitivity, 0)
        }
    }

    private def processKeyInput(): Unit = {
        if(KeyboardInput.KEY_UP) houseFront.moveBy(0, 0.01f, 0)
        if(KeyboardInput.KEY_DOWN) houseFront.moveBy(0, -0.01f, 0)
        if(KeyboardInput.KEY_RIGHT) houseFront.moveBy(0.01f, 0, 0)
        if(KeyboardInput.KEY_LEFT) houseFront.moveBy(-0.01f, 0, 0)
        if(KeyboardInput.KEY_PLUS) houseFront.moveBy(0.0f, 0, 0.01f)
        if(KeyboardInput.KEY_MINUS) houseFront.moveBy(0.0f, 0, -0.01f)
        if(KeyboardInput.KEY_Q) houseFront.rotateBy(0, 0, 1.0f)
        if(KeyboardInput.KEY_E) houseFront.rotateBy(0, 0, -1.0f)
        // if(KeyboardInput.KEY_W) mainHouse.rotateBy(1.0f, 0, 0)
        // if(KeyboardInput.KEY_S) mainHouse.rotateBy(-1.0f, 0, 0)
        // if(KeyboardInput.KEY_A) mainHouse.rotateBy(0, 1.0f, 0)
        // if(KeyboardInput.KEY_D) mainHouse.rotateBy(0, -1.0f, 0)
        if(KeyboardInput.KEY_W) camera.moveByLocal(0.0f, 0.0f, -cameraStep)
        if(KeyboardInput.KEY_S) camera.moveByLocal(0.0f, 0.0f, cameraStep)
        if(KeyboardInput.KEY_A) camera.moveByLocal(-cameraStep, 0.0f, 0.0f)
        if(KeyboardInput.KEY_D) camera.moveByLocal(cameraStep, 0.0f, 0.0f)
        if(KeyboardInput.KEY_SPACE) {
            houseFront.moveTo(0, 0, -2.5f)
            houseFront.rotateTo(0, 0, 0)
            camera.moveTo(0, 0, 0)
            camera.rotateTo(0, 0, 0)
        }
        delta = if(KeyboardInput.KEY_R || KeyboardInput.KEY_G || KeyboardInput.KEY_B) {
            val sign = if(KeyboardInput.KEY_CTRL) -1 else 1
            (
                if(KeyboardInput.KEY_R) sign * colorStep else 0.0f,
                if(KeyboardInput.KEY_G) sign * colorStep else 0.0f,
                if(KeyboardInput.KEY_B) sign * colorStep else 0.0f,
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