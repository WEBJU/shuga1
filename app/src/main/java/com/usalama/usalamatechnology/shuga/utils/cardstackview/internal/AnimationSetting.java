package com.usalama.usalamatechnology.shuga.utils.cardstackview.internal;

import android.view.animation.Interpolator;

import com.usalama.usalamatechnology.shuga.utils.cardstackview.Direction;

public interface AnimationSetting {
    Direction getDirection();
    int getDuration();
    Interpolator getInterpolator();
}
