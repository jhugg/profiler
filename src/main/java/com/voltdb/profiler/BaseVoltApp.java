package com.voltdb.profiler;

/* This file is part of VoltDB.
 * Copyright (C) 2008-2012 VoltDB Inc.
 *
 * This file contains original code and/or modifications of original code.
 * Any modifications made by VoltDB Inc. are licensed under the following
 * terms and conditions:
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import org.voltdb.client.Client;
import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientStats;
import org.voltdb.client.ClientStatsContext;
import org.voltdb.client.NoConnectionsException;

import com.voltdb.profiler.configuration.SampleConfiguration;

/**
 * Helper class that sets up the basic voltdb client services. TODO: This should
 * be moved to a common jar so all the Java examples share the same base class.
 * 
 * @author awilson
 * 
 */
public abstract class BaseVoltApp {

	public final static String HORIZONTAL_RULE = "----------" + "----------"
			+ "----------" + "----------" + "----------" + "----------"
			+ "----------" + "----------" + "\n";

	protected Client client;

	protected final SampleConfiguration config;

	public BaseVoltApp(SampleConfiguration config) {
		this.config = config;
		this.client = initClient(config);
	}

	public Client initClient(SampleConfiguration config) {
		ClientConfig clientConfig = new ClientConfig("", "");
		client = ClientFactory.createClient(clientConfig);
		return client;
	}

	/**
	 * Connect to a single server with retry. Limited exponential backoff. No
	 * timeout. This will run until the process is killed if it's not able to
	 * connect.
	 * 
	 * @param server
	 *            hostname:port or just hostname (hostname can be ip).
	 */
	void connectToOneServerWithRetry(String server) {
		int sleep = 1000;
		while (true) {
			try {
				client.createConnection(server);
				break;
			} catch (Exception e) {
				System.err.printf(
						"Connection failed - retrying in %d second(s).\n",
						sleep / 1000);
				try {
					Thread.sleep(sleep);
				} catch (Exception interruted) {
				}
				if (sleep < 8000)
					sleep += sleep;
			}
		}
		System.out.printf("Connected to VoltDB node at: %s.\n", server);
	}

	/**
	 * Connect to a set of servers in parallel. Each will retry until
	 * connection. This call will block until all have connected.
	 * 
	 * @param servers
	 *            A comma separated list of servers using the hostname:port
	 *            syntax (where :port is optional).
	 * @throws InterruptedException
	 *             if anything bad happens with the threads.
	 */
	void connect(String servers) throws InterruptedException {
		System.out.println("Connecting to VoltDB...");

		String[] serverArray = servers.split(",");
		final CountDownLatch connections = new CountDownLatch(
				serverArray.length);

		// use a new thread to connect to each server
		for (final String server : serverArray) {
			new Thread(new Runnable() {

				public void run() {
					connectToOneServerWithRetry(server);
					connections.countDown();
				}
			}).start();
		}
		// block until all have connected
		connections.await();
	}

	/**
	 * Block until the transaction queue completes, display example statistics
	 * and close the connection to the cluster.
	 * 
	 * @throws NoConnectionsException
	 * @throws InterruptedException
	 */
	private void closeConnections() throws NoConnectionsException,
			InterruptedException {
		this.client.drain();
		this.client.close();
	}

	/**
	 * Manages the execution of the example and gathers its runtime statistics
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {

		this.connect(this.config.servers);
		this.execute();
		this.closeConnections();

	}

	public Client getClient() {
		return this.client;
	}

	/**
	 * Implement the body of the example in the derived class.
	 * 
	 * @throws Exception
	 */
	protected abstract void execute() throws Exception;

}
