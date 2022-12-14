package de.uniluebeck.itm.schiffeversenken.game;

import de.uniluebeck.itm.schiffeversenken.engine.*;
import de.uniluebeck.itm.schiffeversenken.game.model.*;

/**
 * Game field rendering methods.
 *
 * It only contains methods designated to rendering a game field. In future it will also provide scroll bars in case the
 * field doesn't fit on the canvas.
 * 
 * @author leondietrich, modiefied by B. Voss, ,modified by F. Junghans
 * 
 */
public class GameFieldRenderer {

	/**
     * The game field
     */
	private final GameField field;

    /**
     * Construct a new game field renderer
     * @param field
     */
	public GameFieldRenderer(GameField field) {
        this.field = field;
    }

    /**
     * Use this method in order to render a game field.
     *
     * @param c The canvas to render on.
     * @param x The x coordinate where to render the field.
     * @param y The y coordinate where to render the field.
     * 
     */
    public void renderGameField(Canvas c, int x, int y) {
        final int tileSize = Constants.TILE_SIZE;

        // Look up the default water tiles
        final Tile waterTile = AssetRegistry.getTile("water");
        final Tile waterMissed = AssetRegistry.getTile("water.hit");

        // draw the fields tiles
        for (int tileX = 0; tileX < this.field.getSize().getX(); tileX++) {
            for (int tileY = 0; tileY < this.field.getSize().getY(); tileY++) {
                final Vec2 tilePosition = new Vec2(x + tileX * tileSize, y + tileY * tileSize);
                getTileAt(tileX, tileY, waterTile, waterMissed).renderAt(c, tilePosition);
            }
        }
        
        final int width = tileSize * this.field.getSize().getX();
        final int height = tileSize * this.field.getSize().getY();

        /*
         * upperLeft The upper left corner of the field
         * upperRight The upper right corner of the field
         * bottomLeft The bottom left corner of the field 
         * bottomRight The bottom right corner of the field
         */
        Vec2 upperLeft = new Vec2(x,y);
        Vec2 upperRight = new Vec2(x + width,y);
        Vec2 bottomLeft = new Vec2(x, y + height);
        Vec2 bottomRight = new Vec2(x + width, y + height);
        //draw the white lines around the border of the field
        c.setColor(1, 1, 1);
        c.drawLine(upperLeft, upperRight);
        c.drawLine(upperLeft, bottomLeft);
        c.drawLine(bottomLeft, bottomRight);
        c.drawLine(bottomRight, upperRight);
        
        /*
         * temp1 The position on top of the field from where to draw the black line
         * temp2 The position on the bottom of the field to where the black line is drawn
         */
        //draw the black vertical lines between each tile
        c.setColor(0, 0, 0);
        //the variable i counts up in increments of tileSize to make it modular for different sized fields
        for (int i=tileSize; i < width; i += tileSize) {
        	Vec2 temp1 = new Vec2(x + i, y);
        	Vec2 temp2 = new Vec2(x + i, y + height);
        	c.drawLine(temp1, temp2);
        }
        
        /*
         * temp3 The position on the left of the field from where to draw the black line
         * temp4 The position on the right of the field to where the black line is drawn
         * draw the black horizontal lines between each tile
         */
        // @param j counts up in increments of tileSize to make it modular for different sized fields
        for (int j=tileSize; j < height; j += tileSize) {
        	Vec2 temp3 = new Vec2(x, y + j);
        	Vec2 temp4 = new Vec2(x + width, y + j);
        	c.drawLine(temp3, temp4);
        }
        
        
    }

    /**
     * This method gets called by the rendering method in order to look up the correct tile at a given position.
     * @param x The x coordinate of the tile to look up
     * @param y The y coordinate of the tile to look up
     * @param waterTile A cached version for a water tile (will be the most returned one)
     * @param waterHitTile A cached version of the water_missed tile (Will be the second most returned one)
     * @return The correct tile to render
     */
    protected Tile getTileAt(int x, int y, Tile waterTile, Tile waterHitTile) {
        switch(this.field.getTileAt(x, y).getTilestate()) {
            default:
                return AssetRegistry.getTile("unknown");
            case STATE_WATER:
                return waterTile;
            case STATE_MISSED:
                return waterHitTile;
            case STATE_SHIP:
                return lookupShipTile(x, y, false);
            case STATE_SHIP_HIT:
                return lookupShipTile(x, y, true);
        }
    }

    /**
     * This method looks up the correct tile if the position happens to be a ship.
     * @param x The x coordinate of the ships tile
     * @param y The y coordinate of the ships tile
     * @param alreadyHit A Flag that indicates whether nor not the tile has been hit yet.
     * @return The composed ships tile
     */
    private Tile lookupShipTile(int x, int y, boolean alreadyHit) {
        return AssetRegistry.getTile("up.ship.middle");
    }

    
    /**
     * The purpose of this method is to enable the usage of the game field on expanding classes.
     * @return The game field
     */
    protected GameField getField() {
        return this.field;
    }

}
