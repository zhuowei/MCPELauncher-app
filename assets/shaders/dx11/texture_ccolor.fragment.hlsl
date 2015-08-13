#include "ShaderConstants.fxh"

// be sure to change the shader type to pertex shader and shader model to ps_40 (in visual studio)

struct PS_Input
{
    float4 position : SV_Position;
    float2 uv : TEXCOORD_0;
};

struct PS_Output
{
    float4 color : SV_Target;
};

void main( in PS_Input PSInput, out PS_Output PSOutput )
{
    float4 diffuse = TEXTURE_0.Sample( TextureSampler0, PSInput.uv );


#ifdef ALPHA_TEST
    if( diffuse.a < 0.5 )
    {
        discard;
    }
#endif

    PSOutput.color = CURRENT_COLOR * diffuse;
}