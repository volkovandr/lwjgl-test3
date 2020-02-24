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

import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Mesh(positions: Array[Float], indices: Array[Int]) {
    private var vao: Option[CustomTypes.VaoId] = None
    private var posVbo: Option[CustomTypes.VboId] = None
    private var idxVbo: Option[CustomTypes.VboId] = None
    var vertexCount: Int = 0

    def vaoId: CustomTypes.VaoId = vao.getOrElse(0)
    def posVboId: CustomTypes.VboId = posVbo.getOrElse(0)
    def idxVboId: CustomTypes.VboId = idxVbo.getOrElse(0)

    init(positions, indices)
    
    private def init(positions: Array[Float], indices: Array[Int]) {
        val vaoId = glGenVertexArrays()
        glBindVertexArray(vaoId)

        var verticesBuffer: FloatBuffer = null
        try {
            verticesBuffer = MemoryUtil.memAllocFloat(positions.size)
            verticesBuffer.put(positions).flip()
            val posVboId = glGenBuffers()
            glBindBuffer(GL_ARRAY_BUFFER, posVboId)
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
            glBindBuffer(GL_ARRAY_BUFFER, 0)
            posVbo = Some(posVboId)
        } finally {
            if(verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer)
            }
        }

        var indicesBuffer: IntBuffer = null
        try {
            indicesBuffer = MemoryUtil.memAllocInt(indices.size)
            indicesBuffer.put(indices).flip()
            val idxVboId = glGenBuffers()
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId)
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)
            idxVbo = Some(idxVboId)
        } finally {
            if(indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer)
            }
        }
        glBindVertexArray(0)
        vao = Some(vaoId)
        vertexCount = indices.size
    }

    def cleanup(): Unit = {
        glDisableVertexAttribArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        posVbo.foreach(id => glDeleteBuffers(id))
        idxVbo.foreach(id => glDeleteBuffers(id))
        glBindVertexArray(0)
        vao.foreach(id => glDeleteVertexArrays(id))
    }
}