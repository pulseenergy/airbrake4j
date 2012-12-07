package com.pulseenergy.oss.logging.logback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import com.pulseenergy.oss.logging.NotificationSerializer;
import com.pulseenergy.oss.logging.http.HttpNotificationBuilder;
import com.pulseenergy.oss.logging.http.HttpNotificationSender;

public abstract class AbstractLogbackHttpAppenderBase<T> extends AppenderBase<ILoggingEvent> {
	private static final int MAX_QUEUED_MESSAGES = 1000;
	private final HttpNotificationSender notificationSender;
	private final HttpNotificationBuilder<T, ILoggingEvent> notificationBuilder;
	private final ExecutorService executorService;
	private final LinkedBlockingQueue<String> workQueue;
	private final NotificationSerializer<String, T> serializer;

	protected AbstractLogbackHttpAppenderBase() {
		super();
		this.executorService = Executors.newSingleThreadExecutor();
		this.notificationSender = buildNotificationSender();
		this.notificationBuilder = buildNotificationGenerator();
		this.serializer = buildNotificationSerializer();
		this.workQueue = new LinkedBlockingQueue<String>(MAX_QUEUED_MESSAGES);
	}

	protected abstract NotificationSerializer<String,T> buildNotificationSerializer();

	@Override
	public void start() {
		super.start();
		executorService.execute(new AsynchronousNotificationHandler(notificationSender, workQueue, serializer.getContentType()));
		startInternal();
	}

	@Override
	public void stop() {
		shutdownWorkQueueHandler();
		stopInternal();
		super.stop();
	}

	private void shutdownWorkQueueHandler() {
		final boolean success = workQueue.offer(AsynchronousNotificationHandler.POISON_PILL);
		boolean shutdown = false;
		if (success) {
			executorService.shutdown();
			try {
				shutdown = executorService.awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		if (!shutdown) {
			executorService.shutdownNow();
		}
	}

	protected void startInternal() {
	}

	protected abstract HttpNotificationBuilder<T, ILoggingEvent> buildNotificationGenerator();

	protected abstract HttpNotificationSender buildNotificationSender();

	@Override
	protected void append(ILoggingEvent eventObject) {
		eventObject.prepareForDeferredProcessing();
		final T notification = notificationBuilder.build(eventObject);
		final String serialized = serializer.serialize(notification);
		final boolean success = workQueue.offer(serialized);
		if (!success) {
			addError(String.format("Unable to send message due to work queue saturation. Discarded message: %s", serialized));
		}
	}

	protected void stopInternal() {
	}

	public class AsynchronousNotificationHandler implements Runnable {
		private final HttpNotificationSender notificationSender;
		private final LinkedBlockingQueue<String> workQueue;
		public static final String POISON_PILL = "POISON_PILL";
		private final String contentType;

		public AsynchronousNotificationHandler(final HttpNotificationSender notificationSender, final LinkedBlockingQueue<String> workQueue, final String contentType) {
			this.notificationSender = notificationSender;
			this.workQueue = workQueue;
			this.contentType = contentType;
		}

		@Override
		public void run() {
			while (true) {
				try {
					String item = workQueue.take();
					if (POISON_PILL.equals(item)) {
						return;
					}
					notificationSender.send(item, contentType);
				} catch (InterruptedException e) {
					// TODO Error handler
					Thread.currentThread().interrupt();
					return;
				} catch (Exception e) {
					// TODO Error handler
				}
			}
		}
	}
}
