package com.wesine.device_sdk.utils;

import android.content.Context;
import android.opengl.GLES20;


/**
 * Created by doug on 18-2-27.
 */

public class ShaderProgram {
    public final String TAG = getClass().getSimpleName();

    protected final int program;

    protected final Context context;

    protected ShaderProgram(Context context, int vertexId, int fragId) {
        this.context = context;
        program = ShaderHelper.buildProgram(ResourceUtils.readText(context, vertexId),
                ResourceUtils.readText(context, fragId));
    }

    public void useProgram() {
        GLES20.glUseProgram(program);
    }
}
