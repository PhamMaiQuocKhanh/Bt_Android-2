package com.example.bt_android_2;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    MessageAdapter adapter;

    List<MessageModule> messageList; //bien toan cuc
    AppCompatButton button;
    TextView id,iduser,title,message, txt_dem;
    Gson gson;
    MessageModule messageModule = new MessageModule();
    private static final String CHANNEL_ID = "my_channel_id";
    String json;
    int dem=0;
    Context myContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anhXa();
        gson = new Gson();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messageList = new ArrayList<>();

                // Tạo Retrofit instance
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://jsonplaceholder.typicode.com/posts/") // URL chính xác của API
                        .addConverterFactory(GsonConverterFactory.create()) // Dùng Gson để parse JSON
                        .build();

                Api apiService = retrofit.create(Api.class);
                Call<List<MessageModule>> call = apiService.getJsonData(); // Sửa Call<List<MessageModule>>

                call.enqueue(new Callback<List<MessageModule>>() {
                    @Override
                    public void onResponse(Call<List<MessageModule>> call, Response<List<MessageModule>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            messageList = response.body();
                            dem=0;
                            if (!messageList.isEmpty()) {
                                HienThi1PT();
                                MessageAdapter adapter;
                                adapter = new MessageAdapter(getApplicationContext(), messageList);
                                recyclerView.setAdapter(adapter);
                                MessageModule messageModule = messageList.get(dem); // Lấy bài viết đầu tiên
                                id.setText(String.valueOf(messageModule.getId()));
                                iduser.setText(String.valueOf(messageModule.getUserId()));
                                title.setText(messageModule.getTitle());
                                message.setText(messageModule.getBody());
                                dem++;
                                showNotification(messageModule.getTitle(),messageModule.getBody());
                            }
                        } else {
                            System.err.println("Response is empty or unsuccessful");
                        }
                    }
                    @Override
                    public void onFailure(Call<List<MessageModule>> call, Throwable t) {
                        System.err.println("Error: " + t.getMessage());
                    }
                });
            }
        });
    }

    private void anhXa() {
        button = findViewById(R.id.button);
        id = findViewById(R.id.id1);
        title = findViewById(R.id.title1);
        message = findViewById(R.id.message1);
        iduser = findViewById(R.id.iduser1);
        txt_dem = findViewById(R.id.txt_dem);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    void HienThi1PT(){
        if(messageList==null) return;
        else {
            MessageModule messageModule = messageList.get(dem); // Lấy bài viết đầu tiên
            id.setText(String.valueOf(messageModule.getId()));
            iduser.setText(String.valueOf(messageModule.getUserId()));
            title.setText(messageModule.getTitle());
            message.setText(messageModule.getBody());
            txt_dem.setText(""+(dem+1)+"/"+messageList.size());
            showNotification(messageModule.getTitle(),messageModule.getBody());
        }

    }

    private void showNotification(String title, String body) {
    }
}