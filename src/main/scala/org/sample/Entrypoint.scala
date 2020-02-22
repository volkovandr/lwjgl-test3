package org.sample

object Entrypoint extends App {
    println("This is a LWJGL sample program")
    try {
        Window.init()
        println("OpenGL is intialialized and a Window is created")
        MainLoop.init()
        println("Main loop is initialized and starts now!")
        MainLoop.run()
    } catch {
        case e: Throwable => e.printStackTrace()
    } finally {
        MainLoop.cleanup()
        Window.cleanup()
    }
    println("Bye")
}