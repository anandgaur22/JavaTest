package anandgaur.com.javanotestest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

import anandgaur.com.javanotestest.Common.Common;
import anandgaur.com.javanotestest.Model.Question;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Start extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference questions;

    @BindView(R.id.btn_Play)
    Button btn_Play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        loadQuestions(Common.categoryId);
    }

    private void loadQuestions(String categoryId) {

        // first clear list if have old question
        if (Common.questions.size() > 0)
            Common.questions.clear();

        questions.orderByChild("CategoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postDatasnapshop : dataSnapshot.getChildren())
                        {
                            Question question = postDatasnapshop.getValue(Question.class);
                            Common.questions.add(question);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        // Random list
        Collections.shuffle(Common.questions);

    }

    @OnClick(R.id.btn_Play)
    public void onViewClicked() {
        Intent intent = new Intent(this,Playing.class);
        startActivity(intent);
        finish();
    }
}
