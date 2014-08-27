#version 330 core

vec3 light_position_world = vec3 (10.0, 10.0, 10.0);
const vec3 Ls = vec3 (1.0, 1.0, 1.0); // white specular colour
const vec3 Ld = vec3 (0.7, 0.7, 0.7); // dull white diffuse light colour
const vec3 La = vec3 (0.2, 0.2, 0.2); // grey ambient colour

// surface reflectance
const vec3 Ks = vec3 (1.0, 1.0, 1.0); // fully reflect specular light
const vec3 Kd = vec3 (1.0, 0.5, 0.0); // orange diffuse surface reflectance
const vec3 Ka = vec3 (1.0, 1.0, 1.0); // fully reflect ambient light
const float specular_exponent = 20.0; // specular 'power'

in vec3 viewSpaceNormal; 
in vec3 viewSpacePosition;

uniform mat4 V;
uniform vec3 color;

void main()
{   
   
    // ambient intensity
  vec3 Ia = La * color;

  // diffuse intensity
  vec3 viewSpaceLightPosition = vec3 (V * vec4 (light_position_world, 1.0));
  vec3 normal = normalize(viewSpaceNormal);
  vec3 directionToLight = normalize(viewSpaceLightPosition - viewSpacePosition);
  vec3 Id = Ld * color * max(0, dot(normal, directionToLight));
  
  // specular intensity
  //vec3 reflection_eye = reflect(-directionToLight, normal);
  //vec3 surface_to_viewer_eye = normalize(-viewSpacePosition);
  //float dot_prod_specular = dot (reflection_eye, surface_to_viewer_eye);
  //dot_prod_specular = max (dot_prod_specular, 0.0);
  //float specular_factor = pow (dot_prod_specular, specular_exponent);
  //vec3 Is = Ls * Ks * specular_factor;
  vec3 Is = vec3(0.0);

  // final colour
  gl_FragColor = vec4 (Is + Id + Ia, 1.0);
}