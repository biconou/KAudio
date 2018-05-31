package com.github.biconou.kaudio.legacy;

import com.github.biconou.kaudio.audio.system.AudioSystemUtilsKt;
import org.junit.Test;
import org.modelmapper.internal.util.Assert;

import javax.sound.sampled.Mixer;
import java.util.Arrays;

public class AudioSystemUtilsTest {

    @Test
    public void allMixers() {
        Arrays.stream(AudioSystemUtilsKt.listAllMixers()).forEach(m -> System.out.println(m.getName()));
    }

    @Test
    public void defaultMixer() {
        Mixer mixer = AudioSystemUtilsKt.findMixerByName("default [default]");
        Assert.notNull(mixer);

        mixer = AudioSystemUtilsKt.findMixerByName("not defined");
        Assert.isNull(mixer);
    }
}
