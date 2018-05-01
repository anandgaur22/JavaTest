package anandgaur.com.javanotestest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import anandgaur.com.javanotestest.Common.Common;
import anandgaur.com.javanotestest.Interface.ItemClickListener;
import anandgaur.com.javanotestest.Model.Category;
import anandgaur.com.javanotestest.ViewHolder.CategoryViewHolder;


public class CategoryFragment extends Fragment {

    View myFragment;
    RecyclerView list_Category;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category,CategoryViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference categories;

    public static CategoryFragment newInstance(){
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database=FirebaseDatabase.getInstance();
        categories=database.getReference("Category");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment=inflater.inflate(R.layout.fragment_category,container,false);

        list_Category = (RecyclerView)myFragment.findViewById(R.id.list_Category);
        list_Category.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(container.getContext());
        list_Category.setLayoutManager(layoutManager);
        loadCategories();

        return myFragment;
    }

    private void loadCategories() {
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
                Category.class,
                R.layout.category_layout,
                CategoryViewHolder.class,
                categories
        ) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, int position) {

                viewHolder.category_name.setText(model.getName());
                Picasso.get()
                        .load(model.getImage())
                        .into(viewHolder.category_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(getActivity(), String.format("%s|%s",adapter.getRef(position).getKey(),
//                                model.getName()), Toast.LENGTH_SHORT).show();
                        Intent starGame = new Intent(getActivity(),Start.class);
                        Common.categoryId = adapter.getRef(position).getKey();
                        Common.cateogryName = model.getName();
                        startActivity(starGame);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        list_Category.setAdapter(adapter);
    }
}
