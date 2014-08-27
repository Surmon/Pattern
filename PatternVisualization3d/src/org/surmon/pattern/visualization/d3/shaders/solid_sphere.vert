#version 330

layout(location = 0) in vec3 vVertex;
layout(location = 1) in vec3 vNormal;

uniform mat4 MVP;
uniform mat4 MV;
uniform mat4 normalMatrix;

out vec3 viewSpaceNormal; 
out vec3 viewSpacePosition;

void main()
{   
    // transform position and normal to view space
    viewSpacePosition = (MV * vec4(vVertex, 1.0)).xyz;
    viewSpaceNormal = normalize( (normalMatrix * vec4(vNormal, 0.0)).xyz );
    
    gl_Position = MVP * vec4(vVertex, 1);
}