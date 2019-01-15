package com.strikalov.pogoda4.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

public class ButtonBehavior extends CoordinatorLayout.Behavior<FloatingActionButton>{

    private float initialY;
    private boolean firstMove = true;   // Нам надо получить начальное значение X только один раз

    // конструктор должен быть именно такой
    public ButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child,
                                   @NonNull View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent,
                                          @NonNull FloatingActionButton child, @NonNull View dependency) {

        // инициализация начального значения X
        if (firstMove) {
            firstMove = false;
            initialY = child.getY();
        }

        // изображение будет перемещаться в нижней части экрана
        child.setY(initialY + dependency.getY());
        return false;
    }

}
