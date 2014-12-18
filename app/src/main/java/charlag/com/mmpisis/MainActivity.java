package charlag.com.mmpisis;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import org.w3c.dom.Text;

import java.util.Arrays;


public class MainActivity extends Activity {


    private EditText mAEditText;
    private EditText mVEditText;
    private EditText mNEditText;
    private TextView mPtTextView;
    private Button mCalculateBtn;
    private TableLayout mTable;
    private XYPlot mPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAEditText = (EditText) findViewById(R.id.aEditText);
        mVEditText = (EditText) findViewById(R.id.vEditText);
        mNEditText = (EditText) findViewById(R.id.nEditText);
        mPtTextView = (TextView) findViewById(R.id.ptTextView);
        mCalculateBtn = (Button) findViewById(R.id.calculateBtn);
        mTable = (TableLayout) findViewById(R.id.table);
        mPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        mCalculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mNEditText.getWindowToken(), 0);
                double a = Double.parseDouble(mAEditText.getText().toString());
                int v = Integer.parseInt(mVEditText.getText().toString());
                long n = Long.parseLong(mNEditText.getText().toString());
                QueringModel model = new QueringModel(a, v, n);
                Log.d("TEST", "NEW TEST");
                Number[] values = model.compute();

                Number[] valuesChangedV = new QueringModel(a, v + v/2, n).compute();

                Number[] valuesChangedN = new QueringModel(a, v, n + n/2).compute();


                XYSeries series = new SimpleXYSeries(
                        Arrays.asList(values),
                        SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                        "orig"
                );

                XYSeries series1 = new SimpleXYSeries(
                        Arrays.asList(valuesChangedV),
                        SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                        "ch V"
                );

                XYSeries series2 = new SimpleXYSeries(
                        Arrays.asList(valuesChangedN),
                        SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                        "ch N"
                );

                LineAndPointFormatter seriesFormat = new LineAndPointFormatter();
                seriesFormat.setPointLabelFormatter(new PointLabelFormatter());
                seriesFormat.configure(getApplicationContext(),
                        R.xml.line_point_formatter_with_plf1);

                LineAndPointFormatter seriesFormat1 = new LineAndPointFormatter(Color.RED,
                        Color.GREEN, null, null);

                LineAndPointFormatter seriesFormat2 = new LineAndPointFormatter(Color.BLUE,
                        Color.CYAN, null, null);

                // add a new series' to the xyplot:
                mPlot.clear();
                mPlot.addSeries(series, seriesFormat);
                mPlot.addSeries(series1, seriesFormat1);
                mPlot.addSeries(series2, seriesFormat);
                mPlot.setTicksPerRangeLabel(3);
                mPlot.getGraphWidget().setDomainLabelOrientation(-45);
                mPlot.redraw();

                mPtTextView.setText(String.valueOf( model.computePt() ));

            for (int i = 0; i < v; i++) {
                TableRow row = new TableRow(MainActivity.this);
                row.addView(makeTableCell(String.format("%f2  ", (Double) values[i])));
                row.addView(makeTableCell(String.format("%f2  ", (Double) valuesChangedV[i])));
                row.addView(makeTableCell(String.format("%f2  ", (Double) valuesChangedN[i])));
                mTable.addView(row);
            }
            for (int i = v; i < v + v/2; i++) {
                TableRow row = new TableRow(MainActivity.this);
                row.addView(makeTableCell(""));
                row.addView(makeTableCell(String.format("%f2  ", (Double) valuesChangedV[i])));
                row.addView(makeTableCell(""));
                mTable.addView(row);
            }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View makeTableCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        return tv;
    }
}
