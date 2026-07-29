package jp.ac.sus.t124042.todoapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class AddTaskActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private EditText editTextTask;
    private EditText editTextDeadline;
    private EditText editTextCategory;
    private EditText editTextMemo;
    private EditText editTextUrl;
    private TextView textViewMessage;

    //タスク追加画面の初期設定
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        textViewTitle = findViewById(R.id.textViewTitle);
        editTextTask = findViewById(R.id.editTextTask);
        editTextDeadline = findViewById(R.id.editTextDeadline);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextMemo = findViewById(R.id.editTextMemo);
        editTextUrl = findViewById(R.id.editTextUrl);
        textViewMessage = findViewById(R.id.textViewMessage);

        applySettings();
    }

    private void applySettings() {
        SharedPreferences prefs =
                getSharedPreferences("prefs", MODE_PRIVATE);

        int textSize = prefs.getInt("textSize", 18);

        textViewTitle.setTextSize(textSize + 4);
        editTextTask.setTextSize(textSize);
        editTextDeadline.setTextSize(textSize);
        editTextCategory.setTextSize(textSize);
        editTextMemo.setTextSize(textSize);
        editTextUrl.setTextSize(textSize);
        textViewMessage.setTextSize(textSize);
    }

    //入力されたタスクをファイルへ保存する
    public void saveTask(View view) {
        String task = sanitize(
                editTextTask.getText().toString()
        );

        String deadline = sanitize(
                editTextDeadline.getText().toString()
        );

        String category = sanitize(
                editTextCategory.getText().toString()
        );

        String memo = sanitize(
                editTextMemo.getText().toString()
        );

        String url = sanitize(
                editTextUrl.getText().toString()
        );

        if (task.isEmpty()) {
            textViewMessage.setText(
                    "タスク名を入力してください"
            );
            return;
        }

        if (deadline.isEmpty()) {
            deadline = "未設定";
        }

        if (category.isEmpty()) {
            category = "その他";
        }

        /*
         * https:// が省略された場合に追加する
         */
        if (!url.isEmpty()
                && !url.startsWith("http://")
                && !url.startsWith("https://")) {

            url = "https://" + url;
        }

        String lineSeparator = System.lineSeparator();

        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(
                        openFileOutput(
                                "tasks.dat",
                                Context.MODE_APPEND
                        )))) {

            /*
             * タスク|締切|分類|メモ|URL|完了状態
             */
            pw.print(task);
            pw.print("|");
            pw.print(deadline);
            pw.print("|");
            pw.print(category);
            pw.print("|");
            pw.print(memo);
            pw.print("|");
            pw.print(url);
            pw.print("|");
            pw.print("false");
            pw.print(lineSeparator);

            textViewMessage.setText(
                    "タスクを保存しました"
            );

            clearInput();

        } catch (Exception e) {
            e.printStackTrace();

            textViewMessage.setText(
                    "タスクの保存に失敗しました"
            );
        }
    }

    private String sanitize(String text) {
        return text
                .replace("|", "｜")
                .replace("\r", " ")
                .replace("\n", " ")
                .trim();
    }

    private void clearInput() {
        editTextTask.getEditableText().clear();
        editTextDeadline.getEditableText().clear();
        editTextCategory.getEditableText().clear();
        editTextMemo.getEditableText().clear();
        editTextUrl.getEditableText().clear();
    }

     //メイン画面へ戻る
    public void backToMain(View view) {
        finish();
    }
}