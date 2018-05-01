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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import anandgaur.com.javanotestest.Common.Common;
import anandgaur.com.javanotestest.Interface.ItemClickListener;
import anandgaur.com.javanotestest.Interface.RankingCallback;
import anandgaur.com.javanotestest.Model.QuestionScore;
import anandgaur.com.javanotestest.Model.Ranking;
import anandgaur.com.javanotestest.ViewHolder.RankingViewHolder;


public class RankingFragment extends Fragment {

    View myFragment;
    FirebaseDatabase database;
    DatabaseReference questionScore, rankingTbl;

    RecyclerView ranking_List;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

    int sum = 0;

    public static RankingFragment newInstance() {
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTbl = database.getReference("Ranking");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment=inflater.inflate(R.layout.fragment_ranking,container,false);

        // init view
        ranking_List = (RecyclerView)myFragment.findViewById(R.id.ranking_List);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        ranking_List.setHasFixedSize(true);


        // because OrderByChild method of Firebase will not sort list with ascending
        // so we need reverse our recycler data by LayoutManager
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        ranking_List.setLayoutManager(linearLayoutManager);


        // set adapter
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                Ranking.class,
                R.layout.layout_ranking,
                RankingViewHolder.class,
                rankingTbl.orderByChild("score")
        ) {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, final Ranking model, int position) {

                viewHolder.txt_name.setText(model.getUsername());
                viewHolder.txt_score.setText(String.valueOf(model.getScore()));

                // Fixed crash when click to item
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent scoreDetail = new Intent(getActivity(),ScoreDetail.class);
                        scoreDetail.putExtra("viewUser",model.getUsername());
                        startActivity(scoreDetail);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        ranking_List.setAdapter(adapter);

        // after add RankingCallback<Ranking> we need implement it here
        updateScore(Common.currentUser.getUserName(), new RankingCallback<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                // update to Ranking
                rankingTbl.child(ranking.getUsername()).setValue(ranking);
//                ShowRanking(); // after upload we need sort ranking tabel and show result
            }
        });


        return myFragment;
    }

    private void updateScore(final String userName, final RankingCallback<Ranking> callback) {
        questionScore.orderByChild("user").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data: dataSnapshot.getChildren())
                        {
                            QuestionScore ques = data.getValue(QuestionScore.class);
                            sum+= Integer.parseInt(ques.getScore());
                        }
                        /*
                        After summary all score, we need process sum variable here
                        because firebase is async db, so if process outside, our 'sum'
                        value will not reset to 0
                         */

                        Ranking ranking = new Ranking(userName,sum);
                        callback.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
