package example.app.progetto_am;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table")
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int Immagine;
    private String Titolo;
    private String Sottotitolo;
    private int Priority;

    public Item(int Immagine, String Titolo, String Sottotitolo, int Priority)
    {
        this.Immagine = Immagine;
        this.Titolo = Titolo;
        this.Sottotitolo = Sottotitolo;
        this.Priority = Priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getImmagine()
    {
        return Immagine;
    }

    public String getTitolo()
    {
        return Titolo;
    }

    public String getSottotitolo()
    {
        return Sottotitolo;
    }

    public int getPriority(){return Priority;}
}
