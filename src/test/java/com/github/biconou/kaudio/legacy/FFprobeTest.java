package com.github.biconou.kaudio.legacy;

/*-
 * #%L
 * newaudioplayer
 * %%
 * Copyright (C) 2016 - 2017 Rémi Cocula
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.github.biconou.kaudio.TestResourcesUtils;
import com.github.biconou.audioplayer.legacy.config.Configuration;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by remi on 05/04/17.
 */
public class FFprobeTest {


    private static FFprobe fFprobe;

    @BeforeClass
    public static void setup() throws IOException {
        Configuration.setFFmpegPath("/usr/bin/ffmpeg");
        Configuration.setFFprobePath("/usr/bin/ffprobe");
        fFprobe = new FFprobe(Configuration.ffprobePath);
    }

    private FFmpegProbeResult probeFile(Path file)  {
        try {
            String filePath = file.toRealPath().toString();
            return fFprobe.probe(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void probeAllAudioFiles() throws IOException {
        String root = TestResourcesUtils.resolveFilePath("");

        Map<Path, FFmpegProbeResult> probeResultMap = Files.walk(Paths.get(root))
                .filter(Files::isRegularFile)
                .collect(Collectors.toMap(Function.identity(), this::probeFile));

        probeResultMap.forEach((k,v) -> {
            System.out.format("+++ %s\n"
                    ,k.toString().replace(root,""));
            System.out.format("     %s\n"
                    ,v.format.format_long_name);
        });

    }

}