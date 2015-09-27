package hackgt.directionapp;

import android.content.Context;
import android.location.Location;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Ojan on 9/26/2015.
 */
public class TurnArrow extends ImageView {
    public static final double APPROACH_DISTANCE = 200;

    private Location location;
    private float scale;

    public TurnArrow(Context context) {
        super(context);
        setScaleX(0f);
        setScaleY(0f);
    }

    public TurnArrow(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleX(0f);
        setScaleY(0f);
    }

    public TurnArrow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleX(0f);
        setScaleY(0f);
    }

    public TurnArrow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setScaleX(0f);
        setScaleY(0f);
    }


    public void approachArrow(Location newLocation) {
        if (shouldBeDisplayed(newLocation)) {
            scale = (float) (1 -
                    (location.distanceTo(newLocation))
                            / APPROACH_DISTANCE);
            setScaleX(scale);
            setScaleY(scale);
        }
    }

    public boolean shouldBeDisplayed(Location curLocation) {
        if (location != null && curLocation != null) {
            if (location.distanceTo(curLocation) < APPROACH_DISTANCE) {
                return true;
            }
        }
        return false;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public float getScale() {
        return scale;
    }
}
