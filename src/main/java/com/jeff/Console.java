package com.jeff;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.Collections;


/**
 * CLASS Console
 * - Provides services to application to setup the view, refresh after changes,
 * - handle wait, and write to the screen. The Console class writes out status info
 * - It is not used by the Display [grid] view
 */
public class Console {

    /**
     * Constuctor to configure the console output
     * @throws IOException
     */
    public Console() throws IOException {

        //terminal outputs detailed status - it's a separate window
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        _screen = new TerminalScreen(terminal);
        _screen.startScreen();

        //gui is grid window that shows each plane
        _gui = new MultiWindowTextGUI(_screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        _window = new BasicWindow();
        _mainPanel = new Panel(new BorderLayout());
        AddStatusPanel();
        _window.setComponent(_mainPanel);
        _window.setHints(Collections.singletonList(Window.Hint.CENTERED));
        _gui.addWindow(_window);
        _gui.setActiveWindow(_window);
    }

    /**
     * Handles waiting on a keystroke
     */
    public void Wait() {
        System.out.println("about to wait");

        KeyStroke key;
        try {
            WriteLine("\r\nPRESS ENTER TO EXIT");
            while ((key = _screen.readInput()).getKeyType() != KeyType.Enter) {
                _statusBox.handleKeyStroke(key);
                _gui.updateScreen();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("done waiting");
    }

    /**
     * Refreshes the screen immediately and returns
     * @throws IOException
     */
    public void Refresh() throws IOException {
        Refresh(0);
    }

    /**
     * Refreshes the screen, then waits n milliseconds
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
     * Resets the logs to empty
     */
    public void Reset() {
        _statusBox.setText("");
    }

    /**
     * Adds text to the log view
     * @param output
     */
    public void WriteLine(String output) {
        _statusBox.addLine(output);
        _statusBox.setCaretPosition(_statusBox.getLineCount() - 1, 1);
        try {
            _gui.updateScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(output);
    }

    /**
     * Adds the component that shows log entries to the main log window
     */
    private void AddStatusPanel() {
        Panel panel = new Panel();
        _mainPanel.addComponent(panel);
        _statusBox = new TextBox(new TerminalSize(100, 100), TextBox.Style.MULTI_LINE);
        panel.addComponent(_statusBox);
        panel.setLayoutData(BorderLayout.Location.BOTTOM);
    }

    private MultiWindowTextGUI _gui;
    private BasicWindow  _window;
    private Screen _screen;
    private Panel _mainPanel;
    private TextBox _statusBox;
}

