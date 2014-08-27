/* Copyright (C) 2012 James L. Royalty
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
package cz.pattern.jglm.support;

import java.nio.ByteBuffer;


/**
 * @author James Royalty [Jan 26, 2013]
 */
public interface LinearType<T>
{
	int getNumElements();
	
	T add(T linType);
	
	T subtract(T linType);
	
	T multiply(T linType);
	
	T negate();
	
	Class<? extends Number> getElementType();
	
	ByteBuffer getRawData();
}
