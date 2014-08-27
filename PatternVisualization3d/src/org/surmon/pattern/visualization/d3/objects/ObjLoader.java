/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.d3.objects;

import cz.pattern.jglm.Vec2;
import cz.pattern.jglm.Vec3;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.surmon.pattern.visualization.d3.VecList;

/**
 *
 * @author palasjiri
 */
public class ObjLoader {

    public static void load(File file, VecList out_vertices, List<Vec2> out_uvs, VecList out_normals) throws IOException {

        List<Integer> vertexIndices = new ArrayList<>();
        List<Integer> uvIndices = new ArrayList<>();
        List<Integer> normalIndices = new ArrayList<>();
        List<Vec3> temp_vertices = new ArrayList<>();
        List<Vec2> temp_uvs = new ArrayList<>();
        List<Vec3> temp_normals = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        StringTokenizer st;

        while ((line = br.readLine()) != null) {
            st = new StringTokenizer(line);

            String lineHeader = st.nextToken();

            switch (lineHeader) {
                case "v": {
                    float x = Float.valueOf(st.nextToken());
                    float y = Float.valueOf(st.nextToken());
                    float z = Float.valueOf(st.nextToken());
                    temp_vertices.add(new Vec3(x, y, z));
                    break;
                }
                case "vt": {
                    float x = Float.valueOf(st.nextToken());
                    float y = Float.valueOf(st.nextToken());
                    temp_uvs.add(new Vec2(x, y));
                    break;
                }
                case "vn": {
                    float x = Float.valueOf(st.nextToken());
                    float y = Float.valueOf(st.nextToken());
                    float z = Float.valueOf(st.nextToken());
                    temp_normals.add(new Vec3(x, y, z));
                    break;
                }
                case "f":
                    StringTokenizer ist;
                    int[] vertexIndex,
                     uvIndex,
                     normalIndex;
                    vertexIndex = new int[3];
                    uvIndex = new int[3];
                    normalIndex = new int[3];
                    ist = new StringTokenizer(st.nextToken(), "/");
                    int tokens = ist.countTokens();
                    vertexIndex[0] = Integer.valueOf(ist.nextToken());
                    if (tokens == 3) {
                        uvIndex[0] = Integer.valueOf(ist.nextToken());
                    }
                    normalIndex[0] = Integer.valueOf(ist.nextToken());
                    ist = new StringTokenizer(st.nextToken(), "/");
                    vertexIndex[1] = Integer.valueOf(ist.nextToken());
                    if (tokens == 3) {
                        uvIndex[1] = Integer.valueOf(ist.nextToken());
                    }
                    normalIndex[1] = Integer.valueOf(ist.nextToken());
                    ist = new StringTokenizer(st.nextToken(), "/");
                    vertexIndex[2] = Integer.valueOf(ist.nextToken());
                    if (tokens == 3) {
                        uvIndex[2] = Integer.valueOf(ist.nextToken());
                    }
                    normalIndex[2] = Integer.valueOf(ist.nextToken());
                    vertexIndices.add(vertexIndex[0]);
                    vertexIndices.add(vertexIndex[1]);
                    vertexIndices.add(vertexIndex[2]);
                    uvIndices.add(uvIndex[0]);
                    uvIndices.add(uvIndex[1]);
                    uvIndices.add(uvIndex[2]);
                    normalIndices.add(normalIndex[0]);
                    normalIndices.add(normalIndex[1]);
                    normalIndices.add(normalIndex[2]);
                    break;
            }

            for (Integer vertexIndex : vertexIndices) {
                Vec3 vertex = temp_vertices.get(vertexIndex - 1);
                out_vertices.add(vertex);
            }
            if (!temp_uvs.isEmpty()) {
                for (Integer uvIndex : uvIndices) {
                    Vec2 vec = temp_uvs.get(uvIndex - 1);
                    out_uvs.add(vec);
                }
            }

            for (Integer normalIndex : normalIndices) {
                Vec3 vec = temp_normals.get(normalIndex - 1);
                out_normals.add(vec);
            }
        }

    }

}
