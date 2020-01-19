package com.example.yoyo.firstapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yoyo on 2015/7/23.
 */
public class UI extends View {
    //dimens
    public int XO;
    public int YO;
    public int UNIT;
    public int GRID_ROWS;	//vertices, not squares
    public int GRID_COLS;	//vertices, not square
    public int SEARCH_TIME;	//in miliseconds
    public GameState gameState;
    public GameTree agent;
    public Context c;
    public Point moveTrace;
    private GameOverListener gameOverListener;
    private DebugMessageListener debugMessageListener;
    public interface GameOverListener{
        public void gameOver(String message);
    }
    public interface DebugMessageListener{
        public void handleDebugMessage(String message);
    }
    public void setGameOverListener(GameOverListener listener){
        gameOverListener = listener;
    }
    public void setDebugMessageListener(DebugMessageListener listener){
        debugMessageListener = listener;
    }
    public UI(Context context){
        this(context, null);
    }
    public UI(Context context, AttributeSet attrs){
        super(context, attrs);
        c = context;
        setGameParams();
        setFocusable(true);
        setFocusableInTouchMode(true);
        newGame();
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        int x = (int) e.getX();
        int y = (int) e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                moveTrace = new Point(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                moveTrace = null;
                if(gameState.getCurrSide()!=gameState.MAX_PLAYER){
                    return false;
                }else if(gameState.isGameOver()!=-1){
                    checkGameOver(gameState.isGameOver());
                }else{
                    Point p = posOnGrid(new Point(x,y));
                    if(gameState.addPiece((int)p.getX(), (int)p.getY())){
                        System.out.println("mouse click " + p);
                        if(gameState.isGameOver()!=-1){
                            checkGameOver(gameState.isGameOver());

                        }else{
                            agentMove();
                            checkGameOver(gameState.isGameOver());
                        }
                    }
                }
                invalidate();
        }
        return true;
    }
    private void checkGameOver(int over){
        if(over!=-1){
            String message;
            if(over==gameState.MAX_PLAYER) message = "Black Win";
            else if(over==gameState.MIN_PLAYER) message = "White Win";
            else message = "Tie";
            gameOverListener.gameOver(message);
        }
    }

    private void setGameParams(){
        XO=50;
        YO=50;
        UNIT = 30;
        GRID_ROWS = 13;	//vertices, not squares
        GRID_COLS = 13;	//vertices, not squares
        SEARCH_TIME = 5000; //miliseconds
        moveTrace = null;
    }
    private void newGame(){
        gameState = new GameState(GRID_ROWS,GRID_COLS,GameState.MAX_PLAYER);
        agent = new GameTree(gameState, SEARCH_TIME);
    }
    private Point posOnGrid(Point p){
        Point pos = new Point((int)((p.getX()-XO+UNIT/2.0)/UNIT),(int)((p.getY()-YO+UNIT/2.0)/UNIT));
        return pos;
    }
    private Point posAbsolute(Point p){
        Point pos = new Point((int)(p.getX()*UNIT+XO),(int)(p.getY()*UNIT+YO));
        return pos;
    }
    public void agentMove(){
        long start = System.currentTimeMillis();
        Point nextBestMove = agent.nextMove();
        long end = System.currentTimeMillis();
        String aiDebugMessage = agent.getDebugMessage();
        aiDebugMessage += "ai move = " + nextBestMove + " move calculation time = " + (end - start) / 1000.0 + " s\n";
        debugMessageListener.handleDebugMessage(aiDebugMessage);
        gameState.addPiece((int) nextBestMove.getX(), (int) nextBestMove.getY());
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Paint painter = new Paint();
        painter.setAntiAlias(true);
        painter.setStrokeWidth(5);
        painter.setStyle(Paint.Style.STROKE);
        painter.setStrokeJoin(Paint.Join.ROUND);
        painter.setStrokeCap(Paint.Cap.ROUND);
        painter.setColor(Color.BLACK);

        int viewWidth = getWidth();
        int viewHeight = getHeight();

        UNIT = (viewWidth - 2*XO)/(GRID_COLS-1);
        for(int i=0; i<GRID_ROWS-1; i++){
            for(int j=0; j<GRID_COLS-1; j++){
                int x = XO+j*UNIT;
                int y = YO+i*UNIT;
                canvas.drawRect(x,y,x+UNIT, y+UNIT,painter);
            }
        }

        int[][] states = gameState.getGameState();
        int[] bitMaps = new int[GRID_COLS];
        for(int i=0; i<bitMaps.length; i++){
            bitMaps[i] = 1<<GRID_COLS-1-i;
        }

        for (int k = 0; k<states.length; k++){
            int state[] = states[k];
            for(int i=0; i<state.length; i++) {
                int row = state[i];
                for(int j=0; j<GRID_COLS; j++){
                    int bit = row & bitMaps[j];
                    if(bit!=0){
                        Point p = new Point(j,i);
                        Point ap = posAbsolute(p);
                        if(k==0){
                            painter.setColor(Color.BLACK);
                            painter.setStyle(Paint.Style.FILL);
                        }else{
                            painter.setColor(Color.WHITE);
                            painter.setStyle(Paint.Style.FILL);
                        }
                        int d = (int) (UNIT*0.4);
                        canvas.drawCircle((int) ap.getX(), (int) ap.getY(), d, painter);
                        painter.setColor(Color.BLACK);
                        painter.setStyle(Paint.Style.STROKE);
                        canvas.drawCircle((int) ap.getX(), (int) ap.getY(), d, painter);
                    }
                }
            }
        }

        if(moveTrace!=null){
            painter.setColor(Color.RED);
            painter.setStyle(Paint.Style.FILL);
            int d = (int) (UNIT*0.8);
            canvas.drawCircle((int) moveTrace.getX(), (int) moveTrace.getY(), d, painter);
            painter.setColor(Color.RED);
            painter.setStyle(Paint.Style.STROKE);
            d = (int) (UNIT);
            canvas.drawCircle((int) moveTrace.getX(), (int) moveTrace.getY(), d, painter);
        }

        if(!gameState.getMoves().empty()){
            painter.setColor(Color.RED);
            painter.setStyle(Paint.Style.FILL);
            int d = (int) (UNIT*0.1);
            Point lastMove = gameState.getMoves().peek();
            Point ap = posAbsolute(lastMove);
            canvas.drawCircle((int) ap.getX(), (int) ap.getY(), d, painter);
        }

    }
}
