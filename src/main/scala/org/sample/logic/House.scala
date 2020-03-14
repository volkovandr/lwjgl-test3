package org.sample.logic

import org.sample.graph.Mesh

class House extends GameObject {
    private val vertices = Array(
        -0.5f, 0.5f,  0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f,  0.0f,
        0.5f, 0.5f,   0.0f,
        0.0f, 0.7f,   0.0f,
        
        0.45f, 0.65f, 0.0f,
        0.40f, 0.65f, 0.0f,
        0.40f, 0.5f,  0.0f,
        0.45f, 0.5f,  0.0f
    )

    private val colors = Array(
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f,
        
        0.0f, 0.0f, 0.5f,
        0.0f, 0.0f, 0.5f,
        0.0f, 0.0f, 0.5f,
        0.0f, 0.0f, 0.5f
    )

    private val indices = Array(
        0, 1, 3,
        3, 1, 2,
        4, 0, 3,
        5, 6, 7,
        5, 7, 8
    )   
        
    override def init(): Unit = {
         val mesh = new Mesh(vertices, indices, colors)
         super.init(mesh)
    }
}
