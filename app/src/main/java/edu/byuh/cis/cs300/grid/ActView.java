package edu.byuh.cis.cs300.grid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class ActView extends View {

    private Paint blackLine;

    public ActView(Context c) {
        super(c);
        blackLine = new Paint();
        blackLine.setColor(Color.BLACK);
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        float w = getWidth();
        float h = getHeight();
        blackLine.setStrokeWidth(getWidth() * 0.01f);
        c.drawColor(Color.WHITE);
        //column lines
        c.drawLine(w*0.15f, h*0.4f, w*0.15f, h*0.8f, blackLine);
        c.drawLine(w*0.29f, h*0.4f, w*0.29f, h*0.8f, blackLine);
        c.drawLine(w*0.43f, h*0.4f, w*0.43f, h*0.8f, blackLine);
        c.drawLine(w*0.57f, h*0.4f, w*0.57f, h*0.8f, blackLine);
        c.drawLine(w*0.71f, h*0.4f, w*0.71f, h*0.8f, blackLine);
        c.drawLine(w*0.85f, h*0.4f, w*0.85f, h*0.8f, blackLine);
        //row lines
        c.drawLine(w*0.15f, h*0.4f, w*0.85f, h*0.4f, blackLine);
        c.drawLine(w*0.15f, h*0.48f, w*0.85f, h*0.48f, blackLine);
        c.drawLine(w*0.15f, h*0.56f, w*0.85f, h*0.56f, blackLine);
        c.drawLine(w*0.15f, h*0.64f, w*0.85f, h*0.64f, blackLine);
        c.drawLine(w*0.15f, h*0.72f, w*0.85f, h*0.72f, blackLine);
        c.drawLine(w*0.15f, h*0.8f, w*0.85f, h*0.8f, blackLine);
    }
}