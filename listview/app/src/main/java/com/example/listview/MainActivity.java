package com.example.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        listView.setAdapter(new MyAdapter());
        SettingListenser();
    }

    // Inialize_component
    private void initialize(){
        listView = findViewById(R.id.listView_main) ;
    }

    // Event Listenser setting
    private  void SettingListenser(){
        listView.setOnItemClickListener(onItemClick);
    }

    // Event Set
    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(MainActivity.this,"Number:"+(position+1),Toast.LENGTH_SHORT).show();
        }
    };

    // About new class  extends Baseadapter
    private class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView ;
            Holder holder;
            // Check first
            if (view ==null){
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.style_listview,null);

                holder =new Holder();
                holder.imageView=view.findViewById(R.id.imageView);
                holder.textView_name=view.findViewById(R.id.textView_name);
                holder.textView_height=view.findViewById(R.id.textView_height);
                holder.textView_weight=view.findViewById(R.id.textView_weight);
                holder.textView_bmi=view.findViewById(R.id.textView_bmi);
                holder.textView_bmr=view.findViewById(R.id.textView_bmr);

                view.setTag(holder);

            }else{
                holder = (Holder)view.getTag();
            }

            // Basic setting is finished
            // Below it to put data in listView
            try{
                switch (position){
                    case 0:
                        holder.imageView.setImageResource(R.drawable.test_a);
                        holder.textView_name.setText("Ann");
                        break;
                    case 1:
                        holder.imageView.setImageResource(R.drawable.test_b);
                        holder.textView_name.setText("GGGGG");
                        break;
                    case 2:
                        holder.imageView.setImageResource(R.drawable.test_c);
                        holder.textView_name.setText("Jimmy");
                        break;
                }
            }catch (Exception e){
                Toast.makeText(MainActivity.this,"Error Happen",Toast.LENGTH_SHORT).show();
            }

            return view;
        }
    }
    //About new class Hold
    private class Holder{
        ImageView imageView;

        TextView textView_name;
        TextView textView_height;
        TextView textView_weight;
        TextView textView_bmi;
        TextView textView_bmr;
    }
}
