package com.game.airfight.backend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;


public class FuselagePart {

    private Element fuselagePart = null;

    public RoundedBitmapDrawable getRoundedBitmapDrawable() {
        return roundedBitmapDrawable;
    }

    private RoundedBitmapDrawable roundedBitmapDrawable = null;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private boolean isActive = false;

    private Context context = null;


    public FuselagePart(Context _context, int squareSize, int _row, int _column, int _resourceDrawable) {

        context = _context;

        fuselagePart = new Element(new Coordinate(_row, _column), new Coordinate(_row, _column));

        Bitmap iconBm = BitmapFactory.decodeResource(context.getResources(), _resourceDrawable);
        Bitmap rescaled = Bitmap.createScaledBitmap(iconBm, squareSize, squareSize, true);
        RoundedBitmapDrawable _roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), rescaled);
        _roundedBitmapDrawable.setCornerRadius(dpToPixels(3));

        roundedBitmapDrawable = _roundedBitmapDrawable;
    }

    public boolean isPartOfMenuField(int _row, int _column) {

        return fuselagePart.isInRange(_row, _column);
    }

    private int dpToPixels(final float dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dip * scale + 0.5f);
    }
}
