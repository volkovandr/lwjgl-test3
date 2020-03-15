#version 330

in vec2 outTexCoord;
out vec4 FragColor;

uniform sampler2D texture_sampler;

void main()
{
	FragColor = texture(texture_sampler, outTexCoord);
}