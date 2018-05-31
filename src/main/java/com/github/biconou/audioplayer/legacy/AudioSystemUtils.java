package com.github.biconou.audioplayer.legacy;

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

import javax.sound.sampled.*;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Created by remi on 19/03/17.
 */
@Deprecated
public class AudioSystemUtils {



    public static Mixer.Info[] listAllMixers() {
        return AudioSystem.getMixerInfo();
    }

    public static Mixer findMixerByName(String mixerName) {

        Mixer.Info[] allMixers = AudioSystemUtils.listAllMixers();

        Predicate<Mixer.Info> p = mixer -> mixer.getName().equals(mixerName);
        Mixer.Info found = Arrays.stream(allMixers).filter(p).findFirst().get();
        if (found != null) {
            return AudioSystem.getMixer(found);
        } else {
            return null;
        }
    }



}
