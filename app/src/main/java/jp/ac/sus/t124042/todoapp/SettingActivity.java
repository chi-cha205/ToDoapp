package jp.ac.sus.t124042.todoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    private EditText editTextName;
    private TextView textViewSettingMessage;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    //設定画面の初期設定
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        editTextName = findViewById(R.id.editTextName);
        textViewSettingMessage = findViewById(R.id.textViewSettingMessage);

        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        editor = prefs.edit();

        String name = prefs.getString("name", "");
        editTextName.setText(name);
    }

    //入力した名前をSharedPreferencesへ保存する
    public void saveSetting(View view) {

        String name = editTextName.getText().toString().trim();

        if (name.isEmpty()) {
            name = "ゲスト";
        }

        editor.putString("name", name);
        editor.apply();

        textViewSettingMessage.setText("設定を保存しました");
    }

    //メイン画面へ戻る
    public void backToMain(View view) {
        finish();
    }
}