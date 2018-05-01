package anandgaur.com.javanotestest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import anandgaur.com.javanotestest.Common.Common;
import anandgaur.com.javanotestest.Model.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    // Todo 1 generate Butterknife

    @BindView(R.id.edt_User)
    MaterialEditText edt_User;
    @BindView(R.id.edt_Password)
    MaterialEditText edt_Password;
    @BindView(R.id.btn_Sign_Up)
    Button btn_Sign_Up;
    @BindView(R.id.btn_Sign_In)
    Button btn_Sign_In;

    // Todo 2
    MaterialEditText edt_New_User, edt_New_Password, edt_New_Email;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //todo 2.1
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
    }



    @OnClick(R.id.btn_Sign_Up)
    public void onBtnSignUpClicked() {

        // Todo 3
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View sign_up = getLayoutInflater().inflate(R.layout.sign_up_layout, null);

        edt_New_User = (MaterialEditText) sign_up.findViewById(R.id.edtNewUserName);
        edt_New_Password = (MaterialEditText) sign_up.findViewById(R.id.edtNewUserPassword);
        edt_New_Email = (MaterialEditText) sign_up.findViewById(R.id.edtNewUserEmail);

        alertDialog.setTitle("Sign Up")
                .setMessage("Please fill full information")
                .setIcon(R.drawable.ic_account_circle_black_24dp)
                .setPositiveButton("Sign up",null)
//                .setNeutralButton("Batalkan", null)
                .setView(sign_up);

        final AlertDialog setButton = alertDialog.create();
        setButton.show();

        Button daftar = setButton.getButton(DialogInterface.BUTTON_POSITIVE);
        daftar.setTextColor(getResources().getColor(android.R.color.white));
        daftar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final User user = new User(edt_New_User.getText().toString(),
                        edt_New_Password.getText().toString(),
                        edt_New_Email.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(user.getUserName()).exists())
                            Toast.makeText(MainActivity.this
                                    ,"User Already Exits", Toast.LENGTH_SHORT).show();

                        else
                        {
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this
                                    ,"User Registration Success", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                setButton.dismiss();
            }
        });

        Button batalkan = setButton.getButton(DialogInterface.BUTTON_NEUTRAL);
        batalkan.setTextColor(getResources().getColor(R.color.white));
        batalkan.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        batalkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButton.dismiss();
            }
        });
    }


    // Todo 4
    @OnClick(R.id.btn_Sign_In)
    public void onBtnSignInClicked() {
        signIn(edt_User.getText().toString(), edt_Password.getText().toString());
    }

    private void signIn(final String user, final String pwd) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user).exists()) {
                    if (!user.isEmpty()) {
                        User login = dataSnapshot.child(user).getValue(User.class);
                        if (login.getPassword().equals(pwd)) {
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            Common.currentUser = login;
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this
                                    ,"Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this
                                ,"Please Enter Your user Name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this
                            ,"user is not exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
