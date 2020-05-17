package example.app.progetto_am;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private ItemRepository repository;
    private LiveData<List<Item>> allItems;
    private LiveData<List<Item>> allItemsDESC;
    private LiveData<List<Item>> onlyGreen;
    private LiveData<List<Item>> onlyYellow;
    private LiveData<List<Item>> onlyRed;
    private LiveData<Integer> numRossi;
    private LiveData<Integer> numRossiGialli;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemRepository(application);
        allItems = repository.getAllItems();
        allItemsDESC = repository.getAllItemsDESC();
        onlyGreen = repository.getOnlyGreen();
        onlyYellow = repository.getOnlyYellow();
        onlyRed = repository.getOnlyRed();

        numRossi = repository.getDATAREDcount();
        numRossiGialli = repository.getDATAREDYELLOWcount();

    }


    public void insert(Item item) {
        repository.insert(item);
    }

    public void update(Item item) {
        repository.update(item);
    }

    public void delete(Item item) {
        repository.delete(item);
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public LiveData<List<Item>> getAllItemsDESC() {
        return allItemsDESC;
    }

    public LiveData<List<Item>> getOnlyGreen() {
        return onlyGreen;
    }
    public LiveData<List<Item>> getOnlyYellow() {
        return onlyYellow;
    }
    public LiveData<List<Item>> getOnlyRed() {
        return onlyRed;
    }

    public LiveData<Integer> getNumRossi(){return numRossi;}
    public LiveData<Integer> getNumRossiGialli(){return numRossiGialli;}
}