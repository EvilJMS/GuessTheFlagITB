package cat.itb.geoguesser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity{
    QuizViewModel quizViewModel;
    TextView mainQuestion, progressText;
    ImageView flag;
    ProgressBar progressBar;
    Button answer1, answer2, answer3, answer4, hintButton;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (quizViewModel == null){
            quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        }
        mainQuestion = findViewById(R.id.question);
        flag = findViewById(R.id.imageFlag);
        progressBar = findViewById(R.id.progressBar);
        answer1 = findViewById(R.id.button1);
        answer2 = findViewById(R.id.button2);
        answer3 = findViewById(R.id.button3);
        answer4 = findViewById(R.id.button4);
        hintButton = findViewById(R.id.hintButton);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCorrectAnswer();
            }
        });
        progressText = findViewById(R.id.contadorPreguntes);
        if (quizViewModel.getCurrentIndex()==0){
            quizViewModel.randomQuestions();
        }
        showQuestion();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void showQuestion(){
        ArrayList<String> quiz = new ArrayList<>();
        flag.setImageDrawable(getResources().getDrawable(quizViewModel.getCurrentImage(quizViewModel.getCurrentIndex())));
        String msg = getString(R.string.contadorPreguntes, quizViewModel.getCurrentIndex() +1);
        progressText.setText(msg);
        quiz.add(quizViewModel.getCorrectAnswer(quizViewModel.getCurrentIndex()));
        quiz.add(quizViewModel.getAnswer1(quizViewModel.getCurrentIndex()));
        quiz.add(quizViewModel.getAnswer2(quizViewModel.getCurrentIndex()));
        quiz.add(quizViewModel.getAnswer3(quizViewModel.getCurrentIndex()));
        Collections.shuffle(quiz);
        answer1.setBackgroundColor(Color.rgb(200,200,200));
        answer2.setBackgroundColor(Color.rgb(200,200,200));
        answer3.setBackgroundColor(Color.rgb(200,200,200));
        answer4.setBackgroundColor(Color.rgb(200,200,200));
        answer1.setText(quiz.get(0));
        answer2.setText(quiz.get(1));
        answer3.setText(quiz.get(2));
        answer4.setText(quiz.get(3));
        quizViewModel.setHintPressed(false);
        quizViewModel.setFinishedWithTimer(false);
        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            progressBar.setVisibility(View.INVISIBLE);
        }
        quizViewModel.setTime(0);
        progressBar.setProgress(quizViewModel.getTime());
        countDownTimer=new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                quizViewModel.setTime(quizViewModel.getTime()+1);
                progressBar.setProgress((int)quizViewModel.getTime()*100/(10000/1000));
            }

            @Override
            public void onFinish() {
                quizViewModel.setFinishedWithTimer(true);
                showCorrectAnswer();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        quizViewModel.moveToNextQuestion();
                        if (quizViewModel.getCurrentIndex()!=0){
                            showQuestion();
                        } else {
                            endProgram();
                        }
                    }
                }, 3000);
            }
        };
        countDownTimer.start();
        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            countDownTimer.cancel();
        }
        if (quizViewModel.getHintCounter() ==0){
            hintButton.setVisibility(View.INVISIBLE);
        }
    }

    public void checkAnswer(View v){
        countDownTimer.cancel();
        Button answerBtn = findViewById(v.getId());
        String btnText = answerBtn.getText().toString();
        if (btnText.equals(quizViewModel.getCorrectAnswer(quizViewModel.getCurrentIndex()))){
            answerBtn.setBackgroundColor(Color.GREEN);
            if (!quizViewModel.isHintPressed()){
                quizViewModel.setPoints(quizViewModel.getPoints()+1);
            }
        } else {
            answerBtn.setBackgroundColor(Color.RED);
            if (quizViewModel.getPoints()>0) quizViewModel.setPoints(quizViewModel.getPoints()-0.5);
        }
        Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    quizViewModel.moveToNextQuestion();
                    if (quizViewModel.getCurrentIndex()!=0){
                        showQuestion();
                    } else {
                        endProgram();
                    }
                }
            }, 3000);
    }

    public void showCorrectAnswer(){
        countDownTimer.cancel();
        if (answer1.getText().toString().equals(quizViewModel.getCorrectAnswer(quizViewModel.getCurrentIndex()))){
            answer1.setBackgroundColor(Color.GREEN);
        } else if (answer2.getText().toString().equals(quizViewModel.getCorrectAnswer(quizViewModel.getCurrentIndex()))){
            answer2.setBackgroundColor(Color.GREEN);
        } else if (answer3.getText().toString().equals(quizViewModel.getCorrectAnswer(quizViewModel.getCurrentIndex()))){
            answer3.setBackgroundColor(Color.GREEN);
        } else if (answer4.getText().toString().equals(quizViewModel.getCorrectAnswer(quizViewModel.getCurrentIndex()))){
            answer4.setBackgroundColor(Color.GREEN);
        }
        if (!quizViewModel.isFinishedWithTimer()){
            quizViewModel.setHintCounter(quizViewModel.getHintCounter()-1);
            quizViewModel.setHintPressed(true);
            if (quizViewModel.getHintCounter() ==0){
                Toast.makeText(MainActivity.this,"No te quedan usos del hint",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this,"Quedan "+ quizViewModel.getHintCounter() +" usos del hint",Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void endProgram(){
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setTitle("Congratulations, you finished the quiz!");
        builder1.setMessage("Points: "+(quizViewModel.getPoints()*10)+"/100");
        builder1.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder1.setNegativeButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                quizViewModel.randomQuestions();
                quizViewModel.setPoints(0);
                quizViewModel.setHintCounter(3);
                hintButton.setVisibility(View.VISIBLE);
                showQuestion();
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder1.create();
        alertDialog.show();
    }
}