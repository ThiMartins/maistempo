package dev.tantto.barra_progresso;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class BarraProgresso extends View {

    /** Valores padrões */

    private final float Largura_Minima_Padrao = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 256, getResources().getDisplayMetrics());
    private final static int Barra_Altura_Padrao = 16;

    private final static int Barra_Primaria_Cor_Padrao = Color.argb(255, 125, 125, 255);
    private final static int Barra_Primaria_Drawable_Padrao = 0;

    private final static int Barra_Secundaria_Cor_Padrao = Color.argb(255, 125, 0, 75);
    private final static int Barra_Secundaria_Drawable_Padrao = 0;

    private final static int Thumb_Raio_Padrao = 30;
    private final static int Thumb_Cor_Padrao = Color.argb(255, 255, 125, 125);
    private final static int Thumb_Drawable_Padrao = 0;

    private final static int TickMark_Raio_Padrao = 30;
    private final static int TickMark_Cor_Padrao = Color.argb(255, 255, 125, 125);
    private final static int TickMark_Drawable_Padrao = 0;
    private final static boolean IsTickMark_Padrao = false;

    private final static float Barra_Max_Padrao = 100.0F;
    private final static float Barra_Min_Padrao = 0.0F;
    private final static float Barra_Progresso_Padrao = 50.0F;

    private final static int Indicador_Cor_Padrao = Color.argb(255, 125, 125, 125);
    private final float Indicador_Tamanho_Padrao = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getResources().getDisplayMetrics());
    private final static int Indicador_Tipo_Padrao = 3;
    private final static float Indicador_Raio_Padrao = 40;
    private final static float Indicador_Raio__Quadrado_Padrao = 20;
    private final static float Indicador_Distancia_Thumb_Padrao = 30;

    private final static int Texto_Tamanho_Padrao = 2;
    private final static int Texto_Cor_Padrao = Color.argb(255, 255,125,125);

    /** Variaveis globais */

    private float Barra_Max;
    private float Barra_Min;
    private float Barra_Altura;
    private boolean Barra_IsEnable = true;
    private Paint Barra_Cinza = new Paint();
    private boolean Barra_Iniciado = false;
    private float Barra_Progresso = 0;
    private OnBarraChanged Barra_Change;
    private boolean Barra_IsPressing = false;

    /** Variaveis da barra de progresso primaria */

    private int Barra_Primaria_Cor;
    private int Barra_Primaria_Drawable;
    private Bitmap Barra_Primaria_Bitmap;
    private float Barra_Primaria_Center_Y;
    private float Barra_Primaria_Center_X;
    private Path Barra_Primaria = new Path();
    private Paint desenhoProgressoPrimaria = new Paint();

    /** Variaveis da barra de progresso secundario */

    private int Barra_Secundaria_Cor;
    private int Barra_Secundaria_Drawable;
    private Bitmap Barra_Secundaria_Bitmap;
    private Path Barra_Secundaria = new Path();
    private Paint desenhoProgressoSecundario = new Paint();

    /** Variaveis da thumb */

    private int Thumb_Cor;
    private int Thumb_Raio;
    private int Thumb_Drawable;
    private Bitmap Thumb_Bitmap;
    private Path Thumb = new Path();
    private Paint desenhoThumb = new Paint();

    /** Variaveis da tick matk */

    private boolean IsTickMark;
    private int TickMark_Cor;
    private int TickMark_Raio;
    private int TickMark_Drawable;
    private Bitmap TickNark_Bitmap;
    private Path TickMark = new Path();
    private Paint desenhoTickMark = new Paint();

    /** Variaveis do indicador */

    private int Indicador_Tipo;
    private float Indicador_Tamanho;
    private int Indicador_Cor;
    private float Indicador_Raio;
    private float Indicador_Raio_Quadrado;
    private float Indicador_Distancia_Thumb;
    private Path Indicador = new Path();
    private Paint desenhoIndicador = new Paint();
    private float Indicador_Centro_Y;
    private float Indicador_Centro_X;

    /** Variaveis textos*/

    private List<String> Texto_Valores = new ArrayList<>();
    private int Texto_Tamanho;
    private int Texto_Cor;
    private Paint desenhoTexto = new Paint();

    /** Variaveis da tela */

    private int Largura;
    private int Altura;
    private int Cima;
    private int Baixo;
    private int Esquerda;
    private int Direita;
    private int PaddingStart;
    private int PaddingEnd;
    private int PaddingTop;
    private int PaddingBottom;



    /** Construtores da classe */

    public BarraProgresso(Context context) {
        this(context, null);
    }

    public BarraProgresso(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarraProgresso(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        recuperarValoresAttr(attrs);
    }


    /** Métodos sobreescritos */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        recuperarPaddinds();
        Altura = Math.round(Barra_Altura + PaddingTop + PaddingBottom + (int) Indicador_Distancia_Thumb + Indicador_Tamanho + Cima);
        Largura = Math.round(Largura_Minima_Padrao + PaddingStart + PaddingEnd);
        setMeasuredDimension(resolveSize(Largura, widthMeasureSpec), resolveSize(Altura, heightMeasureSpec));
        Barra_Cinza.setColor(Color.argb(175, 175, 175, 175));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        recuperarPaddinds();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        ViewParent parent = getParent();
        if (parent == null) {
            return super.dispatchTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                parent.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                parent.requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Barra_IsPressing = true;
                Barra_Progresso = limitacaoProgresso(event.getX());
                if(Barra_Change != null){
                    Barra_Change.setOnClickListener(this);
                }
                performClick();
                if(Barra_IsEnable) invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                performClick();
                Barra_Progresso = limitacaoProgresso(event.getX());
                if(Barra_Change != null){
                    Barra_Change.setOnBarraChangeListener(this, Barra_Progresso);
                }
                if(Barra_IsEnable) invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                Barra_IsPressing = false;

                if(Barra_Change != null){
                    Barra_Change.setOnFinishListener(this);
                }
                if(Barra_IsEnable){
                    ajustarTick();
                    invalidate();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        Barra_IsEnable = enabled;
        invalidate();
    }

    @Override
    public boolean isEnabled() {
        return Barra_IsEnable;
    }

    @Override
    protected synchronized void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        desenharSecundario(canvas, (float) (PaddingStart + Esquerda), (float) (Baixo - PaddingBottom - Barra_Altura), (float) (Largura - PaddingEnd), (float) (Baixo - PaddingBottom));
        desenharPrimario(canvas, (float) (PaddingStart + Esquerda), (float) (Baixo - PaddingBottom - Barra_Altura), converterProgressoEmPixel(), (float) (Baixo - PaddingBottom));
        desenharThumb(canvas, converterProgressoEmPixel(), Barra_Primaria_Center_Y, true);
        desenharIndicador(canvas);
        desenharTexto(canvas);
        if(IsTickMark){
            desenharTickMark(canvas);
        }
        Barra_Iniciado = true;
    }


    /** Metodos para desenhar os elementos no layout */

    private void desenharTickMark(@NonNull final Canvas canvas){
        int tamanho = getTamanhoLista();
        tamanho = tamanho > 0 ? tamanho : 1;
        final int Espacamento = (Largura - PaddingEnd - PaddingStart) / tamanho;
        for(int posicao = 0; posicao <= getTamanhoLista(); posicao++){
            desenharThumb(canvas, (float) ((Espacamento * posicao) + PaddingStart + Esquerda), Barra_Primaria_Center_Y, false);
        }

    }

    private void desenharTexto(@NonNull Canvas canvas){
        desenhoTexto.setColor(Texto_Cor);
        desenhoTexto.setTextSize(Texto_Tamanho);

        int Tamanho = getTamanhoLista();
        Tamanho = Tamanho > 0 ? Tamanho : 1;
        int Posicao = getPosicao(Tamanho);
        String Elemento = getElemento(Tamanho, Posicao);

        float TamanhoTexto = Elemento.length() * getTamanhoTexto();
        float Bordas = (Indicador_Tamanho - TamanhoTexto) / 8;

        float PosicaoFinal = Indicador_Centro_X - (TamanhoTexto / Tamanho) + Bordas;

        canvas.drawText(Elemento, PosicaoFinal, Indicador_Centro_Y, desenhoTexto);

    }

    private void desenharIndicador(@NonNull Canvas canvas){
        desenhoIndicador.setColor(Indicador_Cor);
        int TamanhoLista = getTamanhoLista();
        int Posicao = getPosicao(TamanhoLista);
        String Elemento = getElemento(TamanhoLista, Posicao);

        if(Indicador_Tipo == IndicadoresTipos.QuadradoArrendodado.valor){
            Indicador = new Path();

            float TamanhoDaCaixa = Elemento.length() * getTamanhoTexto();
            float TamanhoConvertido = Indicador_Tamanho > TamanhoDaCaixa ? Indicador_Tamanho : TamanhoDaCaixa;

            float Tamanho = converterProgressoEmPixel() - (TamanhoConvertido / 2) >= PaddingStart + Esquerda ? converterProgressoEmPixel() - (TamanhoConvertido / 2) : PaddingStart + Esquerda;
            float TamanhoFinal = converterProgressoEmPixel() - (TamanhoConvertido / 2) <= PaddingStart + Esquerda ? Tamanho + TamanhoConvertido : converterProgressoEmPixel() + (TamanhoConvertido / 2);
            Tamanho = converterProgressoEmPixel() + (TamanhoConvertido / 2) >= Largura - PaddingEnd ? Largura - PaddingEnd - TamanhoConvertido : Tamanho;
            TamanhoFinal = converterProgressoEmPixel() + (TamanhoConvertido / 2) >= Largura - PaddingEnd ? Largura - PaddingEnd : TamanhoFinal;

            Indicador_Centro_Y = (Barra_Primaria_Center_Y - (Indicador_Distancia_Thumb / 2)) - (Barra_Primaria_Center_Y - Indicador_Distancia_Thumb - (Indicador_Tamanho / 2));
            Indicador_Centro_X = Tamanho + (TamanhoConvertido / 2);

            RectF square = new RectF(Tamanho, Barra_Primaria_Center_Y - Indicador_Distancia_Thumb - Indicador_Tamanho, TamanhoFinal, Barra_Primaria_Center_Y - Indicador_Distancia_Thumb);
            Indicador.addRoundRect(square, Indicador_Raio_Quadrado, Indicador_Raio_Quadrado, Path.Direction.CCW);
            canvas.drawPath(Indicador, Barra_IsEnable ? desenhoIndicador : Barra_Cinza);
        }
        if(Indicador_Tipo == IndicadoresTipos.Circular.valor){
            Indicador = new Path();
            float Tamanho = converterProgressoEmPixel() - Indicador_Raio >= PaddingStart + Esquerda ? converterProgressoEmPixel() : PaddingStart + Esquerda + Indicador_Raio;
            Tamanho = converterProgressoEmPixel() + Indicador_Raio <= Largura - PaddingEnd ? Tamanho : Largura - PaddingEnd - Indicador_Raio;

            Indicador_Centro_X = Tamanho;
            Indicador_Centro_Y = Barra_Primaria_Center_Y - (Indicador_Distancia_Thumb * 1.25F);

            Indicador.addCircle(Tamanho, Barra_Primaria_Center_Y - (Indicador_Distancia_Thumb * 1.1255F), Indicador_Raio, Path.Direction.CCW);
            canvas.drawPath(Indicador, Barra_IsEnable ? desenhoIndicador : Barra_Cinza);
        }
        if(Indicador_Tipo == IndicadoresTipos.Oval.valor){
            Indicador = new Path();
            float Tamanho = converterProgressoEmPixel() - Indicador_Raio >= PaddingStart + Esquerda ? converterProgressoEmPixel() : PaddingStart + Esquerda + Indicador_Raio;
            Tamanho = converterProgressoEmPixel() + Indicador_Raio <= Largura - PaddingEnd ? Tamanho : Largura - PaddingEnd - Indicador_Raio;

            Indicador_Centro_X = Tamanho;
            Indicador_Centro_Y = Barra_Primaria_Center_Y - (Indicador_Distancia_Thumb * 1.125F);

            Indicador.addCircle(Tamanho, Barra_Primaria_Center_Y - (Indicador_Distancia_Thumb * 1.5F), Indicador_Raio, Path.Direction.CCW);
            canvas.drawPath(Indicador, Barra_IsEnable ? desenhoIndicador : Barra_Cinza);
        }
        if(Indicador_Tipo == IndicadoresTipos.Quadrado.valor){
            Indicador = new Path();
            float Tamanho = converterProgressoEmPixel() - (Indicador_Tamanho / 2) >= PaddingStart + Esquerda ? converterProgressoEmPixel() - (Indicador_Tamanho / 2) : PaddingStart + Esquerda;
            float TamanhoFinal = converterProgressoEmPixel() - (Indicador_Tamanho / 2) <= PaddingStart + Esquerda ? Tamanho + Indicador_Tamanho : converterProgressoEmPixel() + (Indicador_Tamanho / 2);
            Tamanho = converterProgressoEmPixel() + (Indicador_Tamanho / 2) >= Largura - PaddingEnd ? Largura - PaddingEnd - Indicador_Tamanho : Tamanho;
            TamanhoFinal = converterProgressoEmPixel() + (Indicador_Tamanho / 2) >= Largura - PaddingEnd ? Largura - PaddingEnd : TamanhoFinal;

            Indicador_Centro_Y = (Barra_Primaria_Center_Y - (Indicador_Distancia_Thumb / 2)) - (Barra_Primaria_Center_Y - Indicador_Distancia_Thumb - (Indicador_Tamanho / 2));
            Indicador_Centro_X = Tamanho + (Indicador_Tamanho / 2);

            RectF square = new RectF(Tamanho, Barra_Primaria_Center_Y - Indicador_Distancia_Thumb - Indicador_Tamanho, TamanhoFinal, Barra_Primaria_Center_Y - Indicador_Distancia_Thumb);
            Indicador.addRect(square, Path.Direction.CCW);
            canvas.drawPath(Indicador, Barra_IsEnable ? desenhoIndicador : Barra_Cinza);
        }
    }

    private void desenharThumb(@NonNull Canvas canvas, float X0, float Y0, boolean Modo){
        if(Modo){
            if(Thumb_Drawable == 0){
                Thumb = new Path();
                desenhoThumb.setColor(Thumb_Cor);
                Thumb.addCircle(X0, Y0, Thumb_Raio, Path.Direction.CCW);
                canvas.drawPath(Thumb, Barra_IsEnable ? desenhoThumb : Barra_Cinza);
            } else {
                if(Thumb_Bitmap == null){
                    Thumb_Bitmap = drawableToBitmap(getResources().getDrawable(Thumb_Drawable), Thumb_Raio, Thumb_Raio);
                }
                canvas.drawBitmap(Thumb_Bitmap, X0 - (Thumb_Raio / 2F), Y0 - (Thumb_Raio / 2F), Barra_IsEnable ? desenhoThumb : Barra_Cinza);
            }
        } else {
            if(TickMark_Drawable == 0){
                TickMark = new Path();
                desenhoTickMark.setColor(TickMark_Cor);
                TickMark.addCircle(X0, Y0, TickMark_Raio, Path.Direction.CCW);
                canvas.drawPath(TickMark, Barra_IsEnable ? desenhoThumb : Barra_Cinza);
            } else {
                if(TickNark_Bitmap == null){
                    TickNark_Bitmap = drawableToBitmap(getResources().getDrawable(TickMark_Drawable), TickMark_Raio, TickMark_Raio);
                }
                canvas.drawBitmap(TickNark_Bitmap, X0 - (TickMark_Raio / 2F), Y0 - (TickMark_Raio / 2F), Barra_IsEnable ? desenhoThumb : Barra_Cinza);
            }
        }
    }

    private void desenharPrimario(@NonNull Canvas canvas, float X0, float Y0, float X1, float Y1){
        if(Barra_Primaria_Drawable == 0){
            Barra_Primaria = new Path();
            Barra_Primaria.moveTo(X0, Y0);
            Barra_Primaria.lineTo(X1, Y0);
            Barra_Primaria.lineTo(X1, Y1);
            Barra_Primaria.lineTo(X0, Y1);
            Barra_Primaria.lineTo(X0, Y0);
            Barra_Primaria.close();
            desenhoProgressoPrimaria.setColor(Barra_Primaria_Cor);
            canvas.drawPath(Barra_Primaria, Barra_IsEnable ? desenhoProgressoPrimaria : Barra_Cinza);
        } else {
            float LarguraBitmap =  (X1 - X0);
            if(LarguraBitmap > 0){
                Barra_Primaria_Bitmap = drawableToBitmap(getResources().getDrawable(Barra_Primaria_Drawable), (int) LarguraBitmap, (int) Barra_Altura);
                canvas.drawBitmap(Barra_Primaria_Bitmap, X0, Y0, Barra_IsEnable ? desenhoProgressoPrimaria : Barra_Cinza);
            }
        }
        Barra_Primaria_Center_Y = Y0 + ((Y1 - Y0) / 2);
        Barra_Primaria_Center_X = X0 + ((X1 - X0) / 2);
    }

    private void desenharSecundario(@NonNull Canvas canvas, float X0, float Y0, float X1, float Y1){
        if(Barra_Secundaria_Drawable == 0){
            Barra_Secundaria.moveTo(X0, Y0);
            Barra_Secundaria.lineTo(X1, Y0);
            Barra_Secundaria.lineTo(X1, Y1);
            Barra_Secundaria.lineTo(X0, Y1);
            Barra_Secundaria.lineTo(X0, Y0);
            Barra_Secundaria.close();
            desenhoProgressoSecundario.setColor(Barra_Secundaria_Cor);
            canvas.drawPath(Barra_Secundaria, Barra_IsEnable ? desenhoProgressoSecundario : Barra_Cinza);
        } else {
            if(Barra_Secundaria_Bitmap == null){
                Barra_Secundaria_Bitmap = drawableToBitmap(getResources().getDrawable(Barra_Secundaria_Drawable), (int) (X1 - X0), (int) Barra_Altura);
            }
            canvas.drawBitmap(Barra_Secundaria_Bitmap, X0, Y0, Barra_IsEnable ? desenhoProgressoSecundario : Barra_Cinza);
        }
    }

    private void ajustarTick() {
        int tamanho = getTamanhoLista();
        tamanho = tamanho > 0 ? tamanho : 1;
        final int Espacamento = (Largura - PaddingEnd - PaddingStart) / tamanho;
        if(!Barra_IsPressing && Barra_Iniciado){
            for(int posicao = 0; posicao <= getTamanhoLista(); posicao ++){
                if(posicao * Espacamento >= converterProgressoEmPixel()){
                    float mudar;
                    if(converterProgressoEmPixel() >= ((posicao - 1) * Espacamento) + (Espacamento / 2F) + PaddingStart){
                        mudar = (float) ((posicao * Espacamento) + PaddingStart);
                    } else {
                        mudar = (float) (((posicao - 1) * Espacamento) + PaddingStart);
                    }
                    animacao(converterProgressoEmPixel(), mudar);
                    break;
                }
            }
        }

    }

    private void animacao(float momento, float direcao){
        final ValueAnimator animacao = ValueAnimator.ofFloat(momento, direcao);
        animacao.start();
        animacao.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Barra_Progresso = converterPixelEmProgresso((float) animacao.getAnimatedValue());
                invalidate();
            }
        });
    }


    /** Metodos de ferramentas */

    private float getTamanhoTexto(){
        desenhoTexto.setTextSize(Texto_Tamanho);
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, desenhoTexto.getTextSize(), getResources().getDisplayMetrics());
    }

    private String getElemento(int tamanho, int posicao) {
        return tamanho > 0 ? Texto_Valores.get(posicao) : String.valueOf((int) Barra_Progresso);
    }

    private int getPosicao(int tamanho) {
        float DividirElementos = tamanho > 0 ? (Barra_Max - Barra_Min) / tamanho : 1;
        return (int) (Barra_Progresso / DividirElementos) < tamanho ? (int) (Barra_Progresso / DividirElementos) : tamanho - 1;
    }

    private int getTamanhoLista() {
        return Texto_Valores != null ? Texto_Valores.size() - 1 : 0;
    }

    private void recuperarPaddinds(){
        Altura = getHeight();
        Largura = getWidth();
        Esquerda = getLeft();
        Direita = getRight();
        Cima = getTop();
        Baixo = getBottom();
        PaddingTop = getPaddingTop();
        PaddingBottom = getPaddingBottom();

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1){
            PaddingStart = getPaddingStart();
            PaddingEnd = getPaddingEnd();
        } else {
            PaddingStart = getPaddingLeft();
            PaddingEnd = getPaddingRight();
        }
    }

    private float converterProgressoEmPixel(){
        return ((Largura - PaddingStart - PaddingEnd) / (Barra_Max - Barra_Min)) * (Barra_Progresso - Barra_Min) + PaddingStart;
    }

    private float limitacaoProgresso(float pixel){
        if(pixel < PaddingStart + Esquerda){
            return 0;
        } else if(pixel > Largura - PaddingEnd) {
            return 100;
        }
        return converterPixelEmProgresso(pixel);
    }

    private void escalaProgresso(float progresso) {
        if(progresso < Barra_Min){
            Barra_Progresso = Barra_Min;
        } else if(progresso > Barra_Max){
            Barra_Progresso = Barra_Max;
        } else {
            Barra_Progresso = progresso;
        }
    }

    private float converterPixelEmProgresso(float pixel){
        return (pixel - PaddingStart) / ((Largura - PaddingStart - PaddingEnd) / (Barra_Max - Barra_Min)) + Barra_Min;
    }

    private void recuperarValoresAttr(AttributeSet attrs){
        TypedArray Ta = getContext().obtainStyledAttributes(attrs, R.styleable.BarraProgresso);

        Barra_Altura = Ta.getDimension(R.styleable.BarraProgresso_barra_altura, Barra_Altura_Padrao);

        Barra_Primaria_Cor = Ta.getColor(R.styleable.BarraProgresso_barra_primaria_color, Barra_Primaria_Cor_Padrao);
        Barra_Primaria_Drawable = Ta.getResourceId(R.styleable.BarraProgresso_barra_primaria_drawable, Barra_Primaria_Drawable_Padrao);

        Barra_Secundaria_Cor = Ta.getColor(R.styleable.BarraProgresso_barra_secundaria_color, Barra_Secundaria_Cor_Padrao);
        Barra_Secundaria_Drawable = Ta.getResourceId(R.styleable.BarraProgresso_barra_secundaria_drawable, Barra_Secundaria_Drawable_Padrao);

        Thumb_Cor = Ta.getColor(R.styleable.BarraProgresso_barra_thumb_cor, Thumb_Cor_Padrao);
        Thumb_Raio = Ta.getInt(R.styleable.BarraProgresso_barra_thumb_raio, Thumb_Raio_Padrao);
        Thumb_Drawable = Ta.getResourceId(R.styleable.BarraProgresso_barra_thumb_drawable, Thumb_Drawable_Padrao);

        TickMark_Cor = Ta.getColor(R.styleable.BarraProgresso_barra_tick_cor, TickMark_Cor_Padrao);
        TickMark_Raio = Ta.getInt(R.styleable.BarraProgresso_barra_tick_raio, TickMark_Raio_Padrao);
        TickMark_Drawable = Ta.getResourceId(R.styleable.BarraProgresso_barra_tick_drawable, TickMark_Drawable_Padrao);
        IsTickMark = Ta.getBoolean(R.styleable.BarraProgresso_barra_tick_enable, IsTickMark_Padrao);

        Barra_Max = Ta.getFloat(R.styleable.BarraProgresso_barra_max, Barra_Max_Padrao);
        Barra_Min = Ta.getFloat(R.styleable.BarraProgresso_barra_min, Barra_Min_Padrao);
        escalaProgresso(Ta.getFloat(R.styleable.BarraProgresso_barra_progresso, Barra_Progresso_Padrao));

        Indicador_Cor = Ta.getColor(R.styleable.BarraProgresso_barra_indicador_cor, Indicador_Cor_Padrao);
        Indicador_Tamanho = Ta.getDimension(R.styleable.BarraProgresso_barra_indicador_tamanho, Indicador_Tamanho_Padrao);
        Indicador_Tipo = Ta.getInt(R.styleable.BarraProgresso_barra_indicador_tipo, Indicador_Tipo_Padrao);
        Indicador_Raio = Ta.getFloat(R.styleable.BarraProgresso_barra_indicador_raio, Indicador_Raio_Padrao);
        Indicador_Raio_Quadrado = Ta.getFloat(R.styleable.BarraProgresso_barra_indicador_raio_quadrado, Indicador_Raio__Quadrado_Padrao);
        Indicador_Distancia_Thumb = Ta.getFloat(R.styleable.BarraProgresso_barra_indicador_distancia_thumb, Indicador_Distancia_Thumb_Padrao) + (Thumb_Raio / 2F);

        Texto_Tamanho = 24 * Ta.getInt(R.styleable.BarraProgresso_barra_texto_size, Texto_Tamanho_Padrao);
        Texto_Cor = Ta.getColor(R.styleable.BarraProgresso_barra_texto_cor, Texto_Cor_Padrao);
        CharSequence[] Valores = Ta.getTextArray(R.styleable.BarraProgresso_barra_texto);
        if (Valores != null){
            for(CharSequence Item : Valores){
                Texto_Valores.add(Item.toString());
            }
        }

        Ta.recycle();
    }

    private Bitmap drawableToBitmap(@NonNull Drawable drawable, int largura, int altura){
        largura = largura < 1 ? 1 : largura;
        altura = altura < 1 ? 1 : altura;
        Bitmap bitmap = Bitmap.createBitmap(largura, altura, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }



    /** Metodos publicos

     /** Eventos */

    public void addOnBarraChanged(@NonNull OnBarraChanged event){
        this.Barra_Change = event;
    }



    /** Setter's */

    public void setMax(float Max){
        this.Barra_Max = Max;
        this.escalaProgresso(Barra_Progresso);
        this.invalidate();
    }

    public void setMin(float Min){
        this.Barra_Min = Min;
        this.escalaProgresso(Barra_Progresso);
        this.invalidate();
    }

    public void setThumbRaio(int raio){
        this.Thumb_Raio = raio;
        this.invalidate();
    }

    public void setIndicadorRaio(float raio){
        this.Indicador_Raio = raio;
        this.invalidate();
    }

    public void setProgresso(float progresso){
        this.escalaProgresso(progresso);
        this.invalidate();
    }

    public void setTextoCor(@ColorInt int cor){
        this.Texto_Cor = cor;
        this.invalidate();
    }

    public void setThumbCor(@ColorInt int color){
        this.Thumb_Cor = color;
        this.invalidate();
    }

    public void setIndicadorCor(@ColorInt int cor){
        this.Indicador_Cor = cor;
        this.invalidate();
    }

    public void setIndicadorTamanho(float tamanho){
        this.Indicador_Tamanho = tamanho;
        this.invalidate();
    }

    public void setIndicadorRaioQuadrado(float raio){
        this.Indicador_Raio_Quadrado = raio;
        this.invalidate();
    }

    public void setThumbDrawable(@DrawableRes int id){
        this.Thumb_Drawable = id;
        this.invalidate();
    }

    public void setProgressoPrimarioCor(@ColorInt int cor){
        this.Barra_Primaria_Cor = cor;
        this.invalidate();
    }

    public void setTextoLista(@NonNull List<String> lista){
        this.Texto_Valores = lista;
        this.invalidate();
    }

    public void setIndicadorDistanciaThumb(float distancia){
        this.Indicador_Distancia_Thumb = distancia;
        this.requestLayout();
    }

    public void setProgressoSecundarioCor(@ColorInt int cor){
        this.Barra_Secundaria_Cor = cor;
        this.invalidate();
    }

    public void setIndicadorTipo(@NonNull IndicadoresTipos tipo){
        this.Indicador_Tipo = tipo.valor;
        this.invalidate();
    }

    public void setProgressoPrimarioDrawable(@DrawableRes int id){
        this.Barra_Primaria_Drawable = id;
        this.invalidate();
    }

    public void setProgressoSecundarioDrawable(@DrawableRes int id){
        this.Barra_Secundaria_Drawable = id;
        this.invalidate();
    }



    /** Getter's */

    public int getCima() {
        return Cima;
    }

    public int getBaixo() {
        return Baixo;
    }

    public int getAltura(){
        return Altura;
    }

    public int getDireita(){
        return Direita;
    }

    public int getLargura(){
        return Largura;
    }

    public float getMax(){
        return Barra_Max;
    }

    public float getMin(){
        return Barra_Min;
    }

    public int getEsquerda(){
        return Esquerda;
    }

    @ColorInt
    public int getTextoCor(){
        return Texto_Cor;
    }

    @ColorInt
    public int getThumbColor(){
        return Thumb_Cor;
    }

    public int getThumbRaio(){
        return Thumb_Raio;
    }

    public int getPaddingFim() {
        return PaddingEnd;
    }

    public int getPaddingCima() {
        return PaddingTop;
    }

    public int getPaddingBaixo() {
        return PaddingBottom;
    }

    public int getPaddingComeco() {
        return PaddingStart;
    }

    public float getProgresso(){
        return Barra_Progresso;
    }

    public int getIndicadorTipo(){
        return Indicador_Tipo;
    }

    @DrawableRes
    public int getThumbDrawable(){
        return Thumb_Drawable;
    }

    @ColorInt
    public int getIndicadorColor(){
        return Indicador_Cor;
    }

    public float getIndicadorRaio(){
        return Indicador_Raio;
    }

    @NonNull
    public List<String> getTextoLista(){
        return Texto_Valores;
    }

    public float getIndicadorTamamho(){
        return Indicador_Tamanho;
    }

    @ColorInt
    public int getProgressoPrimarioCor(){
        return Barra_Primaria_Cor;
    }

    @ColorInt
    public int getProgressoSecundarioCor(){
        return Barra_Secundaria_Cor;
    }

    public float getIndicadorRaioQuadrado(){
        return Indicador_Raio_Quadrado;
    }

    @DrawableRes
    public int getProgressoPrimarioDrawable(){
        return Barra_Primaria_Drawable;
    }

    public float getIndicadorDistanciaThumb(){
        return Indicador_Distancia_Thumb;
    }

    @DrawableRes
    public int getProgressoSecundarioDrawable(){
        return Barra_Secundaria_Drawable;
    }

}
