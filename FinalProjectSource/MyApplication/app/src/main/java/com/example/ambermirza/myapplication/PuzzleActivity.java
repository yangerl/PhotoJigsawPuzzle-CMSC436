package com.example.ambermirza.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

public class PuzzleActivity extends AppCompatActivity implements View.OnTouchListener {
    private Bitmap fullImage;
    private FrameLayout mFrame;
    private FrameLayout mFrameCompleted;
    private PieceView moving = null;
    private String puzzleName;

    // Eric's Edits
    private Intent fromPrevActivity;
    // End edits

    float dX, dY;
    private float wiggle = .25f;
    int[] pd = {3, 4}; // Puzzle Dimentions
    ArrayList<PieceView> pieces = new ArrayList<PieceView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        // Eric's Edit Start
        fromPrevActivity = getIntent();
        // decodes bytearray of info sent by intent
        byte[] byteArray = getIntent().getByteArrayExtra("picture");
        fullImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        puzzleName = fromPrevActivity.getStringExtra("name");
        // Eric's Edit End
        mFrame = (FrameLayout) findViewById(R.id.frame);
        mFrameCompleted = (FrameLayout) findViewById(R.id.frameImage);
        mFrameCompleted.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mFrameCompleted.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = mFrameCompleted.getWidth();
                int height = mFrameCompleted.getHeight();

                fullImage = Bitmap.createScaledBitmap(fullImage, width, height, false);

                float[] f = new float[] {0,0};
                float[] size = {width, height};
                PieceView v = new PieceView(getApplicationContext(), f, size, fullImage, mFrameCompleted);
                v.fixed = true;
                mFrameCompleted.addView(v);
            }
        });
        mFrame.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mFrame.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = mFrame.getWidth();
                int height = mFrame.getHeight();
                int x = width / pd[0];
                int y = height / pd[1];

                //uncomment to use a hardcoded image
                //fullImage = BitmapFactory.decodeResource(getResources(), R.drawable.test4);
                fullImage = Bitmap.createScaledBitmap(fullImage, width, height, false);

                for (int i = 0; i < pd[0]; i++) {
                    for (int j = 0; j < pd[1]; j++) {
                        Bitmap b = Bitmap.createBitmap(fullImage, i * x, j * y, x, y);
                        float[] start = {i * x, j * y};
                        float[] pos = start;
                        float[] size = {x, y};
                        pieces.add(new PieceView(getApplicationContext(), start, size, b, mFrame));
                    }
                }
                for (int i = 0; i < pd[0] * pd[1]; i++) {
                    int a = i;
                    int b = (int) (Math.random() * pd[0] * pd[1]);
                    float[] f = pieces.get(a).pos;
                    f[0] += x * .1 * (Math.random() - 1);
                    f[1] += y * .1 * (Math.random() - 1);
                    pieces.get(b).pos[0] += x * wiggle * (Math.random() - 1);
                    pieces.get(b).pos[1] += y * wiggle * (Math.random() - 1);
                    pieces.get(a).movePieceTo(pieces.get(b).pos);
                    pieces.get(b).movePieceTo(f);
                }
                setTouchListeners();
            }
        });

    }

    public void setTouchListeners() {
        for (PieceView p : pieces) {
            p.setOnTouchListener(this);
            mFrame.addView(p);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view.getClass().equals(PieceView.class)) {
            PieceView p = (PieceView) view;
            if (!p.fixed) {
                view.bringToFront();
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        p.movePieceTo(new float[]{event.getRawX() + dX, event.getRawY() + dY});
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(p.pos[0] - p.start[0]) < .5 * p.size[0] && Math.abs(p.pos[1] - p.start[1]) < .5 * p.size[1]) {
                            p.movePieceTo(p.start);
                            p.fixed = true;
                            p.sendViewToBack(p);
                            if (isComplete()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setMessage("You completed the puzzle!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        finish();
                                    }
                                });
                                alert.show();
                            }

                        }
                    default:
                        return false;
                }
                mFrame.invalidate();
            }
        }
        return true;
    }

    public void showCompleted(View v) {
        if (mFrame.getVisibility() == View.VISIBLE) {
            Button b = (Button) findViewById(R.id.showCompletedButton);
            b.setText("Return to Puzzle");
            mFrame.setVisibility(View.INVISIBLE);
            mFrameCompleted.setVisibility(View.VISIBLE);

        } else {
            Button b = (Button) findViewById(R.id.showCompletedButton);
            b.setText("Show Completed Puzzle");
            mFrame.setVisibility(View.VISIBLE);
            mFrameCompleted.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void finish() {
        /*
        super.finish();
        if (isComplete()) {
            setResult(1);
        } else {
            setResult(0);
        }
        */
        // Eric's Edit
        Intent resultIntent = new Intent();
        resultIntent.putExtra("solved", isComplete());
        resultIntent.putExtra("name", puzzleName);
        setResult(Activity.RESULT_OK, resultIntent);
        super.finish();
        // end edits
    }

    public boolean isComplete() {
        for (PieceView p : pieces) {
            if (!p.fixed) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}