#pragma version(1)
#pragma rs java_package_name(com.example.jonathan.imageed)



rs_allocation origin;


int w_origin;
int h_origin;
int left;
int top;
int right;
int bottom;
float zoom;

//Script de zoom par interpolation


void root(const uchar4* in,uchar4* out,uint32_t x,uint32_t y) {
  int xO,yO;
  float xAbs,yAbs,dX,dY;

    xAbs = (float)(x)/zoom;
    yAbs = (float)(y)/zoom;
    xO = (int)(xAbs);
    yO = (int)(yAbs);

    float4 ouF = {0.0f,0.0f,0.0f,0.0f};
    if((xO+1)<w_origin && (yO+1)<h_origin)
        {
          //Operation d'interpolation

                //ouF = ((1-(xAbs - xO))*(1-(yAbs - yO)))*(rsUnpackColor8888(rsGetElementAt_uchar4(origin,xO,yO))) + ((1-(xAbs - xO))*(yAbs - yO))*(rsUnpackColor8888(rsGetElementAt_uchar4(origin,xO+1,yO))) + ((1-(yAbs - yO))*(xAbs - xO))*(rsUnpackColor8888(rsGetElementAt_uchar4(origin,xO,yO+1))) + (xAbs - xO)*(yAbs - yO)*(rsUnpackColor8888(rsGetElementAt_uchar4(origin,xO+1,yO+1)));
                //ouF = convert_float4(rsGetElementAt_uchar4(origin,xO,yO));
                //*out = convert_uchar4(((1-(xAbs - xO))*(1-(yAbs - yO)))*(convert_float4(rsGetElementAt_uchar4(origin,xO,yO))) + ((1-(xAbs - xO))*(yAbs - yO))*(convert_float4((rsGetElementAt_uchar4(origin,xO+1,yO))) + ((1-(yAbs - yO))*(xAbs - xO))*(convert_float4((rsGetElementAt_uchar4(origin,xO,yO+1))) + (xAbs - xO)*(yAbs - yO)*(convert_float4((rsGetElementAt_uchar4(origin,xO+1,yO+1)));
                *out = convert_uchar4(((1-(xAbs - xO))*(1-(yAbs - yO)))*(convert_float4(rsGetElementAt_uchar4(origin,xO,yO))) + ((1-(xAbs - xO))*(yAbs - yO))*(convert_float4((rsGetElementAt_uchar4(origin,xO+1,yO)))) + ((1-(yAbs - yO))*(xAbs - xO))*(convert_float4((rsGetElementAt_uchar4(origin,xO,yO+1))) ) + (xAbs - xO)*(yAbs - yO)*(convert_float4((rsGetElementAt_uchar4(origin,xO+1,yO+1)))));

        }
        else
        {

            *out = *in;
        }
}


