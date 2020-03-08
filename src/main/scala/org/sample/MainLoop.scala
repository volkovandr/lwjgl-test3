package org.sample

import org.lwjgl.opengl.GL11.GL_VERSION
import org.lwjgl.opengl.GL11.GL_VENDOR
import org.lwjgl.opengl.GL11.GL_RENDERER
import org.lwjgl.opengl.GL11.GL_EXTENSIONS
import org.lwjgl.opengl.GL11.GL_MAX_TEXTURE_SIZE

import org.lwjgl.opengl.GL

import org.lwjgl.opengl.GL11.glGetString
import org.lwjgl.opengl.GL11.glGetInteger
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose
import org.lwjgl.glfw.GLFW.glfwSwapBuffers
import org.lwjgl.glfw.GLFW.glfwPollEvents

object MainLoop {
    def init(): Unit = {
        GL.createCapabilities()
        printDiagnostics()
        Renderer.init()
        GameLogic.init()
    }

    def run(): Unit = Window.window.foreach { window =>
        while(!glfwWindowShouldClose(window)) {
            GameLogic.update()
            Renderer.render()
            glfwSwapBuffers(window)
            glfwPollEvents()
        }
    }

    def cleanup(): Unit = {
        Renderer.cleanup()
    }

    private def printDiagnostics(): Unit = {
        println("OpenGL capabilities:")
        println(s"OpenGL version: ${glGetString(GL_VERSION)}")
        println(s"OpenGL Max Texture Size: ${glGetInteger(GL_MAX_TEXTURE_SIZE)}")
        println(s"OpenGL vendor: ${glGetString(GL_VENDOR)}")
        println(s"OpenGL renderer: ${glGetString(GL_RENDERER)}")
        println("OpenGL extensions:")
        Option(glGetString(GL_EXTENSIONS)).foreach(_.split("\\ ").foreach(ext => println(s"  $ext")))
    }
}