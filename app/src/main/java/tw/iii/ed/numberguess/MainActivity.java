package tw.iii.ed.numberguess;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private String answer;
    private TextView inputEdit, logText;
    private boolean isWinner;
    private int counter;
    private String[] items = {"2", "3", "4", "5", "6", "7"};
    private int digit;
    private long lastKeyTime = System.currentTimeMillis();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputEdit = (TextView) findViewById(R.id.inputText);
        logText = (TextView) findViewById(R.id.logText);
        digit = 3;
        initGame();
    }
    // init
    private void initGame(){
        answer = createAnswer(digit);
        Log.d("brad","answer is " + answer);
        counter = 0;
        isWinner = false;
        logText.setText("");
    }

    // create answer
    private String createAnswer (int len){
        Set<Integer> set = new HashSet<>();
        while(set.size() < len){
            set.add((int)(Math.random() * 10));
        }
        StringBuilder sb = new StringBuilder();
        for(int digit : set){
            sb.append(digit);
        }
        return sb.toString();
    }
    // check answer
    private String checkAB(String a, String g){
        int A, B; A = B =0;
        for(int i = 0; i < g.length(); i++){
            if(g.charAt(i) == a.charAt(i)){
                A++;
            }else if(a.indexOf(g.charAt(i)) >= 0){
                B++;
            }
        }
        return A + "A" + B + "B";
    }

    public void doGuess(View view){
        String guess = inputEdit.getText().toString();
        // unlock guess button only if input matches answer's length
        if(guess.length() == digit){
            counter++;
            String result = checkAB(answer, guess);

            // if digit == 7 , the logText will overflow
            if(digit == 7){
                logText.setTextSize(20);
            }else{
                logText.setTextSize(24);
            }

            logText.append(counter + ". " + guess + " : " + result + "\n");
            inputEdit.setText("");
            if(result.equals(digit +"A0B")){
                // Winner
                isWinner = true;
                showDialog();
            }else if(counter == 10){
                // Loser
                isWinner = false;
                showDialog();
            }
        }else{
            // show hint : input more numbers
            Toast.makeText(this, "Need "+(digit - guess.length())+" more number", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(){
        AlertDialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Result");
        builder.setMessage(isWinner?"Winner":"Loser : " + answer);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initGame();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    //nums input button
    public void doAction(View view){
        int id = view.getId() - 2131427427;
        StringBuilder sb = new StringBuilder();
        //Log.i("brad", ""+id);
        if(id < 10){
            String tmp = inputEdit.getText().toString();
            // limit input length & prevent repeat number
            if(!tmp.contains(""+id) && tmp.length() < digit){
                sb.append(inputEdit.getText().toString() + id);
                inputEdit.setText(sb.toString());
            }
        }else if (id == 10){
            sb.append(inputEdit.getText().toString());
            inputEdit.setText(sb.substring(0,
                    (sb.length() - 1) < 0 ? 0 : sb.length() - 1));
        }else if (id == 11){
            inputEdit.setText("");
        }
    }

    // reset, setting, exit => Option  all in one
    public void doOption(View view){
        AlertDialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Button btn = (Button) view;
        String name = btn.getText().toString();
        builder.setTitle(name);
        builder.setCancelable(false);
        //Log.i("brad", name);
        switch (name){
            case "Reset":
                builder.setMessage("Yes or No");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initGame();
                    }
                });
                builder.setNegativeButton("No", null);
                break;
            case "Setting":
                builder.setSingleChoiceItems(items, digit - 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        digit = i + 2;
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initGame();
                    }
                });
                break;
            case "Exit":
                builder.setTitle("Exit?");
                builder.setMessage("Yes or No");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);
                break;
        }
        dialog = builder.create();
        dialog.show();
    }

    // confirmation of exiting
    @Override
    public void finish() {
        if(System.currentTimeMillis() - lastKeyTime <= 3*1000){
            super.finish();
        }else{
            Toast.makeText(this, "Press again", Toast.LENGTH_SHORT).show();
        }
        lastKeyTime = System.currentTimeMillis();
    }
}
