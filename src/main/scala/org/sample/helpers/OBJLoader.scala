package org.sample.helpers

import org.sample.graph.Mesh
import scala.io.Source
import org.joml.Vector3f
import org.joml.Vector2f

object OBJLoader {
    def loadMesh(fileName: String): Mesh = {
        val vertices = scala.collection.mutable.ListBuffer[Vector3f]()
        val texCoords = scala.collection.mutable.ListBuffer[Vector2f]()
        val normals = scala.collection.mutable.ListBuffer[Vector3f]()
        val faces = scala.collection.mutable.ListBuffer[Face]()
        
        val source = Source.fromFile(fileName)
        source.getLines().foreach(line => {
            val tokens = line.split("\\s+")
            tokens(0) match {
                case "v" => vertices.append(new Vector3f(tokens(1).toFloat, tokens(2).toFloat, tokens(3).toFloat))
                case "vt" => texCoords.append(new Vector2f(tokens(1).toFloat, tokens(2).toFloat))
                case "vn" => normals.append(new Vector3f(tokens(1).toFloat, tokens(2).toFloat, tokens(3).toFloat))
                case "f" => faces.append(Face(tokens(1), tokens(2), tokens(3)))
                case _ => {}
            }
        })
    }

    case class IdxGroup(var idxPos: Int = IdxGroup.NO_VALUE, var idxTexPos: Int = IdxGroup.NO_VALUE, var idxNormal: Int = IdxGroup.NO_VALUE) 

    object IdxGroup {
        val NO_VALUE: Int = -1
        def apply(v: String): IdxGroup = {
            val idxGroup = new IdxGroup()
            val tokens = v.split("/")
            idxGroup.idxPos = tokens(0).toInt - 1
            if(tokens.length > 1 && tokens(1).length() > 0) {
                idxGroup.idxTexPos = tokens(1).toInt - 1
            }
            if(tokens.length > 2 && tokens(2).length() > 0) {
                idxGroup.idxNormal = tokens(2).toInt - 1
            }
            idxGroup
        }
    }

    case class Face(indices: Array[IdxGroup])

    object Face {
        def apply(v1: String, v2: String, v3: String): Face = new Face(Array(IdxGroup(v1), IdxGroup(v2), IdxGroup(v3)))
    }
}