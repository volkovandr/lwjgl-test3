package org.sample

import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.GL_COMPILE_STATUS
import org.lwjgl.opengl.GL20.GL_LINK_STATUS
import org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS

import org.lwjgl.opengl.GL20.glCreateProgram
import org.lwjgl.opengl.GL20.glCreateShader
import org.lwjgl.opengl.GL20.glShaderSource
import org.lwjgl.opengl.GL20.glCompileShader
import org.lwjgl.opengl.GL20.glGetShaderi
import org.lwjgl.opengl.GL20.glGetShaderInfoLog
import org.lwjgl.opengl.GL20.glAttachShader
import org.lwjgl.opengl.GL20.glLinkProgram
import org.lwjgl.opengl.GL20.glDetachShader
import org.lwjgl.opengl.GL20.glGetProgrami
import org.lwjgl.opengl.GL20.glGetProgramInfoLog
import org.lwjgl.opengl.GL20.glValidateProgram
import org.lwjgl.opengl.GL20.glUseProgram
import org.lwjgl.opengl.GL20.glDeleteProgram
import org.lwjgl.opengl.GL20.glGetUniformLocation
import org.lwjgl.opengl.GL20.glUniformMatrix4fv

import scala.io.Source
import org.joml.Matrix4f
import org.lwjgl.system.MemoryStack
import scala.util.Using

class ShaderProgram {
    
    private var program: Option[CustomTypes.ShaderProgramId] = None
    private var vertexShaderId: Option[CustomTypes.VertexShaderId] = None
    private var fragmentShaderId: Option[CustomTypes.FragmentShaderId] = None
    private var uniforms: Map[String, Int] = Map()
    
    def init(): Unit = {
        val programId = glCreateProgram()
        if(programId == 0) {
            throw new RuntimeException("Failed creating an OpenGL program")
        }
        program = Some(programId)
    }

    def createVertexShader(shaderCode: String): Unit = vertexShaderId = Some(createShader(shaderCode, GL_VERTEX_SHADER))
    def createFragmentShader(shaderCode: String): Unit = fragmentShaderId = Some(createShader(shaderCode, GL_FRAGMENT_SHADER))

    def createVertexShaderFromResourceFile(resourceFileName: String): Unit = createVertexShader(Source.fromResource(resourceFileName).getLines().mkString("\n"))
    def createFragmentShaderFromResourceFile(resourceFileName: String): Unit = createFragmentShader(Source.fromResource(resourceFileName).getLines().mkString("\n"))

    def createShader(shaderCode: String, shaderType: Int): Int = {
        val shaderId = glCreateShader(shaderType)
        if(shaderId == 0) {
            throw new RuntimeException(s"Failed creating shader of type $shaderType")
        }
        glShaderSource(shaderId, shaderCode)
        glCompileShader(shaderId)

        if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException(s"Failed compiling shader: ${glGetShaderInfoLog(shaderId, 1024)}")
        }

        program.foreach { programId => glAttachShader(programId, shaderId) }

        shaderId
    }

    def createUniform(uniformName: String): Unit = program.foreach(programId => {
        val uniformLocation = glGetUniformLocation(programId, uniformName)
        if(uniformLocation < 0) {
            throw new RuntimeException(s"Cannot find uniform named [$uniformName]")
        }
        uniforms += uniformName -> uniformLocation
    })

    def setUniform(uniformName: String, matrix: Matrix4f): Unit = uniforms
        .get(uniformName)
        .foreach(uniform => Using(MemoryStack.stackPush()) { stack =>
            val fb = stack.mallocFloat(16)
            matrix.get(fb)
            glUniformMatrix4fv(uniform, false, fb)
        })

    def link(): Unit = program.foreach { programId =>
        glLinkProgram(programId)
        if(glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException(s"Failed linking shader code: ${glGetProgramInfoLog(programId, 1024)}")
        }

        vertexShaderId.foreach { shaderId => glDetachShader(programId, shaderId)}
        fragmentShaderId.foreach { shaderId => glDetachShader(programId, shaderId) }

        glValidateProgram(programId)
        if(glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            throw new RuntimeException(s"Failed validating shader code: ${glGetProgramInfoLog(programId, 1024)}")
        }
    }

    def bind(): Unit = glUseProgram(program.getOrElse(0))

    def unbind(): Unit = glUseProgram(0)

    def cleanup(): Unit = {
        unbind()
        program.foreach { id => glDeleteProgram(id) }
    }
}