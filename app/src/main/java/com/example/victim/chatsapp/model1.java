package com.example.victim.chatsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class model1 extends AppCompatActivity {
    DatabaseReference db;
    Button group,logout;
    FirebaseHelper1 helper;
    Adapter1 adapter;
    ListView lv;
    TextView t;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);
        lv = (ListView) findViewById(R.id.lv);
        group=(Button) findViewById(R.id.group);
        logout=(Button) findViewById(R.id.logout);
        t=(TextView) findViewById(R.id.t);
        t.setText("Hello "+UserDetails.username);
        lv.setBackgroundResource(R.drawable.rounded_corner1);
        //INITIALIZE FIREBASE DB
        db = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper1(db);
        //ADAPTER

        adapter = new Adapter1(this, helper.retrieve());
        lv.setAdapter(adapter);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(model1.this, com.example.victim.chatsapp.group.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(model1.this);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("LOGIN",1);
                editor.commit();
                startActivity(new Intent(model1.this,Login.class));
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = ((TextView) view.findViewById(R.id.username)).getText().toString();
              /*  final ProgressDialog pd = new ProgressDialog(model1.this);
                pd.setMessage("Loading...");
                pd.show();

                ImageView img=(ImageView) view.findViewById(R.id.pic);
                Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes = baos.toByteArray();
                String imgString = Base64.encodeToString(bytes, Base64.DEFAULT);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteFormat = stream.toByteArray();
                String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                UserDetails.encoded=imgString;*/
                startActivity(new Intent(model1.this, Chat.class));
            }
        });
    }
}
class Adapter1 extends BaseAdapter {
    Context c;
    Animation animationUp,animationDown;
    ArrayList<user1> uses;
    public Adapter1(Context c, ArrayList<user1> uses) {
        this.c = c;
        this.uses = uses;
    }
    @Override
    public int getCount() {
        return uses.size();
    }
    @Override
    public Object getItem(int position) {
        return uses.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.activity_model2,parent,false);
        }
        TextView username=(TextView) convertView.findViewById(R.id.username);
        ImageView pic=(ImageView) convertView.findViewById(R.id.pic);
        final user1 s= (user1) this.getItem(position);

            username.setText(s.getUsername());
            String encoded = s.getPic();

            byte[] dec = Base64.decode(encoded, Base64.DEFAULT);
            Bitmap decodeByte = BitmapFactory.decodeByteArray(dec, 0, dec.length);
            pic.setImageBitmap(decodeByte);

        return convertView;
    }


}
class FirebaseHelper1 {
    DatabaseReference db;
    ArrayList<user1> uses=new ArrayList<>();

    public FirebaseHelper1(DatabaseReference db) {
        this.db = db;
    }

    public ArrayList<user1> retrieve()
    {


        db.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uses.clear();
                for(DataSnapshot inters:dataSnapshot.getChildren()){

                    user1 use=inters.getValue(user1.class);
                    if(!UserDetails.username.equals(use.getUsername()))
                    uses.add(use);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return uses;
    }
}

