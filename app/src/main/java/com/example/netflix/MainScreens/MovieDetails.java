package com.example.netflix.MainScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.netflix.Activities.PaymentGateway;
import com.example.netflix.Adapters.CommentAdapter;
import com.example.netflix.Model.Comment;
import com.example.netflix.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MovieDetails extends AppCompatActivity {
    ImageView movieImage;
    EditText commentboxedittext;
    TextView movieName,usernametextview;
    FirebaseAuth firebaseAuth;
    DocumentReference userReference;
    FirebaseFirestore firebaseFirestore;
    Button play,add;
    String userID,name,image,fileUrl,movieId,userName,userComment,mName;
    RecyclerView commentBox;
    CommentAdapter commentAdapter;
    Map<String,Object> m;
    List<Comment> commentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().hide();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        movieImage=findViewById(R.id.imageDetails);
        commentboxedittext=findViewById(R.id.comment);
        usernametextview=findViewById(R.id.commentUsername);
        add=findViewById(R.id.commentButton);
        movieName=findViewById(R.id.movieName);
        play=findViewById(R.id.playButton);
        commentBox=findViewById(R.id.commentboxrecycler);
        userID=firebaseAuth.getCurrentUser().getUid();
        userReference=firebaseFirestore.collection("Users ").document(userID);
        movieId=getIntent().getStringExtra("MovieId");
        name=getIntent().getStringExtra("MovieName");
        image=getIntent().getStringExtra("MovieImageURL");
        fileUrl=getIntent().getStringExtra("MovieFile");
        Glide.with(this).load(image).into(movieImage);
        movieName.setText(name);
        if(firebaseAuth.getCurrentUser()!=null)
        {
            userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    userName=documentSnapshot.getString("First Name")+" "+documentSnapshot.getString("Last Name");
                    usernametextview.setText(userName);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e instanceof FirebaseNetworkException)
                        Toast.makeText(MovieDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MovieDetails.this, "Error! Name not fetched", Toast.LENGTH_SHORT).show();
                }
            });
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> user=new HashMap<String,Object>();
                String commentContent=commentboxedittext.getText().toString();
                user.put(name,commentContent);
                userReference.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        iniRvComment();
                        Toast.makeText(MovieDetails.this, "Comment Added", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof FirebaseNetworkException)
                            Toast.makeText(MovieDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MovieDetails.this, "Values Not Stored", Toast.LENGTH_SHORT).show();
                    }
                });
                Comment comment=new Comment(commentContent,userID,userName);
                commentboxedittext.setText("");
            }
        });
        iniRvComment();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MovieDetails.this,VideoPlayer.class);
                i.putExtra("FileURL",fileUrl);
                startActivity(i);
            }
        });
    }

    private void iniRvComment() {
        commentBox.setLayoutManager(new LinearLayoutManager(this));
        commentList=new ArrayList<>();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    userComment=documentSnapshot.getString(name);
                    if(documentSnapshot.contains(name))
                    userName=documentSnapshot.getString("First Name")+" "+documentSnapshot.getString("Last Name");
                    else
                        userName="";
                    Comment comment=new Comment(userComment,userID,userName);
                    commentList.add(comment);
                    commentAdapter=new CommentAdapter(getApplicationContext(),commentList);
                    commentBox.setAdapter(commentAdapter);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e instanceof FirebaseNetworkException)
                        Toast.makeText(MovieDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MovieDetails.this, "Error! Name not fetched", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}