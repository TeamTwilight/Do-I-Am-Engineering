#version 150

#moj_import <fog.glsl>

#define M_PI 3.1415926535897932384626433832795

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float GameTime;

const vec3 topColors = vec3(0.0, 1.0, 0.0);
const vec3 bottomColors = vec3(0.0, 0.0, 1.0);

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;

out vec4 fragColor;

vec3 sqerp(vec3 a, vec3 b, float factor) {
    return sqrt(mix(a * a, b * b, factor));
}

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    if (color.a < 0.1) {
        discard;
    }

    float colorShift = sin((2 * M_PI) * (color.r + color.g + color.b + GameTime * 16.0 + normal.y * 0.5));

    fragColor = vec4(sqerp(bottomColors, topColors, colorShift * colorShift), color.a);
}
