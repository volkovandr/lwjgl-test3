package org.sample.logic

import org.sample.graph.Mesh
import org.sample.graph.Texture

class House extends GameObject {
    private val vertices = Array(
        //some of the vertices need to be duplicated otherwise the texture looks totally wrong
        //left-back
        -0.5f, 0.5f, -0.5f,   //0
        -0.5f, -0.5f, -0.5f,  //1
        //left-front
        -0.5f, 0.5f, 0.5f,    //2
        -0.5f, -0.5f, 0.5f,   //3
        //right-front
        0.5f, 0.5f, 0.5f,   //4
        0.5f, -0.5f, 0.5f,   //5
        //right-back
        0.5f, 0.5f, -0.5f,  //6
        0.5f, -0.5f, -0.5f, //7
        //left-back
        -0.5f, 0.5f, -0.5f,   //8
        -0.5f, -0.5f, -0.5f,  //9
        //back-bottom-left and right
        -0.5f, -0.5f, -0.5f, //10
        0.5f, -0.5f, -0.5f,  //11

        //roof-tops for the front and back
        0.0f, 0.8f, 0.5f,    //12
        0.0f, 0.8f, -0.5f,   //13

        //roof-tops for the right side
        0.0f, 0.8f, 0.5f,    //14
        0.0f, 0.8f, -0.5f,   //15

        //roof-tops for the left side
        0.0f, 0.8f, 0.5f,    //16
        0.0f, 0.8f, -0.5f    //17
    )

    private val texCoords = Array(
        // left-back
        0.0f, 0.5f, 
        0.0f, 0.75f, 
        // left-front
        0.25f, 0.5f, 
        0.25f, 0.75f, 
        // right-front
        0.5f, 0.5f, 
        0.5f, 0.75f,
        // right-back
        0.75f, 0.5f,
        0.750f, 0.75f,
        // left-back
        1.0f, 0.5f,
        1.0f, 0.75f,
        //back-bottom left and right
        0.25f, 1.0f,
        0.5f, 1.0f,
        //roof top front and back
        0.25f + 0.125f, 0.5f - 0.120f,
        0.75f + 0.125f, 0.5f - 0.120f,
        //roof tops for the right side, front and back
        0.5f, 0.5f - 0.120f,
        0.75f, 0.5f - 0.120f,
        //roof tops for the left side, front and back
        0.25f, 0.5f - 0.120f,
        0.0f, 0.5f - 0.120f
    )

    private val indices = Array(
        //left side
        2, 0, 1,
        2, 1, 3,
        //front side
        2, 3, 4,
        4, 3, 5,
        //right side
        4, 5, 6,
        6, 5, 7,
        //back side
        6, 7, 8,
        8, 7, 9,
        //bottom
        3, 10, 11,
        3, 11, 5,
        //roof front and back
        12, 2, 4,
        13, 6, 8,
        // roof right
        15, 14, 4,
        15, 4, 6,
        // roof left
        16, 17, 0,
        16, 0, 2
    )   
        
    override def init(): Unit = {
         val mesh = new Mesh(vertices, indices, texCoords, new Texture("/house.png"))
         super.init(mesh)
    }
}
