#version 330 core

in vec3 EntryPoint;
in vec2 UV;
 
uniform float     StepSize;
uniform vec2      ScreenSize;
uniform vec3      boundingBox;
uniform vec3      stepRatio;
uniform sampler2D exitPoints;
uniform sampler3D VolumeTex;
uniform sampler1D TransferFunc;
uniform sampler3D gradients;
uniform sampler2D depthTex;
uniform mat4 MVPInv;
uniform mat4 V;
uniform mat4 normalMatrix;

vec3 backprojectDepth(vec2 wpos);

void main()
{   
    vec2 wpos = gl_FragCoord.st/ScreenSize;
    vec3 exitPoint = texture(exitPoints, wpos).xyz;
    
    // empty space skipping
    if (EntryPoint == exitPoint){
        gl_FragColor = vec4(1.0, 0, 0 , 1.0);
    }else{

    
    // get depth
    vec3 geometryIntersect = backprojectDepth(wpos);
    float stopLength = distance(geometryIntersect, EntryPoint);

    vec3 dir = (exitPoint - EntryPoint);
    //float stopLength = length(dir);
    vec3 dirN = normalize(dir);
    
    vec3 deltaDir = dirN * StepSize;
    float deltaDirLen = length(deltaDir);
    
    // jittering
    //float random = 1* fract(sin(gl_FragCoord.x * 12.9898 + gl_FragCoord.y * 78.233) * 43758.5453); 
    float random = 0;
    vec3 voxelCoord = EntryPoint;// * stepRatio + deltaDir * random;

    vec4 colorAcum = vec4(0.0); // The dest color
    float alphaAcum = 0.0;

    float intensity;
    float lengthAcum = 0.0;
    vec4 sample;
    vec3 gradient;

    vec4 bgColor = vec4(1.0);

    vec3 viewSpacePosition;
    vec3 viewSpaceNormal;

    for(int i = 0; i < 2000; i++)
    {   
        vec3 voxel = voxelCoord/boundingBox;
    	intensity =  texture(VolumeTex, voxel).x;
        //gradient = normalize(texture(gradients, voxelCoord).xyz);
        sample = texture(TransferFunc, intensity);
        
        //viewSpacePosition = voxelCoord;
        //viewSpaceNormal = normalize(( normalMatrix* vec4(gradient, 0.0)).xyz);

        if (sample.a > 0.0) {
            // correction
    	    //sample.a = 1.0 - pow(1.0 - sample.a, StepSize*1000.0f);
            //sample.rbg = phong(sample.rgb, viewSpaceNormal, viewSpacePosition);
    	    
            colorAcum.rgb = colorAcum.rbg + (1.0 - colorAcum.a) * sample.rgb * sample.a ;
            colorAcum.a = colorAcum.a + (1.0 - colorAcum.a) * sample.a;
    	}

        voxelCoord += deltaDir;
    	lengthAcum += deltaDirLen;
        
        if (lengthAcum >= stopLength ) {	
    	    //colorAcum.rgb = colorAcum.rgb*colorAcum.a + (1 - colorAcum.a)*bgColor.rgb;		
    	    break;  	
    	}else if (colorAcum.a > 1.0) {
    	    colorAcum.a = 1.0;
    	    break;
    	}
    }

    gl_FragColor = colorAcum;
    //gl_FragColor = vec4(dir, 1.0);
    //gl_FragColor = vec4(EntryPoint, 1.0);
    //gl_FragColor = vec4(stopLength, stopLength, stopLength, 1.0);
    }
}

vec3 backprojectDepth(vec2 wpos){
    vec4 point;
    point.xy = wpos;
    point.z = texture(depthTex, wpos).x;
    point.w = 1.0;
    
    point = point * 2.0 - 1.0;

    vec4 a = MVPInv * point;
    return a.xyz / a.w;
}