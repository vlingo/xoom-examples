// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.workoffload1;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Random;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.common.Cancellable;
import io.vlingo.common.Scheduled;
import io.vlingo.common.Tuple2;

public class FileProcessorTest {
  private static Random random = new Random();

  private Tuple2<File,String> dataFile;
  private Stage fileCheckerStage;
  private World world;

  @Test
  public void testThatNonChangingFileIsStable() {
    final int retries = 2;

    System.out.println("testThatNonChangingFileIsStable: may require " + retries + " seconds.");

    final FileProcessor processor = FileProcessor.using(world.stage(), Duration.ofSeconds(1), retries);

    final ResultType result = processor.process(dataFile._2).await(seconds(retries));

    assertEquals(ResultType.Stable, result);
  }

  @Test
  public void testThatChangingFileIsStable() {
    final int retries = 5;

    System.out.println("testThatChangingFileIsStable: may require " + retries + " seconds.");

    final FileProcessor processor = FileProcessor.using(fileCheckerStage, Duration.ofSeconds(1), retries);

    world.actorFor(FileModifier.class, FileModifierActor.class)
      .modifyFileWith(dataFile._1, Duration.ofMillis(10), 100);

    final ResultType result = processor.process(dataFile._2).await(seconds(retries));

    assertEquals(ResultType.Stable, result);
  }

  @Test
  public void testThatChangingFileExhaustsAttempts() {
    final int retries = 5;

    System.out.println("testThatChangingFileExhaustsAttempts: may require " + retries + " seconds.");

    final FileProcessor processor = FileProcessor.using(fileCheckerStage, Duration.ofSeconds(1), retries);

    world.actorFor(FileModifier.class, FileModifierActor.class)
      .modifyFileWith(dataFile._1, Duration.ofMillis(100), 100);

    final ResultType result = processor.process(dataFile._2).await(seconds(retries));

    assertEquals(ResultType.AttemptsExhausted, result);
  }

  @Test
  public void testThatNonExistingFileFails() {
    final int retries = 2;

    System.out.println("testThatNonExistingFileFails: may require " + retries + " seconds.");

    final FileProcessor processor = FileProcessor.using(fileCheckerStage, Duration.ofSeconds(1), retries);

    final ResultType result = processor.process("./non-existing-file.dat").await(seconds(retries));

    assertEquals(ResultType.NonExisting, result);
  }

  @Before
  public void setUp()throws Exception {
    world = World.startWithDefaults("test-file-processor");
    fileCheckerStage = world.stageNamed("file-checker");
    dataFile = generateFile();
  }

  @After
  public void tearDown() {
    dataFile._1.delete();
    world.terminate();
  }

  private Tuple2<File,String> generateFile() throws Exception {
    final String prefix = "data" + random.nextLong();
    final String suffix = ".dat";

    final File file = File.createTempFile(prefix, suffix);

    return Tuple2.from(file, file.getAbsolutePath());
  }

  private long seconds(final int retries) {
    return retries * 1000 + 500;
  }

  public static interface FileModifier {
    void modifyFileWith(final File file, final Duration frequency, final int times);
  }

  public static final class FileModifierActor extends Actor implements FileModifier, Scheduled<Object> {
    private Cancellable cancellable;
    private int count;
    private String path;
    private int times;
    private FileWriter writer;

    public FileModifierActor() { }

    @Override
    @SuppressWarnings("unchecked")
    public void modifyFileWith(final File file, final Duration frequency, final int times) {
      this.count = 1;
      this.times = times;
      this.path = file.getAbsolutePath();
      this.writer = open(file);

      writeBlock();

      cancellable = scheduler().schedule(selfAs(Scheduled.class), null, Duration.ZERO, frequency);
    }

    @Override
    public void intervalSignal(final Scheduled<Object> scheduled, final Object data) {
      if (++count >= times) {
        close();
        return;
      }
      writeBlock();
    }

    private String blockOfText() {
      final StringBuffer buffer = new StringBuffer();

      while (buffer.length() < 1024) {
        buffer.append(UUID.randomUUID().toString());
      }

      return buffer.toString();
    }

    private void close() {
      try {
        writer.close();
        cancellable.cancel();
        // TODO: This is causing a strange problem by suspending the
        // mailbox before it has a chance to delivery everything up
        // to the Stoppable::conclude() message.
        // 
        // selfAs(Stoppable.class).conclude();
      } catch (IOException e) {
        throw new IllegalStateException("Cannot close file: " + path, e);
      } finally {
        writer = null;
      }
    }

    private FileWriter open(final File file) {
      try {
        return new FileWriter(file);
      } catch (Exception e) {
        throw new IllegalStateException("Cannot write to file: " + path, e);
      }
    }

    private void writeBlock() {
      if (writer == null) return;

      try {
        final String randomBlock = blockOfText();
        writer.write(randomBlock, 0, randomBlock.length());
        writer.flush();
      } catch (Exception e) {
        throw new IllegalStateException("Cannot write to file: " + path, e);
      }
    }
  }
}
