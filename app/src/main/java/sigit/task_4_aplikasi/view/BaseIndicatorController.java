package sigit.task_4_aplikasi.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by sigit on 24/07/17.
 */

public abstract class BaseIndicatorController {
    private View mTarget;

    public abstract void createAnimation();

    public abstract void draw(Canvas canvas, Paint paint);

    public void setTarget(View target) {
        this.mTarget = target;
    }

    public View getTarget() {
        return this.mTarget;
    }

    public int getWidth() {
        return this.mTarget.getWidth();
    }

    public int getHeight() {
        return this.mTarget.getHeight();
    }

    public void postInvalidate() {
        this.mTarget.postInvalidate();
    }
}

