package org.sample

import org.lwjgl._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.system._

import java.nio._

import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._
import scala.io.Source

object SimpleTry extends App {
    println(s"Hello. LWJGL version [${Version.getVersion()}]")
    GLFWErrorCallback.createPrint(System.err).set()
    if(!glfwInit()) throw new IllegalStateException("Could not init GLFW")

    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
    val window = glfwCreateWindow(300, 300, "Hello", NULL, NULL)
    if(window == NULL) throw new RuntimeException("Failed creating a window")

    glfwSetKeyCallback(window, (window, key, scancode, action, mods) => {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true)
    })
    
    var stack: MemoryStack = _
    try {
        stack = stackPush()
		val pWidth = stack.mallocInt(1)
		val pHeight = stack.mallocInt(1)
    	glfwGetWindowSize(window, pWidth, pHeight)
        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())
		glfwSetWindowPos(
			window,
			(vidmode.width() - pWidth.get(0)) / 2,
			(vidmode.height() - pHeight.get(0)) / 2
		)
	} finally {
        if(stack != null) {
            stack.close()
        }
    }

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)
    glfwShowWindow(window)
    GL.createCapabilities()

    val programId = glCreateProgram()
    if(programId == 0) throw new RuntimeException("Failed creating an OpenGL program")
    
    val vertexShaderId = glCreateShader(GL_VERTEX_SHADER)
    if(vertexShaderId == 0) throw new RuntimeException("Failed creating Vertex Shader")
    glShaderSource(vertexShaderId, Source.fromResource("vertex.vs").getLines().mkString("\n"))
    glCompileShader(vertexShaderId)
    if(glGetShaderi(vertexShaderId, GL_COMPILE_STATUS) == 0) {
        throw new RuntimeException(s"Failed compiling shader: ${glGetShaderInfoLog(vertexShaderId, 1024)}")
    }
    glAttachShader(programId, vertexShaderId)
    
    val fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER)
    if(fragmentShaderId == 0) throw new RuntimeException("Failed creating Vertex Shader")
    glShaderSource(fragmentShaderId, Source.fromResource("fragment.fs").getLines().mkString("\n"))
    glCompileShader(fragmentShaderId)
    if(glGetShaderi(fragmentShaderId, GL_COMPILE_STATUS) == 0) {
        throw new RuntimeException(s"Failed compiling shader: ${glGetShaderInfoLog(fragmentShaderId, 1024)}")
    }
    glAttachShader(programId, fragmentShaderId)
    
    glLinkProgram(programId)
    if(glGetProgrami(programId, GL_LINK_STATUS) == 0) {
        throw new RuntimeException(s"Failed linking shader code: ${glGetProgramInfoLog(programId, 1024)}")
    }
    glDetachShader(programId, vertexShaderId)
    glDetachShader(programId, fragmentShaderId)
    glValidateProgram(programId)
    if(glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
        throw new RuntimeException(s"Failed validating shader code: ${glGetProgramInfoLog(programId, 1024)}")
    }

    val vertices = Array(
        -0.5f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.5f, 0.5f, 0.0f
    )
    val colors = Array(
        1.0f, 1.0f, 1.0f,
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f
    )
    val indices = Array(
        0, 1, 3,
        3, 1, 2
    )

    val vaoId = glGenVertexArrays()
    glBindVertexArray(vaoId)

    val posVboId = glGenBuffers()
    val posBuffer = MemoryUtil.memAllocFloat(vertices.size)
    posBuffer.put(vertices).flip()
    glBindBuffer(GL_ARRAY_BUFFER, posVboId)
    glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW)
    glEnableVertexAttribArray(0)
    glBindBuffer(GL_ARRAY_BUFFER, posVboId)
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

    val colVboId = glGenBuffers()
    val colBuffer = MemoryUtil.memAllocFloat(colors.size)
    colBuffer.put(colors).flip()
    glBindBuffer(GL_ARRAY_BUFFER, colVboId)
    glBufferData(GL_ARRAY_BUFFER, colBuffer, GL_STATIC_DRAW)
    glEnableVertexAttribArray(1)
    glBindBuffer(GL_ARRAY_BUFFER, colVboId)
    glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0)

    val idxVboId = glGenBuffers()
    val idxBuffer = MemoryUtil.memAllocInt(indices.size)
    idxBuffer.put(indices).flip()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, idxBuffer, GL_STATIC_DRAW)

    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glBindVertexArray(0)

    MemoryUtil.memFree(idxBuffer)
    MemoryUtil.memFree(colBuffer)
    MemoryUtil.memFree(posBuffer)

    val vertexCount = indices.size

    glViewport(0, 0, 300, 300)

    while(!glfwWindowShouldClose(window)) {
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f)
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

        glUseProgram(programId)
        glBindVertexArray(vaoId)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)
        glDisableVertexAttribArray(1)
        glDisableVertexAttribArray(0)
        glBindVertexArray(0)
        glUseProgram(0)

        glfwSwapBuffers(window)
        glfwPollEvents()
    }

    glDisableVertexAttribArray(0)
    glDisableVertexAttribArray(1)
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glDeleteBuffers(posVboId)
    glDeleteBuffers(idxVboId)
    glDeleteBuffers(colVboId)
    glBindVertexArray(0)
    glDeleteVertexArrays(vaoId)
    glUseProgram(0)
    glDeleteProgram(programId)
    glDeleteShader(vertexShaderId)
    glDeleteShader(fragmentShaderId)
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)
    glfwTerminate()
    glfwSetErrorCallback(null).free()

}