package com.github.biconou.audioplayer.legacy.config;

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

public class Configuration {

    public static String ffmpegPath;
    public static String ffprobePath;

    public static void setFFmpegPath(String path) {
        Configuration.ffmpegPath = path;
    }

    public static void setFFprobePath(String path) {
        Configuration.ffprobePath = path;
    }
}