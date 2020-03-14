package org.sample

import org.lwjgl.glfw.GLFW.GLFW_TRUE
import org.lwjgl.glfw.GLFW.GLFW_FALSE
import org.lwjgl.glfw.GLFW.GLFW_RESIZABLE
import org.lwjgl.glfw.GLFW.GLFW_VISIBLE
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR
import org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR
import org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE
import org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE
import org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT
import org.lwjgl.system.MemoryUtil.NULL

import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWVidMode

import org.lwjgl.opengl.GL11.glViewport
import org.lwjgl.glfw.GLFW.glfwSetErrorCallback
import org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback
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
import org.lwjgl.opengl.GL

object Window {
    private var errorCallback: Option[GLFWErrorCallback] = None
    private var keyCallback: Option[GLFWKeyCallback] = None
    private var windowHandle: Option[CustomTypes.WindowHandle] = None
    private var resized: Boolean = true

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
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

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

        glfwSetFramebufferSizeCallback(handle, (window, width, height) => {
            resized = true
            Settings.height = height
            Settings.width = width
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

    def resizeWindow(): Boolean =  if(resized) {
        glViewport(0, 0, Settings.width, Settings.height)
        resized = false
        true
    } else {
        false
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