package com.example.csci310project1;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 10;
    private static final int ROW_COUNT = 12;
    private boolean flagging;

    private static final int[] iCombinations = {-1, -1, -1, 0, 1, 1, 1, 0};
    private static final int[] jCombinations = {-1, 0, 1, 1, 1, 0, -1, -1};

    // save the TextViews of all cells in an array, so later on,
    // when a TextView is clicked, we know which cell it is
    private ArrayList<TextView> cell_tvs;
    private ArrayList<Cell> cells;

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cell_tvs = new ArrayList<TextView>();
        cells = new ArrayList<Cell>();

        flagging = false;

        // dynamically created cells
        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
        for (int i = 0; i<ROW_COUNT; i++) {
            for (int j=0; j<COLUMN_COUNT; j++) {
                TextView tv = new TextView(this);
                tv.setHeight( dpToPixel(30) );
                tv.setWidth( dpToPixel(30) );
                tv.setTextSize( 15 );//dpToPixel(32) );
                tv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(Color.GRAY);
                tv.setBackgroundColor(Color.parseColor("lime"));
                tv.setOnClickListener(this::onClickTV);

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.setMargins(dpToPixel(2), dpToPixel(2), dpToPixel(2), dpToPixel(2));
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);

                cell_tvs.add(tv);

                Cell c = new Cell();

                cells.add(c);
            }
        }

        cells.get(13).bomb = true;
    }

    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    public void onClickTV(View view){
        TextView tv = (TextView) view;
        int n = findIndexOfCellTextView(tv);
        Cell c = cells.get(n);
        if(!flagging) {//mining code
            //check if a bomb
            if(c.bomb) {//loose

            } else {//mine this square and all neighbours
                mining(c, tv, n);
            }
        } else { //flagging code
            flagging(c, tv);
        }
    }

    public void onClickFlagging(View view) {
        Button b = (Button) view;
        if(flagging) {
            b.setText(getResources().getString(R.string.pick));
        } else {
            b.setText(getResources().getString(R.string.flag));
        }
        flagging = !flagging;
    }

    //helper function for flagging
    public void flagging(Cell c, TextView tv) {
        //check if the cell has flagged or not
        if(c.flagged) {
            tv.setText("");
            tv.setBackgroundColor(Color.parseColor("lime"));
            c.flagged = false;
        } else {
            tv.setText(getResources().getString(R.string.flag));
            c.flagged = true;
        }
    }

    //helper function for mining
    public void mining(Cell c, TextView tv, int n) {
        //change the mined state
        c.mined = true;
        //set the background to gray
        tv.setBackgroundColor(Color.LTGRAY);
        tv.setTextColor(Color.GRAY);

        //go through all 8 neighbours and check for bombs
        int bombCount = 0;

        int i = n/COLUMN_COUNT;
        int j = n%COLUMN_COUNT;

        ArrayList<Integer> indexes = new ArrayList<Integer>();
        int iNeighbour;
        int jNeighbour;

        //check all 8 neighbours
        for(int k = 0; k < 8; k++) {
            iNeighbour = i + iCombinations[k];
            jNeighbour = j + jCombinations[k];
            if(iNeighbour >= 0 && iNeighbour < ROW_COUNT && jNeighbour >= 0 && jNeighbour < COLUMN_COUNT) {
                int tempN = iNeighbour*COLUMN_COUNT + jNeighbour;
                indexes.add(tempN);
                if(cells.get(tempN).bomb) {//check for a bomb
                    bombCount++;
                }
            }
        }
        //if no neighbouring bombs, reveal all the neighbours not already reveleaed and not flagged
        if(bombCount == 0) { //recursively call the function on all neighbours
            //Log.d("no bombs", "no Bombs");
            for(int k = 0; k < indexes.size(); k++) {
                int tempN = indexes.get(k);
                Cell tempC = cells.get(tempN);
                TextView tempTV = cell_tvs.get(tempN);
                if(!tempC.mined && !tempC.flagged) {
                    mining(cells.get(tempN), cell_tvs.get(tempN), tempN);
                }
            }

        } else {//print the bombcount
            tv.setText(Integer.toString(bombCount));
        }
    }
}