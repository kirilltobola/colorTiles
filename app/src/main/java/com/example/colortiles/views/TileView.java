package com.example.colortiles.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class TileView extends View {
    int CANVAS_SIZE = 4;
    int CANVAS_WIDTH, CANVAS_HEIGHT;
    int[][] TILES = new int[CANVAS_SIZE][CANVAS_SIZE];

    int DARK_COLOR = Color.BLUE;
    int BRIGHT_COLOR = Color.CYAN;

    int MARGIN = 20;
    int MARGIN_COUNT = TILES.length - 1;

    public TileView(Context context) {
        super(context);
    }

    public TileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        for (int i = 0; i < TILES.length; i++) {
            for (int j = 0; j < TILES.length; j++) {
                if (Math.random() > 0.5) TILES[i][j] = DARK_COLOR;
                else TILES[i][j] = BRIGHT_COLOR;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        CANVAS_WIDTH = canvas.getWidth();
        CANVAS_HEIGHT = canvas.getHeight();

        int block_width = (CANVAS_WIDTH - MARGIN_COUNT*MARGIN) / TILES.length;
        int block_height = (CANVAS_HEIGHT - MARGIN_COUNT*MARGIN) / TILES.length;

        int left, right, top, bot;
        left = 0;
        top = 0;
        right = block_width;
        bot = block_height;
        Paint p = new Paint();

        for (int i = 0; i < TILES.length; i++) {
            for (int j = 0; j < TILES.length; j++) {
                p.setColor(TILES[i][j]);
                Rect rect = new Rect();
                rect.set(left, top, right, bot);
                canvas.drawRect(rect, p);

                left += MARGIN + block_width;
                right += MARGIN + block_width;
            }
            left = 0;
            right = block_width;
            top += MARGIN + block_height;
            bot += MARGIN + block_height;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            int x_pos = -1, y_pos = -1;
            int block_width = (CANVAS_WIDTH - MARGIN_COUNT*MARGIN) / TILES.length;
            int block_height = (CANVAS_HEIGHT - MARGIN_COUNT*MARGIN) / TILES.length;

            int left_bound = 0;
            int right_bound = block_width;
            int top_bound = 0;
            int bot_bound = block_height;

            for (int i = 0; i < TILES.length; i++) {
                if(x >= left_bound && x <= right_bound) {
                    y_pos = i;
                }
                left_bound += MARGIN + block_width;
                right_bound += MARGIN + block_width;

                if(y >= top_bound && y <= bot_bound) {
                    x_pos = i;
                }
                bot_bound += block_height + MARGIN;
                top_bound += block_height + MARGIN;
            }
            Log.d("x_pos", String.format("%s", x_pos));
            Log.d("y_pos", String.format("%s", y_pos));

            if (x_pos != -1 && y_pos != -1) {
                for (int i = 0; i < TILES.length; i++) {
                    if (i != x_pos) {
                        if(TILES[i][y_pos] == DARK_COLOR) TILES[i][y_pos] = BRIGHT_COLOR;
                        else TILES[i][y_pos] = DARK_COLOR;
                    }

                    if(TILES[x_pos][i] == DARK_COLOR) TILES[x_pos][i] = BRIGHT_COLOR;
                    else TILES[x_pos][i] = DARK_COLOR;

                }
            }
        }

        int color = TILES[TILES.length - 1][TILES.length - 1];
        for (int i = 0; i < TILES.length; i++) {
            for (int j = 0; j < TILES.length; j++) {
                if (TILES[i][j] != color) {
                    invalidate();
                    return true;
                }
            }
        }
        invalidate();
        Toast.makeText(getContext(), "YOU WON!", Toast.LENGTH_SHORT).show();
        return true;
    }
}
