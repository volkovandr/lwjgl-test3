package org.sample.graph

import org.sample.helpers.CustomTypes

import de.matthiasmann.twl.utils.PNGDecoder
import scala.io.Source
import java.nio.ByteBuffer
import de.matthiasmann.twl.utils.PNGDecoder.Format

import org.lwjgl.opengl.GL11.glGenTextures
import org.lwjgl.opengl.GL11.glDeleteTextures
import org.lwjgl.opengl.GL11.glBindTexture
import org.lwjgl.opengl.GL11.glPixelStorei
import org.lwjgl.opengl.GL11.glTexParameteri
import org.lwjgl.opengl.GL11.glTexImage2D
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.stb.STBImage.stbi_load
import org.lwjgl.stb.STBImage.stbi_failure_reason
import org.lwjgl.stb.STBImage.stbi_image_free

import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
// import org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER
// import org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER
// import org.lwjgl.opengl.GL11.GL_NEAREST
import org.lwjgl.opengl.GL11.GL_ALPHA
import org.lwjgl.opengl.GL11.GL_RGBA
import org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE
import org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT
import scala.util.Using
import org.lwjgl.system.MemoryStack

class Texture {
    
    def this(resourceName: String) = {
        this()
        loadFromResource(resourceName)
    }

    private var texture: Option[CustomTypes.TextureId] = None
    def textureId: CustomTypes.TextureId = texture.getOrElse(0)

    def loadFromResource(resourceName: String): Unit = {
        // Using(MemoryStack.stackPush()) { stack =>
        //     val w = stack.malloc(1)
        //     val h = stack.malloc(1)
        //     val channels = stack.malloc(1)
        //     val buf = stbi_load(getClass().getResource(resourceName).toString(), w, h, channels, 4)
        // }

        val decoder = new PNGDecoder(getClass().getResourceAsStream(resourceName))
        val buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight())
        decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA)
        buffer.flip()
        val textureId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, textureId)
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glGenerateMipmap(GL_TEXTURE_2D)
        texture = Some(textureId)
        println(s"Loaded texture from [$resourceName] ${decoder.getWidth()}x${decoder.getHeight()} ID=$textureId")
    }

    def cleanup(): Unit = texture.foreach(id => glDeleteTextures(id))
}
