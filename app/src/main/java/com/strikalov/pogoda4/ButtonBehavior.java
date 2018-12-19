package com.strikalov.pogoda4;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

public class ButtonBehavior extends CoordinatorLayout.Behavior<FloatingActionButton>{

    private float initialX;         // начальное значение X изображения, от нее и будем отталкиваться
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
            initialX = child.getX();
            initialY = child.getY();
        }

        // изображение будет перемещаться в нижней части экрана
        child.setX(initialX + dependency.getY() * 0.46f);
        child.setY(initialY + dependency.getY());
        return false;
    }

}
