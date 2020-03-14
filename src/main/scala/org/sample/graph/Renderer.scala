package org.sample.graph

import org.sample.helpers.Settings
import org.sample.interface.Window
import org.sample.logic.GameLogic
import org.sample.helpers.CustomTypes._

import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_UNSIGNED_INT
import org.lwjgl.opengl.GL11.GL_TRIANGLES
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.GL_STATIC_DRAW

import org.lwjgl.opengl.GL11.glClear
import org.lwjgl.opengl.GL11.glClearColor
import org.lwjgl.opengl.GL11.glDrawElements
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glDisableVertexAttribArray
import org.lwjgl.opengl.GL30.glBindVertexArray

import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import org.joml.Matrix4f
import org.sample.logic.GameObject
import org.sample.logic.House

object Renderer {

    private var shaderProgram: Option[ShaderProgram] = None
    private var objects: List[GameObject] = List()
    private val projectionMatrixUniformName = "projectionMatrix"
    private var projectionMatrix: Matrix4f = _

    def init(): Unit = {
        println("Loading...")

        val shaderProgram = new ShaderProgram()
        shaderProgram.init()
        shaderProgram.createVertexShaderFromResourceFile("vertex.vs")
        shaderProgram.createFragmentShaderFromResourceFile("fragment.fs")
        shaderProgram.link()
        shaderProgram.createUniform(projectionMatrixUniformName)
        this.shaderProgram = Some(shaderProgram)

        objects = List(new House)
        objects.foreach(_.init())
    }

    def render(): Unit = {
        if(Window.resizeWindow()) {
            val fieldOfView = Math.toRadians(60.0f).toFloat
            val zNear = 0.01f
            val zFar = 1000.0f
            val aspectRatio = Settings.width.toFloat / Settings.height
            projectionMatrix = new Matrix4f().perspective(fieldOfView, aspectRatio, zNear, zFar)
        }
        
        import GameLogic.color
        glClearColor(color._1, color._2, color._3, 0.0f)
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

        for {
            program <- shaderProgram
        } {
            program.bind()
            program.setUniform(projectionMatrixUniformName, projectionMatrix)
            objects.foreach(o => renderMesh(o.mesh))
            program.unbind()
        }
    }

    private def renderMesh(mesh: Mesh): Unit = {
        glBindVertexArray(mesh.vaoId)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glDrawElements(GL_TRIANGLES, mesh.vertexCount, GL_UNSIGNED_INT, 0)
        glDisableVertexAttribArray(1)
        glDisableVertexAttribArray(0)
        glBindVertexArray(0)
    }

    def cleanup(): Unit = {
        shaderProgram.foreach(sp => sp.cleanup())
        objects.foreach(_.cleanup())
    }
}