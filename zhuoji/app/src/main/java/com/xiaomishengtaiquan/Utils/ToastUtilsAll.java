package com.xiaomishengtaiquan.Utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomishengtaiquan.R;

/**
 * Created by chenda on 2018/4/2.
 */

public class ToastUtilsAll {

    //普通的弹框
    public static void alertContent(Context context, String content) {
        Toast.makeText(context, "" +
                content, Toast.LENGTH_LONG).show();
    }
    //可以改变位置的弹框
    public static  void alertLocation(Context context, String content)
    {
        Toast toast=Toast.makeText(context,content,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,50,50);//第二个参数如果/是大于0，向右偏移，小于0向左偏移；第三个参数大于0向下偏移，小于0向上偏移，以上偏移均在第一个参数你设置的位置的基础上
        toast.show();
    }

    //自定义带图片的提示框
    public static  void alertImage(Context context,String txt)
    {
        Toast toast2=Toast.makeText(context, txt, Toast.LENGTH_LONG);
        LinearLayout view=(LinearLayout) toast2.getView();//获取当前toast所在的布局,.xml文件里的布局为线性布局

        ImageView imageView=new ImageView(context);
        imageView.setImageResource(R.drawable.dialog_loading);//将图片加入toast所在的布局

        view.addView(imageView,0);//没有第二个参数的时候默认图片在下面，如果，第二个参数为0，图片在上面，如果第二个参数为1，图片在下面
        toast2.show();
    }
    //自定义弹框
    public static  void alertDefine(Context context,String txt)
    {
        LayoutInflater inflater=LayoutInflater.from(context);
        View root=inflater.inflate(R.layout.define_dilog, null);//创建一个view
        TextView content=root.findViewById(R.id.content);
        content.setText(txt);
        TextView title=root.findViewById(R.id.title_define);
        title.setText("温馨提示");
        Toast toast3=new Toast(context);//完全自定的toast要用构造函数来生成对象
        toast3.setView(root);//设置自定义toast样式
        toast3.setDuration(Toast.LENGTH_LONG);//设置toast的显示时间
        toast3.show();

    }
}
