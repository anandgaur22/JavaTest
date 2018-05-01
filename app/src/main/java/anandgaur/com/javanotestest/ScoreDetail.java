package anandgaur.com.javanotestest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import anandgaur.com.javanotestest.Model.QuestionScore;
import anandgaur.com.javanotestest.ViewHolder.ScoreDetailViewHolder;

public class ScoreDetail extends AppCompatActivity {

    String viewuser = "";

    FirebaseDatabase database;
    DatabaseReference question_score;

    RecyclerView score_List;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<QuestionScore,ScoreDetailViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);
        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");


        score_List = (RecyclerView)findViewById(R.id.score_List);
        score_List.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        score_List.setLayoutManager(layoutManager);


        if (getIntent() != null)
            viewuser = getIntent().getStringExtra("viewUser");
        if (!viewuser.isEmpty())
            loadScoreDetail(viewuser);

    }

    private void loadScoreDetail(String viewuser) {
        adapter = new FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder>(
                QuestionScore.class,
                R.layout.score_detail_layout,
                ScoreDetailViewHolder.class,
                question_score.orderByChild("user").equalTo(viewuser)
        ) {
            @Override
            protected void populateViewHolder(ScoreDetailViewHolder viewHolder, QuestionScore model, int position) {
                viewHolder.txt_name.setText(model.getCategoryName());
                viewHolder.txt_score.setText(model.getScore());
            }
        };
        adapter.notifyDataSetChanged();
        score_List.setAdapter(adapter);
    }
}