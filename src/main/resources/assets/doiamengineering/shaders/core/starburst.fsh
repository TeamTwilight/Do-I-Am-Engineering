#version 150

uniform float GameTime;

in vec2 texCoord;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    float angle = atan(texCoord.y, texCoord.x);
    float ray = clamp(sin(angle * 9.0 + GameTime * 2500.0) * 5.0, 0.0, 1.0);
    float fade = clamp(1.0 - length(texCoord * 1.5), 0.0, 1.0);

    fragColor = vec4(vertexColor.rgb * 0.6666 + 0.3333, min(ray * vertexColor.a, fade));
}
