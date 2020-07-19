package org.sample.graph

import org.joml.Matrix4f
import org.sample.helpers.Settings
import org.joml.Vector3f

object Transformation {
    private val projectionMatrix = new Matrix4f()
    private val worldMatrix = new Matrix4f()
    private val viewMatrix = new Matrix4f()
    private val modelViewMatix = new Matrix4f()

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

    def viewMatrix(camera: Camera): Matrix4f = {
        viewMatrix
            .identity()
            .rotate(Math.toRadians(camera.rotation.x).toFloat, new Vector3f(1.0f, 0.0f, 0.0f))
            .rotate(Math.toRadians(camera.rotation.y).toFloat, new Vector3f(0.0f, 1.0f, 0.0f))
            .rotate(Math.toRadians(camera.rotation.z).toFloat, new Vector3f(0.0f, 0.0f, 1.0f))
            .translate(-camera.position.x, -camera.position.y, -camera.position.z)

        viewMatrix
    }

    def modelViewMatrix(position: Vector3f, rotation: Vector3f, scale: Float, viewMatrix: Matrix4f): Matrix4f = {
        modelViewMatix
            .identity()
            .translate(position)
            .rotateX(Math.toRadians(rotation.x).toFloat)
            .rotateY(Math.toRadians(rotation.y).toFloat)
            .rotateZ(Math.toRadians(rotation.z).toFloat)
            .scale(scale)
        val view = new Matrix4f(viewMatrix)
        view.mul(modelViewMatix)
    }
}
