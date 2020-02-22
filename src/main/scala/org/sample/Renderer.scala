package org.sample

import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT

import org.lwjgl.opengl.GL11.glClear
import org.lwjgl.opengl.GL11.glClearColor

object Renderer {

    def init(): Unit = {
        val shaderProgram = new ShaderProgram()
        shaderProgram.createVertexShaderFromResourceFile("vertex.vs")
        shaderProgram.createFragmentShaderFromResourceFile("fragment.fs")
        shaderProgram.link()
    }

    def render(): Unit = {
        import GameLogic.color
        glClearColor(color._1, color._2, color._3, 0.0f)
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    }
}