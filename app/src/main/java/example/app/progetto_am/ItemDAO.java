package example.app.progetto_am;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDAO {

    @Insert
    void insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);


    @Query("SELECT * FROM item_table ORDER BY priority ASC")
    LiveData<List<Item>> getAllItems();

    @Query("SELECT * FROM item_table ORDER BY priority DESC")
    LiveData<List<Item>> getAllItemsDESC();

    @Query("SELECT * FROM item_table WHERE priority = 1")
    LiveData<List<Item>> getONLYGREEN();

    @Query("SELECT * FROM item_table WHERE priority = 2")
    LiveData<List<Item>> getONLYYELLOW();

    @Query("SELECT * FROM item_table WHERE priority = 3")
    LiveData<List<Item>> getONLYRED();

    @Query("SELECT COUNT(*) FROM item_table WHERE priority = 3")
    LiveData<Integer> getDataREDCount();

    @Query("SELECT COUNT(*) FROM item_table WHERE priority = 3 OR priority = 2")
    LiveData<Integer> getDataREDYELLOWCount();





}
