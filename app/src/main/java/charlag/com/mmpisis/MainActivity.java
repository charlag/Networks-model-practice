package charlag.com.mmpisis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
        // Настройка интерфейса
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
                if (a > 1) {
                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).
                            setTitle("Ошибка!").
                            setMessage("a должно быть меньше 1").
                            setCancelable(true).
                            create();
                    dialog.show();
                    return;
                }

                // Вычисления
                ComputingModel model = new ComputingModel(a, v, n);
                Log.d("TEST", "NEW TEST");
                Number[] values = model.compute();

                Number[] valuesChangedV = new ComputingModel(a, v + v / 2, n).compute();

                Number[] valuesChangedN = new ComputingModel(a, v, n + n / 2).compute();

                // Построение графиков

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
                seriesFormat.setPointLabelFormatter(null);

                LineAndPointFormatter seriesFormat1 = new LineAndPointFormatter(Color.RED,
                        Color.GREEN, null, null);
                seriesFormat1.setPointLabelFormatter(null);

                LineAndPointFormatter seriesFormat2 = new LineAndPointFormatter(Color.BLUE,
                        Color.CYAN, null, null);
                seriesFormat2.setPointLabelFormatter(null);

                // Добавить значения на график
                mPlot.clear();
                mPlot.addSeries(series, seriesFormat);
                mPlot.addSeries(series1, seriesFormat1);
                mPlot.addSeries(series2, seriesFormat2);
                mPlot.setTicksPerRangeLabel(3);
                mPlot.getGraphWidget().setDrawMarkersEnabled(false);
                mPlot.getGraphWidget().setDomainLabelOrientation(-45);

                mPlot.redraw();

                mPtTextView.setText(String.valueOf( model.computePt() ));

                // Добавить таблицу на экран
                mTable.removeAllViews();
                TableRow firstRow = new TableRow(MainActivity.this);
                firstRow.addView(makeTableCell("  #  "));
                firstRow.addView(makeTableCell("Pi"));
                firstRow.addView(makeTableCell("ch V"));
                firstRow.addView(makeTableCell("ch N"));
                mTable.addView(firstRow);
            for (int i = 0; i < v; i++) {
                TableRow row = new TableRow(MainActivity.this);
                row.addView(makeTableCell("  " + i + "  "));
                row.addView(makeTableCell(String.format("%f2  ", (Double) values[i])));
                row.addView(makeTableCell(String.format("%f2  ", (Double) valuesChangedV[i])));
                row.addView(makeTableCell(String.format("%f2  ", (Double) valuesChangedN[i])));
                mTable.addView(row);
            }
            for (int i = v; i < v + v/2; i++) {
                TableRow row = new TableRow(MainActivity.this);
                row.addView(makeTableCell(" " + i + " "));
                row.addView(makeTableCell(""));
                row.addView(makeTableCell(String.format("%f2  ", (Double) valuesChangedV[i])));
                row.addView(makeTableCell(""));
                mTable.addView(row);
            }
            }
        });
    }

    private View makeTableCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        return tv;
    }
}
