package anandgaur.com.javanotestest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import anandgaur.com.javanotestest.Common.Common;
import anandgaur.com.javanotestest.Model.QuestionScore;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Done extends AppCompatActivity {

    @BindView(R.id.text_Total_Score)
    TextView txt_Result_Score;
    @BindView(R.id.text_Total_Questions)
    TextView get_TxtResult_Question;
    @BindView(R.id.done_Progress_Bar)
    ProgressBar progress_Bar;

    @BindView(R.id.btn_Play)
    Button btn_Try_Again;

    FirebaseDatabase database;
    DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        ButterKnife.bind(this);

        //firebase
        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        btn_Try_Again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Done.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle extra =getIntent().getExtras();
        if (extra != null)
        {
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            txt_Result_Score.setText(String.format("SCORE : %d",score));
            get_TxtResult_Question.setText(String.format("PASSED : %d / %d",correctAnswer,totalQuestion));

            progress_Bar.setMax(totalQuestion);
            progress_Bar.setProgress(correctAnswer);

            // Upload point to DB
            question_score.child(String.format("%s_%s", Common.currentUser.getUserName(), Common.categoryId))
                    .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUserName()
                            ,Common.categoryId)
                            ,Common.currentUser.getUserName()
                            ,String.valueOf(score)
                            ,Common.categoryId
                            ,Common.cateogryName));

        }

    }
}
