package anandgaur.com.javanotestest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import anandgaur.com.javanotestest.Common.Common;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Playing extends AppCompatActivity {

    final static long INTERVAL = 1000;  //1sec
    final static long TIMEOUT = 30000;  //30sec
    int progressValue = 0;

    CountDownTimer countDown;
    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswer;


    // Firebase
    FirebaseDatabase database;
    DatabaseReference reference;

    @BindView(R.id.question_image)
    ImageView question_Image;
    @BindView(R.id.question_text)
    TextView question_Text;
    @BindView(R.id.txt_Score)
    TextView txt_Score;
    @BindView(R.id.txt_Total_Question)
    TextView txt_Question_Num;

    @BindView(R.id.progress_Bar)
    ProgressBar progress_Bar;

    @BindView(R.id.btn_AnswerA)
    Button btn_AnswerA;
    @BindView(R.id.btn_AnswerB)
    Button btn_AnswerB;
    @BindView(R.id.btn_AnswerC)
    Button btn_AnswerC;
    @BindView(R.id.btn_AnswerD)
    Button btn_AnswerD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);



        ButterKnife.bind(this);


        // Firebase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Questions");



    }

    @OnClick({R.id.btn_AnswerA, R.id.btn_AnswerB, R.id.btn_AnswerC, R.id.btn_AnswerD})
    public void onViewClicked(View view) {

        countDown.cancel();
        if (index < totalQuestion)
        {
            Button clickedView = (Button)view;
            if (clickedView.getText().equals(Common.questions.get(index).getCorrectAnswer())){
                score+=10;
                correctAnswer++;
                showQuestion(++index); //next question
            }
            else //choose wrong answer
            {
                showQuestion(++index);
            }

            txt_Score.setText(String.format("%d",score));
        }
    }

    private void showQuestion(int index) {
        if (index < totalQuestion)
        {
            thisQuestion++;
            txt_Question_Num.setText(String.format("%d / %d",thisQuestion,totalQuestion));
            progress_Bar.setProgress(0);
            progressValue=0;

            if (Common.questions.get(index).getIsImageQuestion().equals("true"))
            {
                // If is image
                Picasso.get()
                        .load(Common.questions.get(index).getQuestion())
                        .into(question_Image);

                question_Image.setVisibility(View.VISIBLE);
                question_Text.setVisibility(View.INVISIBLE);
            }
            else
            {
                question_Text.setText(Common.questions.get(index).getQuestion());

                question_Image.setVisibility(View.INVISIBLE);
                question_Text.setVisibility(View.VISIBLE);
            }

            btn_AnswerA.setText(Common.questions.get(index).getAnswerA());
            btn_AnswerB.setText(Common.questions.get(index).getAnswerB());
            btn_AnswerC.setText(Common.questions.get(index).getAnswerC());
            btn_AnswerD.setText(Common.questions.get(index).getAnswerD());

            countDown.start();
        }
        else
        {
            // If it is final question
            Intent intent = new Intent(this,Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE",score);
            dataSend.putInt("TOTAL",totalQuestion);
            dataSend.putInt("CORRECT",correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questions.size();

        countDown = new CountDownTimer(TIMEOUT,INTERVAL) {
            @Override
            public void onTick(long minisec) {
                progress_Bar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                countDown.cancel();
                showQuestion(++index);
            }
        };
        showQuestion(index);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Stopping sure Quiz?");
        alert.setMessage("Results will be displayed if the app keeps running");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }
}
