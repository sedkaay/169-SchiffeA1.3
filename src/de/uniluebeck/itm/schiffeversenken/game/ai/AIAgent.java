package de.uniluebeck.itm.schiffeversenken.game.ai;

import de.uniluebeck.itm.schiffeversenken.game.model.FieldTile;
import de.uniluebeck.itm.schiffeversenken.game.model.GameField;
import de.uniluebeck.itm.schiffeversenken.game.model.Ruleset;
import de.uniluebeck.itm.schiffeversenken.game.model.Ship;

import java.util.Random;

/**
 * This class provides the blueprint and basic behavior for every AI agent
 * 
 * @author leondietrich
 *
 */
public abstract class AIAgent {

    /**
     * The difficulty of the AI
     */
	private final int hardness;

    /**
     * Construct a new AI agent
     * 
     * @param hardness The difficulty of the AI
     */
	public AIAgent(int hardness) {
        this.hardness = hardness;
    }

    /**
     * Use this method in order to initialize the agent. It needs to place his ships now.
     * @param r The rule set of the game.
     * @param agentsField The field belonging to the agent
     */
    public abstract void setup(Ruleset r, GameField agentsField);

    /**
     * Use this method in order to let the agent perform its move.
     * @param playersField The game field of the human player.
     * @return true if the agent hit something or otherwise false.
     */
    public abstract boolean performMove(GameField playersField);

    /**
     * Call this method from within a setup method in order to conviniently place your ships.
     * @param r The rule set to obey
     * @param f The field to place the ships on
     */
    protected void placeShipsAccordingToRules (Ruleset r, GameField f) {

        final int width = r.getGameFieldSize().getX();
        final int height = r.getGameFieldSize().getY();

        final int[] shipsToBePlaced = new int[] {
                r.getNumberOf1Ships(),
                r.getNumberOf2Ships(),
                r.getNumberOf3Ships(),
                r.getNumberOf4Ships(),
                r.getNumberOf5Ships()};

        final Random rnd = new Random(System.currentTimeMillis());

        for (int shipsLenghtIndex = 0; shipsLenghtIndex < shipsToBePlaced.length; shipsLenghtIndex++) {
            for (int ship = 0; ship < shipsToBePlaced[shipsLenghtIndex]; ship++) {
                while (!checkAndPlace(f, rnd.nextBoolean(), rnd.nextInt(width),
                        rnd.nextInt(height), shipsLenghtIndex + 1, width, height));
            }
        }
    }

    /**
     * Use this method to correctly place a ship
     * @param f the game field of the AI
     * @param up the orientation of the ship
     * @param x the x coordinate of the ship
     * @param y the y coordinate of the ship
     * @param length the length of the ship
     * @param width the width of the AI player field
     * @param height the height of the AI player field
     * @return Has the ship successfully been placed?
     */
    private boolean checkAndPlace(GameField f, boolean up, int x, int y, int length, int width, int height) {
        if ((up && y + length > height) || (!up && x + length > width)) {
            return false;
        }

        for(int currentShipsX = x, currentShipsY = y, i = 0; i < length; i++) {
            final FieldTile t = f.getTileAt(currentShipsX, currentShipsY);
            if (t.getTilestate() != FieldTile.FieldTileState.STATE_WATER || t.getCorrespondingShip() != null) {
                return false;
            }
            if (up) {
                currentShipsY++;
            } else {
                currentShipsX++;
            }
        }

        final Ship shipToPlace = new Ship(length, up);
        f.placeShip(x, y, length, up, shipToPlace);
        return true;
    }

    /**
     * This method must provide the tile that was previously attacked by the AI agent.
     * @return The last tile under fire.
     */
    public abstract FieldTile getLastAttackedTile();

    /**
     * This method is a getter for the hardness level of the AI
     * @return The current hardness level
     */
    public int getHardness() {
        return this.hardness;
    }
}
