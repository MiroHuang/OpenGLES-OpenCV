attribute vec4 position;
attribute vec4 inputTextureCoordinate;
uniform   mat4 uPosMtx;
uniform   mat4 uTexMtx;
varying   vec2 textureCoordinate;
void main() {
    gl_Position = uPosMtx * position;
    textureCoordinate   = (uTexMtx * inputTextureCoordinate).xy;
}

//attribute vec4 vPosition;
//uniform mat4 vMatrix;
//attribute vec2 vCoordinate;
//varying vec2 textureCoordinate;
//void main() {
//    gl_Position = vMatrix * vPosition;
//    textureCoordinate = vCoordinate;
//}

//attribute：使用顶点数组封装每个顶点的数据，一般用于每个顶点都各不相同的变量，如顶点位置、颜色等
//uniform：顶点着色器使用的常量数据，不能被着色器修改，一般用于对同一组顶点组成的单个3D物体中所有顶点都有相同的变量，如当前光源位置
//sampler：这是可选的，一种特殊的uniform，表示顶点着色器使用的纹理
//mat4：表示4x4浮点数矩阵，该变量存储了组合模型视图和投影矩阵
//vec4：表示包含了4个浮点数的向量
//varying：用于从顶点着色器传递到片元或FragmentsShader传递到下一步的输出变量
//uPosMtx * position：通过4x4的变换位置后，输出给gl_Position，gl_Position是顶点着色器内置的输出变量。
