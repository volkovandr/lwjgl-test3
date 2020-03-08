package org.sample

import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.GL_STATIC_DRAW
import org.lwjgl.opengl.GL11.GL_FLOAT

import org.lwjgl.opengl.GL20.glDisableVertexAttribArray
import org.lwjgl.opengl.GL15.glBindBuffer
import org.lwjgl.opengl.GL15.glDeleteBuffers
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glDeleteVertexArrays
import org.lwjgl.opengl.GL30.glGenVertexArrays
import org.lwjgl.opengl.GL15.glGenBuffers
import org.lwjgl.opengl.GL15.glBufferData
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray

import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Mesh(positions: Array[Float], indices: Array[Int], colors: Array[Float]) {
    private var vao: Option[CustomTypes.VaoId] = None
    private var posVbo: Option[CustomTypes.VboId] = None
    private var idxVbo: Option[CustomTypes.VboId] = None
    private var colVbo: Option[CustomTypes.VboId] = None
    var vertexCount: Int = 0

    def vaoId: CustomTypes.VaoId = vao.getOrElse(0)
    def posVboId: CustomTypes.VboId = posVbo.getOrElse(0)
    def idxVboId: CustomTypes.VboId = idxVbo.getOrElse(0)
    def colVboId: CustomTypes.VboId = colVbo.getOrElse(0)

    init(positions, indices, colors)
    
    private def init(positions: Array[Float], indices: Array[Int], colors: Array[Float]) {
        var posBuffer: FloatBuffer = null
        var colorBuffer: FloatBuffer = null
        var indicesBuffer: IntBuffer = null
        try {
            val vaoId = glGenVertexArrays()
            glBindVertexArray(vaoId)

            val posVboId = glGenBuffers()
            posBuffer = MemoryUtil.memAllocFloat(positions.size)
            posBuffer.put(positions).flip()
            glBindBuffer(GL_ARRAY_BUFFER, posVboId)
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW)
            glEnableVertexAttribArray(0)
            glBindBuffer(GL_ARRAY_BUFFER, posVboId)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
            posVbo = Some(posVboId)

            val colVboId = glGenBuffers()
            colorBuffer = MemoryUtil.memAllocFloat(colors.size)
            colorBuffer.put(colors).flip()
            glBindBuffer(GL_ARRAY_BUFFER, colVboId)
            glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW)
            glEnableVertexAttribArray(1)
            glBindBuffer(GL_ARRAY_BUFFER, colVboId)
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0)
            colVbo = Some(colVboId)

            val idxVboId = glGenBuffers()
            indicesBuffer = MemoryUtil.memAllocInt(indices.size)
            indicesBuffer.put(indices).flip()
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId)
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)
            idxVbo = Some(idxVboId)

            glBindBuffer(GL_ARRAY_BUFFER, 0)
            glBindVertexArray(0)

            vertexCount = indices.size
            vao = Some(vaoId)

            println(s"Mesh: colors: ${colors.mkString("[", ",", "]")}")
        } catch {
            case e: Throwable => println(s"Failed creating Mesh: ${e.getMessage}")
            throw e
        } finally {
            if(indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer)
            }
            if(colorBuffer != null) {
                MemoryUtil.memFree(colorBuffer)
            }
            if(posBuffer != null) {
                MemoryUtil.memFree(posBuffer)
            }
        }
    }

    def cleanup(): Unit = {
        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        posVbo.foreach(id => glDeleteBuffers(id))
        idxVbo.foreach(id => glDeleteBuffers(id))
        colVbo.foreach(id => glDeleteBuffers(id))
        glBindVertexArray(0)
        vao.foreach(id => glDeleteVertexArrays(id))
    }
}