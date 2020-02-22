package org.sample

import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_FLOAT
import org.lwjgl.opengl.GL11.GL_TRIANGLES
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.GL_STATIC_DRAW

import org.lwjgl.opengl.GL11.glClear
import org.lwjgl.opengl.GL11.glClearColor
import org.lwjgl.opengl.GL11.glDrawArrays
import org.lwjgl.opengl.GL11.glViewport
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glDisableVertexAttribArray
import org.lwjgl.opengl.GL15.glGenBuffers
import org.lwjgl.opengl.GL15.glDeleteBuffers
import org.lwjgl.opengl.GL15.glBindBuffer
import org.lwjgl.opengl.GL15.glBufferData
import org.lwjgl.opengl.GL30.glGenVertexArrays
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glDeleteVertexArrays

import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer

object Renderer {

    private var shaderProgram: Option[ShaderProgram] = None
    private var vao: Option[CustomTypes.VaoId] = None
    private var vbo: Option[CustomTypes.VboId] = None

    def init(): Unit = {
        println("Loading...")
        val shaderProgram = new ShaderProgram()
        shaderProgram.createVertexShaderFromResourceFile("vertex.vs")
        shaderProgram.createFragmentShaderFromResourceFile("fragment.fs")
        shaderProgram.link()
        this.shaderProgram = Some(shaderProgram)

        val vertices = Array(
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
        )
        var verticesBuffer: FloatBuffer = null
        try {
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.size)
            verticesBuffer.put(vertices).flip()

            val vaoId = glGenVertexArrays()
            glBindVertexArray(vaoId)

            val vboId = glGenBuffers()
            glBindBuffer(GL_ARRAY_BUFFER, vboId)
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
            glEnableVertexAttribArray(0)

            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
            glBindBuffer(GL_ARRAY_BUFFER, 0)
            glBindVertexArray(0)

            vao = Some(vaoId)
        } finally {
            if(verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer)
            }
        }
        glViewport(0, 0, Settings.width, Settings.height)
    }

    def render(): Unit = {
        import GameLogic.color
        glClearColor(color._1, color._2, color._3, 0.0f)
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

        for {
            program <- shaderProgram
            vaoId <- vao
        } {
            program.bind()
            glBindVertexArray(vaoId)
            glEnableVertexAttribArray(0)
            glDrawArrays(GL_TRIANGLES, 0, 3)
            glDisableVertexAttribArray(0)
            glBindVertexArray(0)
            program.unbind()
        }
    }

    def cleanup(): Unit = {
        shaderProgram.foreach(sp => sp.cleanup())
        glDisableVertexAttribArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        vbo.foreach(id => glDeleteBuffers(id))
        glBindVertexArray(0)
        vao.foreach(id => glDeleteVertexArrays(id))
    }
}