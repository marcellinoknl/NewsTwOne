package com.example.newstwoone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newstwoone.R;
import com.example.newstwoone.model.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<CategoryModel> categoryModel;
    private Context context;
    private final CategoryClickInterface categoryClickInterface;

    public CategoryAdapter(ArrayList<CategoryModel> categoryModel, Context context, CategoryClickInterface categoryClickInterface) {
        this.categoryModel = categoryModel;
        this.context = context;
        this.categoryClickInterface = categoryClickInterface;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories_rv, parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryModel currentCategoryModel = categoryModel.get(position);
        holder.categoryTV.setText(currentCategoryModel.getCategory());
        Picasso.get().load(currentCategoryModel.getCategoryImageUrl()).into(holder.categoryIV);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryClickInterface.onCategoryClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModel.size();
    }

    public interface CategoryClickInterface {
        void onCategoryClick(int position1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView categoryTV;
        private final ImageView categoryIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIV = itemView.findViewById(R.id.ivCategory);
            categoryTV = itemView.findViewById(R.id.tvCategory);
        }
    }
}