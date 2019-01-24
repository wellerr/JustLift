package com.weller.justlift;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
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
        setContentView(R.layout.activity_progress_graph);
        final int posValue = getIntent().getIntExtra("pos", 0); //gets position of the listview starting from 0
        db = new ProfileDB(getApplicationContext());
        mchart = (LineChart) findViewById(R.id.lineChart);
        Cursor cursor;
        Cursor exerciseNames = db.getExerciseNames();
        ArrayList<Entry>yData = new ArrayList<>();

          switch (posValue){//if position of listvalue, enter into specific table

                        case 0:
                            cursor = db.getExerciseData(db.ExerciseTable_1);//sets cursor to first exercise
                            yData = addData(cursor);//sets ydata to the information read from the cursor
                            break;//breaks out of switch and sends the ydata to the next part of code
                        case 1:
                            cursor = db.getExerciseData(db.ExerciseTable_2);
                            yData = addData(cursor);
                            break;
                        case 2:
                            cursor = db.getExerciseData(db.ExerciseTable_3);
                            yData = addData(cursor);
                            break;
                        case 3:
                            cursor = db.getExerciseData(db.ExerciseTable_4);
                            yData=  addData(cursor);
                            break;
                        case 4:
                            cursor = db.getExerciseData(db.ExerciseTable_5);
                            yData = addData(cursor);
                            break;
                    }

        mchart.setOnChartGestureListener(this);
        mchart.setOnChartValueSelectedListener(this);
        mchart.setDragEnabled(true);
        mchart.setScaleEnabled(false);

        LineDataSet setValues = new LineDataSet(yData, "Weight Lifted");
        setValues.setAxisDependency(YAxis.AxisDependency.LEFT);
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setValues);
        setValues.setColor(R.color.colorPrimary);
        setValues.setLineWidth(3f);
        LineData data = new LineData(dataSets);
        mchart.setData(data);
        mchart.invalidate();

        TextView textView = findViewById(R.id.graph_view_label);
        textView.setText("Progress Graph");
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
