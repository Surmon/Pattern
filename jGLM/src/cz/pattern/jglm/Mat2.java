/*
 * Copyright 2014 palasjiri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.pattern.jglm;

/**
 *
 * @author palasjiri
 */
public class Mat2 {

    final float m00, m10;
    final float m01, m11;

    /**
     * Creates a matrix with all elements equal to ZERO.
     */
    public Mat2() {
        m00 = m10 = 0f;
        m01 = m11 = 0f;
    }
    
    
}
