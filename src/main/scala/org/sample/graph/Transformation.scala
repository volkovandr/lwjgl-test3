package org.sample.graph

import org.joml.Matrix4f
import org.sample.helpers.Settings
import org.joml.Vector3f

object Transformation {
    private val projectionMatrix = new Matrix4f()
    private val worldMatrix = new Matrix4f()

    def projectionMatrix(fieldOfView: Float, zNear: Float, zFar: Float): Matrix4f = {
        projectionMatrix.identity()
        projectionMatrix.perspective(
            fieldOfView, 
            Settings.width.toFloat / Settings.height,
            zNear,
            zFar
        )
        projectionMatrix
    }

    def worldMatrix(offset: Vector3f, rotation: Vector3f, scale: Float): Matrix4f = {
        worldMatrix
            .identity()
            .translate(offset)
            .rotateX(Math.toRadians(rotation.x).toFloat)
            .rotateY(Math.toRadians(rotation.y).toFloat)
            .rotateZ(Math.toRadians(rotation.z).toFloat)
            .scale(scale)
        worldMatrix
    }
}
