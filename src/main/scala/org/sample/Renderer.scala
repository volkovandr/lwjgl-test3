package org.sample

import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_UNSIGNED_INT
import org.lwjgl.opengl.GL11.GL_TRIANGLES
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.GL_STATIC_DRAW

import org.lwjgl.opengl.GL11.glClear
import org.lwjgl.opengl.GL11.glClearColor
import org.lwjgl.opengl.GL11.glDrawElements
import org.lwjgl.opengl.GL11.glViewport
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glDisableVertexAttribArray
import org.lwjgl.opengl.GL30.glBindVertexArray

import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer

object Renderer {

    private var shaderProgram: Option[ShaderProgram] = None
    private var vao: Option[CustomTypes.VaoId] = None
    private var vbo: Option[CustomTypes.VboId] = None

    private var mesh: Option[Mesh] = None

    def init(): Unit = {
        println("Loading...")
        val shaderProgram = new ShaderProgram()
        shaderProgram.createVertexShaderFromResourceFile("vertex.vs")
        shaderProgram.createFragmentShaderFromResourceFile("fragment.fs")
        shaderProgram.link()
        this.shaderProgram = Some(shaderProgram)

        val vertices = Array(
            -0.5f, 0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.7f, 0.0f,
            
            0.45f, 0.65f, 0.0f,
            0.40f, 0.65f, 0.0f,
            0.40f, 0.5f,  0.0f,
            0.45f, 0.5f,  0.0f
        )

        val indices = Array(
            0, 2, 1,
            1, 2, 3,
            4, 0, 1,
            5, 6, 7,
            5, 7, 8
        )

        mesh = Some(new Mesh(vertices, indices))
        mesh.foreach(m => println(s"Loaded mesh. ${m.vertexCount} vertices"))

        glViewport(0, 0, Settings.width, Settings.height)
    }

    def render(): Unit = {
        import GameLogic.color
        glClearColor(color._1, color._2, color._3, 0.0f)
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

        for {
            program <- shaderProgram
            mesh <- mesh
        } {
            program.bind()
            renderMesh(mesh)
            program.unbind()
        }
    }

    private def renderMesh(mesh: Mesh): Unit = {
        glBindVertexArray(mesh.vaoId)
        glEnableVertexAttribArray(0)
        glDrawElements(GL_TRIANGLES, mesh.vertexCount, GL_UNSIGNED_INT, 0)
        glDisableVertexAttribArray(0)
        glBindVertexArray(0)
    }

    def cleanup(): Unit = {
        shaderProgram.foreach(sp => sp.cleanup())
        mesh.foreach(m => m.cleanup())
    }
}