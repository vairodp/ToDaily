package example.app.progetto_am;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import java.util.List;

public class ItemRepository {
    private ItemDAO itemDAO;
    private LiveData<List<Item>> allItems;
    private LiveData<List<Item>> allItemsDESC;
    private LiveData<List<Item>> onlyGreen;
    private LiveData<List<Item>> onlyYellow;
    private LiveData<List<Item>> onlyRed;

    private LiveData<Integer> numRossi;
    private LiveData<Integer> numRossiGialli;


    public ItemRepository(Application application) {
        ItemDatabase database = ItemDatabase.getInstance(application);
        itemDAO = database.itemDAO();
        allItems = itemDAO.getAllItems();
        allItemsDESC =itemDAO.getAllItemsDESC();
        onlyGreen = itemDAO.getONLYGREEN();
        onlyYellow = itemDAO.getONLYYELLOW();
        onlyRed = itemDAO.getONLYRED();

        numRossi = itemDAO.getDataREDCount();
        numRossiGialli = itemDAO.getDataREDYELLOWCount();
    }

    public void insert(Item item) {
        new InsertNoteAsyncTask(itemDAO).execute(item);
    }

    public void update(Item item) {
        new UpdateNoteAsyncTask(itemDAO).execute(item);
    }

    public void delete(Item item) {
        new DeleteNoteAsyncTask(itemDAO).execute(item);
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

    public LiveData<Integer> getDATAREDcount() {return numRossi;}

    public LiveData<Integer> getDATAREDYELLOWcount() {return numRossiGialli;}

    private static class InsertNoteAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDAO itemDAO;

        private InsertNoteAsyncTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDAO.insert(items[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDAO itemDAO;

        private UpdateNoteAsyncTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDAO.update(items[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDAO itemDAO;

        private DeleteNoteAsyncTask(ItemDAO itemDAO) {
            this.itemDAO= itemDAO;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDAO.delete(items[0]);
            return null;
        }
    }

}
