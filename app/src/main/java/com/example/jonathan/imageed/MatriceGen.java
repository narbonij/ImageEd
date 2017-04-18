package com.example.jonathan.imageed;

/**
 * Created by Jonathan on 15/04/2017.
 */

public class MatriceGen {


    public static float[][] laplacien()
    {
        float[][] mat = new float[3][3];
        for(int i =0;i<3;i++)
        {
            for(int j = 0;j<3;j++)
            {
                mat[i][j] = -1;
            }
        }


        mat[1][1] = 8;


        return mat;

    }

    public static float[][] sobel1()
    {
        float[][] mat = new float[3][3];
        mat[0][0] = -1;
        mat[0][1] = 0;
        mat[0][2] = 1;
        mat[1][0] = -2;
        mat[1][1] = 0;
        mat[1][2] = 2;
        mat[2][0] = -1;
        mat[2][1] = 0;
        mat[2][2] = 1;

        return mat;

    }

    public static float[][] sobel2()
    {
        float[][] mat = new float[3][3];
        mat[0][0] = -1;
        mat[0][1] = -2;
        mat[0][2] = -1;
        mat[1][0] = 0;
        mat[1][1] = 0;
        mat[1][2] = 0;
        mat[2][0] = 1;
        mat[2][1] = 2;
        mat[2][2] = 1;
        return mat;


    }

    public static float[][] moyenne(int taille)
    {
        float[][] mat = new float[taille][taille];
        for(int i =0;i<taille;i++)
        {
            for(int j = 0;j<taille;j++)
            {
                mat[i][j] = 1;
            }
        }

        return normalise(mat);
    }

    public static float[][] gaussien(int taille,float sigma)
    {
        float[][] mat = new float[taille][taille];
        for(int i =0;i<taille;i++)
        {
            for(int j = 0;j<taille;j++)
            {
                mat[i][j] = (1/(float)(Math.PI*sigma*sigma))*(float)Math.exp((-1)*(i*i+j*j)/(2*sigma*sigma));
            }
        }

        return normalise(mat);

    }

    protected static float[][] normalise(float[][] mat)
    {
        float total = 0;
        for(int i =0;i<mat.length;i++)
        {
            for(int j = 0 ;j<mat.length;j++)
            {
                total+=mat[i][j];
            }
        }
        if(total !=0)
        {
            for(int i =0;i<mat.length;i++)
            {
                for(int j = 0 ;j<mat.length;j++)
                {
                    mat[i][j] /=total;
                }
            }

        }
        return mat;

    }
}