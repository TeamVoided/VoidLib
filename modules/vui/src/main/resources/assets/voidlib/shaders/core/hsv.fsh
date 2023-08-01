#version 150

uniform vec4 ColorModulator;

in vec4 vertexColor;
out vec4 fragColor;

vec3 hsv2rgb(vec3 hsv) {
    vec4 k = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(hsv.xxx + k.xyz) * 6.0 - k.www);
    return hsv.z * mix(k.xxx, clamp(p - k.xxx, 0.0, 1.0), hsv.y);
}

void main() {
    fragColor = vec4(hsv2rgb(vertexColor.xyz).xyz, vertexColor.w) * ColorModulator;
}