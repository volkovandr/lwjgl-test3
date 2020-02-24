package org.sample

import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
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

class Mesh(positions: Array[Float]) {
    private var vao: Option[CustomTypes.VaoId] = None
    private var vbo: Option[CustomTypes.VboId] = None
    var vertexCount: Int = 0

    def vaoId: CustomTypes.VaoId = vao.getOrElse(0)
    def vboId: CustomTypes.VboId = vbo.getOrElse(0)

    init(positions)
    
    private def init(positions: Array[Float]) {
        var verticesBuffer: FloatBuffer = null
        try {
            verticesBuffer = MemoryUtil.memAllocFloat(positions.size)
            this.vertexCount = positions.size / 3
            verticesBuffer.put(positions).flip()

            val vaoId = glGenVertexArrays()
            glBindVertexArray(vaoId)

            val vboId = glGenBuffers()
            glBindBuffer(GL_ARRAY_BUFFER, vboId)
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
            glBindBuffer(GL_ARRAY_BUFFER, 0)

            glBindVertexArray(0)

            vao = Some(vaoId)
            vbo = Some(vboId)
        } finally {
            if(verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer)
            }
        }
    }

    def cleanup(): Unit = {
        glDisableVertexAttribArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        vbo.foreach(id => glDeleteBuffers(id))
        glBindVertexArray(0)
        vao.foreach(id => glDeleteVertexArrays(id))
    }
}