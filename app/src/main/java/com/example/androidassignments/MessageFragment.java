package com.example.androidassignments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MessageFragment extends Fragment {

    ChatWindow chat;

    public MessageFragment(ChatWindow chat){
        this.chat = chat;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View result = inflater.inflate(R.layout.fragment,container,false);
        String message = getArguments().getString("message");
        final long id = getArguments().getLong("id");
        final TextView messageV = (TextView) result.findViewById(R.id.textView6);
        messageV.setText(message);
        final TextView idV = (TextView)result.findViewById(R.id.textView7);
        idV.setText(""+id);
        Button delete = result.findViewById(R.id.button7);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chat == null){
                    Log.d("Wrong place", "onClick: ");
                    Intent data = new Intent();
                    String id1 = idV.getText().toString();
                    data.putExtra("id",id1);
                    getActivity().setResult(Activity.RESULT_OK,data);
                    getActivity().finish();
                }
                else{

                    chat.deleteRow((int)id);


                }

            }
        });
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}