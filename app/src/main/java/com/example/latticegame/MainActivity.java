package com.example.latticegame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


/**
 * 九宮格抽獎 2.0
 * 邏輯: 單選，按了2秒後蓋牌，直到按中後再蓋牌
 */
public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private GridView gridview;
    private String[] text = {"1", "2", "3", "4", "5", "6",
            "7", "8", "9"};
    private int[] unselect = {R.drawable.unselect_item1, R.drawable.unselect_item2, R.drawable.unselect_item3,
            R.drawable.unselect_item4, R.drawable.unselect_item5, R.drawable.unselect_item6, R.drawable.unselect_item7,
            R.drawable.unselect_item8, R.drawable.unselect_item9,};
    private int lastSelectNum = -1;
    ImageView imageView;
    ImageView preImageView;
    Myadapter adapter;
    ArrayList list = new ArrayList();
    Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new Myadapter(MainActivity.this, text, unselect);
        gridview = (GridView) findViewById(R.id.gridview);
        setLattice();
//        gridview.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "你選取了" + text[+position], Toast.LENGTH_SHORT).show();
                View childview = gridview.getChildAt(position); //取得子view 做修改
                imageView = childview.findViewById(R.id.grid_image);
                gridview.setEnabled(false); //用按下了後直接禁按，直到蓋牌或中獎後在解禁，來達到單選的效果
                int DiceNum = r.nextInt(100);
                Log.i(TAG, "DiceNum: " + DiceNum);
                Log.i(TAG, "position: " + position);
                Log.i(TAG, "lastSelectNum: " + lastSelectNum);
                if (DiceNum < 70) { //沒中
                    list.add(position);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                imageView.setImageResource(R.drawable.lose); //沒中就顯示2s 之後蓋牌
                                Thread.sleep(2000);
                                setLattice();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    imageView.setImageResource(unselect[position]);
                    gridview.setEnabled(true);
                    showDialog();
                }

            }
        });
    }

    private void setUnselect(int lastPosition) {
        View preChildview = gridview.getChildAt(lastPosition);
        preImageView = preChildview.findViewById(R.id.grid_image);
        preImageView.setImageResource(unselect[lastPosition]);
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_dialog);
//        dialog.setCancelable(false); //點其他地方也不會取消
        dialog.show();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000); //5秒後結束 dialog
////                            dialog.cancel();
//                    dialog.dismiss();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    private void setLattice() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gridview.setAdapter(adapter);
                gridview.setEnabled(true);
            }
        });
        if (list.size() > 1) {
            list.clear();
        }
        Log.i(TAG, "list size: " + list.size());

    }

}
