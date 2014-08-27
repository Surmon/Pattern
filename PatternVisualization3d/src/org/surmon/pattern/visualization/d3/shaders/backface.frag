#version 330 core

in vec3 vColor;

layout(location = 0) out vec4 vFragColor;	//fragment shader output

void main()
{
	vFragColor = vec4(vColor, 1);
}