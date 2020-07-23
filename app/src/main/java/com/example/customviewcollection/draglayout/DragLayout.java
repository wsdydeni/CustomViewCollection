package com.example.customviewcollection.draglayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.customviewcollection.R;

public class DragLayout extends RelativeLayout {

    private Button chooseButton;
    private Button chooseDirectionButton;
    private AlertDialog alertDialog;
    private TestViewGroup testViewGroup;
    private boolean isChoose = false;

    public DragLayout(Context context) { this(context,null); }

    public DragLayout(Context context, AttributeSet attrs) { this(context, attrs,0); }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.layout_drag,this);
        createDialog(context);
        chooseButton = view.findViewById(R.id.choose_btn);
        testViewGroup = view.findViewById(R.id.layout_drag_content);
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//        PersonView personView = new PersonView(context,null,0);
//        personView.setOnClickListener(v -> {
//            personView.setChoosed(!personView.isChoosed());
//            Toast.makeText(context, "单击", Toast.LENGTH_SHORT).show();
//        });
//        testViewGroup.addView(personView,layoutParams);
        chooseDirectionButton = view.findViewById(R.id.choose_direction_btn);
        chooseDirectionButton.setOnClickListener(v -> alertDialog.show());
        chooseButton.setOnClickListener(v -> {
            isChoose = !isChoose;
            testViewGroup.setMultiChoose(isChoose);
            chooseButton.setText(isChoose ? "多选" : "选择");
            chooseDirectionButton.setVisibility(isChoose ? View.VISIBLE : View.GONE);
        });
    }

    private void createDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_direction_layout, null);
        alertDialog = new AlertDialog.Builder(context).setView(view).create();
        TextView textView1 = view.findViewById(R.id.choose_30_btn);
        TextView textView2 = view.findViewById(R.id.choose_60_btn);
        TextView textView3 = view.findViewById(R.id.choose_180_btn);
        TextView textView4 = view.findViewById(R.id.choose_270_btn);
        textView1.setOnClickListener(v -> {
            Toast.makeText(context, "30", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });
        textView2.setOnClickListener(v -> {
            Toast.makeText(context, "60", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });
        textView3.setOnClickListener(v -> {
            Toast.makeText(context, "180", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });
        textView4.setOnClickListener(v -> {
            Toast.makeText(context, "270", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });
    }


}