package org.sample.graph

import org.sample.helpers.CustomTypes._

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

class Mesh(positions: Array[Float], indices: Array[Int], texCoords: Array[Float], val texture: Texture) {
    private var vao: Option[VaoId] = None
    private var posVbo: Option[VboId] = None
    private var idxVbo: Option[VboId] = None
    private var texVbo: Option[VboId] = None
    var vertexCount: Int = 0

    def vaoId: VaoId = vao.getOrElse(0)
    def posVboId: VboId = posVbo.getOrElse(0)
    def idxVboId: VboId = idxVbo.getOrElse(0)
    def texVboId: VboId = texVbo.getOrElse(0)

    init(positions, indices, texCoords)
    
    private def init(positions: Array[Float], indices: Array[Int], texCoords: Array[Float]): Unit = {
        var posBuffer: FloatBuffer = null
        var texCoordBuffer: FloatBuffer = null
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

            val texVboId = glGenBuffers()
            texCoordBuffer = MemoryUtil.memAllocFloat(texCoords.size)
            texCoordBuffer.put(texCoords).flip()
            glBindBuffer(GL_ARRAY_BUFFER, texVboId)
            glBufferData(GL_ARRAY_BUFFER, texCoordBuffer, GL_STATIC_DRAW)
            glEnableVertexAttribArray(1)
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0)
            texVbo = Some(texVboId)

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
        } catch {
            case e: Throwable => println(s"Failed creating Mesh: ${e.getMessage}")
            throw e
        } finally {
            if(indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer)
            }
            if(texCoordBuffer != null) {
                MemoryUtil.memFree(texCoordBuffer)
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
        texVbo.foreach(id => glDeleteBuffers(id))
        glBindVertexArray(0)
        vao.foreach(id => glDeleteVertexArrays(id))
        texture.cleanup()
    }
}