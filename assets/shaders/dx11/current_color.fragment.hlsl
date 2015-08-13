#include "ShaderConstants.fxh"

// be sure to change the shader type to pertex shader and shader model to ps_40 (in visual studio)

struct PS_Input
{
    float4 position : SV_Position;
};

struct PS_Output
{
    float4 color : SV_Target;
};

void main( in PS_Input PSInput, out PS_Output PSOutput )
{
    PSOutput.color = CURRENT_COLOR;
}