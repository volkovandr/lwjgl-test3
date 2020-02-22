package org.sample

import org.lwjgl.opengl.GL11.GL_VERSION
import org.lwjgl.opengl.GL11.GL_VENDOR
import org.lwjgl.opengl.GL11.GL_RENDERER
import org.lwjgl.opengl.GL11.GL_EXTENSIONS
import org.lwjgl.opengl.GL11.GL_MAX_TEXTURE_SIZE
import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT

import org.lwjgl.opengl.GL

import org.lwjgl.opengl.GL11.glGetString
import org.lwjgl.opengl.GL11.glGetInteger
import org.lwjgl.opengl.GL11.glClear
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose
import org.lwjgl.glfw.GLFW.glfwSwapBuffers
import org.lwjgl.glfw.GLFW.glfwPollEvents

object MainLoop {
    def init(): Unit = {
        GL.createCapabilities()
        printDiagnostics()
    }

    def run(): Unit = Window.window.foreach { window =>
        while(!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
            glfwSwapBuffers(window)
            glfwPollEvents()
        }
    }

    private def printDiagnostics(): Unit = {
        println("OpenGL capabilities:")
        println(s"OpenGL version: ${glGetString(GL_VERSION)}")
        println(s"OpenGL Max Texture Size: ${glGetInteger(GL_MAX_TEXTURE_SIZE)}")
        println(s"OpenGL vendor: ${glGetString(GL_VENDOR)}")
        println(s"OpenGL renderer: ${glGetString(GL_RENDERER)}")
        println("OpenGL extensions:")
        glGetString(GL_EXTENSIONS).split("\\ ").foreach(ext => println(s"  $ext"))
    }
}