package org.sample

import org.lwjgl.glfw.GLFW.GLFW_TRUE
import org.lwjgl.glfw.GLFW.GLFW_FALSE
import org.lwjgl.glfw.GLFW.GLFW_RESIZABLE
import org.lwjgl.glfw.GLFW.GLFW_VISIBLE
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import org.lwjgl.system.MemoryUtil.NULL

import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWVidMode

import org.lwjgl.glfw.GLFW.glfwSetErrorCallback
import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFW.glfwTerminate
import org.lwjgl.glfw.GLFW.glfwDefaultWindowHints
import org.lwjgl.glfw.GLFW.glfwWindowHint
import org.lwjgl.glfw.GLFW.glfwCreateWindow
import org.lwjgl.glfw.GLFW.glfwDestroyWindow
import org.lwjgl.glfw.GLFW.glfwSetKeyCallback
import org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose
import org.lwjgl.glfw.GLFW.glfwGetVideoMode
import org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor
import org.lwjgl.glfw.GLFW.glfwSetWindowPos
import org.lwjgl.glfw.GLFW.glfwMakeContextCurrent
import org.lwjgl.glfw.GLFW.glfwSwapInterval
import org.lwjgl.glfw.GLFW.glfwShowWindow

object Window {
    private var errorCallback: Option[GLFWErrorCallback] = None
    private var keyCallback: Option[GLFWKeyCallback] = None
    private var windowHandle: Option[CustomTypes.WindowHandle] = None

    def window: Option[CustomTypes.WindowHandle] = windowHandle

    def init(): Unit = {
        glfwSetErrorCallback {
            val callback = GLFWErrorCallback.createPrint(System.err)
            errorCallback = Some(callback)
            callback
        }
        
        if(!glfwInit()) {
            throw new IllegalStateException("Failed initializing GLFW")
        }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)

        val handle = glfwCreateWindow(Settings.width, Settings.width, Settings.windowTitle, NULL, NULL)
        if(handle == NULL) {
            throw new RuntimeException("Failed creating wndow")
        }
        windowHandle = Some(handle)

        glfwSetKeyCallback(handle, {
            val callback = new GLFWKeyCallback() {
                override def invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int): Unit = {
                    if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                        glfwSetWindowShouldClose(window, true)
                    } else {
                        GameLogic.input(key, action, mods)
                    }
                }
            }
            keyCallback = Some(callback)
            callback
        })

        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        glfwSetWindowPos(handle, (vidmode.width() - Settings.width) / 2, (vidmode.height() - Settings.height) / 2)

        sys.addShutdownHook {
            windowHandle.foreach { handle => glfwSetWindowShouldClose(handle, true) }
        }

        glfwMakeContextCurrent(handle)
        glfwSwapInterval(1)
        glfwShowWindow(handle)
    }
    
    def cleanup(): Unit = {
        windowHandle.foreach { handle =>
            glfwDestroyWindow(handle)
            windowHandle = None
        }
        keyCallback.foreach { callback =>
            callback.close()
            keyCallback = None
        }
        errorCallback.foreach { callback => 
            callback.close()
            errorCallback = None
        }
        glfwTerminate
    }
}