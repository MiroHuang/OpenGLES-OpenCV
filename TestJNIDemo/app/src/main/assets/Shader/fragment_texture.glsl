#extension GL_OES_EGL_image_external : require
// 设置精度限定符
precision mediump float;
// 纹理
uniform samplerExternalOES uSampler;
// 当前要采集像素的点
varying mediump vec2 textureCoordinate;

uniform int width;
uniform int height;
// 磨皮程度(由低到高: 0.5 ~ 0.99)
uniform float opacity;

void test1() {
    vec4 tc = texture2D(uSampler, textureCoordinate);
    vec3 centralColor = tc.rgb;

    if(opacity < 0.01) {
        gl_FragColor = vec4(centralColor, 1.0);
    } else {
        float x_a = float(width);
        float y_a = float(height);

        float mul_x = 2.0 / x_a;
        float mul_y = 2.0 / y_a;
        vec2 blurCoordinates0 = textureCoordinate + vec2(0.0 * mul_x, -10.0 * mul_y);
        vec2 blurCoordinates2 = textureCoordinate + vec2(8.0 * mul_x, -5.0 * mul_y);
        vec2 blurCoordinates4 = textureCoordinate + vec2(8.0 * mul_x, 5.0 * mul_y);
        vec2 blurCoordinates6 = textureCoordinate + vec2(0.0 * mul_x, 10.0 * mul_y);
        vec2 blurCoordinates8 = textureCoordinate + vec2(-8.0 * mul_x, 5.0 * mul_y);
        vec2 blurCoordinates10 = textureCoordinate + vec2(-8.0 * mul_x, -5.0 * mul_y);

        mul_x = 1.8 / x_a;
        mul_y = 1.8 / y_a;
        vec2 blurCoordinates1 = textureCoordinate + vec2(5.0 * mul_x, -8.0 * mul_y);
        vec2 blurCoordinates3 = textureCoordinate + vec2(10.0 * mul_x, 0.0 * mul_y);
        vec2 blurCoordinates5 = textureCoordinate + vec2(5.0 * mul_x, 8.0 * mul_y);
        vec2 blurCoordinates7 = textureCoordinate + vec2(-5.0 * mul_x, 8.0 * mul_y);
        vec2 blurCoordinates9 = textureCoordinate + vec2(-10.0 * mul_x, 0.0 * mul_y);
        vec2 blurCoordinates11 = textureCoordinate + vec2(-5.0 * mul_x, -8.0 * mul_y);

        mul_x = 1.6 / x_a;
        mul_y = 1.6 / y_a;
        vec2 blurCoordinates12 = textureCoordinate + vec2(0.0 * mul_x,-6.0 * mul_y);
        vec2 blurCoordinates14 = textureCoordinate + vec2(-6.0 * mul_x,0.0 * mul_y);
        vec2 blurCoordinates16 = textureCoordinate + vec2(0.0 * mul_x,6.0 * mul_y);
        vec2 blurCoordinates18 = textureCoordinate + vec2(6.0 * mul_x,0.0 * mul_y);

        mul_x = 1.4 / x_a;
        mul_y = 1.4 / y_a;
        vec2 blurCoordinates13 = textureCoordinate + vec2(-4.0 * mul_x,-4.0 * mul_y);
        vec2 blurCoordinates15 = textureCoordinate + vec2(-4.0 * mul_x,4.0 * mul_y);
        vec2 blurCoordinates17 = textureCoordinate + vec2(4.0 * mul_x,4.0 * mul_y);
        vec2 blurCoordinates19 = textureCoordinate + vec2(4.0 * mul_x,-4.0 * mul_y);

        float central;
        float gaussianWeightTotal;
        float sum;
        float sampler;
        float distanceFromCentralColor;
        float gaussianWeight;

        float distanceNormalizationFactor = 3.6;

        central = texture2D(uSampler, textureCoordinate).g;
        gaussianWeightTotal = 0.2;
        sum = central * 0.2;

        sampler = texture2D(uSampler, blurCoordinates0).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates1).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates2).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates3).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates4).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates5).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates6).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates7).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates8).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates9).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates10).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates11).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates12).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates13).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates14).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates15).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates16).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates17).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates18).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sampler = texture2D(uSampler, blurCoordinates19).g;
        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);
        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);
        gaussianWeightTotal += gaussianWeight;
        sum += sampler * gaussianWeight;

        sum = sum/gaussianWeightTotal;

        sampler = centralColor.g - sum + 0.5;

        // 高反差保留
        for(int i = 0; i < 5; ++i) {
            if(sampler <= 0.5) {
                sampler = sampler * sampler * 2.0;
            } else {
                sampler = 1.0 - ((1.0 - sampler)*(1.0 - sampler) * 2.0);
            }
        }

        float aa = 1.0 + pow(sum, 0.3) * 0.09;
        vec3 smoothColor = centralColor * aa - vec3(sampler) * (aa - 1.0);
        smoothColor = clamp(smoothColor, vec3(0.0), vec3(1.0));

        smoothColor = mix(centralColor, smoothColor, pow(centralColor.g, 0.33));
        smoothColor = mix(centralColor, smoothColor, pow(centralColor.g, 0.39));

        smoothColor = mix(centralColor, smoothColor, opacity);

        gl_FragColor = vec4(pow(smoothColor, vec3(0.96)), 1.0);
    }
}

void main() {
    test1();
}

//varying：用于从顶点着色器传递到片元或FragmentsShader传递到下一步的输出变量
//gl_FragColor：片元着色器内置的输出变量