package com.joe.preview.ui.custom;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class FlipAnimation extends Animation {

    private float fromDegrees;
    private float toDegrees;
    private float centerX;
    private float centerY;
    private Camera camera;

    FlipAnimation(float fromDegrees, float toDegrees, float centerX, float centerY) {
        this.fromDegrees = fromDegrees;
        this.toDegrees = toDegrees;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float fromDegrees = this.fromDegrees;
        float degrees = fromDegrees + ((toDegrees - fromDegrees) * interpolatedTime);

        float centerX = this.centerX;
        float centerY = this.centerY;

        Camera camera = this.camera;

        Matrix matrix = t.getMatrix();

        camera.save();
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.postTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }

}
