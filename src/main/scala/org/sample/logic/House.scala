package org.sample.logic

import org.sample.graph.Mesh

class House extends GameObject {
    private val vertices = Array(
        // base
        -0.5f, 0.5f,  0.5f,
        -0.5f, -0.5f, 0.5f,
        0.5f, -0.5f,  0.5f,
        0.5f, 0.5f,   0.5f,
        -0.5f, 0.5f,  -0.5f,
        -0.5f, -0.5f, -0.5f,
        0.5f, -0.5f,  -0.5f,
        0.5f, 0.5f,   -0.5f,

        //roof
        0.0f, 0.7f,   0.5f,
        0.0f, 0.7f,   -0.5f
    )

    private val colors = Array(
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        1.0f, 0.0f, 0.0f,

        0.8f, 0.0f, 0.0f,
        0.0f, 0.8f, 0.0f,
        0.0f, 0.8f, 0.0f,
        0.8f, 0.0f, 0.0f,

        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 0.8f
    )

    private val indices = Array(
        //front
        0, 1, 3,
        3, 1, 2,
        8, 0, 3,
        
        //right
        3, 2, 7,
        7, 2, 6,
        9, 8, 3,
        9, 3, 7,

        //left
        0, 4, 5,
        0, 5, 1,
        8, 9, 4,
        8, 4, 0,

        //back
        4, 7, 6,
        4, 6, 5,
        9, 7, 4,

        //bottom
        1, 5, 2,
        2, 5, 6
    )   
        
    override def init(): Unit = {
         val mesh = new Mesh(vertices, indices, colors)
         super.init(mesh)
    }
}
