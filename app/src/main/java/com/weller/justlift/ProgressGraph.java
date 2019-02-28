package com.weller.justlift;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

//Library below is used to display the line graph for the user
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class ProgressGraph extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {
    ProfileDB db;
    private static final String TAG = "GraphActivity";
    private LineChart mchart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_graph);//sets layout to activity_progress_graph
        final int posValue = getIntent().getIntExtra("pos", 0); //gets position sent from previous class to know what exercise was clicked1
        db = new ProfileDB(getApplicationContext());//initialises database
        mchart = (LineChart) findViewById(R.id.lineChart);//initiates linechart
        Cursor c;//sets up a cursor to iterate through table
        ArrayList<Entry>yData = new ArrayList<>();//sets up arraylist for the y axis data

          switch (posValue){//runs particular code based on what position of the listview in progressfragment was selected
                        case 0:
                            c = db.getTable(ProfileDB.ExerciseTable_1);//sets cursor to first exercise table
                            yData = addData(c);//sets ydata to the information read from the cursor
                            break;//breaks out of switch and sends the ydata to the next part of code
                        case 1:
                            c = db.getTable(ProfileDB.ExerciseTable_2);//sets cursor to second exercise table ...
                            yData = addData(c);
                            break;
                        case 2:
                            c = db.getTable(ProfileDB.ExerciseTable_3);
                            yData = addData(c);
                            break;
                        case 3:
                            c = db.getTable(ProfileDB.ExerciseTable_4);
                            yData=  addData(c);
                            break;
                        case 4:
                            c = db.getTable(ProfileDB.ExerciseTable_5);
                            yData = addData(c);
                            break;
                    }

        mchart.setOnChartGestureListener(this);//accepts gestures on the chart from user
        mchart.setOnChartValueSelectedListener(this);//accepts specific values being selected by user
        mchart.setDragEnabled(true);//can drag chart to position
        mchart.setScaleEnabled(false);//scales to fit the screen

        LineDataSet setValues = new LineDataSet(yData, "Weight Lifted");//sets the y axis values and title
        setValues.setColor(getColor(R.color.colorTextBody));//sets to body text colour
        setValues.setAxisDependency(YAxis.AxisDependency.LEFT);//starts data from the left
        List<ILineDataSet> dataSets = new ArrayList<>();

        dataSets.add(setValues);//adds the values to the data set
        setValues.setLineWidth(6f);//width of the line, more = more thick line
        LineData data = new LineData(dataSets);
        mchart.setData(data);//sets the data in the graph

        mchart.setAutoScaleMinMaxEnabled(true);//scales chart to min and max values
        mchart.setBackgroundColor(getColor(R.color.colorBackground));//background used from app background

        YAxis leftAxis = mchart.getAxisLeft();
        leftAxis.setTextColor(getColor(R.color.colorTextBody));
        XAxis topAxis = mchart.getXAxis();
        topAxis.setTextColor(getColor(R.color.colorTextBody));
        YAxis rightAxis = mchart.getAxisRight();
        rightAxis.setTextColor(getColor(R.color.colorTextBody));//sets the colour of all axis to contrast background

        mchart.animateX(600, Easing.EaseInBack); // animate horizontal 3000 milliseconds, looks to load in

        TextView textView = findViewById(R.id.graph_view_label);
        textView.setTextColor(getColor(R.color.colorTextBody));
        textView.setText("Progress Graph");//sets a title underneath the graph
        mchart.invalidate();//sets the chart to run with above settings
    }

    public ArrayList<Entry> addData(Cursor cursor){
        int i=0;//start at index 0
        ArrayList<Entry>yData = new ArrayList<>();
        while(cursor.moveToNext()) {
            yData.add(new Entry(i, cursor.getInt(1)));//increments through column, getting value at each position and putting into yData ArrayList
            Log.d("adding to array: ", " y= " + yData.get(i));
            i++;
        }
        return yData;
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
