/* Copyright (C) 2013 James L. Royalty
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.pattern.jglm;

import java.nio.FloatBuffer;

import cz.pattern.jglm.buffer.BufferAllocator;
import cz.pattern.jglm.buffer.BufferAllocatorFactory;
import cz.pattern.jglm.support.Compare;
import cz.pattern.jglm.support.FastMath;

/**
 * @author James Royalty 
 */
abstract class AbstractVec implements Vec {
	private static final BufferAllocator BUFFER_ALLOCATOR = BufferAllocatorFactory.getInstance();
	
	@Override
	public boolean equalsWithEpsilon(final Vec obj) {
		return equalsWithEpsilon(obj, Compare.VEC_EPSILON);
	}
	
	@Override
	public final float getLength() {
		return FastMath.sqrtFast( getLengthSquared() );
	}
	
	protected FloatBuffer allocateFloatBuffer() {
		return BUFFER_ALLOCATOR.allocateFloatBuffer( getDimensions() );
	}
}
