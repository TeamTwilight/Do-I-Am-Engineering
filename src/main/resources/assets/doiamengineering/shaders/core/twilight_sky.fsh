#version 150

#moj_import <fog.glsl>
#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float GameTime;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;
in vec4 texProj0;

out vec4 fragColor;

// background color
const vec3 sky = vec3( 40.0 / 255.0, 37.0 / 255.0, 63.0 / 255.0 );
// fog color
const vec3 fog = vec3( 0.39905882, 0.53, 0.46164703 );

mat4 end_portal_layer(float layer) {
    mat2 rotate = mat2_rotate_z(GameTime + radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));

    mat2 scale = mat2(layer);

    return mat4(scale * rotate);
}

void main() {
    vec4 texColor = texture(Sampler0, texCoord0);
    if (texColor.a < 0.1) {
        discard;
    }

    vec3 color = mix(fog, sky, normal.y * 0.5 + 0.5) + textureProj(Sampler1, texProj0 * end_portal_layer(0.65)).rgb;

    fragColor = vec4(color, texColor.a);
}
