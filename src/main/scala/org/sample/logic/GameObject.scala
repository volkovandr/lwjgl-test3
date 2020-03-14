package org.sample.logic

import org.sample.graph.Mesh
import org.joml.Vector3f

class GameObject {
    def init(): Unit = ???

    private var _mesh: Option[Mesh] = None
    def init(mesh: Mesh): Unit = _mesh = Some(mesh)
    def mesh: Mesh = _mesh.getOrElse(throw new RuntimeException("Mesh not defined"))

    private var _position = new Vector3f(0, 0, 0)
    def position: Vector3f = _position
    def moveTo(x: Float, y: Float, z: Float): Unit = _position = new Vector3f(x, y, z)
    def moveBy(dx: Float, dy: Float, dz: Float): Unit = _position.add(dx, dy, dz)

    private var _rotation = new Vector3f(0, 0, 0)
    def rotation: Vector3f = _rotation
    def rotateTo(x: Float, y: Float, z: Float): Unit = _rotation = new Vector3f(x, y, z)
    def rotateBy(dx: Float, dy: Float, dz: Float): Unit = _rotation.add(dx, dy, dz)

    private var _scale = 1.0f
    def scale: Float = _scale
    def scaleTo(s: Float): Unit = _scale = s
    def scaleBy(s: Float): Unit = _scale *= s

    def cleanup(): Unit = _mesh.foreach(_.cleanup())
}
