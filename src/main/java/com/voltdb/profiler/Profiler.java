package com.voltdb.profiler;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;

import com.voltdb.profiler.configuration.SampleConfiguration;
import com.voltdb.profiler.configuration.SampleConfigurationFactory;

/**
 * Profiler
 * 
 * @author andrewwilson
 * 
 */
public class Profiler extends BaseVoltApp {

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
		drawHeader();
		while (true) {
			
			printStatistics();

			Thread.sleep(5 * 1000);
		}

		// closeUI();
	}

	private void drawHeader() {
		ScreenItem.TIME.writeHeader();
		ScreenItem.PROCEDURE.writeHeader();
		ScreenItem.CALLS.writeHeader();
		ScreenItem.MIN.writeHeader();
		ScreenItem.MAX.writeHeader();
		ScreenItem.AVERAGE.writeHeader();
		System.out.println();

	}

	private void initUI() {

	}

	private void closeUI() {

	}

	protected void printResults() {
	}

	public synchronized void printStatistics() {
		try {
			ClientResponse cr = this.client.callProcedure("@Statistics",
					"procedure", 1);
			if (cr.getStatus() == ClientResponse.SUCCESS) {
				VoltTable[] tables = cr.getResults();
				if (tables != null && tables.length > 0) {
					while (tables[0].advanceRow()) {
						String procedure = tables[0].getString("PROCEDURE");
						long calls = tables[0].getLong("INVOCATIONS");
						long min = tables[0].getLong("MIN_EXECUTION_TIME");
						long max = tables[0].getLong("MAX_EXECUTION_TIME");
						long average = tables[0].getLong("AVG_EXECUTION_TIME");
						
						ScreenItem.TIME.writeColumn(new GregorianCalendar());
						ScreenItem.PROCEDURE.writeColumn(procedure);
						ScreenItem.CALLS.writeColumn(calls);
						ScreenItem.MIN.writeColumn(min);
						ScreenItem.MAX.writeColumn(max);
						ScreenItem.AVERAGE.writeColumn(average);
						System.out.println();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public enum ScreenItem {
		TIME(18, "Time"), 
		PROCEDURE(40, "Procedure"), 
		CALLS(15, "Calls"), 
		MIN(15, "Min(ms)"), 
		MAX(15, "Max(ms)"),
		AVERAGE(15, "Average(ms)");

		int width;
		String text;
		String formatString;

		private ScreenItem(int width, String string) {
			this.width = width;
			text = string;
			formatString = "%" + width + "s";
		}

		public void writeColumn(long value) {
			this.writeColumn("" + value);
		}

		public void writeColumn(Calendar calendar) {
			this.writeColumn(String.format("%1$tD %1$tT", calendar));
		}

		public void writeHeader() {
			System.out.printf(this.formatString, text);
		}

		public void writeColumn(String value) {
			System.out.printf(this.formatString, value);
		}

	}
}
