package org.sample.graph

import org.joml.Vector3f

class Camera {
    private val _position = new Vector3f(0.0f, 0.0f, 0.0f)
    private val _rotation = new Vector3f(0.0f, 0.0f, 0.0f)

    def position: Vector3f = _position
    def rotation: Vector3f = _rotation

    def moveTo(x: Float, y: Float, z: Float): Unit = {
        _position.x = x
        _position.y = y
        _position.z = z
    }

    def moveByLocal(dx: Float, dy: Float, dz: Float): Unit = {
        if(dx != 0) {
            _position.x += Math.sin(Math.toRadians(_rotation.y - 90)).asInstanceOf[Float] * dx * -1.0f
            _position.z += Math.cos(Math.toRadians(_rotation.y - 90)).asInstanceOf[Float] * dx
        }
        if(dz != 0) {
            _position.x += Math.sin(Math.toRadians(_rotation.y)).asInstanceOf[Float] * dz * -1.0f
            _position.z += Math.cos(Math.toRadians(_rotation.y)).asInstanceOf[Float] * dz
        }
        _position.y += dy
    }

    def moveByGlobal(dx: Float, dy: Float, dz: Float): Unit = {
        _position.x += dx
        _position.y += dy
        _position.z += dz
    }

    def rotateTo(dx: Float, dy: Float, dz: Float): Unit = {
        _rotation.x = dx
        _rotation.y = dy
        _rotation.z = dz
    }

    def rotateBy(dx: Float, dy: Float, dz: Float): Unit = {
        _rotation.x += dx
        _rotation.y += dy
        _rotation.z += dz
    }
}