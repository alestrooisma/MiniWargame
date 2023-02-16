package mwg.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

public class TextRenderer implements Disposable {

    private final SpriteBatch batch;
    private final BitmapFont font;
    private final GlyphLayout glyphLayout;
    private int alignment = Align.topLeft;

    public TextRenderer(SpriteBatch batch) {
        this.batch = batch;
        this.font = new BitmapFont();
        this.glyphLayout = new GlyphLayout();
    }

    public void setColor(Color c) {
        font.setColor(c);
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public void draw(String s, float x, float y) {
        draw(s, x, y, alignment);
    }

    public void draw(String s, float x, float y, int alignment) {
        glyphLayout.setText(font, s);
        draw(glyphLayout, x, y, alignment);
    }

    public void draw(GlyphLayout gl, float x, float y) {
        draw(gl, x, y, alignment);
    }

    public void draw(GlyphLayout gl, float x, float y, int alignment) {
        // Handle horizontal alignment
        if (Align.isCenterHorizontal(alignment)) {
            x -= gl.width / 2;
        } else if (Align.isRight(alignment)) {
            x -= gl.width;
        }

        // Handle vertical alignment
        if (Align.isCenterVertical(alignment)) {
            y += gl.height / 2;
        } else if (Align.isBottom(alignment)) {
            y += gl.height;
        }

        // Actually draw the string
        font.draw(batch, gl, x, y);
    }

    public BitmapFont getFont() {
        return font;
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
