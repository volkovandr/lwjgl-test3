#version 330

in vec3 exColour;
out vec4 FragColor;

void main()
{
	FragColor = vec4(exColour, 1.0);
}