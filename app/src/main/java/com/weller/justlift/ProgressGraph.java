package com.weller.justlift;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.softmoore.android.graphlib.Graph;
import com.softmoore.android.graphlib.GraphView;
import com.softmoore.android.graphlib.Point;

public class ProgressGraph extends AppCompatActivity {
    ProfileDB db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_graph);
        db = new ProfileDB(getApplicationContext());
        Cursor cursor = db.getExerciseData("Exercise_Table1");
        int x[] = new int[cursor.getCount()];
        int y[] = new int[cursor.getCount()];
        int i = 0;
        while(cursor.moveToNext()){
            x[i] = cursor.getInt(0);
            y[i] = cursor.getInt(1);
            Log.d("adding to array: ", "x= " + x[i] +" y= " + y[i]);
            i++;
        }

        for (int j =0; j<10; j++) {
            Point[] points = {new Point(1, 178)};
        }
        Graph graph = new Graph.Builder()
               // .addLineGraph(points, Color.RED)
                .build();
        GraphView graphView = findViewById(R.id.graph_view);
        graphView.setGraph(graph);
        setTitle("Empty Graph");
        TextView textView = findViewById(R.id.graph_view_label);
        textView.setText("Graph of Axes");
        //graph = findViewById(R.id.graph_view);
    }

}
