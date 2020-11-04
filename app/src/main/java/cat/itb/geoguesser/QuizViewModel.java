package cat.itb.geoguesser;

import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.Collections;

public class QuizViewModel extends ViewModel {
    QuestionModel[] questionBank = {
            new QuestionModel(R.drawable.catalonia, "Cataluña","Galicia","La Rioja","Tokyo"),
            new QuestionModel(R.drawable.andalucia, "Andalucia","Asturias","Cataluña","Marte"),
            new QuestionModel(R.drawable.asturias, "Asturias","Cantabria","Bilbao","No lo se"),
            new QuestionModel(R.drawable.cantabria, "Cantabria","Islas Canarias","Valencia","ITB"),
            new QuestionModel(R.drawable.extremadura, "Extremadura","Galicia","La Rioja","Madrid"),
            new QuestionModel(R.drawable.galicia, "Galicia","Murcia","Tarragona","Pais Vasco"),
            new QuestionModel(R.drawable.larioja, "La Rioja","Pais Vasco","Galicia","Cataluña"),
            new QuestionModel(R.drawable.madrid, "Madrid","Murcia","Russia","Islas Baleares"),
            new QuestionModel(R.drawable.murcia, "Murcia","Extremadura","Islas Baleares","Islas Canarias"),
            new QuestionModel(R.drawable.paisvasco, "Pais Vasco","Madrid","Extremadura","Japon")
    };
    int currentIndex = 0;
    double points=0;
    int hintCounter = 3;
    boolean hintPressed;
    int time=0;
    boolean finishedWithTimer;

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getCurrentImage(int currentIndex) {
        return questionBank[currentIndex].getFlag();
    }

    public String getCorrectAnswer(int currentIndex) {
        return questionBank[currentIndex].getCorrectAnswer();
    }

    public String getAnswer1(int currentIndex){
        return questionBank[currentIndex].getAnswer1();
    }

    public String getAnswer2(int currentIndex){
        return questionBank[currentIndex].getAnswer2();
    }
    public String getAnswer3(int currentIndex){
        return questionBank[currentIndex].getAnswer3();
    }

    public void randomQuestions() {
        Collections.shuffle(Arrays.asList(questionBank));
    }

    public void moveToNextQuestion(){
        currentIndex = (currentIndex + 1) % questionBank.length;
    }

    public int getLenghtQuestionBank(){
        return questionBank.length;
    }

    public void setCurrentIndex(int num){
        currentIndex = num;
    }

    public int getQuestionBankSize(){
        return questionBank.length;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public int getHintCounter() {
        return hintCounter;
    }

    public void setHintCounter(int hintCounter) {
        this.hintCounter = hintCounter;
    }

    public boolean isHintPressed() {
        return hintPressed;
    }

    public void setHintPressed(boolean hintPressed) {
        this.hintPressed = hintPressed;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isFinishedWithTimer() {
        return finishedWithTimer;
    }

    public void setFinishedWithTimer(boolean finishedWithTimer) {
        this.finishedWithTimer = finishedWithTimer;
    }
}
