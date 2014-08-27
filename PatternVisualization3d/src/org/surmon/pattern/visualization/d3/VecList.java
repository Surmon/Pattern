/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.d3;

import cz.pattern.jglm.Vec;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 *
 * @author palasjiri
 */
public class VecList extends ArrayList<Vec> {

    public FloatBuffer getBuffer() {
        FloatBuffer buffer;

        if (!isEmpty()) {
            int count = this.size() * 3;
            buffer = FloatBuffer.allocate(count);
            for (Vec vec : this) {
                buffer.put(vec.getBuffer());
            }
            buffer.rewind();
            return buffer;
        } else {
            return FloatBuffer.allocate(0);
        }
    }

    public int sizeOf() {
        if (!isEmpty()) {
            return this.size() * 3 * Float.SIZE;
        } else {
            return 0;
        }
    }

}
