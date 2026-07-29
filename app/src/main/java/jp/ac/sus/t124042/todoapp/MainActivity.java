package jp.ac.sus.t124042.todoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private TextView textViewGreeting;
    private LinearLayout taskContainer;
    private final ArrayList<String> taskLines = new ArrayList<>();

    //アプリ起動時の初期設定
    @Override
    protected void onCreate(Bundle savedInstanceState) {     //アプリが起動時に一度だけ実行
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewGreeting = findViewById(R.id.textViewGreeting);
        taskContainer = findViewById(R.id.taskContainer);
    }

    @Override
    protected void onResume() {     //メイン画面に戻ってくるたびに実行
        super.onResume();
        setView();
    }

    private void setView() {    //第12回目の講義を参考
        SharedPreferences prefs =
                getSharedPreferences("prefs", MODE_PRIVATE);

        String name = prefs.getString("name", "ゲスト");

        textViewGreeting.setText(
                "こんにちは " + name + " さん"
        );

        loadTasks();
    }

    private void loadTasks() {   //タスクを読み込み画面表示
        taskContainer.removeAllViews();
        taskLines.clear();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        openFileInput("tasks.dat")))) {

            String line;
            int index = 0;

            while ((line = br.readLine()) != null) {
                taskLines.add(line);

                String[] data = line.split("\\|", -1);

                String task;
                String deadline;
                String category;
                String memo;
                String url;
                boolean completed;

                if (data.length >= 6) {   //チェックの判定
                    task = data[0];
                    deadline = data[1];
                    category = data[2];
                    memo = data[3];
                    url = data[4];
                    completed = Boolean.parseBoolean(data[5]);

                } else if (data.length >= 5) {
                    task = data[0];
                    deadline = data[1];
                    category = data[2];
                    memo = data[3];
                    url = "";
                    completed = Boolean.parseBoolean(data[4]);

                } else {
                    continue;
                }

                addTaskView(
                        index,
                        task,
                        deadline,
                        category,
                        memo,
                        url,
                        completed
                );

                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (taskLines.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText("登録されたタスクはありません");
            emptyView.setPadding(8, 16, 8, 16);

            taskContainer.addView(emptyView);
        }
    }

    private void addTaskView(    //タスクの追加
            int taskIndex,
            String task,
            String deadline,
            String category,
            String memo,
            String url,
            boolean completed) {

        LinearLayout taskLayout = new LinearLayout(this);
        taskLayout.setOrientation(LinearLayout.VERTICAL);
        taskLayout.setPadding(16, 16, 16, 16);

        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(task);
        checkBox.setChecked(completed);

        checkBox.setOnCheckedChangeListener(
                (buttonView, isChecked) ->
                        updateTaskStatus(taskIndex, isChecked)
        );

        taskLayout.addView(checkBox);

        TextView detailView = new TextView(this);

        StringBuilder sb = new StringBuilder();
        sb.append("締切：").append(deadline).append("\n");
        sb.append("分類：").append(category);

        if (!memo.isEmpty()) {
            sb.append("\nメモ：").append(memo);
        }

        if (!url.isEmpty()) {
            sb.append("\nURL：").append(url);
        }

        detailView.setText(sb.toString());
        taskLayout.addView(detailView);

        if (!url.isEmpty()) {
            Button webButton = new Button(this);
            webButton.setText("Webページを開く");

            final String targetUrl = url;

            webButton.setOnClickListener(view -> {
                Intent intent =
                        new Intent(MainActivity.this, WebActivity.class);

                intent.putExtra("url", targetUrl);
                startActivity(intent);
            });

            taskLayout.addView(webButton);
        }

        TextView separator = new TextView(this);
        separator.setText("------------------------------");

        taskLayout.addView(separator);
        taskContainer.addView(taskLayout);
    }

    private void updateTaskStatus(     //チェックボックスが押されたときに実行
            int taskIndex,
            boolean completed) {

        if (taskIndex < 0 || taskIndex >= taskLines.size()) {
            return;
        }

        String oldLine = taskLines.get(taskIndex);
        String[] data = oldLine.split("\\|", -1);

        String newLine;

        if (data.length >= 6) {
            data[5] = String.valueOf(completed);

            newLine =
                    data[0] + "|" +
                            data[1] + "|" +
                            data[2] + "|" +
                            data[3] + "|" +
                            data[4] + "|" +
                            data[5];

        } else if (data.length >= 5) {
            data[4] = String.valueOf(completed);

            newLine =
                    data[0] + "|" +
                            data[1] + "|" +
                            data[2] + "|" +
                            data[3] + "|" +
                            data[4];

        } else {
            return;
        }

        taskLines.set(taskIndex, newLine);
        saveAllTasks();
    }

    private void saveAllTasks() {    //アプリを終了してもチェック状態が残るように
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(
                        openFileOutput(
                                "tasks.dat",
                                Context.MODE_PRIVATE
                        )))) {

            for (String line : taskLines) {
                pw.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openAddTask(View view) {     //タスク追加ボタンが押されると実行
        Intent intent =
                new Intent(this, AddTaskActivity.class);

        startActivity(intent);
    }

    public void openSetting(View view) {     //設定画面へ移動
        Intent intent =
                new Intent(this, SettingActivity.class);

        startActivity(intent);
    }

    public void openStatistics(View view) {     //進捗状況画面へ移動
        Intent intent =
                new Intent(this, StatisticsActivity.class);

        startActivity(intent);
    }
}