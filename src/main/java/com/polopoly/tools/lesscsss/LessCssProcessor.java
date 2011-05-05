/*
 * Copyright (c) Polopoly AB (publ).
 * Dual licensed under the MIT or GPL Version 2 licenses.
 */
package com.polopoly.tools.lesscsss;

import static java.lang.String.format;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;

import com.asual.lesscss.LessEngine;

public class LessCssProcessor
{
    private final String _sourceDir;
    private final String _targetDir;
    private final String[] _excludes;

    public LessCssProcessor(String sourceDir, String targetDir, String[] excludes)
    {
        _sourceDir = sourceDir;
        _targetDir = targetDir;
        _excludes = excludes == null ? new String[]{"include/**"} : excludes;
    }

    public void process() throws MojoFailureException, MojoExecutionException
    {
        DirectoryScanner directoryScanner = new DirectoryScanner();
        directoryScanner.setExcludes(_excludes);
        directoryScanner.setBasedir(_sourceDir);
        directoryScanner.setIncludes(new String[]{"**/*.less"});
        directoryScanner.scan();
        
        String[] files = directoryScanner.getIncludedFiles();
        if (files.length == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("Could not find any less files to process, sourceDir='");
            msg.append(_sourceDir).append("' excludes=[");
            join(msg, _excludes);
            msg.append("].");
            throw new MojoFailureException(msg.toString());
        }
        
        LessEngine lessEngine = new LessEngine();
        for (String source : files) {
            File sourceFile = new File(_sourceDir, source);
            if (!sourceFile.exists()) {
                throw new MojoExecutionException(format("Could not find source file, tried %s and %s.",
                                                        source, sourceFile));
            }
            File targetFile = new File(_targetDir, source.replace(".less", ".css"));
            targetFile.getParentFile().mkdirs();
            try {
                format("Creating %s from %s.", targetFile, sourceFile);
                lessEngine.compile(sourceFile, targetFile);
            }
            catch (Exception e) {
                throw new MojoExecutionException("Failed to compile " + sourceFile + " to " + targetFile + ".", e);
            }
        }
    }
    
    private void join(StringBuilder builder, String[] list)
    {
        String delimiter = "";
        for (String element : list) {
            builder.append(delimiter);
            builder.append("'");
            builder.append(element);
            builder.append("'");
            delimiter = ", ";
        }
    }
}
