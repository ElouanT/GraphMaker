package views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.graphmaker.R;

import java.util.ArrayList;

import model.Data;
import model.Player;
import model.Character;
import model.Rescale;

public class Graph extends View {
    String templateName;

    String title;
    String date;
    String entrants;
    String location;

    ArrayList<Player> players;

    Data characterData;
    int IMAGE_HEIGHT = 500;

    BitmapDrawable drawable;

    public Graph(Context context, String template, String title, String date, String entrants, String location, ArrayList<Player> players) {

        super(context);
        this.templateName = template;
        this.title = title;
        this.date = date;
        this.entrants = entrants;
        this.location = location;
        this.players = players;
        this.characterData = new Data();

        this.createDrawable();
    }

    public void createDrawable(){
        // Bitmap création
        Bitmap bitmap = Bitmap.createBitmap(1600, 2331, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);

        // Template background
        int id = getContext().getResources().getIdentifier("template_" + this.templateName, "drawable", getContext().getPackageName());
        Drawable background = getContext().getResources().getDrawable(id);
        Rect bounds = c.getClipBounds();
        background.setBounds(bounds);
        background.draw(c);

        // Title
        Paint titleText = new TextPaint();
        titleText.setAntiAlias(true);
        titleText.setTextAlign(Paint.Align.CENTER);
        titleText.setTypeface(ResourcesCompat.getFont(getContext(), R.font.bebas_neue));
        titleText.setTextSize(70);

        if (this.templateName.matches("man")) {
            titleText.setColor(ContextCompat.getColor(getContext(), R.color.title_man));
        } else if (this.templateName.matches("mdtk")) {
            titleText.setColor(ContextCompat.getColor(getContext(), R.color.title_mdtk));
        } else if (this.templateName.matches("sun")) {
            titleText.setColor(ContextCompat.getColor(getContext(), R.color.title_sun));
        } else if (this.templateName.matches("cash")) {
            titleText.setColor(ContextCompat.getColor(getContext(), R.color.title_cash));
        }

        c.drawText(this.title,  800, 560, titleText);

        // Texts
        Paint text = new TextPaint();
        text.setAntiAlias(true);
        text.setTextAlign(Paint.Align.LEFT);
        text.setTypeface(ResourcesCompat.getFont(getContext(), R.font.bebas_neue));
        text.setColor(Color.WHITE);
        text.setTextSize(64);

        c.drawText(this.entrants,  125, 448, text);
        c.drawText(this.location,  1250, 448, text);
        c.drawText(this.date,  754, 2312, text);

        // Player text
        Paint playerText = new TextPaint();
        playerText.setAntiAlias(true);
        playerText.setTextAlign(Paint.Align.LEFT);
        playerText.setTypeface(ResourcesCompat.getFont(getContext(), R.font.bebas_neue));
        playerText.setColor(Color.WHITE);
        playerText.setTextSize(150);

        for (int i=0; i<8; i++) {
            int[] y = {811, 1025, 1234, 1441, 1654, 1840, 2055, 2241}; // The gap between layout aren't equals
            Player player = this.players.get(i);

            // Characters
            // Remove null character
            ArrayList<Character> toRemove = new ArrayList<>();
            player.characters.remove(null);
            for (Character character : player.characters) {
                int characterId = getContext().getResources().getIdentifier(character.getPath(templateName), "drawable", getContext().getPackageName());
                if (characterId == 0) {
                    toRemove.add(character);
                }
            }
            player.characters.removeAll(toRemove);

            int[] posX = new int[]{1200};;
            int posY = y[i];

            ArrayList<Integer> widthArray = new ArrayList<>();
            int totalWidth = 0;
            for (Character character : player.characters) {
                int characterId = getContext().getResources().getIdentifier(character.getPath(templateName), "drawable", getContext().getPackageName());
                if (characterId == 0) break;
                Bitmap characterBitmap = BitmapFactory.decodeResource(getContext().getResources(), characterId);

                // Rescale and crop the image
                int initialWidth = characterBitmap.getWidth();
                int initialHeight = characterBitmap.getHeight();

                Rescale rescale = characterData.getRescale(character.getPath(templateName).split("_alt")[0]);

                int rescaleWidth = rescale.width * initialHeight / IMAGE_HEIGHT; // characterBitmap.getHeight != original image height

                characterBitmap = Bitmap.createBitmap(characterBitmap,
                        0,
                        (i == 0 ? 0 : rescale.cropTop), // Character not rescaled for the first player
                        initialWidth,
                        (initialHeight - rescale.cropBottom - (i == 0 ? 0 : rescale.cropTop)));

                int height = 169 + (i == 0 ? rescale.cropTop * 169 / characterBitmap.getHeight() : 0 );
                int reduceGap = (player.characters.size() == 3 ? 50 : 0); // reduce gap by 50 if 3 characters
                int width = (rescaleWidth * height) / characterBitmap.getHeight() - reduceGap;
                widthArray.add(width);
                totalWidth += width;
            }

            switch(player.characters.size()) {

                case 1:
                    posX = new int[]{1200 - (totalWidth / 2)};
                    break;

                case 2:
                    posX = new int[]{1200 - (totalWidth / 2), 1200 - (totalWidth / 2) + widthArray.get(0)};
                    break;

                case 3:
                    posX = new int[]{1200 - (totalWidth / 2),
                            1200 - (totalWidth / 2) + widthArray.get(0),
                            1200 - (totalWidth / 2) + widthArray.get(0) + widthArray.get(1)};
                    break;
            }

            for (int index = player.characters.size()-1; index>=0; index--) { // draw the character in reverse order for the layer level
                Character character = player.characters.get(index);
                // Get drawable
                int characterId = getContext().getResources().getIdentifier(character.getPath(templateName), "drawable", getContext().getPackageName());
                Bitmap characterBitmap = BitmapFactory.decodeResource(getContext().getResources(), characterId);

                // Rescale and crop the image
                int initialWidth = characterBitmap.getWidth();
                int initialHeight = characterBitmap.getHeight();

                Rescale rescale = characterData.getRescale(character.getPath(templateName).split("_alt")[0]);

                characterBitmap = Bitmap.createBitmap(characterBitmap,
                        0,
                        (i == 0 ? 0 : rescale.cropTop), // Character not rescaled for the first player
                        initialWidth,
                        (initialHeight - rescale.cropBottom - (i == 0 ? 0 : rescale.cropTop)));

                int height = 169 + (i == 0 ? rescale.cropTop * 169 / characterBitmap.getHeight() : 0 );
                int width = (characterBitmap.getWidth() * height) / characterBitmap.getHeight();
                int horizontalOffset = rescale.horizontalOffset * width / characterBitmap.getWidth();
                Drawable characterDraw = new BitmapDrawable(getResources(), characterBitmap);

                // Set bounds
                characterDraw.setBounds(posX[index] - horizontalOffset,
                        posY - height,
                        posX[index] + width - horizontalOffset,
                        posY);

                characterDraw.draw(c);
            }

            // Team
            if (player.team != null) {
                int teamId = getContext().getResources().getIdentifier("team_" + player.team, "drawable", getContext().getPackageName());
                if (teamId != 0) {
                    Bitmap teamBitmap = BitmapFactory.decodeResource(getContext().getResources(), teamId);

                    // Rescale
                    int height = 120;
                    int width = (teamBitmap.getWidth() * height) / teamBitmap.getHeight();
                    Drawable team = new BitmapDrawable(getResources(), teamBitmap);

                    team.setBounds(340 - (width / 2),
                            posY - height - 22,
                            340 + (width / 2),
                            posY - 22);
                    team.draw(c);
                }
            }

            // Pseudo
            c.drawText(player.pseudo, 425, posY-30, playerText);

        }

        // Set canvas bitmap to background
        drawable = new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        this.setBackground(drawable);
    }
}
