package com.voltdb.profiler;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.CheckBox;
import com.googlecode.lanterna.gui.component.EmptySpace;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.ProgressBar;
import com.googlecode.lanterna.gui.component.Table;
import com.googlecode.lanterna.gui.component.TextBox;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;
import com.voltdb.profiler.configuration.SampleConfiguration;
import com.voltdb.profiler.configuration.SampleConfigurationFactory;

/**
 * Profiler
 * @author andrewwilson
 *
 */
public class Profiler extends BaseVoltApp {

	private Screen screen;
	private ScreenWriter writer;

	public static void main(String[] args) {
		SampleConfiguration config = SampleConfigurationFactory
				.getConfiguration(args);
		Profiler profiler = new Profiler(config);
		try {
			profiler.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Profiler(SampleConfiguration config) {
		super(config);
	}

	@Override
	protected void execute() throws Exception {
		initUI();
		
		while (true) {
			screen.clear();
			screen.refresh();
			drawHeader();
			printStatistics();
			screen.refresh();
			Thread.sleep(5 * 1000);
		}
		
		//closeUI();
	}

	
	private void drawHeader() {
		ScreenItem.PROCEDURE.draw(this.writer);
		ScreenItem.CALLS.draw(this.writer);
		ScreenItem.MIN.draw(this.writer);
		ScreenItem.MAX.draw(this.writer);
		
	}

	private void initUI() {
		Terminal terminal = TerminalFacade.createTextTerminal();
		screen = TerminalFacade.createScreen(terminal);
		writer = new ScreenWriter(screen);
        screen.startScreen();
	}


	private void closeUI() {
		screen.stopScreen();
	}

	
	protected void printResults() {
	}

	public synchronized void printStatistics() {
		try {
			ClientResponse cr = this.client.callProcedure("@Statistics",
					"procedure", 0);
			if (cr.getStatus() == ClientResponse.SUCCESS) {
				VoltTable[] tables = cr.getResults();
				if (tables != null && tables.length > 0) {
					int y=1;
					while (tables[0].advanceRow()) {
						String procedure = tables[0].getString("PROCEDURE");
						long calls= tables[0].getLong("INVOCATIONS");
						long min = tables[0].getLong("MIN_EXECUTION_TIME");
						long max = tables[0].getLong("MAX_EXECUTION_TIME");
						ScreenItem.PROCEDURE.add(this.writer, y, procedure);
						ScreenItem.CALLS.add(this.writer, y, ""+calls);
						ScreenItem.MIN.add(this.writer, y, "" + min);
						ScreenItem.MAX.add(this.writer, y, ""+ max);
						y++;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public enum ScreenItem {

		PROCEDURE(0, 0, 30, "Procedure"), 
		CALLS(35, 0, 15, "Calls"),
		MIN(52, 0, 15, "Min(ms)"),
		MAX(67, 0, 15, "Max(ms)");

		int xLoc;
		int yLoc;
		int maxWidth;
		String text;
		
		private final static String PADDING = "                                                  ";

		private ScreenItem(int x, int y, int width, String string) {
			xLoc = x;
			yLoc = y;
			maxWidth = width;
			text = string;
		}

		public void draw(ScreenWriter writer) {

			writer.drawString(xLoc, yLoc, text);
		}

		public void add(ScreenWriter writer, int y, String value) {
			if (value.length() > maxWidth) {
				value = value.substring(value.length() - maxWidth);
			} else if ( value.length() < maxWidth ) {
				value = PADDING.substring(0,maxWidth-value.length()) + value;
			}
			writer.drawString(xLoc, y, value);
		}

	}
}
