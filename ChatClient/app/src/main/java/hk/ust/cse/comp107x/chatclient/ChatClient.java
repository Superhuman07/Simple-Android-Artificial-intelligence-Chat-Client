package hk.ust.cse.comp107x.chatclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import org.alicebot.ab.*;

public class ChatClient extends Activity implements View.OnClickListener {

    Button sendButton;
    EditText messageText;
    ListView messageList;
    MyArrayAdapter mAdapter = null;
    ArrayList<Message> messages = null;
    Chat chatsession =null;

    int in_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_client);

        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);

        messageText = (EditText) findViewById(R.id.messageText);

        messageList = (ListView) findViewById(R.id.messageList);

        // messages = new ArrayList<String>();
        messages = new ArrayList<Message>();

        // mAdapter = new ArrayAdapter<String>(this, R.layout.mymessage, R.id.mymessageTextView, messages);
        mAdapter = new MyArrayAdapter(this, messages);

        messageList.setAdapter((ListAdapter) mAdapter);




        //bot extract
        File file = new File(getExternalFilesDir(null).getAbsolutePath()+"/bots");
        if (!file.exists()) {
            ZipFileExtraction extraction = new ZipFileExtraction();
            try
            {
                extraction.unZipIt(getAssets().open("bots.zip"),getExternalFilesDir(null).getAbsolutePath()+"/");

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        String path = getExternalFilesDir(null).getAbsolutePath();
        Bot bot = new Bot ("alice2", path);
        chatsession = new Chat(bot);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //chat bot chats
    public void sendMessage(String message)
    {
        String respose = chatsession.multisentenceRespond(message);
        Message botmessage = new Message("alice2", respose, false, new Date());
        messages.add(botmessage);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sendButton:

                String messString = messageText.getText().toString();
                if (!messString.equals("")) {
                    Message message = new Message("", messString, true, new Date());
                    messages.add(message);
                    mAdapter.notifyDataSetChanged();
                    sendMessage(messString);
                    message = null;
                    messageText.setText("");

                }

                break;

            default:
                break;
        }
    }


    /*public void sendMessage() {

        String[] incoming = {"Hey, How's it going?",
                "Super! Let's do lunch tomorrow",
                "How about Mexican?",
                "Great, I found this new place around the corner",
                "Ok, see you at 12 then!"};

        if (in_index < incoming.length) {
            Message message = new Message("John", incoming[in_index], false,  new Date());
            messages.add(message);
            in_index++;
            mAdapter.notifyDataSetChanged();
        }*/


}
