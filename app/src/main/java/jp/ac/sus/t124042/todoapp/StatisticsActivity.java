package jp.ac.sus.t124042.todoapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class StatisticsActivity extends AppCompatActivity {

    private ProgressView progressView;


    //達成状況画面の初期設定
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        progressView = findViewById(R.id.progressView);

        loadTaskCount();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadTaskCount();
    }

    //タスク数と完了数を読み込み、星の位置を更新する
    private void loadTaskCount() {

        int totalCount = 0;
        int completedCount = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        openFileInput("tasks.dat")))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\|", -1);

                /*
                 * 新しい保存形式
                 * タスク名|締切|分類|メモ|URL|完了状態
                 */
                if (data.length >= 6) {

                    totalCount++;

                    boolean completed =
                            Boolean.parseBoolean(data[5]);

                    if (completed) {
                        completedCount++;
                    }

                    /*
                     * URL追加前の古い保存形式にも対応
                     * タスク名|締切|分類|メモ|完了状態
                     */
                } else if (data.length >= 5) {

                    totalCount++;

                    boolean completed =
                            Boolean.parseBoolean(data[4]);

                    if (completed) {
                        completedCount++;
                    }
                }
            }

        } catch (Exception e) {
            // 初回起動時など、tasks.datがない場合
            e.printStackTrace();
        }

        progressView.setTaskCount(
                totalCount,
                completedCount
        );
    }

    //メイン画面へ戻る
    public void backToMain(View view) {
        finish();
    }
}