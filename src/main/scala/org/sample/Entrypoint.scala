package org.sample

import org.sample.interface.Window
import org.sample.logic.MainLoop
import org.sample.interface.MouseInput
import org.sample.interface.KeyboardInput

object Entrypoint extends App {
    println("This is a LWJGL sample program")
    try {
        Window.init()
        println("OpenGL is intialialized and a Window is created!")
        MainLoop.init()
        println("Main loop is initialized and starts now!")
        MouseInput.init()
        KeyboardInput.init()
        println("Keyboard & Mouse callbacks are initialized!")
        MainLoop.run()
    } catch {
        case e: Throwable => e.printStackTrace()
    } finally {
        MainLoop.cleanup()
        KeyboardInput.cleanup()
        Window.cleanup()
    }
    println("Bye")
}