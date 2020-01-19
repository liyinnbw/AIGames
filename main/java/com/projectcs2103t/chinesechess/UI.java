package com.projectcs2103t.chinesechess;

/**
 * Created by yoyo on 2015/7/25.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
    public static final int GRID_ROWS = 10;	//vertices, not squares
    public static final int GRID_COLS = 9;	//vertices, not squares
    public int SEARCH_TIME;	//in miliseconds
    public static final int PIECE_TYPES = 7;
    public static int startSide;
    public GameState gameState;
    public GameTree agent;
    public Drawable[][] chessImgs;
    public Point selectedPos;
    public boolean waitForSelection;
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
        initChessImages();
        newGame();
    }
    private void initChessImages(){
        chessImgs = new Drawable[4][PIECE_TYPES];
        chessImgs[0][0] = c.getResources().getDrawable(R.drawable._jiang);
        chessImgs[0][1] = c.getResources().getDrawable(R.drawable._shi);
        chessImgs[0][2] = c.getResources().getDrawable(R.drawable._xiang);
        chessImgs[0][3] = c.getResources().getDrawable(R.drawable._ma);
        chessImgs[0][4] = c.getResources().getDrawable(R.drawable._che);
        chessImgs[0][5] = c.getResources().getDrawable(R.drawable._pao);
        chessImgs[0][6] = c.getResources().getDrawable(R.drawable._zu);
        chessImgs[1][0] = c.getResources().getDrawable(R.drawable.shuai);
        chessImgs[1][1] = c.getResources().getDrawable(R.drawable.shi);
        chessImgs[1][2] = c.getResources().getDrawable(R.drawable.xiang);
        chessImgs[1][3] = c.getResources().getDrawable(R.drawable.ma);
        chessImgs[1][4] = c.getResources().getDrawable(R.drawable.che);
        chessImgs[1][5] = c.getResources().getDrawable(R.drawable.pao);
        chessImgs[1][6] = c.getResources().getDrawable(R.drawable.bing);
        chessImgs[2][0] = c.getResources().getDrawable(R.drawable._jiang_1);
        chessImgs[2][1] = c.getResources().getDrawable(R.drawable._shi_1);
        chessImgs[2][2] = c.getResources().getDrawable(R.drawable._xiang_1);
        chessImgs[2][3] = c.getResources().getDrawable(R.drawable._ma_1);
        chessImgs[2][4] = c.getResources().getDrawable(R.drawable._che_1);
        chessImgs[2][5] = c.getResources().getDrawable(R.drawable._pao_1);
        chessImgs[2][6] = c.getResources().getDrawable(R.drawable._zu_1);
        chessImgs[3][0] = c.getResources().getDrawable(R.drawable.shuai_1);
        chessImgs[3][1] = c.getResources().getDrawable(R.drawable.shi_1);
        chessImgs[3][2] = c.getResources().getDrawable(R.drawable.xiang_1);
        chessImgs[3][3] = c.getResources().getDrawable(R.drawable.ma_1);
        chessImgs[3][4] = c.getResources().getDrawable(R.drawable.che_1);
        chessImgs[3][5] = c.getResources().getDrawable(R.drawable.pao_1);
        chessImgs[3][6] = c.getResources().getDrawable(R.drawable.bing_1);

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
                if(gameState.isGameOver()!=-1){
                    checkGameOver(gameState.isGameOver());
                }else if(gameState.getCurrSide()!=startSide){
                    return false;
                }else{
                    Point p = posOnGrid(new Point(x,y));

                    if(waitForSelection){
                        if(gameState.isValidSelection((int) p.getY(), (int) p.getX())){
                            selectedPos = p;
                            waitForSelection = false;
                        }else{
                            return false;
                        }
                    }else{
                        if(gameState.isValidMove((int) selectedPos.getY(), (int) selectedPos.getX(), (int) p.getY(), (int) p.getX())){
                            gameState.makeMove((int) selectedPos.getY(), (int) selectedPos.getX(), (int) p.getY(), (int) p.getX());
                            System.out.println("player move = " + gameState.getMoves().peek());
                            if(gameState.isGameOver()!=-1){
                                checkGameOver(gameState.isGameOver());
                            }else{
                                agentMove();
                                checkGameOver(gameState.isGameOver());
                            }
                        }
                        selectedPos = null;
                        waitForSelection = true;
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
            else if(over==gameState.MIN_PLAYER) message = "Red Win";
            else message = "Tie";
            gameOverListener.gameOver(message);
        }
    }

    private void setGameParams(){
        XO=50;
        YO=50;
        UNIT = 30;
        SEARCH_TIME = 5000; //miliseconds
        moveTrace = null;
    }
    private void newGame(){
        startSide = GameState.MIN_PLAYER;
        gameState = new GameState(GRID_ROWS,GRID_COLS,startSide);
        agent = new GameTree(gameState, SEARCH_TIME);
        selectedPos = null;
        waitForSelection = true;
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
        if(gameState.getCurrSide()==startSide){
            return;
        }else{
            long start = System.currentTimeMillis();
            Move nextBestMove = agent.nextMove();
            long end = System.currentTimeMillis();
            String aiDebugMessage = agent.getDebugMessage();
            aiDebugMessage += "ai move = " + nextBestMove + "\n";
            aiDebugMessage += "move calculation time = " + (end - start) / 1000.0 + " s\n";
            debugMessageListener.handleDebugMessage(aiDebugMessage);
            gameState.makeMove(nextBestMove);
        }
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

        //draw grid
        UNIT = (viewWidth - 2*XO)/(GRID_COLS-1);
        for(int i=0; i<GRID_ROWS-1; i++){
            for(int j=0; j<GRID_COLS-1; j++){
                if(i!=4) {
                    int x = XO + j * UNIT;
                    int y = YO + i * UNIT;
                    canvas.drawRect(x, y, x + UNIT, y + UNIT, painter);
                }
            }
        }
        canvas.drawLine(XO, YO+((GRID_ROWS/2)-1)*UNIT, XO, YO+(GRID_ROWS/2)*UNIT,painter);
        canvas.drawLine(XO+(GRID_COLS-1)*UNIT, YO+((GRID_ROWS/2)-1)*UNIT, XO+(GRID_COLS-1)*UNIT, YO+(GRID_ROWS/2)*UNIT, painter);
        canvas.drawLine(XO+3*UNIT, YO+0*UNIT, XO+5*UNIT, YO+2*UNIT,painter);
        canvas.drawLine(XO+5*UNIT, YO+0*UNIT, XO+3*UNIT, YO+2*UNIT,painter);
        canvas.drawLine(XO+3*UNIT, YO+(GRID_ROWS-1)*UNIT, XO+5*UNIT, YO+(GRID_ROWS-3)*UNIT,painter);
        canvas.drawLine(XO+5*UNIT, YO+(GRID_ROWS-1)*UNIT, XO+3*UNIT, YO+(GRID_ROWS-3)*UNIT,painter);

        //draw pieces
        int[]state = gameState.getGameState();
        for(int i=0; i<state.length; i++){
            int pieceId = state[i];
            if(pieceId!=GameState.UNOCCUPIED){
                int picGroup = pieceId/PIECE_TYPES;
                int picType	= pieceId%PIECE_TYPES;
                int r		= i>>4;
                int c		= i%16;
                if(selectedPos!=null && (selectedPos.getX()==c && selectedPos.getY()==r)){
                    picGroup = picGroup+2;
                }

                Drawable img = chessImgs[picGroup][picType];
                Point ap = posAbsolute(new Point(c,r));
                img.setBounds((int) ap.getX()-UNIT/2,(int) ap.getY()-UNIT/2,(int) ap.getX()+UNIT/2,(int) ap.getY()+UNIT/2);
                img.draw(canvas);
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

    }
}

