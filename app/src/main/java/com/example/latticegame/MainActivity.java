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

import java.util.Random;


/**
 * 九宮格抽獎 1.0
 * 邏輯:沒中獎的就喚回上一個按過的格子
 * 缺點:邏輯只能走中獎一次之後就不能玩了
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Myadapter adapter = new Myadapter(MainActivity.this, text, unselect);
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
        gridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "你選取了" + text[+position], Toast.LENGTH_SHORT).show();
                View childview = gridview.getChildAt(position); //取得子view 做修改
                imageView = childview.findViewById(R.id.grid_image);
                Random r = new Random();
                int DiceNum = r.nextInt(100);
                Log.i(TAG, "DiceNum: " + DiceNum);
                Log.i(TAG, "position: "+ position);
                Log.i(TAG, "lastSelectNum: " + lastSelectNum);
                if (DiceNum < 50) { //沒中
                    if (lastSelectNum != position && lastSelectNum != -1) {
                        setUnselect(lastSelectNum); //把上一個按過的格子換回
                    }
                    imageView.setImageResource(R.drawable.lose); //沒中就要變
                } else {
                    imageView.setImageResource(unselect[position]);
                    showDialog();
                }
                lastSelectNum = position;
            }
        });
    }

    private void setUnselect(int lastPosition) {
        View preChildview = gridview.getChildAt(lastPosition);
        preImageView =  preChildview.findViewById(R.id.grid_image);
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

}
