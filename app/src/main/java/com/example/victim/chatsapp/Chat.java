package com.example.victim.chatsapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity implements EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    private static final int PICK_IMAGE_REQUEST = 234;
    LinearLayout layout;
    ImageView sendButton;
    TextView username;
    ScrollView scrollView;
    DatabaseReference db;
    ImageButton button,media;
    FrameLayout lay;


    ImageButton b,k;  EmojiconEditText mEditEmojicon;
    Firebase reference1, reference2;

    int mid = 0, temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        lay=(FrameLayout) findViewById(R.id.emojicons);
        layout = (LinearLayout) findViewById(R.id.layout1);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        media = (ImageButton) findViewById(R.id.media);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        username = (TextView) findViewById(R.id.username);
        b=(ImageButton) findViewById(R.id.b);
        k=(ImageButton) findViewById(R.id.k);
        lay.setVisibility(View.GONE);
        k.setVisibility(View.GONE);

        mEditEmojicon = (EmojiconEditText) findViewById(R.id.editEmojicon);
        button = (ImageButton) findViewById(R.id.button);
        Firebase.setAndroidContext(this);

        reference1 = new Firebase("https://androidchatapp-dfc08.firebaseio.com//messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://androidchatapp-dfc08.firebaseio.com//messages/" + UserDetails.chatWith + "_" + UserDetails.username);
        db = FirebaseDatabase.getInstance().getReference("users").child(UserDetails.chatWith).child("pic");
        username.setText(UserDetails.chatWith);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Chat.this,model1.class));
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = mEditEmojicon.getText().toString();
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => "+c.getTime());
                mEditEmojicon.setText(" ");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                if(!messageText.equals("")){
                    Map<String, String > map = new HashMap<String,String >();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    map.put("time",formattedDate);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String time=map.get("time").toString();
                if(!message.equals("Picture")) {
                    if (userName.equals(UserDetails.username)) {
                        addMessageBox(message + "\n" + time, 1);
                    } else {
                        addMessageBox(message + "\n" + time, 2);
                    }
                }
                else {
                    if (userName.equals(UserDetails.username)) {
                        generateImageView(map.get("pic").toString(), 1);
                    }
                    else
                        generateImageView(map.get("pic").toString(), 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        mEditEmojicon.addTextChangedListener(new TextWatcher() {

            /**
             * This notify that, within s,
             * the count characters beginning at start are about to be replaced by new text with length
             * @param s
             * @param start
             * @param count
             * @param after
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            /**
             * This notify that, somewhere within s, the text has been changed.
             * @param s
             */
            @Override
            public void afterTextChanged(Editable s) {}

            /**
             * This notify that, within s, the count characters beginning at start have just
             * replaced old text that had length
             * @param s
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            //message=s.toString();

            }
        });
        InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            lay.setVisibility(View.VISIBLE);
        } else {
            lay.setVisibility(View.GONE);
        }
        setEmojiconFragment(false);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  showFileChooser();
                lay.setVisibility(View.VISIBLE);
                k.setVisibility(View.VISIBLE);
                b.setVisibility(View.GONE);
            }
        });
        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  showFileChooser();
                lay.setVisibility(View.GONE);
                k.setVisibility(View.GONE);
                b.setVisibility(View.VISIBLE);
            }
        });
        media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

    }
    /**
     * Set the Emoticons in Fragment.
     * @param useSystemDefault
     */
    private void setEmojiconFragment(boolean useSystemDefault) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

    /**
     * It called, when click on icon of Emoticons.
     * @param emojicon
     */
    @Override
    public void onEmojiconClicked(Emojicon emojicon) {

        EmojiconsFragment.input(mEditEmojicon, emojicon);
    }

    /**
     * It called, when backspace button of Emoticons pressed
     * @param view
     */
    @Override
    public void onEmojiconBackspaceClicked(View view) {

        EmojiconsFragment.backspace(mEditEmojicon);
    }

    public void addMessageBox(String message, int type){
        EmojiconTextView textView = new EmojiconTextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);


        if(type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
            textView.setGravity(Gravity.RIGHT);
            lp.gravity=Gravity.RIGHT;
            textView.setLayoutParams(lp);
        }
        else{
            textView.setBackgroundResource(R.drawable.rounded_corner2);
            textView.setX(0);
           /* NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notify=new Notification.Builder
                    (getApplicationContext()).setContentTitle(UserDetails.chatWith).setContentText(message).
                    setContentTitle("new message").setSmallIcon(R.drawable.back).build();

            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.notify(0, notify);*/
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.abc)
                            .setContentTitle(UserDetails.chatWith)
                            .setContentText(message);
// Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(this, Users.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(Users.class);
// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mid=temp;
            mNotificationManager.notify(mid, mBuilder.build());
            mid++;

        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
        temp=mid;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            Bitmap bitm = null;
            try {
                bitm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            // imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            byte[] bytes = baos.toByteArray();
            String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => "+c.getTime());
            mEditEmojicon.setText(" ");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            if(!base64Image.equals("")){
                Map<String, String > map = new HashMap<String,String >();
                map.put("message","Picture");
                map.put("pic", base64Image);
                map.put("user", UserDetails.username);
                map.put("time",formattedDate);
                reference1.push().setValue(map);
                reference2.push().setValue(map);
            }

        }

    }
    public void generateImageView(String pic,int type)
    {
        ImageView imageView = new ImageView(Chat.this);

        byte[] dec = Base64.decode(pic,Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(dec, 0, dec.length);
        imageView.setImageBitmap(decodeByte);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200,200);
        lp.setMargins(0, 0, 0, 10);
        imageView.setLayoutParams(lp);
        if(type == 1) {
            imageView.setBackgroundResource(R.drawable.rounded_corner1);
            lp.gravity=Gravity.RIGHT;
            imageView.setLayoutParams(lp);
        }
        else {
            imageView.setBackgroundResource(R.drawable.rounded_corner2);
            imageView.setX(0);
        }
        layout.addView(imageView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
