package example.app.progetto_am;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener {

    private ItemViewModel itemViewModel;

    FloatingActionButton fab;
    private FloatingActionButton fab1,fab2;

    final  Adapter Adapter = new Adapter();

    private boolean isFABOpen = false;

    private String timePicked;

    private Spinner addCategory;

    int totalTaskCount,totalDoneCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(Adapter);

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable List<Item> items) {
                Adapter.setItems(items);
            }
        });

        fab  = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isFABOpen) { showFABMenu(); }
                else { closeFABMenu(); }
            }

        });

        //Add button
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddOrEditItem(1,null);
            }
        });

        //Pie chart button
        fab2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(MainActivity.this, PieChartActivity.class);

                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                int totalScore = sharedPref.getInt("counterTotal", 0);
                int doneScore = sharedPref.getInt("counterDone", 0);

                intent.putExtra("counterTotal",totalScore);
                intent.putExtra("counterDone",doneScore);
                startActivity(intent);
            }
        });

        //Swipe to delete handler
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                addDoneNumber();
                itemViewModel.delete(Adapter.getItemAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        Adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                AddOrEditItem(2,item);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,day);

        timePicked = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

    }

    public boolean insertItem(String name, String category, int priority, String date, int id)
    {
        //Get drawable from category
        int drawable = 0;

        switch (category)
        {
            case "Work":
                drawable = R.drawable.category_work;
                break;
            case "Social":
                drawable = R.drawable.category_people;
                break;
            case "Health":
                drawable = R.drawable.ic_fitness_center_black_24dp;
                break;
            case "Chores":
                drawable = R.drawable.category_chores;
                break;
            case "Shopping":
                drawable = R.drawable.category_shopping;
                break;
            default:
                //TODO change icon
                drawable = R.drawable.ic_whatshot_black_24dp;
        }

        Item item = new Item(drawable,name,date,priority);

        if (id == -1)
        {
            itemViewModel.insert(item);
            return true;
        }

        item.setId(id);
        itemViewModel.update(item);
        return false;
    }

    public void AddOrEditItem(final int mode,final Item item)
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_design,null);

        //Task name
        final EditText mEdit = layout.findViewById(R.id.taskname);
        //Seek bar priority
        final SeekBar sb = layout.findViewById(R.id.seekbarvera);
        //Text priority displayed
        final TextView testoPriority = layout.findViewById(R.id.textImportanza);
        //Text Edit/Add
        final TextView testoJob = layout.findViewById(R.id.taskJob);
        //Add new Category Button
        final Button add = layout.findViewById(R.id.buttonAddCategory);
        //Spinner categories
        addCategory = layout.findViewById(R.id.spinner_categories);

        spinnerAdapter();

        sb.setMax(2);

        //Edit mode layout
        if (mode == 2)
        {
            testoJob.setText("Edit Task");
            mEdit.setText(item.getTitolo());
            sb.setProgress(item.getPriority()-2);
            testoPriority.setText(""+0);
        }

        //Add category button handler
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPersonalizedCategory();
            }
        });

        //Confirm button handler
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(layout)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Get variables
                        int priority = (sb.getProgress()) + 1;
                        String category = addCategory.getSelectedItem().toString();
                        String name = mEdit.getText().toString();
                        String date = timePicked;

                        //Check if name is inserted
                        if (TextUtils.isEmpty(mEdit.getText()))
                        {
                            Toast.makeText(MainActivity.this,"Task name required!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //TODO check if date is picked
                        else if (false)
                        {
                            Toast.makeText(MainActivity.this, "Date required!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //All set, we can go
                        else
                        {
                            //Normal mode
                            if (mode != 2)
                            {
                                addTaskNumber();
                                insertItem(name,category,priority,date,-1);
                                Toast.makeText(MainActivity.this, "Task added!", Toast.LENGTH_SHORT).show();
                            }

                            //Edit mode
                            else
                            {
                                insertItem(name,category,priority,date,item.getId());
                                Toast.makeText(MainActivity.this, "Task edited!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //Listener seek bar movement
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                testoPriority.setText(""+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void addPersonalizedCategory()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insert new category name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addCategory(input.getText().toString());

                Toast.makeText(MainActivity.this, "New category added", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    public void showCalendar (View view)
    {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(),"date picker");
    }

    private void showFABMenu()
    {
        isFABOpen = true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
    }

    private void closeFABMenu()
    {
        isFABOpen = false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        int num=0;

        switch (id)
        {
            case R.id.action_activate_only_red_notifications:
                itemViewModel.getNumRossi().observe(MainActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        checkAlarm(4,integer);
                    }
                });
                return true;
            case R.id.action_activate_only_yellow_and_red_notifications:
                itemViewModel.getNumRossiGialli().observe(MainActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        checkAlarm(3,integer);
                    }
                });
                return true;
            case R.id.action_activate_no_notifications:
                checkAlarm(2,num);
                return true;
            case R.id.action_activate_all_notifications:
                num = itemViewModel.getAllItems().getValue().size();
                checkAlarm(1,num);
                return true;
            case R.id.action_filter_ASC:
                itemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
                    @Override
                    public void onChanged(@Nullable List<Item> items) {
                        Adapter.setItems(items);
                    }
                });
                return true;
            case R.id.action_filter_DESC:
                itemViewModel.getAllItemsDESC().observe(MainActivity.this, new Observer<List<Item>>() {
                    @Override
                    public void onChanged(@Nullable List<Item> items) {
                        Adapter.setItems(items);
                    }
                });
                return true;
            case R.id.action_filter_GREEN:
                doThing(1);
                return true;
            case R.id.action_filter_YELLOW:
                doThing(2);
                return true;
            case R.id.action_filter_RED:
                doThing(3);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void checkAlarm(int mode,int size)
    {
        removeAlarm();

        switch (mode) {
            case 4:
                if (size > 0)
                {
                    makeAlarm(size);
                }
                break;
            case 3:
                if (size > 0)
                {
                    makeAlarm(size);
                }
                break;
            case 2:
                break;
            case 1:
                if (size > 0)
                {
                    makeAlarm(size);
                }
                break;
        }
    }

    public void makeAlarm(int size)
    {
        Date dat  = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);
        cal_alarm.setTime(dat);
        cal_alarm.set(Calendar.HOUR_OF_DAY,18);
        cal_alarm.set(Calendar.MINUTE, 00);
        cal_alarm.set(Calendar.SECOND,0);
        if(cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE,1);
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
    }

    public void removeAlarm()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    public void doThing(int priority)
    {
        switch (priority)
        {
            case 3:
                itemViewModel.getOnlyRed().observe(MainActivity.this, new Observer<List<Item>>() {
                    @Override
                    public void onChanged(@Nullable List<Item> items) {
                        Adapter.setItems(items);
                    }
                });
                break;
            case 2:
                itemViewModel.getOnlyYellow().observe(MainActivity.this, new Observer<List<Item>>() {
                    @Override
                    public void onChanged(@Nullable List<Item> items) {
                        Adapter.setItems(items);
                    }
                });
                break;
            case 1:
                itemViewModel.getOnlyGreen().observe(MainActivity.this, new Observer<List<Item>>() {
                    @Override
                    public void onChanged(@Nullable List<Item> items) {
                        Adapter.setItems(items);
                    }
                });
                break;
        }
    }

    public void addTaskNumber()
    {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        totalTaskCount = prefs.getInt("counterTotal",0);
        totalTaskCount = totalTaskCount + 1;

        editor.putInt("counterTotal",totalTaskCount);
        editor.apply();
    }

    public void addDoneNumber()
    {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        totalDoneCount = prefs.getInt("counterDone",0);
        totalDoneCount = totalDoneCount + 1;

        editor.putInt("counterDone",totalDoneCount);
        editor.apply();
    }

    public void addCategory(String name)
    {
        List<String> list = retrieveCategoriesList();
        list.add(name);

        StringBuilder csvList = new StringBuilder();
        for (String s : list)
        {
            csvList.append(s);
            csvList.append(",");
        }

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("CategoryList",csvList.toString());
        editor.apply();

        spinnerAdapter();
    }

    public List<String> retrieveCategoriesList()
    {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

        String csvList = prefs.getString("CategoryList",",");
        String[] items = csvList.split(",");

        List<String> list = new ArrayList<>();

        for(int i = 0; i < items.length; i++)
        {
            list.add(items[i]);
        }

        return list;
    }

    public void spinnerAdapter()
    {
        List<String> list = retrieveCategoriesList();

        list.add("Work");
        list.add("Social");
        list.add("Health");
        list.add("Chores");
        list.add("Shopping");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,list);
        categoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        categoryAdapter.notifyDataSetChanged();
        addCategory.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

    }

}