package com.seq.util;

import java.io.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import com.seq.SeqBot;

public class AudioUtil {

	public static void playAwJeez() {
		playSound("jeez.wav");
	}

	public static void playKit() {
		playSound("start.wav");
	}

	public static void playMyMan() {
		playSound("my_man.wav");
	}

	public static void playGenius() {
		playSound("genius.wav");
	}

	public static void playShowGot() {
		playSound("show_got.wav");
	}

	public static void playQuit() {
		playSound("shut_down.wav");
	}

	public static void playLikeGot() {
		playSound("like_got.wav");
	}

	public static void playHeaven() {
		playSound("heaven.wav");
	}

	public static void playWubbaLubbaDubDub() {
		playSound("wubba_lubba.wav");
	}

	public static void playSchwifty() {
		playSound("schwifty.wav");
	}

	/**
	 * Please the sound of specific file name (from lib directory).
	 * 
	 * @param fileName
	 */
	private static void playSound(String fileName) {

		try {
			if (!SeqBot.ENABLE_SOUND)
				return;

			File soundFile = new File(SeqBot.AUDIO_PATH + fileName);
			AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);

			DataLine.Info info = new DataLine.Info(Clip.class, audio.getFormat());
			Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(audio);
			clip.addLineListener(new LineListener() {
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.STOP) {
						event.getLine().close();
					}
				}
			});

			clip.start();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}
