package example.app.progetto_am;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>
{
    private List<Item> items = new ArrayList<>();
    private OnItemClickListener listener;


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView Immagine;
        private TextView Titolo;
        private TextView Sottotitolo;
        private FrameLayout Priority;

        private ImageButton Edit;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            Immagine = itemView.findViewById(R.id.Immagine);
            Titolo = itemView.findViewById(R.id.Titolo);
            Sottotitolo = itemView.findViewById(R.id.Sottotitolo);
            Priority = itemView.findViewById(R.id.framePriority);

            Edit = itemView.findViewById(R.id.editButton);

            Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(items.get(position));
                    }
                }
            });

        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(Item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.example_item, viewGroup, false);
        ViewHolder evh = new ViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {
        Item currentItem = items.get(i);
        viewHolder.Immagine.setImageResource(currentItem.getImmagine());
        viewHolder.Titolo.setText(currentItem.getTitolo());
        viewHolder.Sottotitolo.setText(currentItem.getSottotitolo());

        switch (currentItem.getPriority())
        {
            case 1:
                viewHolder.Priority.setBackgroundColor(Color.GREEN);
                break;
            case 2:
                viewHolder.Priority.setBackgroundColor(Color.YELLOW);
                break;
            case 3:
                viewHolder.Priority.setBackgroundColor(Color.RED);
                break;
        }

    }

    public void setItems(List<Item> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }

    public Item getItemAt(int position)
    {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
