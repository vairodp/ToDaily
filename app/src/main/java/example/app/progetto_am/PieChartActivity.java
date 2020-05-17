package example.app.progetto_am;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


public class PieChartActivity extends AppCompatActivity {

    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart_layout);

        setUpPieChart();
    }

    private void setUpPieChart() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int totalTask = extras.getInt("counterTotal");
            int totalDoneTask = extras.getInt("counterDone");

            Log.d("pocoo", "setUpPieChart: "+totalTask+" "+totalDoneTask);

            int newarray[] = {totalTask,totalDoneTask};
            String namearray[] = {"Total Tasks","Tasks Done"};

            List<PieEntry> pieEntries = new ArrayList<>();

            for (int i=0; i < newarray.length; i++)
            {
                pieEntries.add(new PieEntry(newarray[i],namearray[i]));
            }

            PieDataSet dataSet = new PieDataSet(pieEntries,"Total/Done Ratio");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            PieData data = new PieData(dataSet);

            PieChart chart = findViewById(R.id.piechart);
            chart.setData(data);
            chart.invalidate();
        }
    }
}
