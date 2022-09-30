#ifdef GL_ES
precision mediump float;
#endif

#extension GL_OES_standard_derivatives : enable

#define RGB(r, g, b) vec3(r / 255.0, g / 255.0, b / 255.0)
const vec3 WHITE = vec3(1.0, 1.0, 1.0);
const vec3 BLUE = RGB(85.0, 205.0, 252.0);
const vec3 PINK = RGB(247.0, 168.0, 184.0);

uniform float time;
uniform vec2 resolution;

vec3 band(vec2 pos) {
    float y = abs(pos.y) - 0.5;
    if (y <= 0.0) return WHITE;
    if (y <= 1.0) return PINK;
    if (y <= 2.0) return BLUE;
}

void main() {
    vec2 position = (gl_FragCoord.xy / resolution.xy * 5.0) - vec2(0.0, 2.5);
    gl_FragColor = vec4(band(position + vec2(0.0, sin(position.x + time))), 1.0);
}