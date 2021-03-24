package com.example.workingwithrecyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public static ArrayList<DataFavs> dataList = new ArrayList<>();
    private EditText editText;
    private EditText editTextGetName;
    private Button btnInsertData;
    private Button btnSetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        editText = findViewById(R.id.idEditTextgetData);
        editTextGetName = findViewById(R.id.idEditTextgetName);
        btnInsertData = findViewById(R.id.idBtnInsertData);
        btnSetData = findViewById(R.id.idBtnsetData);

    }

    public void saveData(View view) {
        String name = editTextGetName.getText().toString();
        String editTextData = editText.getText().toString();
        databaseReference.child("favourites").child(name).setValue(editTextData);
    }

    public void setData(View view) {
        saveDataToList();
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        Log.e("tag", dataList.toString());
        intent.putExtra("textData", dataList);
        startActivity(intent);
    }

    private void saveDataToList() {
        databaseReference.child("favourites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {

                    String name = datasnapshot.getKey();
                    String target = (String) datasnapshot.getValue();

                    dataList.add(new DataFavs(name, target));
                    Log.e("tag", name + " " + target);
                    Log.e("tag", dataList.toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Data not Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

}