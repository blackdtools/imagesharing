package net.blackdevelopers.imagesharing;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SquareCheckBox extends androidx.appcompat.widget.AppCompatCheckBox {
    public SquareCheckBox(@NonNull Context context) {
        super(context);
    }

    public SquareCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareCheckBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        //noinspection SuspiciousNameCombination
        setMeasuredDimension(width, width);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
    }
}
