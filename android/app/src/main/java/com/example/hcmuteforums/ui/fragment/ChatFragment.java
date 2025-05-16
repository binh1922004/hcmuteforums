package com.example.hcmuteforums.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.ChatAdapter;
import com.example.hcmuteforums.data.remote.api.ChatApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.ChatMessage;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.response.FollowResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messages = new ArrayList<>();
    private EditText inputQuestion;
    private Button sendButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        inputQuestion = view.findViewById(R.id.edit_question);
        sendButton = view.findViewById(R.id.button_send);

        chatAdapter = new ChatAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(v -> sendQuestion());

        return view;
    }

    private void sendQuestion() {
        String question = inputQuestion.getText().toString().trim();
        if (question.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập câu hỏi", Toast.LENGTH_SHORT).show();
            return;
        }


        ChatApi chatApi = LocalRetrofit.getRetrofit().create(ChatApi.class);
        chatApi.askChatBot(question).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    ApiResponse<String> apiRes = response.body();
                    if(apiRes.getResult()!=null){
                        String answer = apiRes.getResult();
                        ChatMessage chatMessage = new ChatMessage(question, answer);
                        messages.add(chatMessage);
                        chatAdapter.notifyItemInserted(messages.size() - 1);
                        recyclerView.scrollToPosition(messages.size() - 1);

                    }else{
                        Toast.makeText(getContext(), "Loi nhan duoc roi", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(response.errorBody()!=null){
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        Toast.makeText(getContext(), apiError.getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "Loi k gui duoc roi", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable throwable) {
                Log.d("Error ToFollow", throwable.getMessage());
            }
        });
        inputQuestion.setText("");
    }
}