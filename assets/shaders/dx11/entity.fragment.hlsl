#include "ShaderConstants.fxh"

struct PS_Input {
	float4 position : SV_Position;

	float4 light : LIGHT;
	float4 fogColor : FOG_COLOR;

#ifdef GLINT
	// there is some alignment issue on the Windows Phone 1320 that causes the position
	// to get corrupted if this is two floats and last in the struct memory wise
	float4 layerUV : GLINT_UVS;
#endif

#ifdef USE_OVERLAY
	float4 overlayColor : OVERLAY_COLOR;
#endif

	float2 uv : TEXCOORD_0;

};

struct PS_Output
{
    float4 color : SV_Target;
};

#ifdef USE_EMISSIVE
#define NEEDS_DISCARD(C)	(C.a + C.r + C.g + C.b == 0.0)
#else
#define NEEDS_DISCARD(C)	(C.a < 0.5)
#endif

float4 glintBlend(float4 dest, float4 source) {
	// glBlendFuncSeparate(GL_SRC_COLOR, GL_ONE, GL_ONE, GL_ZERO)
	return float4(source.rgb * source.rgb, source.a) + float4(dest.rgb, 0.0);
}

void main( in PS_Input PSInput, out PS_Output PSOutput )
{
    float4 color = float4( 1.0f, 1.0f, 1.0f, 1.0f );

#ifndef COLOR_BASED
        color = TEXTURE_0.Sample( TextureSampler0, PSInput.uv );

#ifdef ALPHA_TEST
        if( NEEDS_DISCARD( color ) )
        {
            discard;
        }
#endif
#endif

#ifdef USE_COLOR_MASK
	#ifdef GLINT
		// Applies color mask to glint texture instead and blends with original color
		float4 layer1 = TEXTURE_1.Sample(TextureSampler1, frac(PSInput.layerUV.xy)).rgbr * CHANGE_COLOR;
		float4 layer2 = TEXTURE_1.Sample(TextureSampler1, frac(PSInput.layerUV.zw)).rgbr * CHANGE_COLOR;
		float4 glint = (layer1 + layer2) * TILE_LIGHT_COLOR;
	#else
		color.rgb = lerp( color, color * CHANGE_COLOR, color.a ).rgb;
		color.a = 1.0;
	#endif
#endif

#ifdef USE_OVERLAY
        //use either the diffuse or the OVERLAY_COLOR
        color.rgb = lerp( color, PSInput.overlayColor, PSInput.overlayColor.a ).rgb;
#endif

#ifdef USE_EMISSIVE
        //make glowy stuff
        color *= lerp( float( 1.0 ).xxxx, PSInput.light, color.a );
#else
        color *= PSInput.light;
#endif

    //apply fog
    color.rgb = lerp( color.rgb, PSInput.fogColor.rgb, PSInput.fogColor.a );

#ifdef USE_COLOR_MASK
	#ifdef GLINT
		color = glintBlend(color, glint);
	#endif
#endif

    //WARNING do not refactor this 
    PSOutput.color = color;
}