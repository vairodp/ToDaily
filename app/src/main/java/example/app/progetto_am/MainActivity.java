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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, DatePickerDialog.OnDateSetListener {

    private ItemViewModel itemViewModel;

    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;

    final  Adapter Adapter = new Adapter();

    private boolean isFABOpen = false;

    private ArrayList<Item> lista;

    private Button showMenu;
    private String timePicked;

    private SharedPreferences assoluto;
    private SharedPreferences.Editor editore;
    private int totalTaskCount,totalDoneCount;



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

        fab = findViewById(R.id.fab);
        fab1=findViewById(R.id.fab1);
        fab2=findViewById(R.id.fab2);

        createList();
        //clearAllNumbers();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }

        });


        //BUTTON ADD
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddOrEditItem(1,null);
            }
        });

        //OPEN PIECHART
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

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        timePicked = currentDateString;

    }

    public boolean insertItem(String name, String category, int priority,int id)
    {
        Item item;

        switch (category)
        {
            case "Work":
                item = new Item(R.drawable.category_work,name,timePicked,priority);
                if (id == -1) {
                    itemViewModel.insert(item);
                    Toast.makeText(MainActivity.this, "Task added", Toast.LENGTH_SHORT).show();
                    return true;
                }
                item.setId(id);
                itemViewModel.update(item);
                Toast.makeText(MainActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
                return true;
            case "Social":
                item = new Item(R.drawable.category_people,name, timePicked,priority);
                if (id == -1) {
                    itemViewModel.insert(item);
                    Toast.makeText(MainActivity.this, "Task added", Toast.LENGTH_SHORT).show();
                    return true;
                }
                item.setId(id);
                itemViewModel.update(item);
                Toast.makeText(MainActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
                return true;
            case "Health":
                item = new Item(R.drawable.ic_fitness_center_black_24dp,name, timePicked,priority);
                if (id == -1) {
                    itemViewModel.insert(item);
                    Toast.makeText(MainActivity.this, "Task added", Toast.LENGTH_SHORT).show();
                    return true;
                }
                item.setId(id);
                itemViewModel.update(item);
                Toast.makeText(MainActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
                return true;
            case "Chores":
                item = new Item(R.drawable.category_chores,name, timePicked,priority);
                if (id == -1) {
                    itemViewModel.insert(item);
                    Toast.makeText(MainActivity.this, "Task added", Toast.LENGTH_SHORT).show();
                    return true;
                }
                item.setId(id);
                itemViewModel.update(item);
                Toast.makeText(MainActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
                return true;
            case "Shopping":
                item = new Item(R.drawable.category_shopping,name, timePicked,priority);
                if (id == -1) {
                    itemViewModel.insert(item);
                    Toast.makeText(MainActivity.this, "Task added", Toast.LENGTH_SHORT).show();
                    return true;
                }
                item.setId(id);
                itemViewModel.update(item);
                Toast.makeText(MainActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
                return true;
        }

        return false;
    }

    public void AddOrEditItem(final int mode,final Item item)
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_design,null);

        final EditText mEdit = layout.findViewById(R.id.taskname);
        final SeekBar sb = layout.findViewById(R.id.seekbarvera);
        final TextView testoPriority = layout.findViewById(R.id.textImportanza);
        final Button showMenuPass = layout.findViewById(R.id.buttonShowMenu);
        final TextView testoJob = layout.findViewById(R.id.taskJob);

        showMenu = showMenuPass;
        sb.setMax(2);

        if (mode == 2)
        {
            testoJob.setText("Edit Task");
            mEdit.setText(item.getTitolo());
            sb.setProgress(item.getPriority()-2);
            testoPriority.setText(""+(item.getPriority()+1));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(layout)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int priority = sb.getProgress();
                        priority = priority+1;

                        String name = mEdit.getText().toString();
                        String category = showMenu.getText().toString();


                        if (mode != 2)
                        {
                            //aggiungo normale
                            addTaskNumber();
                            insertItem(name,category,priority,-1);
                        }

                        else {
                            //aggiungo edito
                            insertItem(name, category, priority,item.getId());
                        }
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

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

    public void createList()
    {
        lista = new ArrayList<>();
    }


    public void showPopup (View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    public void showCalendar (View view)
    {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(),"date picker");
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.item_work:
                chosen("Work");
                return true;
            case R.id.item_social:
                chosen("Social");
                return true;
            case R.id.item_health:
                chosen("Health");
                return true;
            case R.id.item_chores:
                chosen("Chores");
                return true;
            case R.id.item_shopping:
                chosen("Shopping");
                return true;
        }

        return false;
    }

    public void chosen(String category)
    {
        showMenu.setText(category);
        showMenu.setCompoundDrawablesWithIntrinsicBounds(R.drawable.category_vector_on,0,0,0);

    }


    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
    }

    private void closeFABMenu(){
        isFABOpen=false;
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

}