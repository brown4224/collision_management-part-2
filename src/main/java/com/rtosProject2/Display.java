package com.rtosProject2;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CLASS Display
 * - Handles creating a grid of 4 planes that can be updated to show X, Y, and Z trains,
 * - plus a composite view of all 3. Also has a status window at the bottom for short messages.
 */
public class Display {

    /**
     * Constructor to setup grid view, colors, panels, etc.
     * @param cols
     * @throws IOException
     */
    public Display(int cols) throws IOException {

        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        _screen = new TerminalScreen(terminal);
        _screen.startScreen();

        _gui = new MultiWindowTextGUI(_screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        _window = new BasicWindow();
        _mainPanel = new Panel(new BorderLayout());
        _planePanel = new Panel(new GridLayout(cols));
        _mainPanel.addComponent(_planePanel, BorderLayout.Location.CENTER);
        AddStatusPanel();
        _window.setComponent(_mainPanel);
        _window.setHints(Collections.singletonList(Window.Hint.CENTERED));
        _gui.addWindow(_window);
    }

    /**
     * Number of seconds we are going to run the simulation
     */
    public final int Seconds = 200;

    /**
     * Refreshes the window without any blocking after
     * @throws IOException
     */
    public void Refresh() throws IOException {
        Refresh(0);
    }

    /**
     * Refreshes the window, then blocks for n milliseconds
     * @param waitMs
     * @throws IOException
     */
    public void Refresh(int waitMs) throws IOException {
        _gui.updateScreen();
        try {
            if (waitMs > 0) Thread.sleep(waitMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pauses execution to await a keypress. Used by single step mode.
     */
    public void AwaitKeypress() {
        try {
            while ((_screen.readInput()).getKeyType() != KeyType.Enter) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the status box without appending to what is already there
     * @param text
     */
    public void UpdateStatus(String text) {
        UpdateStatus(text, false);
    }

    /**
     * Updates the status box and optionally appends to the current status text
     * @param text
     * @param append
     */
    public void UpdateStatus(String text, boolean append) {
        if (append) text = _statusBox.getText() + text;
        if (text.length() > 99) text = text.substring(0, 99);
        _statusBox.setText(" " + text);
    }

    /**
     * Used during init to add the status panel textbox below the plane view
     */
    private void AddStatusPanel() {
        Panel panel = new Panel();
        _mainPanel.addComponent(panel);
        _statusBox = new Label("");
        _statusBox.setSize(new TerminalSize(100, 5));
        panel.addComponent(_statusBox);
        panel.setLayoutData(BorderLayout.Location.BOTTOM);
    }

    private MultiWindowTextGUI _gui;
    private BasicWindow  _window;
    private Screen _screen;
    private Panel _mainPanel;
    private Panel _planePanel;
    private Label _statusBox;
    private Plane _statusPlane;
    private List<Plane> _planes = new ArrayList<>();

    /**
     * Getter to return the list of planes
     * @return
     */
    public List<Plane> GetPlanes() {
        return _planes;
    }

    /**
     * Getter to return the status plane
     * @return
     */
    public Plane GetStatusPlane() {
        return _statusPlane;
    }

    /**
     * Getter to return the current state of all planes in a 3D matrix.
     * @return
     */
    public String[][][] GetCurrentState() {
        String[][][] currentState = new String[3][Plane.rows][Plane.cols];

        for (int i = 0; i < _planes.size(); i++) {
            currentState[i] = GetPlanes().get(i).GetState();
        }
        return currentState;
    }

    /**
     * Adds a display plane with coordinates to the overall grid. Used during initialization.
     * @param title
     * @param movement
     * @param marker
     * @param initX
     * @param initY
     * @param isStatusPlane
     * @return
     */
    public Plane AddPlane(String title, Plane.Movement movement, String marker, int initX, int initY, boolean isStatusPlane) {
        // Create panel to hold components
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(8));

        Label[][] labels = new Label[Plane.rows + 1][Plane.cols + 1];
        for (int row = 0; row <= Plane.rows; row++){
            for (int col = 0; col <= Plane.cols; col++) {
                Label newLabel;
                if (row == 0 && col == 0) {
                    newLabel = new Label(Plane.emptyFormat);
                } else if (row == 0) {
                    newLabel = new Label(String.format(Plane.labelFormat, col - 1));
                } else if (col == 0) {
                    newLabel = new Label(String.format(Plane.labelFormat, row - 1));
                } else {
                    newLabel = new Label("");
                    Plane.SetEmptyFormat(newLabel);
                }
                newLabel.setLabelWidth(Plane.emptyFormat.length());
                panel.addComponent(newLabel);
                labels[row][col] = newLabel;
            }
        }
        panel.setSize(new TerminalSize(8,9));
        _planePanel.addComponent(panel.withBorder(Borders.singleLine(title)));
        Plane newPlane =  new Plane(marker, movement, labels);
        if (initX > -1 && initX > -1) newPlane.Initialize(initX, initY);
        if (!isStatusPlane) { _planes.add(newPlane); } else { _statusPlane = newPlane; }
        return newPlane;
    }
}

